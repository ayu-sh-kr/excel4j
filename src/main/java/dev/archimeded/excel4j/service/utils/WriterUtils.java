package dev.archimeded.excel4j.service.utils;

import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class WriterUtils {
    public static void writeList(Cell cell, Object value, String delimiter) {
        if(value instanceof List<?> list){
            StringBuilder builder = new StringBuilder();

            for(Object object: list){
                builder.append(object);
                builder.append(delimiter);
            }

            cell.setCellValue(builder.toString());
            return;
        }
        throw new IllegalStateException("Type not list");
    }

    private static void write(Cell cell, Object value, String delimiter){
    }
}
