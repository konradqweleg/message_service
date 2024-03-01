package com.example.message_history_service.port.in;

import com.example.message_history_service.entity.request.IdUserData;
import com.example.message_history_service.entity.response.LastMessageWithUser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MessageHistoryPort {
    Flux<LastMessageWithUser> getLastMessagesWithFriends(Mono<IdUserData> idUserDataMono);
}
