package dev.archimedes.excel4j.utils;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class ResolveExceptionLog {

    public static String prepare(Field field, Cell cell, String message){
        StringBuilder msg = new StringBuilder();
        msg.append(message);
        msg.append(", ");
        FieldResolver.resolveField(field, msg);
        switch (cell.getCellType()) {
            case NUMERIC -> {
                msg.append("Numeric Value: ");
                msg.append(cell.getNumericCellValue());
                return msg.toString();
            }
            case STRING -> {
                msg.append("String Value: ");
                msg.append(cell.getStringCellValue());
                return msg.toString();
            }
        }
        return "";
    }
}
