package com.example.service;

import com.example.exception.ExcelProcessingException;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("Сервис обработки Excel файлов")
class ExcelServiceTest {

    @InjectMocks
    private ExcelServiceImpl excelService;

    private String testFilePath;

    @BeforeEach
    @DisplayName("Создание тестового Excel файла перед каждым тестом")
    void setUp() throws IOException {
        File tempFile = File.createTempFile("test_excel", ".xlsx");
        testFilePath = tempFile.getAbsolutePath();
        createTestExcelFile(testFilePath, List.of(5, 3, 1, 4, 2));
    }

    private void createTestExcelFile(String filePath, List<Integer> values) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(true);
             FileOutputStream fileOut = new FileOutputStream(filePath)) {

            Sheet sheet = workbook.createSheet("Sheet1");
            for (int i = 0; i < values.size(); i++) {
                Row row = sheet.createRow(i);
                Cell cell = row.createCell(0);
                cell.setCellValue(values.get(i));
            }
            workbook.write(fileOut);
        }
    }

    @Test
    @DisplayName("Должен вернуть N-ое минимальное значение при корректных данных")
    void findNthMinimum_ValidInput_ReturnsCorrectValue() throws ExcelProcessingException {
        assertThat(excelService.findNthMinimum(testFilePath, 1)).isEqualTo(1);
        assertThat(excelService.findNthMinimum(testFilePath, 2)).isEqualTo(2);
        assertThat(excelService.findNthMinimum(testFilePath, 3)).isEqualTo(3);
    }

    @Test
    @DisplayName("Должен выбросить исключение при N < 1")
    void findNthMinimum_NLessThan1_ThrowsException() {
        assertThatThrownBy(() -> excelService.findNthMinimum(testFilePath, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("N должно быть больше 0.");
    }

    @Test
    @DisplayName("Должен выбросить исключение при N > количества чисел в файле")
    void findNthMinimum_NGreaterThanFileContent_ThrowsException() {
        assertThatThrownBy(() -> excelService.findNthMinimum(testFilePath, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("В файле меньше чисел, чем указано в N.");
    }

    @Test
    @DisplayName("Должен выбросить исключение при неверном пути к файлу")
    void findNthMinimum_InvalidFilePath_ThrowsException() {
        assertThatThrownBy(() -> excelService.findNthMinimum("invalid_path.xlsx", 1))
                .isInstanceOf(ExcelProcessingException.class)
                .hasMessageContaining("Ошибка при чтении файла Excel");
    }

    @Test
    @DisplayName("Должен вернуть пустой список при чтении пустого файла")
    void readNumbersFromExcel_EmptyFile_ReturnsEmptyList() throws IOException {
        File emptyFile = File.createTempFile("empty", ".xlsx");
        try (Workbook workbook = WorkbookFactory.create(true);
             FileOutputStream fileOut = new FileOutputStream(emptyFile)) {
            workbook.createSheet("Sheet1");
            workbook.write(fileOut);
        }

        List<Integer> result = excelService.readNumbersFromExcel(emptyFile.getAbsolutePath());
        assertThat(result).isEmpty();
    }
}