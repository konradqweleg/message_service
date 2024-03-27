package com.example.message_history_service.adapter.in;

import com.example.message_history_service.adapter.util.ConvertToJSON;
import com.example.message_history_service.entity.request.IdUserData;
import com.example.message_history_service.entity.response.LastMessageWithUser;
import com.example.message_history_service.port.in.MessageHistoryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequestMapping(value = "/messageHistory/api/v1/history")
public class MessageHistoryAdapter {

    private final MessageHistoryPort messageHistoryPort;

    public MessageHistoryAdapter(MessageHistoryPort messageHistoryPort) {
        this.messageHistoryPort = messageHistoryPort;
    }


    @GetMapping("/getLastMessagesWithFriendForUser")
    public Mono<ResponseEntity<String>> getLastMessagesWithFriendForUser(@RequestParam Long idUser) {
        return ConvertToJSON.convert(messageHistoryPort.getLastMessagesWithFriends(Mono.just(new IdUserData(idUser))));
    }


}
