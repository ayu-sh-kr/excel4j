package dev.archimeded.excel4j.annotations;

import dev.archimeded.excel4j.enums.FontFamily;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HeaderStyle {
    FontFamily font() default FontFamily.ARIAL;

    int padding() default 1;

    HorizontalAlignment horizontalAlignment() default HorizontalAlignment.LEFT;

    boolean wrapText() default true;

    IndexedColors fontColor() default IndexedColors.BLACK;

    int fontHeight() default 11;


}
