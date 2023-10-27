//package org.siever.routes;
//
//import org.junit.jupiter.api.Test;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.utility.DockerImageName;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.apache.camel.CamelContext;
//import org.apache.camel.RoutesBuilder;
//import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.test.junit5.CamelTestSupport;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.junit.jupiter.api.Test;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import java.util.Properties;
//
//@Testcontainers
//public class CamelKafkaListenerTest extends CamelTestSupport {
//
//    @Container
//    public static final KafkaContainer kafka = new KafkaContainer();
//
//    @Override
//    protected RoutesBuilder createRouteBuilder() throws Exception {
//        return new RouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                from("kafka:your-topic?brokers=" + kafka.getBootstrapServers())
//                        .to("mock:result");
//            }
//        };
//    }
//
//    @Test
//    public void testCamelKafkaListener() {
//        // Create a Kafka producer to send test messages
//        Properties producerProps = new Properties();
//        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
//        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//
//        Producer<String, String> producer = new KafkaProducer<>(producerProps);
//        producer.send(new ProducerRecord<>("your-topic", "Test Message"));
//
//        // Perform assertions using Camel's mock endpoints
//        assertMockEndpointsSatisfied();
//    }
//}
//
//class SieveRouterTest {
//
//    @Test
//    public void test1 () {
////        final var factory = new CPTestContainerFactory();
//        KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));
//    }
//}