package com.example.controller;

import com.example.model.ExcelResponse;
import com.example.service.ExcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
@Tag(name = "Excel Controller", description = "API для обработки Excel файлов")
public class ExcelController {

    private final ExcelService excelService;

    @GetMapping("/nth-minimum")
    @Operation(summary = "Найти N-ое минимальное число в Excel файле")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный результат",
                    content = @Content(schema = @Schema(implementation = ExcelResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ExcelResponse> findNthMinimum(
            @RequestParam @NotBlank(message = "Путь к файлу обязателен") String filePath,
            @RequestParam @Min(value = 1, message = "N должно быть ≥ 1") int n) {

        int result = excelService.findNthMinimum(filePath, n);
        return ResponseEntity.ok(new ExcelResponse(result));
    }
}