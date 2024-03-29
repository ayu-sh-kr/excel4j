package dev.archimeded.excel4j.processor.utils;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public class TypeResolver<T> {

    public void resolveString(T instance, Field field, Cell cell) throws IllegalAccessException {
        field.set(instance, cell.getStringCellValue());
    }

    public void resolveNumber(T instance, Field field, Cell cell) throws IllegalAccessException {
        switch (field.getType().getSimpleName()){
            case "Long", "long" -> field.set(instance, (long) cell.getNumericCellValue());
            case "Integer", "int" -> field.set(instance, (int) cell.getNumericCellValue());
            case "Double", "double" -> field.set(instance,  cell.getNumericCellValue());
            case "Float", "float" -> field.set(instance, (float) cell.getNumericCellValue());
            case "Date" -> field.set(instance, cell.getDateCellValue());
            case "LocalDateTime" -> field.set(instance, cell.getLocalDateTimeCellValue());
            default -> throw new IllegalStateException("Unexpected value: " + field.getType());
        }
    }

    public void resolveBoolean(T instance, Field field, Cell cell) throws IllegalAccessException {
        field.setBoolean(instance, cell.getBooleanCellValue());
    }

}
