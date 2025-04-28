package com.example.service;

import com.example.algorithm.QuickSelect;
import com.example.exception.ExcelProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public int findNthMinimum(String filePath, int n) throws ExcelProcessingException {
        validateInput(n);
        List<Integer> numbers = readNumbersFromExcel(filePath);
        validateNumbersSize(numbers, n);
        return QuickSelect.findKthSmallest(numbers, n);
    }

    private void validateInput(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("N должно быть больше 0.");
        }
    }

    private void validateNumbersSize(List<Integer> numbers, int n) {
        if (numbers.size() < n) {
            throw new IllegalArgumentException("В файле меньше чисел, чем указано в N.");
        }
    }

    List<Integer> readNumbersFromExcel(String filePath) throws ExcelProcessingException {
        List<Integer> numbers = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                processRow(numbers, row);
            }
        } catch (IOException e) {
            log.error("Ошибка при чтении файла Excel: {}", filePath, e);
            throw new ExcelProcessingException("Ошибка при чтении файла Excel: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return numbers;
    }

    private void processRow(List<Integer> numbers, Row row) {
        if (row == null) return;

        Cell cell = row.getCell(0);
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            numbers.add((int) cell.getNumericCellValue());
        }
    }
}