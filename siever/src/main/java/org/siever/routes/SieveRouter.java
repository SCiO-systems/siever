package org.siever.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.siever.beans.TeiExtractorBean;
import org.siever.beans.UrlDownloaderBean;
import org.siever.models.InputJob;
import org.siever.models.Result;

public class SieveRouter extends RouteBuilder {

    public void configure() throws Exception {

        from("kafka:test_siever_topic?brokers=kafka.scio.services:9092")
                .routeId("kafka_consumer")
                .unmarshal().json(JsonLibrary.Jackson, InputJob.class)
                .bean(UrlDownloaderBean.class, "pdfPath")
                .bean(TeiExtractorBean.class, "extractTei")
                .setHeader(KafkaConstants.KEY, simple("${in.body.sieverID}"))
                .marshal().json(JsonLibrary.Jackson, Result.class)
//                .log("${in.body}")
                .to("kafka:test_siever_results?brokers=kafka.scio.services:9092");
    }

}
