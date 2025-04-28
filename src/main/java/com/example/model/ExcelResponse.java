package com.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель ответа с результатом обработки Excel файла")
public class ExcelResponse {

    @Schema(description = "Результат - N-ое минимальное число", example = "42")
    private int result;
}