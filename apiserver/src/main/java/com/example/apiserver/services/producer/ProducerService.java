package com.example.apiserver.services.producer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import jakarta.annotation.PreDestroy;

@Service
public class ProducerService {
    private String clientId = (System.getenv("CLIENT_ID") != null) ? System.getenv("CLIENT_ID") : "jackpot-server-producer";
    private String configType = (System.getenv("CONFIG_TYPE") != null) ? System.getenv("CONFIG_TYPE") : "FILE"; 
    private String configFile = (System.getenv("CONFIG") != null) ? System.getenv("CONFIG_FILE") : "server.properties";
    private KafkaProducer<String, GenericRecord> producer;
    
    @EventListener(ApplicationStartedEvent.class)
    private void initClient() throws ConfigException, IOException {
        Properties props = new Properties();
        if (configType.equals("FILE")) {
            addPropsFromFile(props, configFile);
        } else {
            preInitChecks();
            props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, System.getenv("BOOTSTRAP_SERVERS"));
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule   required username='"+System.getenv("KAFKA_KEY")+"'   password='"+System.getenv("KAFKA_SECRET")+"';");
            props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, System.getenv("SCHEMA_REGISTRY_URL"));
            props.put(AbstractKafkaSchemaSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE, "USER_INFO");
            props.put(AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG, System.getenv("SCHEMA_REGISTRY_KEY")+":"+System.getenv("SCHEMA_REGISTRY_SECRET"));
        }
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, false);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);    
        producer = new KafkaProducer<>(props);
    }

    /**
     * Create and send an event to Kafka.
     * 
     * @param key - A String key for the event to be produced to Kafka.
     * @param value - An Object value for the event to be produced to Kafka.
     * @param topic - A String of the topc to send the event. 
     * @return A Future that has the RecordMetadata if the event is successfully produced. 
     */
    public Future<RecordMetadata> send(String key, GenericRecord value, String topic) {
        ProducerRecord<String, GenericRecord> producerRecord = new ProducerRecord<String, GenericRecord>(topic, key, value);
        return producer.send(producerRecord);
    }

    /**
     * Check for the necessary configurations to initialize the Ksql client. If any are missing, fails. 
     * 
     * @throws ConfigException
     */
    private void preInitChecks() throws ConfigException {
        ArrayList<String> requiredProps = new ArrayList<String>(Arrays.asList("BOOTSTRAP_SERVERS", "KAFKA_KEY", "KAFKA_SECRET", "SCHEMA_REGISTRY_URL", "SCHEMA_REGISTRY_KEY", "SCHEMA_REGISTRY_SECRET"));
        ArrayList<String> missingProps = new ArrayList<String>();
        for (String prop : requiredProps) {
            if (System.getenv(prop).equals(null)) {
                missingProps.add(prop);
            }
        }
        if (missingProps.size() > 0) {
            throw new ConfigException("Missing required properties: " + missingProps.toString());
        }
    }

    /**
     * Load properties from an application properties file.
     * 
     * @param props - An existing Properties object to add the properties to.
     * @param file - An existing file containing properties to add to the Properties object. 
     * @throws IOException
     */
    private static void addPropsFromFile(Properties props, String file) throws IOException {
        if (!Files.exists(Paths.get(file))) {
            throw new IOException("Ksql config file (" + file + ") does not exist or was not found.");
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            props.load(inputStream);
        }
    }
    
    @PreDestroy
    private void closeProducer() {
        producer.close();
    }
}
