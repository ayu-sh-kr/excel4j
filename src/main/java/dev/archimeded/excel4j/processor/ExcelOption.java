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

    private int start;

    private int end;

    private int sheetIndex;

    private String listDelimiter;


    public static ExcelOptionBuilder builder(){
        return new ExcelOptionBuilder();
    }

    public static class ExcelOptionBuilder {
        private Class<?> clazz;

        private int start = 1;

        private int end = -1;

        private int sheetIndex = 0;

        private String listDelimiter = ";";

        public ExcelOptionBuilder with(Class<?> clazz){
            this.clazz = clazz;
            return this;
        }

        public ExcelOptionBuilder start(int start){
            this.start = start;
            return this;
        }

        public ExcelOptionBuilder end(int end){
            this.end = end;
            return this;
        }

        public ExcelOptionBuilder sheetIndex(int sheetIndex){
            this.sheetIndex = sheetIndex;
            return this;
        }

        public ExcelOptionBuilder listDelimiter(String delimiter){
            this.listDelimiter = delimiter;
            return this;
        }

        public ExcelOption build(){
            return new ExcelOption(clazz, start, end, sheetIndex, listDelimiter);
        }
    }
}
