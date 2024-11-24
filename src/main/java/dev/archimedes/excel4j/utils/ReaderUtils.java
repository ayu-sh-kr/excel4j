package dev.archimedes.excel4j.utils;

import dev.archimedes.excel4j.options.ExcelOption;
import dev.archimedes.excel4j.resolvers.TypeResolver;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.text.ParseException;

public class ReaderUtils {

    public static <T> void read(int rowIdx, Field field, Cell cell, ExcelOption<T> option, T instance) throws IllegalAccessException, ParseException {
        switch (field.getType().getSimpleName()) {
            case "Integer", "int" -> TypeResolver.resolveInteger(instance, field, cell);
            case "Long", "long" -> TypeResolver.resolveLong(instance, field, cell);
            case "float", "Float" -> TypeResolver.resolveFloat(instance, field, cell);
            case "double", "Double" -> TypeResolver.resolveDouble(instance, field, cell);
            case "bool", "Boolean" -> TypeResolver.resolveBoolean(instance, field, cell);
            case "String" -> TypeResolver.resolveString(instance, field, cell);
            case "List" -> TypeResolver.resolveList(instance, field, cell, option);
            case "Date" -> TypeResolver.resolveDate(instance, field, cell, rowIdx, option);
            case "LocalDate" -> TypeResolver.resolveLocalDate(instance, field, cell, option);
            case "LocalDateTime" -> TypeResolver.resolveLocalDateTime(instance, field, cell, option);
            case "LocalTime" -> TypeResolver.resolveLocalTime(instance, field, cell, option);
        }
    }
}
