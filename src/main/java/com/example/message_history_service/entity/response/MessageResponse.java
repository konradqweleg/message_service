package com.example.message_history_service.entity.response;

import java.sql.Timestamp;

public record MessageResponse(Long idFriend,Long idSender,Long idReceiver, String message,Long idMessage, Timestamp dateTimeMessage) {
}

