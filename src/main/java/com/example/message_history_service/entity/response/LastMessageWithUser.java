package com.example.message_history_service.entity.response;

import java.sql.Timestamp;

public record LastMessageWithUser(Long idUser,Long idMessage, String name, String surname, String message, Timestamp dateTimeMessage) {
}
