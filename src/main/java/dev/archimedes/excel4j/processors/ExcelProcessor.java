package dev.archimedes.excel4j.processors;

import dev.archimedes.excel4j.options.ExcelOption;
import dev.archimedes.excel4j.service.ExcelReaderService;
import dev.archimedes.excel4j.service.ExcelWriterService;
import dev.archimedes.excel4j.service.contracts.ExcelReader;
import dev.archimedes.excel4j.service.contracts.ExcelWriter;

public class ExcelProcessor {

    public static <T> ExcelReader<T> getReaderFromOption(ExcelOption<T> excelOption){
        return new ExcelReaderService<>(excelOption);
    }

    public static <T> ExcelWriter<T> getWriterFromOption(ExcelOption<T> excelOption){
        return new ExcelWriterService<>(excelOption);
    }

}
