package dev.archimeded.excel4j.processor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExcelOption {

    private Class<?> clazz;


    public static ExcelOptionBuilder builder(){
        return new ExcelOptionBuilder();
    }

    public static class ExcelOptionBuilder {
        private Class<?> clazz;

        public ExcelOptionBuilder with(Class<?> clazz){
            this.clazz = clazz;
            return this;
        }

        public ExcelOption build(){
            return new ExcelOption(clazz);
        }
    }
}
