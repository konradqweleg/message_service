package com.example.message_history_service.entity.response;

import java.sql.Timestamp;

public record MessageResponse(Long idFriend, String message,Long idMessage, Timestamp dateTimeMessage) {
}
