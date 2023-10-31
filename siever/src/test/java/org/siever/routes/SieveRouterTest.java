package org.siever.routes;

import org.apache.camel.Exchange;
import org.apache.camel.spi.PropertiesComponent;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.DefaultRegistry;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import org.apache.camel.impl.DefaultCamelContext;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Properties;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SieveRouterTest {


    CamelContext ctx;
    KafkaContainer kafka;

    @BeforeAll
    void setUp(){
        ctx = new DefaultCamelContext();
        kafka = new KafkaContainer((DockerImageName.parse("confluentinc/cp-kafka:7.5.0")));
        kafka.start();
        String broker =  kafka.getBootstrapServers().toString();

        PropertiesComponent properties = ctx.getPropertiesComponent();
        properties.setLocation("classpath:/env.properties");
        Properties props = new Properties();
//        props.setProperty("KAFKA_INPUT_TOPIC", "test_topic");
        props.setProperty("KAFKA_BROKER", broker);
        properties.setOverrideProperties(props);

        DefaultRegistry registry = (DefaultRegistry) ctx.getRegistry();
        ((org.apache.camel.impl.DefaultCamelContext) ctx).setRegistry(registry);
        registry.bind("broker", broker);
    }

    @Test
    void testSieveRouter (){
        Exchange exchange = new DefaultExchange(ctx);
        String broker = exchange.getContext().getRegistry()
                .lookupByNameAndType("broker", String.class);


        System.out.println("edw ame " + ctx.getPropertiesComponent().loadPropertiesAsMap().get("KAFKA_BROKER").toString());
        assertEquals(1,1);
    }

    @AfterAll
    void tearDown(){
        ctx.stop();
        kafka.stop();
    }

}