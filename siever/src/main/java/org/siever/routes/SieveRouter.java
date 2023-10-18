package org.siever.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.siever.beans.TeiExtractorBean;
import org.siever.beans.UrlDownloaderBean;
import org.siever.models.InputJob;

public class SieveRouter extends RouteBuilder {

    public void configure() throws Exception {

//        from ("direct:sieve")
//                .routeId("siever")
//                .bean(UrlDownloaderBean.class, "pdfPath")
//                .bean(TeiExtractorBean.class, "extractTei");

        from("kafka:test_siever_topic?brokers=kafka.scio.services:9092")
                .routeId("kafka_consumer")
                .unmarshal().json(JsonLibrary.Jackson, InputJob.class)
//                .bean(UrlDownloaderBean.class, "printTest")
                .bean(UrlDownloaderBean.class, "pdfPath")
                .bean(TeiExtractorBean.class, "extractTei");
    }

}
