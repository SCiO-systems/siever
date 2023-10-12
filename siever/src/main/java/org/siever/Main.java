package org.siever;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.grobid.core.data.BiblioItem;
import org.grobid.core.document.DocumentSource;
import org.grobid.core.engines.config.GrobidAnalysisConfig;
import org.grobid.core.main.GrobidHomeFinder;
import org.grobid.core.utilities.GrobidProperties;
import org.grobid.core.engines.Engine;
//import org.grobid.core.document.Document;
import org.grobid.core.factory.GrobidFactory;
import java.io.File;
import java.util.StringTokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        try {

            String pGrobidHome = "/home/anastasis/Desktop/grobid-0.7.3/grobid-home";
//            Properties prop = new Properties();
//            prop.load(new FileInputStream("grobidExp.properties"));
//            String pGrobidHome = prop.getProperty("grobid_example.pGrobidHome");
            // Set the GROBID home directory
            GrobidHomeFinder grobidHomeFinder = new GrobidHomeFinder(Arrays.asList(pGrobidHome));
            GrobidProperties.getInstance(grobidHomeFinder);

            // Create the GROBID engine
            Engine engine = GrobidFactory.getInstance().createEngine();

//            engine.downloadPDF("https://cgi.di.uoa.gr/~izambo/K10Java.pdf", "/home/anastasis/Desktop/downloadedpdfs", "example.pdf");

//            String pdfPathex = "/home/anastasis/Desktop/downloadedpdfs/example.pdf";

            // PDF or input string to process
            String pdfPath = "/home/anastasis/Desktop/Political_science.pdf";
            File input = new File(pdfPath);
//            File input = new File(pdfPathex);

            // Biblio object for the result
//            BiblioItem resHeader = new BiblioItem();
//            String tei = engine.processHeader(pdfPath, 1, resHeader);
//            // Process the PDF and get the bibliographic data
//            Document document = engine.processAuthorsHeader(pdfPath, new Document());
//
//            // Access the bibliographic data
//            String title = document.getTitle();
//            // Access more bibliographic data as needed

            GrobidAnalysisConfig conf = GrobidAnalysisConfig.builder().build();

//            DocumentSource docSource = DocumentSource.fromPdf(input);
            String tei2 = engine.fullTextToTEI(input, conf);
//            String tei2 = document.getTei();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(tei2)));
            Element rootElement = document.getDocumentElement();
            NodeList HeaderList = rootElement.getElementsByTagName("teiHeader");
            Element headitem = (Element) HeaderList.item(0);
            NodeList list = headitem.getElementsByTagName("fileDesc");
            Element item = (Element) list.item(0);
            NodeList titleNode = item.getElementsByTagName("title");
            String title = titleNode.item(0).getTextContent();
            NodeList profList = headitem.getElementsByTagName("profileDesc");
            item = (Element) profList.item(0);
            list = item.getElementsByTagName("abstract");
            item = (Element) list.item(0);
            if(item!=null) {
                list = item.getElementsByTagName("div");
                item = (Element) list.item(0);

                if (item != null) {
                    list = item.getElementsByTagName("p");
                    int len = list.getLength();
                    String abstr = "";
                    for (int i = 0; i < len; i++) {
                        Element paragraph = (Element) list.item(i);
                        abstr += paragraph.getTextContent();
                    }
                    System.out.println(abstr);
                }
            }

            NodeList terms = headitem.getElementsByTagName("term");
            int termCount = terms.getLength();
            ArrayList<String> keywords = new ArrayList<>();
            for (int i=0; i <termCount; i++) {
                Element term = (Element) terms.item(i);
                keywords.add(term.getTextContent());
            }
            System.out.println(title);
            System.out.println(keywords);

            Element body = (Element) rootElement.getElementsByTagName("body").item(0);
            NodeList chapters = body.getElementsByTagName("div");
            int numberOfChapters = chapters.getLength();

            for (int ch = 0; ch < numberOfChapters; ch++) {
                int index = ch + 1;
                Element chapter = (Element) chapters.item(ch);
                Element head = (Element) chapter.getElementsByTagName("head").item(0);
                String headStr = "";
                if(head != null) {
                    headStr += head.getTextContent();
                }
                NodeList paragraphs = chapter.getElementsByTagName("p");
                int numberOfParagraphs = paragraphs.getLength();

                System.out.println(index);
                System.out.println(headStr);
                System.out.println("paragraphs : \n");

                for (int par = 0; par < numberOfParagraphs; par++) {
                    Element paragraph = (Element) paragraphs.item(par);
                    int parIndex = par + 1;
                    String paragraphStr = paragraph.getTextContent();
                    StringTokenizer st = new StringTokenizer(paragraphStr);
                    int wordCount = st.countTokens();
                    System.out.println(paragraphStr);
                    System.out.println(parIndex);
                    System.out.println(wordCount);
                }

                System.out.println("\n\n");
            }

            NodeList figures = body.getElementsByTagName("figure");
            int figuresCount = figures.getLength();

            for (int fi=0; fi<figuresCount; fi++) {
                String txt = "";
                Element figure = (Element) figures.item(fi);
                Element head = (Element) figure.getElementsByTagName("head").item(0);
                if (head != null)
                    txt += head.getTextContent() + " ";
                Element figDesc = (Element) figure.getElementsByTagName("figDesc").item(0);
                if (figDesc != null)
                    txt += figDesc.getTextContent() + " ";
                NodeList tables = figure.getElementsByTagName("table");
                int tablesCount = tables.getLength();
                for (int ta=0; ta<tablesCount; ta++) {
                    Element table = (Element) tables.item(ta);
                    NodeList rows = table.getElementsByTagName("row");
                    int rowsCount = rows.getLength();
                    for(int r=0; r<rowsCount; r++) {
                        Element row = (Element) rows.item(r);
                        if (row!=null){
                            NodeList cells = row.getElementsByTagName("cell");
                            int cellsCount = cells.getLength();
                            for (int c=0; c<cellsCount; c++) {
                                Element cell = (Element) cells.item(c);
                                if (cell != null) {
                                    txt += cell.getTextContent() + " ";
                                }
                            }
                            txt += row.getTextContent() + " ";
                        }
                    }
                }

                System.out.println(txt);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}