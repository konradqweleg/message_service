package com.example.message_history_service.port.out;

import com.example.message_history_service.entity.request.IdUserData;
import com.example.message_history_service.entity.response.Result;
import com.example.message_history_service.entity.response.UserData;
import reactor.core.publisher.Mono;

public interface UserManagementPort {
    Mono<Result<UserData>> getUserAboutId(Mono<IdUserData> idUserDataMono);
}
