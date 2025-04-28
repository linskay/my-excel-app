package com.example.service;

import com.example.exception.ExcelProcessingException;

public interface ExcelService {
    int findNthMinimum(String filePath, int n) throws ExcelProcessingException;
}