package com.example.message_history_service.entity.response;

import jakarta.validation.constraints.NotNull;


public record UserData( Long id, @NotNull String name, @NotNull String surname, @NotNull String email) {
}
