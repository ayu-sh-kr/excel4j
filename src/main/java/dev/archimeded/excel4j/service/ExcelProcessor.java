package dev.archimeded.excel4j.service;

import dev.archimeded.excel4j.service.contracts.ExcelReader;
import dev.archimeded.excel4j.service.contracts.ExcelWriter;

public class ExcelProcessor {

    public static <T> ExcelReader<T> getReaderFromOption(ExcelOption<T> excelOption){
        return new ExcelReaderService<>(excelOption);
    }

    public static <T> ExcelWriter<T> getWriterFromOption(ExcelOption<T> excelOption){
        return new ExcelWriterService<>(excelOption);
    }

}
