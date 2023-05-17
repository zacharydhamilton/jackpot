package com.example.apiserver.services.ksql;

import org.apache.log4j.Logger;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.example.apiserver.objects.Jackpot;
import com.example.apiserver.repository.JackpotEntity;
import com.example.apiserver.repository.JackpotRepository;
import com.google.gson.Gson;

import io.confluent.ksql.api.client.KsqlArray;
import io.confluent.ksql.api.client.Row;

public class PushQuerySubscriber implements Subscriber<Row> {
    Logger logger = Logger.getLogger(PushQuerySubscriber.class);
    private Gson gson = new Gson();
    private Subscription subscription;
    private JackpotRepository repository;
    private SimpMessagingTemplate channel;

    public PushQuerySubscriber(JackpotRepository repository, SimpMessagingTemplate channel) {
        this.repository = repository;
        this.channel = channel;
    }

    @Override
    public synchronized void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public synchronized void onNext(Row row) {
        logger.info("Rceived a row: "+ row.values());
        KsqlArray values = row.values();
        String jackpotPoolId = values.getString(0);
        String region = values.getString(1);
        int jackpot = values.getInteger(2);
        JackpotEntity entity = new JackpotEntity(new Jackpot(jackpotPoolId, region, jackpot));
        repository.save(entity);
        channel.convertAndSend("/topic/jackpot", gson.toJson(entity));
        channel.convertAndSend("/topic/jackpot/"+jackpotPoolId, gson.toJson(entity));
        subscription.request(1);
    }

    @Override
    public synchronized void onError(Throwable throwable) {
        // TODO Do error handling
        logger.error("Recieved an error: " + throwable);
        // 7444571 [vert.x-eventloop-thread-2] ERROR com.example.apiserver.services.ksql.PushQuerySubscriber  - Recieved an error: java.lang.Exception: io.vertx.core.VertxException: Connection was closed
    }

    @Override
    public synchronized void onComplete() {
        // TODO Do something on complete
        logger.info("Push query has completed");
    }

}
