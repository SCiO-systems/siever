package org.siever.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.siever.beans.SieverBean;
import org.siever.models.InputJob;
import org.siever.models.OutputJob;
import org.siever.models.Result;

public class SieveRouter extends RouteBuilder {

    public void configure() throws Exception {

        from("kafka:{{KAFKA_INPUT_TOPIC}}?brokers={{KAFKA_BROKER}}")
                .routeId("kafka_consumer")
                .unmarshal().json(JsonLibrary.Jackson, InputJob.class)
                .bean(SieverBean.class, "sieve")
//                .log("${in.body}")
                .setHeader(AWS2S3Constants.KEY,
                        simple("${in.body.sieverID}.json"))
                .marshal().json(JsonLibrary.Jackson, Result.class)
                .toD("aws2-s3://{{AWS_BUCKET}}?" +
                        "accessKey=RAW({{AWS_S3_ACCESS_KEY}})&" +
                        "secretKey=RAW({{AWS_S3_SECRET_KEY}})&" +
                        "region={{AWS_REGION}}&" +
                        "deleteAfterWrite=false&" +
                        "deleteAfterRead=false")
                .unmarshal().json(JsonLibrary.Jackson, Result.class)
                .setHeader(KafkaConstants.KEY, simple("${in.body.sieverID}"))
                .bean(SieverBean.class, "outputMessage")
                .marshal().json(JsonLibrary.Jackson, OutputJob.class)
                .to("kafka:{{KAFKA_OUTPUT_TOPIC}}?brokers={{KAFKA_BROKER}}");
    }

}
