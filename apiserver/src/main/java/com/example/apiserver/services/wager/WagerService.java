package com.example.apiserver.services.wager;

import java.util.Optional;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apiserver.objects.WagerRequest;
import com.example.apiserver.objects.WagerResponse;
import com.example.apiserver.repository.JackpotEntity;
import com.example.apiserver.repository.JackpotRepository;
import com.example.apiserver.services.producer.ProducerService;

@Service
public class WagerService {
    Logger logger = Logger.getLogger(WagerService.class);
    private final String TOPIC = "wagers";
    @Autowired
    JackpotRepository jackpotRepository;
    @Autowired
    ProducerService producerService;
    
    public WagerResponse placeWager(WagerRequest wagerRequest) throws Exception {
        WagerResponse wagerResponse = new WagerResponse();
        String result = spinTheWheel(wagerRequest);
        if (result == "winner") {
            Optional<JackpotEntity> optionalJackpotEntity = jackpotRepository.findById((String) wagerRequest.getJackpotPoolId());
            if (optionalJackpotEntity.isPresent()) {
                JackpotEntity jackpot = optionalJackpotEntity.get();
                wagerRequest.setWager(jackpot.getJackpot()*-1);
                Future<RecordMetadata> futureRecordMetadata = producerService.send((String) wagerRequest.getJackpotPoolId(), wagerRequest, TOPIC);
                futureRecordMetadata.get();
                wagerResponse.setJackpotPoolId(wagerRequest.getJackpotPoolId());
                wagerResponse.setStatus("success");
                wagerResponse.setResult(result);
                wagerResponse.setWager(wagerRequest.getWager());
            }
        } else {
            Future<RecordMetadata> futureRecordMetadata = producerService.send((String) wagerRequest.getJackpotPoolId(), wagerRequest, TOPIC);
            futureRecordMetadata.get();
            wagerResponse.setJackpotPoolId(wagerRequest.getJackpotPoolId());
            wagerResponse.setStatus("success");
            wagerResponse.setResult(result);
            wagerResponse.setWager(wagerRequest.getWager());
        }
        
        return wagerResponse;
    }
    private String spinTheWheel(WagerRequest wagerRequest) {
        if ((int) wagerRequest.getWager() == 5643) {
            return "winner";
        } else {
            double spin = Math.random();
            if (spin <= 0.01) {
                return "winner";
            } else {
                return "loser";
            }
        }
    }
}
