package dev.archimeded.excel4j.service;

import dev.archimeded.excel4j.service.contracts.ExcelReader;
import dev.archimeded.excel4j.service.contracts.ExcelWriter;

public class ExcelProcessor {

    public static <T> ExcelReader<T> getReaderFromOption(ExcelOption excelOption, Class<T> clazz){
        return new ExcelReaderService<>(excelOption);
    }

    public static <T> ExcelWriter<T> getWriterFromOption(ExcelOption excelOption, Class<T> clazz){
        return new ExcelWriterService<>(excelOption, clazz);
    }

}
