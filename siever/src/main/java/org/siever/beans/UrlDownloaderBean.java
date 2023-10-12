package org.siever.beans;

import org.apache.camel.Exchange;
import org.grobid.core.engines.Engine;

public class UrlDownloaderBean {

    public String pdfPath (Exchange exchange) {

        String url = exchange.getIn().getBody(String.class);
        Engine engine = exchange.getContext().getRegistry()
                .lookupByNameAndType("engine",Engine.class);

        String dest = exchange.getContext().getRegistry()
                .lookupByNameAndType("destination", String.class);

        engine.downloadPDF(url, dest, "example.pdf");

        String pdfPath = dest + "/example.pdf";

        pdfPath = "/home/anastasis/Desktop/Muscle_hypertrophy.pdf";
        exchange.getIn().setBody(pdfPath, String.class);

        return pdfPath;
    }
}
