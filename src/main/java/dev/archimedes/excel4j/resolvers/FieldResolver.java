package dev.archimedes.excel4j.resolvers;

import dev.archimedes.excel4j.annotations.ExcelCell;

import java.lang.reflect.Field;

public class FieldResolver {

    public static void resolveField(Field field, StringBuilder msg){
        msg.append("Field Type: ");
        msg.append(field.getType().getSimpleName());
        msg.append(", ");
        if (field.isAnnotationPresent(ExcelCell.class)){
            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            msg.append("Cell Name: ");
            msg.append(excelCell.name());
            msg.append(", Cell Number: ");
            msg.append(excelCell.cellNumber());
            msg.append(", ");
        }
    }
}
