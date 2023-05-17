package com.example.apiserver.services.ksql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.common.config.ConfigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.apiserver.repository.JackpotRepository;

import io.confluent.ksql.api.client.BatchedQueryResult;
import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import io.confluent.ksql.api.client.Row;
import io.confluent.ksql.api.client.StreamedQueryResult;
import jakarta.annotation.PreDestroy;

@Service
public class KsqlService {
    private String configType = (System.getenv("CONFIG_TYPE") != null) ? System.getenv("CONFIG_TYPE") : "FILE"; 
    private String configFile = (System.getenv("CONFIG") != null) ? System.getenv("CONFIG_FILE") : "server.properties";
    private Client ksqlClient;

    @Autowired
    JackpotRepository jackpotRepository;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @EventListener(ApplicationStartedEvent.class)
    private void initClient() throws ConfigException, IOException {
        Properties props = new Properties();
        if (configType.equals("FILE")) {
            addPropsFromFile(props, configFile);
        } else {
            preInitChecks();
            props.put("ksql.url", System.getenv("KSQL_URL"));
            props.put("ksql.port", System.getenv("KSQL_PORT"));
            props.put("ksql.key", System.getenv("KSQL_KEY"));
            props.put("ksql.secret", System.getenv("KSQL_SECRET"));
        }
        ClientOptions ksqlClientOptions = ClientOptions.create()
            .setHost(props.getProperty("ksql.url"))
            .setPort(Integer.valueOf(props.getProperty("ksql.port")))
            .setBasicAuthCredentials(props.getProperty("ksql.key"), props.getProperty("ksql.secret"))
            .setUseTls(true)
            .setUseAlpn(true);
        ksqlClient = Client.create(ksqlClientOptions);
        getJackpots();
    }
    private void getJackpots() {
        pushQuery("SELECT * FROM JACKPOTS EMIT CHANGES;")
            .thenAccept(result -> {
                PushQuerySubscriber subscriber = new PushQuerySubscriber(jackpotRepository, simpMessagingTemplate);
                result.subscribe(subscriber);
            }).exceptionally(error -> {
                // Do an error thing
                System.out.println(error.getMessage());
                error.printStackTrace();
                return null;
            });
    }

    /**
     * Places a Ksql pull query to the currently configured instance of Ksql and returns any resulting rows. 
     * 
     * @param query - A string representing a valid Ksql pull query.
     * @return The resulting rows.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public List<Row> pullQuery(String query) throws InterruptedException, ExecutionException {
        BatchedQueryResult batchedQueryResult = ksqlClient.executeQuery(query);
        return batchedQueryResult.get();
    }

    /**
     * Places a Ksql push query to the currently configured instance of Ksql and returns a completable future object to capture emitted events. 
     * 
     * @param query - A string representing a valid Ksql push query. 
     * @return An object containing completeable future that represent result rows from the input query.
     */
    public CompletableFuture<StreamedQueryResult> pushQuery(String query) {
        Map<String, Object> properties = Collections.singletonMap("auto.offset.reset", "earliest");
        return ksqlClient.streamQuery(query, properties);
    }

    /**
     * Check for the necessary configurations to initialize the Ksql client. If any are missing, fails. 
     * 
     * @throws ConfigException
     */
    private void preInitChecks() throws ConfigException {
        ArrayList<String> requiredProps = new ArrayList<String>(Arrays.asList("KSQL_URL", "KSQL_KEY", "KSQL_SECRET"));
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
    private void closeClient() {
        ksqlClient.close();
    }
}
