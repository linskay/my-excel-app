package com.example.controller;

import com.example.exception.ExcelProcessingException;
import com.example.service.ExcelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExcelController.class)
@DisplayName("Тесты контроллера Excel")
class ExcelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExcelService excelService;

    @Test
    @DisplayName("Успешный запрос - должен вернуть 200 OK и результат")
    void findNthMinimum_ValidRequest_ReturnsOk() throws Exception {
        when(excelService.findNthMinimum(anyString(), anyInt())).thenReturn(42);

        mockMvc.perform(get("/excel/nth-minimum")
                        .param("filePath", "test.xlsx")
                        .param("n", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(42));
    }

    @Test
    @DisplayName("Некорректный параметр N - должен вернуть 400 Bad Request")
    void findNthMinimum_InvalidN_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/excel/nth-minimum")
                        .param("filePath", "test.xlsx")
                        .param("n", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("N должно быть ≥ 1"));
    }

    @Test
    @DisplayName("Неверный путь к файлу - должен вернуть 400 Bad Request")
    void findNthMinimum_InvalidFilePath_ReturnsBadRequest() throws Exception {
        when(excelService.findNthMinimum(anyString(), anyInt()))
                .thenThrow(new ExcelProcessingException("Файл не найден", HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/excel/nth-minimum")
                        .param("filePath", "invalid.xlsx")
                        .param("n", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Файл не найден"));
    }

    @Test
    @DisplayName("Внутренняя ошибка сервера - должен вернуть 500 Internal Server Error")
    void findNthMinimum_ServerError_ReturnsInternalServerError() throws Exception {
        when(excelService.findNthMinimum(anyString(), anyInt()))
                .thenThrow(new RuntimeException("Ошибка сервера"));

        mockMvc.perform(get("/excel/nth-minimum")
                        .param("filePath", "test.xlsx")
                        .param("n", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Ошибка сервера"));
    }

    @Test
    @DisplayName("Отсутствуют обязательные параметры - должен вернуть 400 Bad Request")
    void findNthMinimum_MissingParams_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/excel/nth-minimum")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Путь к файлу обязателен"));
    }
}