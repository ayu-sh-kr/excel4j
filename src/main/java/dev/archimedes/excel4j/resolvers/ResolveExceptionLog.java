package dev.archimedes.excel4j.resolvers;

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
                msg.append(" at row");
                msg.append(cell.getRowIndex());
                return msg.toString();
            }
            case STRING -> {
                msg.append("String Value: ");
                msg.append(cell.getStringCellValue());
                msg.append(" at row");
                msg.append(cell.getRowIndex());
                return msg.toString();
            }
        }
        return "";
    }
}
