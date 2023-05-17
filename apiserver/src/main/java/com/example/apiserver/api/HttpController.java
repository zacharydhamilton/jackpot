package com.example.apiserver.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apiserver.repository.JackpotEntity;
import com.example.apiserver.repository.JackpotRepository;
import com.example.apiserver.objects.JackpotResponse;
import com.example.apiserver.services.wager.WagerService;
import com.google.gson.Gson;

@CrossOrigin
@RequestMapping("/api")
@RestController
public class HttpController {
    Logger logger = Logger.getLogger(HttpController.class);
    @Autowired
    WagerService wagerService;
    @Autowired
    JackpotRepository jackpotRepository;
    private Gson gson = new Gson();
    
    public HttpController() {}
    
    @GetMapping("/jackpots")
    List<String> getCurrentJackpot() throws Exception {
        List<String> jackpotResponses = new ArrayList<>();
        for (JackpotEntity entity : jackpotRepository.findAll()) {
            jackpotResponses.add(gson.toJson(new JackpotResponse(entity.getJackpotPoolId(), entity.getRegion(), entity.getJackpot(), "success")));
        }
        return jackpotResponses;
    }

    @GetMapping("/jackpots/{jackpotPoolId}")
    JackpotResponse getSpecificJackpot(@PathVariable String jackpotPoolId) throws Exception {
        Optional<JackpotEntity> optionalEntity = jackpotRepository.findById(jackpotPoolId);
        if (optionalEntity.isPresent()) {
            JackpotEntity entity = optionalEntity.get();
            return new JackpotResponse(entity.getJackpotPoolId(),entity.getRegion(), entity.getJackpot(), "success");
        } else {
            return new JackpotResponse(jackpotPoolId, "n/a", 0, "pool-not-found");
        }
    }

    @ExceptionHandler
    public String handleException(Throwable exception) {
        System.out.println(exception.getMessage());
        exception.printStackTrace();
        return exception.getMessage();
    }
}
