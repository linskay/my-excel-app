package com.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Модель запроса для обработки Excel файла")
public class ExcelRequest {

    @NotBlank(message = "Путь к файлу не может быть пустым")
    @Schema(description = "Путь к файлу Excel", example = "/Users/user/Documents/data.xlsx")
    private String filePath;

    @NotNull(message = "Параметр N не может быть null")
    @Min(value = 1, message = "N должно быть больше 0")
    @Schema(description = "Порядковый номер минимального числа", example = "3")
    private Integer n;
}