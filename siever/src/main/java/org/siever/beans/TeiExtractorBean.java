package org.siever.beans;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.camel.Exchange;
import org.apache.camel.util.json.JsonObject;
import org.grobid.core.engines.Engine;
import org.grobid.core.engines.config.GrobidAnalysisConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.xml.sax.SAXException;

public class TeiExtractorBean {
    public void extractTei (Exchange exchange) throws Exception {

        String pdfPath = exchange.getIn().getBody(String.class);
        Engine engine = exchange.getContext().getRegistry()
                .lookupByNameAndType("engine", Engine.class);
        File input = new File(pdfPath);

        PDDocument doc = Loader.loadPDF(input);
        int count = doc.getNumberOfPages();
        doc.close();

        GrobidAnalysisConfig conf = GrobidAnalysisConfig.builder().build();

        String tei = engine.fullTextToTEI(input, conf);
        JSONObject json = teiToJson(tei);
        json.put("pageCount", count);

        System.out.println(json);

        exchange.getIn().setBody(json, JSONObject.class);

    }

    private JSONObject teiToJson (String tei) throws ParserConfigurationException, IOException, SAXException {
        JSONObject json = new JSONObject();

        Element rootElement = documentInitializeParser(tei);
        NodeList HeaderList = rootElement.getElementsByTagName("teiHeader");
        Element headitem = (Element) HeaderList.item(0);

        Element body = (Element) rootElement.getElementsByTagName("body").item(0);

        json.put("title", getTitle(headitem));
        json.put("abstract", getAbstract(headitem));
        json.put("keywords", getKeywords(headitem));
        json.put("chapters", getChapters(body));
        json.put("figures", getFigures(body));

//        System.out.println(json);
        return json;
    }

    private Element documentInitializeParser (String tei) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(tei)));
        Element rootElement = document.getDocumentElement();
        return rootElement;
    }

    private String getTitle(Element headitem) {
        NodeList list = headitem.getElementsByTagName("fileDesc");
        Element item = (Element) list.item(0);
        NodeList titleNode = item.getElementsByTagName("title");
        String title = titleNode.item(0).getTextContent();
//        System.out.println("title : " + title);
        return title;
    }

    private String getAbstract(Element headitem) {
        NodeList profList = headitem.getElementsByTagName("profileDesc");
        Element item = (Element) profList.item(0);
        NodeList list = item.getElementsByTagName("abstract");
        item = (Element) list.item(0);
        String abstr = "";
        if(item!=null) {
            list = item.getElementsByTagName("div");
            item = (Element) list.item(0);

            if (item != null) {
                list = item.getElementsByTagName("p");
                int len = list.getLength();
                for (int i = 0; i < len; i++) {
                    Element paragraph = (Element) list.item(i);
                    abstr += paragraph.getTextContent();
                }
            }
        }
//        System.out.println("abstract : " + abstr);
        return abstr;
    }

    private ArrayList<String> getKeywords (Element headitem) {
        NodeList terms = headitem.getElementsByTagName("term");
        int termCount = terms.getLength();
        ArrayList<String> keywords = new ArrayList<>();
        for (int i=0; i <termCount; i++) {
            Element term = (Element) terms.item(i);
            keywords.add(term.getTextContent());
        }
//        System.out.println("keywords : " + keywords);
        return keywords;
    }

    private JSONArray getChapters (Element body) {
        JSONArray chapters = new JSONArray();
        NodeList chaps = body.getElementsByTagName("div");
        int numberOfChapters = chaps.getLength();

        for (int ch = 0; ch < numberOfChapters; ch++) {
            int index = ch + 1;
            Element chapter = (Element) chaps.item(ch);
            chapters.add(getChapter(index, chapter));
        }

        return chapters;
    }

    private JSONObject getChapter(int index, Element chapter) {
        JSONObject chap = new JSONObject();
        chap.put("index", index);
        chap.put("head", getChapterHead(chapter));
        chap.put("paragraphs", getChapterParagraphs(chapter));
        return chap;
    }

    private String getChapterHead(Element chapter){
        Element head = (Element) chapter.getElementsByTagName("head").item(0);
        String headStr = "";
        if (head != null) {
            headStr += head.getTextContent();
        }
        return headStr;
    }

    private JSONArray getChapterParagraphs(Element chapter) {
        JSONArray pars = new JSONArray();
        NodeList paragraphs = chapter.getElementsByTagName("p");
        int numberOfParagraphs = paragraphs.getLength();

        for (int par = 0; par < numberOfParagraphs; par++) {
            int index = par + 1;
            Element paragraph = (Element) paragraphs.item(par);
            pars.add(getParagraph(index, paragraph));
        }
        return pars;
    }

    private JSONObject getParagraph(int index, Element paragraph) {
        JSONObject par = new JSONObject();

        par.put("index", index);

        String paragraphStr = paragraph.getTextContent();
        par.put("text", paragraphStr);

        StringTokenizer st = new StringTokenizer(paragraphStr);
        int wordCount = st.countTokens();
        par.put("size", wordCount);

        return par;
    }

    private JSONArray getFigures(Element body) {

        JSONArray figures = new JSONArray();

        NodeList figs = body.getElementsByTagName("figure");
        int figuresCount = figs.getLength();

        for (int i = 0; i < figuresCount; i++) {
            figures.add(getFigure(i, figs));
        }
        return figures;
    }

    private JSONObject getFigure(int index, NodeList figures) {

        JSONObject fig = new JSONObject();

        String txt = "";
        Element figure = (Element) figures.item(index);
        txt += getFigureHead(figure);
        txt += getFigureDesc(figure);
        txt += getFiguresTables(figure);

        fig.put("text", txt);
        return fig;
    }

    private String getFigureHead (Element figure) {
        Element head = (Element) figure.getElementsByTagName("head").item(0);
        String txt = "";
        if (head != null)
            txt += head.getTextContent() + " ";
        return txt;
    }
    private String getFigureDesc (Element figure) {
        String txt = "";
        Element figDesc = (Element) figure.getElementsByTagName("figDesc").item(0);
        if (figDesc != null)
            txt += figDesc.getTextContent() + " ";
        return txt;
    }

    private String getFiguresTables (Element figure) {
        String txt = "";
        NodeList tables = figure.getElementsByTagName("table");
        int tablesCount = tables.getLength();
        for (int ta = 0; ta < tablesCount; ta++) {
            Element table = (Element) tables.item(ta);
            txt += getTableTextContent(table);
        }

        return txt;
    }

    private String getTableTextContent(Element table) {
        String txt = "";
        NodeList rows = table.getElementsByTagName("row");
        int rowsCount = rows.getLength();
        for (int r = 0; r < rowsCount; r++) {
            Element row = (Element) rows.item(r);
            txt += getRowTextContent(row);

        }
         return txt + " ";
    }

    private String getRowTextContent(Element row) {
        String txt = "";
        if (row != null) {
            NodeList cells = row.getElementsByTagName("cell");
            int cellsCount = cells.getLength();
            for (int c = 0; c < cellsCount; c++) {
                Element cell = (Element) cells.item(c);
                txt += getCellTextContent(cell);
            }
            txt += row.getTextContent() + " ";
        }
        return txt;
    }

    private String getCellTextContent (Element cell) {
        String txt = "";
        if (cell != null) {
            txt += cell.getTextContent() + " ";
        }
        return txt;
    }

}
