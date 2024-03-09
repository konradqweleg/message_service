package com.example.message_history_service.services;

import com.example.message_history_service.entity.request.IdUserData;
import com.example.message_history_service.entity.response.LastMessageWithUser;
import com.example.message_history_service.port.in.MessageHistoryPort;
import com.example.message_history_service.port.out.MessageContainerPort;
import com.example.message_history_service.port.out.UserManagementPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Service
public class MessageHistoryService implements MessageHistoryPort {

    private final MessageContainerPort  messageContainerPort;
    private final UserManagementPort userManagementPort;

    public MessageHistoryService(MessageContainerPort messageContainerPort, UserManagementPort userManagementPort) {
        this.messageContainerPort = messageContainerPort;
        this.userManagementPort = userManagementPort;
    }


    @Override
    public Flux<LastMessageWithUser> getLastMessagesWithFriends(Mono<IdUserData> idUserDataMono) {
        return messageContainerPort.getLastMessagesWithFriendForUser(idUserDataMono)
                .flatMap(message -> userManagementPort.getUserAboutId(Mono.just(new IdUserData(message.idFriend())))
                        .map(user -> new LastMessageWithUser(
                                message.idFriend(),
                                message.idSender(),
                                message.idReceiver(),
                                message.idMessage(),
                                user.getValue().name(),
                                user.getValue().surname(),
                                message.message(),
                                message.dateTimeMessage()
                        ))
                );
    }
}
