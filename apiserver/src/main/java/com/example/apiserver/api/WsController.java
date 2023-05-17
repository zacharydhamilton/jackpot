package com.example.apiserver.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.apiserver.objects.WagerRequest;
import com.example.apiserver.objects.WagerResponse;
import com.example.apiserver.services.wager.WagerService;
import com.google.gson.Gson;

@Controller
@CrossOrigin
public class WsController {
    @Autowired
    WagerService wagerService;
    @Autowired
    SimpMessagingTemplate channel;

    private Gson gson = new Gson();

    public WsController() {
        // TODO At the time of writing, I don't have a need for this constructor.
        // But I'm adding this comment to come back later.
    }
    @MessageMapping("/wager")
    @SendToUser("/queue/reply")
    public String placeWager(@Payload WagerRequest wagerRequest) throws Exception {
        WagerResponse wagerResponse = wagerService.placeWager(wagerRequest);
        channel.convertAndSend("/topic/activity", gson.toJson(wagerResponse));
        return gson.toJson(wagerResponse);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        System.out.println(exception.getMessage());
        exception.printStackTrace();
        return exception.getMessage();
    }
}
