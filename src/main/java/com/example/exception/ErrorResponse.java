package com.example.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель ответа с ошибкой")
public class ErrorResponse {

    @Schema(description = "Сообщение об ошибке", example = "Ошибка валидации параметров")
    private String message;
}