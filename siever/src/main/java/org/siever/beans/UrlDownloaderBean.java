package org.siever.beans;

import org.apache.camel.Exchange;
import org.grobid.core.engines.Engine;
import org.siever.models.InputJob;

public class UrlDownloaderBean {

    public void pdfPath (Exchange exchange) {

        InputJob input = new InputJob();
        input = exchange.getIn().getBody(InputJob.class);
        String url = input.getUrl();
//        System.out.println(url);

        Engine engine = exchange.getContext().getRegistry()
                .lookupByNameAndType("engine",Engine.class);

        String dest = exchange.getContext().getRegistry()
                .lookupByNameAndType("destination", String.class);

        engine.downloadPDF(url, dest, "example.pdf");

        String pdfPath = dest + "/downloaded.pdf";

        System.out.println(pdfPath);

        pdfPath = "/home/anastasis/Desktop/Muscle_hypertrophy.pdf";

        exchange.getIn().setHeader("pdfPath", pdfPath);
        exchange.getIn().setBody(input, InputJob.class);

    }

//    public String printTest (Exchange exchange) {
//        InputJob input = new InputJob();
//        input = exchange.getIn().getBody(InputJob.class);
//        System.out.println(input.toString() );
//        return input.toString();
//    }

}
