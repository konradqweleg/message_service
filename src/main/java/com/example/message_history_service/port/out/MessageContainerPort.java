package com.example.message_history_service.port.out;

import com.example.message_history_service.entity.request.IdUserData;
import com.example.message_history_service.entity.response.MessageResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MessageContainerPort {

    Flux<MessageResponse> getLastMessagesWithFriendForUser(Mono<IdUserData> idUserDataMono);
}
