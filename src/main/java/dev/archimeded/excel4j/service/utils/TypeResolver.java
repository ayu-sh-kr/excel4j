package dev.archimeded.excel4j.service.utils;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;

public class TypeResolver {

    public static <T> void resolveString(T instance, Field field, Cell cell, String delimiter) throws IllegalAccessException {

        switch (field.getType().getSimpleName()){
            case "String" -> field.set(instance, cell.getStringCellValue());
            case "List" -> {
                System.out.println(cell.getStringCellValue());
                field.set(instance, Arrays.stream(cell.getStringCellValue().split(delimiter)).toList());
            }
        }
    }

    public static <T> void resolveNumber(T instance, Field field, Cell cell, String delimiter) throws IllegalAccessException, ParseException {
        NumberFormat parser = NumberFormat.getInstance();
        switch (field.getType().getSimpleName()){
            case "Long", "long" -> field.set(instance, parser.parse(cell.getStringCellValue()));
            case "Integer", "int" -> field.set(instance, (int) cell.getNumericCellValue());
            case "Double", "double" -> field.set(instance,  cell.getNumericCellValue());
            case "Float", "float" -> field.set(instance, (float) cell.getNumericCellValue());
            case "Date" -> field.set(instance, cell.getDateCellValue());
            case "LocalDateTime" -> field.set(instance, cell.getLocalDateTimeCellValue());
            case "List" -> field.set(
                    instance,
                    Arrays.stream(cell.getStringCellValue()
                            .split(delimiter))
                            .map(string -> {
                                try {
                                    return NumberFormat.getInstance().parse(string);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .toList()
            );
            default -> throw new IllegalStateException("Unexpected value: " + field.getType());
        }
    }

    public static <T> void resolveBoolean(T instance, Field field, Cell cell) throws IllegalAccessException {
        field.setBoolean(instance, cell.getBooleanCellValue());
    }

}
