package com.example.message_history_service.adapter.out;

import com.example.message_history_service.entity.request.IdUserData;
import com.example.message_history_service.entity.response.MessageResponse;
import com.example.message_history_service.entity.response.Result;
import com.example.message_history_service.entity.response.UserData;
import com.example.message_history_service.port.out.MessageContainerPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class MessageContainerAdapter implements MessageContainerPort {
    private final String uriGetLastMessageWithAllFriends = "http://localhost:8085/messageService/api/v1/message/getLastMessagesWithFriendForUser?idUser=";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public MessageContainerAdapter() throws URISyntaxException {
    }

    @Override
    public Flux<MessageResponse> getLastMessagesWithFriendForUser(Mono<IdUserData> idUserDataMono) {

        return idUserDataMono.flatMapMany(idUserData -> WebClient.create().get().uri(uriGetLastMessageWithAllFriends + idUserData.idUser().toString())
                .retrieve()
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        clientResponse -> clientResponse.bodyToMono(String.class).map(Exception::new)
                )
                .bodyToMono(String.class)
                .flatMapMany(responseBody -> {
                    try {
                        List<MessageResponse> userData = objectMapper.readValue(responseBody, new TypeReference<List<MessageResponse>>() {});
                        return Flux.fromIterable(userData);
                    } catch (JsonProcessingException e) {
                        return Flux.error(new RuntimeException(e));
                    }
                })
                .onErrorResume(JsonProcessingException.class, e -> Flux.error(new RuntimeException(e)))
        );
    }
}
