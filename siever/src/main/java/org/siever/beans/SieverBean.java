package org.siever.beans;

import org.apache.camel.Exchange;
import org.grobid.core.engines.Engine;
import org.grobid.core.engines.config.GrobidAnalysisConfig;
import org.siever.models.*;
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
import java.util.UUID;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.xml.sax.SAXException;

public class SieverBean {

    public void sieve (Exchange exchange) throws Exception {
        DownloadPdf(exchange);
        extractTei(exchange);
        exchange.getIn().removeHeader("pdfPath");
    }

    private void extractTei (Exchange exchange) throws Exception {

        String pdfPath = exchange.getIn().getHeader("pdfPath", String.class);
        InputJob inputj = exchange.getIn().getBody(InputJob.class);
        Engine engine = exchange.getContext().getRegistry()
                .lookupByNameAndType("engine", Engine.class);
        File input = new File(pdfPath);

        PDDocument doc = Loader.loadPDF(input);
        int count = doc.getNumberOfPages();
        doc.close();

        GrobidAnalysisConfig conf = GrobidAnalysisConfig.builder().build();

        UUID uuid = UUID.randomUUID();

        String tei = engine.fullTextToTEI(input, conf);
        Result result = teiToResult(tei);
        result.setMetadata(inputj);
        result.setPageCount(count);
        result.setSieverID(uuid.toString());

        exchange.getIn().setBody(result, Result.class);

    }

    private void DownloadPdf (Exchange exchange) {

        InputJob input = exchange.getIn().getBody(InputJob.class);
        String url = input.getUrl();
        String fileName = input.getId() + ".pdf";

        Engine engine = exchange.getContext().getRegistry()
                .lookupByNameAndType("engine",Engine.class);

        String dest = exchange.getContext().getRegistry()
                .lookupByNameAndType("destination", String.class);

        engine.downloadPDF(url, dest, fileName);

        String pdfPath = dest + "/" + fileName;

        //remove the following line in order to use the dowloaded pdf as an input for the siever
//        pdfPath = "/home/anastasis/Desktop/Muscle_hypertrophy.pdf";

        exchange.getIn().setHeader("pdfPath", pdfPath);
        exchange.getIn().setBody(input, InputJob.class);

    }

    public void outputMessage(Exchange exchange) {
        Result result = exchange.getIn().getBody(Result.class);
        String sieverID = result.getSieverID();
        OutputJob output = new OutputJob();
        output.setSieverID(sieverID);
        exchange.getIn().setBody(output, OutputJob.class);
    }

    private Result teiToResult (String tei) throws ParserConfigurationException, IOException, SAXException {
        Result result = new Result();

        Element rootElement = documentInitializeParser(tei);
        NodeList HeaderList = rootElement.getElementsByTagName("teiHeader");
        Element headitem = (Element) HeaderList.item(0);

        Element body = (Element) rootElement.getElementsByTagName("body").item(0);

        result.setTitle(getTitle(headitem));
        result.setAbstrct(getAbstract(headitem));
        result.setKeywords(getKeywords(headitem));
        result.setChapters(getChapters(body));
        result.setFigures(getFigures(body));

        return result;
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
        return keywords;
    }

    private ArrayList<Chapter> getChapters (Element body) {
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();
        NodeList chaps = body.getElementsByTagName("div");
        int numberOfChapters = chaps.getLength();

        for (int ch = 0; ch < numberOfChapters; ch++) {
            int index = ch + 1;
            Element chapter = (Element) chaps.item(ch);
            chapters.add(getChapter(index, chapter));
        }

        return chapters;
    }

    private Chapter getChapter(int index, Element chapter) {
        Chapter chap = new Chapter();
        chap.setHead(getChapterHead(chapter));
        chap.setIndex(index);
        chap.setParagraphs(getChapterParagraphs(chapter));

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

    private ArrayList<Paragraph> getChapterParagraphs(Element chapter) {
        ArrayList<Paragraph> pars = new ArrayList<Paragraph>();
        NodeList paragraphs = chapter.getElementsByTagName("p");
        int numberOfParagraphs = paragraphs.getLength();

        for (int par = 0; par < numberOfParagraphs; par++) {
            int index = par + 1;
            Element paragraph = (Element) paragraphs.item(par);
            pars.add(getParagraph(index, paragraph));
        }
        return pars;
    }

    private Paragraph getParagraph(int index, Element paragraph) {
        Paragraph par = new Paragraph();

        par.setIndex(index);

        String paragraphStr = paragraph.getTextContent();
        par.setText(paragraphStr);

        StringTokenizer st = new StringTokenizer(paragraphStr);
        int wordCount = st.countTokens();
        par.setSize(wordCount);

        return par;
    }

    private ArrayList<Figure> getFigures(Element body) {

        ArrayList<Figure> figures = new ArrayList<Figure>();

        NodeList figs = body.getElementsByTagName("figure");
        int figuresCount = figs.getLength();

        for (int i = 0; i < figuresCount; i++) {
            figures.add(getFigure(i, figs));
        }
        return figures;
    }

    private Figure getFigure(int index, NodeList figures) {

        Figure fig = new Figure();

        String txt = "";
        Element figure = (Element) figures.item(index);
        txt += getFigureHead(figure);
        txt += getFigureDesc(figure);
        txt += getFiguresTables(figure);

        fig.setText(txt);
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
