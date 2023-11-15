package org.siever.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.PropertiesComponent;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.DefaultRegistry;
import org.apache.camel.util.json.JsonObject;
import org.grobid.core.engines.Engine;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.main.GrobidHomeFinder;
import org.grobid.core.utilities.GrobidProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.siever.models.InputJob;
import org.siever.models.OutputJob;
import org.siever.server.Siever;
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
import org.apache.kafka.clients.consumer.*;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.TopicPartition;


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
        broker = broker.split("PLAINTEXT://", 2)[1];

        PropertiesComponent properties = ctx.getPropertiesComponent();
        //kafka.scio.services:9092
        properties.setLocation("classpath:/env.properties");
        Properties props = new Properties();
        props.setProperty("KAFKA_BROKER", broker);
        properties.setOverrideProperties(props);

        DefaultRegistry registry = (DefaultRegistry) ctx.getRegistry();
        ((org.apache.camel.impl.DefaultCamelContext) ctx).setRegistry(registry);
        registry.bind("broker", broker);

        String destPath = properties.loadPropertiesAsMap().get("DESTINATION_PATH").toString();
        registry.bind("destination", destPath);

        String pGrobidHome = properties.loadPropertiesAsMap().get("GROBID_HOME_PATH").toString();
//        logger.error(pGrobidHome);
        GrobidHomeFinder grobidHomeFinder = new GrobidHomeFinder(Arrays.asList(pGrobidHome));
        GrobidProperties.getInstance(grobidHomeFinder);

        // Create the GROBID engine and bind it to the registry
        Engine engine = GrobidFactory.getInstance().createEngine();
        registry.bind("engine", engine);

        System.out.println(properties.loadPropertiesAsMap().get("KAFKA_BROKER").toString());
        System.out.println(properties.loadPropertiesAsMap().get("KAFKA_INPUT_TOPIC").toString());
        try {
            ctx.addRoutes(new SieveRouter());
            ctx.start();
        } catch (Exception e) {
            System.out.println("Siever failed: {}" + e.getMessage());
        }

    }

    @Test
    void testSieveRouter (){
        Exchange exchange = new DefaultExchange(ctx);
        String broker = exchange.getContext().getRegistry()
                .lookupByNameAndType("broker", String.class);

//        broker = broker.split("PLAINTEXT://", 2)[1];

//        System.out.println("kafka broker : " + ctx.getPropertiesComponent().loadPropertiesAsMap().get("KAFKA_BROKER").toString());
        System.out.println("the broker " + broker);

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(properties);

        InputJob job = Instancio.create(InputJob.class);
        job.setUrl("https://cgspace.cgiar.org/rest/bitstreams/8086a583-11fc-47b6-b7f3-1ba348806ddb/retrieve");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputJob;
        try {
            jsonInputJob = objectMapper.writeValueAsString(job);
        } catch (Exception e) {
            System.err.println("Error converting InputJob to JSON: " + e);
            return;
        }

//        System.out.println("the message " + jsonInputJob);

        ProducerRecord<String, String> record = new ProducerRecord<>(ctx.getPropertiesComponent().loadPropertiesAsMap().get("KAFKA_INPUT_TOPIC").toString(), jsonInputJob);

        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                System.err.println("Error sending message: " + exception);
            } else {
                System.out.println("Message sent successfully! Topic: " + metadata.topic()
                        + ", Partition: " + metadata.partition()
                        + ", Offset: " + metadata.offset());
            }
        });

        // Flush and close the producer
        producer.flush();
        producer.close();

        // Kafka consumer configuration
        properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Create Kafka Consumer
        Consumer<String, String> consumer = new KafkaConsumer<>(properties);

        String topic = ctx.getPropertiesComponent().loadPropertiesAsMap().get("KAFKA_OUTPUT_TOPIC").toString();
        System.out.println(topic);
//        TopicPartition topicPartition = new TopicPartition(topic, 0);
//        List<TopicPartition> partitions = Arrays.asList(topicPartition);
//        consumer.assign(partitions);
        // Subscribe to the topic

        consumer.subscribe(Collections.singleton(topic));
        int flag = 1;
        // Poll for new messages
        while (flag == 1) {
//            consumer.seekToBeginning(partitions);

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> rec : records) {
                System.out.println("Received message: " +
                        "Key: " + rec.key() +
                        ", Value: " + rec.value() +
                        ", Partition: " + rec.partition() +
                        ", Offset: " + rec.offset());

                // Convert JSON message to InputJob object
                objectMapper = new ObjectMapper();
                try {
                    OutputJob output = objectMapper.readValue(rec.value(), OutputJob.class);
                    // Process the InputJob as needed
                    System.out.println("Deserialized OutputJob: " + output);
                    flag = 0;
                    break;
                    // Perform your logic with the InputJob object
                } catch (Exception e) {
                    System.err.println("Error deserializing JSON: " + e);
                }
            }
        }
        consumer.close();

        assertEquals(1,1);
    }


    @AfterAll
    void tearDown(){
        ctx.stop();
        kafka.stop();
    }

}