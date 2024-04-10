package org.siever.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.consumer.KafkaManualCommit;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.kafka.clients.consumer.Consumer;
import org.siever.beans.SieverBean;
import org.siever.models.InputJob;
import org.siever.models.OutputJob;
import org.siever.models.Result;
import org.siever.processors.ExceptionHandler;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.HashMap;
import java.util.UUID;

public class SieveRouter extends RouteBuilder {

    public void configure() throws Exception {

        onException(S3Exception.class)
                .onWhen(exceptionMessage().contains("404"))
                    .handled(true)
                    .log("File does not exist in S3 bucket")
                    .to("direct:siever_work");

        onException(Exception.class)
                .handled(true)
                .process(new ExceptionHandler())
                .marshal().json()
                .to("file:///home/anastasis/Desktop/sieverErrors");


        from("kafka:{{KAFKA_INPUT_TOPIC}}?brokers={{KAFKA_BROKER}}" +
//                "&autoCommitEnable=false&allowManualCommit=true" +
                "&maxPollIntervalMs=1500000&pollTimeoutMs=20000" +
                "&autoOffsetReset=earliest&maxPollRecords=1&breakOnFirstError=false")

                .routeId("kafka_consumer")
                .unmarshal().json(JsonLibrary.Jackson, InputJob.class)
                .setHeader(AWS2S3Constants.KEY,
                        simple("${in.body.id}.json"))
                .marshal().json(JsonLibrary.Jackson, InputJob.class)
                .toD("aws2-s3://{{AWS_BUCKET}}?" +
                        "accessKey=RAW({{AWS_S3_ACCESS_KEY}})&" +
                        "secretKey=RAW({{AWS_S3_SECRET_KEY}})&" +
                        "region={{AWS_REGION}}&" +
                        "deleteAfterWrite=false&" +
                        "deleteAfterRead=false&" +
                        "operation=getObject")
//                .choice()
//                    .when(body().isNull())
//                        .log("den uparxei")
//                    .otherwise()
//                        .log("uparxei")
//                    .end()
//                .process(new RestoreOriginalInput())
//                .setHeader("url", simple("${in.body.url}"))
//                .setHeader(AWS2S3Constants.KEY,
//                        simple("${in.body.id}.json"))
//                .bean(SieverBean.class, "sieve")
////                .log("${in.body}")
//                .marshal().json(JsonLibrary.Jackson, Result.class)
//                .toD("aws2-s3://{{AWS_BUCKET}}?" +
//                        "accessKey=RAW({{AWS_S3_ACCESS_KEY}})&" +
//                        "secretKey=RAW({{AWS_S3_SECRET_KEY}})&" +
//                        "region={{AWS_REGION}}&" +
//                        "deleteAfterWrite=false&" +
//                        "deleteAfterRead=false")
                .unmarshal().json(JsonLibrary.Jackson, Result.class)
                .setHeader(KafkaConstants.KEY, simple("${in.body.sieverID}"))
                .bean(SieverBean.class, "outputMessage")
                .marshal().json(JsonLibrary.Jackson, OutputJob.class)
                .to("kafka:{{KAFKA_OUTPUT_TOPIC}}?brokers={{KAFKA_BROKER}}");


        from("direct:siever_work")
            .routeId("siever_worker")
            .unmarshal().json(JsonLibrary.Jackson, InputJob.class)
            .setHeader("url", simple("${in.body.url}"))
            .setHeader(AWS2S3Constants.KEY,
                    simple("${in.body.id}.json"))
            .bean(SieverBean.class, "sieve")
//                .log("${in.body}")
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


//        from("kafka:{{KAFKA_INPUT_TOPIC}}?brokers={{KAFKA_BROKER}}")
//                .routeId("kafka_consumer")
//                .unmarshal().json(JsonLibrary.Jackson, InputJob.class)
//                .process(new Processor() {
//                    @Override
//                    public void process(Exchange exchange) throws Exception {
//                        Long offset = exchange.getIn().getHeader("kafka.OFFSET", Long.class);
//                        System.out.println(offset);
//                    }
//                })
////                .bean(SieverBean.class, "sieve")
////                .log("${in.body}")
//                .setHeader(AWS2S3Constants.KEY,
//                        simple("${in.header.kafka.OFFSET}.json"))
//                .marshal().json(JsonLibrary.Jackson, InputJob.class)
//                .toD("aws2-s3://{{AWS_BUCKET}}?" +
//                        "accessKey=RAW({{AWS_S3_ACCESS_KEY}})&" +
//                        "secretKey=RAW({{AWS_S3_SECRET_KEY}})&" +
//                        "region={{AWS_REGION}}&" +
//                        "deleteAfterWrite=false&" +
//                        "deleteAfterRead=false")
//                .unmarshal().json(JsonLibrary.Jackson, InputJob.class)
//                .setHeader(KafkaConstants.KEY, simple("${in.body.id}"))
////                .bean(SieverBean.class, "outputMessage")
//                .marshal().json(JsonLibrary.Jackson, InputJob.class)
//                .to("kafka:{{KAFKA_OUTPUT_TOPIC}}?brokers={{KAFKA_BROKER}}");



//        from("kafka:{{KAFKA_INPUT_TOPIC}}?brokers={{KAFKA_BROKER}}")
//                .routeId("kafka_consumer_test")
//                .unmarshal().json(JsonLibrary.Jackson, InputJob.class)
//                .process(new Processor() {
//                    @Override
//                    public void process(Exchange exchange) throws Exception {
//                        InputJob inputj = exchange.getIn().getBody(InputJob.class);
//                        UUID filename = UUID.randomUUID();
//                        exchange.getIn().setHeader("CamelFileName", filename);
//                        Long offset = exchange.getIn().getHeader("kafka.OFFSET", Long.class);
////                        System.out.println(offset + " " + filename);
////                        System.out.println(filename);
//                    }
//                })
//                .marshal().json()
//                .to("file:///home/anastasis/Desktop/siverTest/");

    }

}