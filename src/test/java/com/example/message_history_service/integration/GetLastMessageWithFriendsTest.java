package com.example.message_history_service.integration;

import com.example.message_history_service.entity.request.IdUserData;
import com.example.message_history_service.entity.response.MessageResponse;
import com.example.message_history_service.entity.response.Result;
import com.example.message_history_service.entity.response.UserData;
import com.example.message_history_service.port.out.MessageContainerPort;
import com.example.message_history_service.port.out.UserManagementPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import static org.mockito.ArgumentMatchers.any;

public class GetLastMessageWithFriendsTest extends DefaultTestConfiguration {

    @MockBean
    private UserManagementPort userServicePort;

    @MockBean
    private MessageContainerPort messageContainerPort;


    private Long getLongTimestampValueFromString(String date) throws ParseException {
        SimpleDateFormat dateFormatterPattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date messageDate = dateFormatterPattern.parse(date);
        return new Timestamp(messageDate.getTime()).getTime();
    }

    private Timestamp createTimestampFromString(String date) throws ParseException {
        SimpleDateFormat dateFormatterPattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date messageDate = dateFormatterPattern.parse(date);
        return new Timestamp(messageDate.getTime());
    }
    @Test
    public void requestShouldReturnLastMessageWithFriends() throws URISyntaxException, ParseException {

        //given
        List<MessageResponse> lastMessagesWithFriends = List.of(new MessageResponse(2L, 1L, 2L, "First Message", 1L, createTimestampFromString("2022-10-10 10:00:00")),
                new MessageResponse(3L, 1L, 3L, "Second Message", 2L, createTimestampFromString("2022-10-12 12:00:00")),
                new MessageResponse(4L, 1L, 4L, "Third Message", 3L, createTimestampFromString("2022-10-13 13:00:00"))
        );

        Flux<MessageResponse> lastMessagesWithFriendsFlux = Flux.fromIterable(lastMessagesWithFriends);
        Mockito.when(messageContainerPort.getLastMessagesWithFriendForUser(any(Mono.class))).thenAnswer(invocation -> lastMessagesWithFriendsFlux);

        Mockito.when(userServicePort.getUserAboutId(any(Mono.class))).thenAnswer(invocation -> {
            Mono<IdUserData> monoIdUSer = invocation.getArgument(0);
            if (Objects.requireNonNull(monoIdUSer.block()).idUser() == 2L) {
                return Mono.just(Result.success(new UserData(2L, "John", "Doe", "User1")));
            }else if (Objects.requireNonNull(monoIdUSer.block()).idUser() == 3L) {
                return Mono.just(Result.success(new UserData(3L, "Mark", "Patchett", "User2")));
            }else {
                return Mono.just(Result.success(new UserData(4L, "Conrad", "Herman", "User2")));
            }
        });

        // when
        // then
        String idUser = "1";
        webTestClient.get().uri(createRequestUtil().createRequestGetLastMessageWithUser() + idUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].idFriend").isEqualTo(2L)
                .jsonPath("$[1].idFriend").isEqualTo(3L)
                .jsonPath("$[2].idFriend").isEqualTo(4L)
                .jsonPath("$[2].idSender").isEqualTo(1L)
                .jsonPath("$[2].idSender").isEqualTo(1L)
                .jsonPath("$[2].idSender").isEqualTo(1L)
                .jsonPath("$[0].idReceiver").isEqualTo(2L)
                .jsonPath("$[1].idReceiver").isEqualTo(3L)
                .jsonPath("$[2].idReceiver").isEqualTo(4L)
                .jsonPath("$[0].idMessage").isEqualTo(1L)
                .jsonPath("$[1].idMessage").isEqualTo(2L)
                .jsonPath("$[2].idMessage").isEqualTo(3L)
                .jsonPath("$[0].name").isEqualTo("John")
                .jsonPath("$[1].name").isEqualTo("Mark")
                .jsonPath("$[2].name").isEqualTo("Conrad")
                .jsonPath("$[0].surname").isEqualTo("Doe")
                .jsonPath("$[1].surname").isEqualTo("Patchett")
                .jsonPath("$[2].surname").isEqualTo("Herman")
                .jsonPath("$[0].message").isEqualTo("First Message")
                .jsonPath("$[1].message").isEqualTo("Second Message")
                .jsonPath("$[2].message").isEqualTo("Third Message")
                .jsonPath("$[0].dateTimeMessage").isEqualTo(getLongTimestampValueFromString("2022-10-10 10:00:00"))
                .jsonPath("$[1].dateTimeMessage").isEqualTo(getLongTimestampValueFromString("2022-10-12 12:00:00"))
                .jsonPath("$[2].dateTimeMessage").isEqualTo(getLongTimestampValueFromString("2022-10-13 13:00:00"))
                .jsonPath("$.length()").isEqualTo(3);


    }

    @Test
    public void requestShouldReturnEmptyResponseWhenNoMessages() throws URISyntaxException {

        //given
        Mockito.when(messageContainerPort.getLastMessagesWithFriendForUser(any(Mono.class))).thenAnswer(invocation -> Flux.empty());

        // when
        // then
        String idUser = "1";
        webTestClient.get().uri(createRequestUtil().createRequestGetLastMessageWithUser() + idUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(0);

    }
}
