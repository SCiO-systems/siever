package org.siever.beans;

import org.apache.camel.Exchange;
import org.grobid.core.engines.Engine;
import org.siever.models.InputJob;

public class UrlDownloaderBean {

    public void pdfPath (Exchange exchange) {

        InputJob input = new InputJob();
        input = exchange.getIn().getBody(InputJob.class);
        String url = input.getUrl();
        String fileName = input.getId() + ".pdf";
//        System.out.println(url);

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

}
