package dev.archimeded.excel4j.service;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExcelOption<T> {

    private Class<T> clazz;

    private int start;

    private int end;

    private int sheetIndex;

    private String listDelimiter;

    private String dateRegex;

    private String dateTimeRegex;


    public static <T> ExcelOptionBuilder<T> builder(){
        return new ExcelOptionBuilder<>();
    }

    public static class ExcelOptionBuilder<T> {
        private Class<T> clazz;

        private int start = 1;

        private int end = -1;

        private int sheetIndex = 0;

        private String listDelimiter = ";";

        private String dateRgx = "dd/MM/yyyy";

        private String dateTimeRgx = "dd/MM/yyyy hh:mm:ss";

        public ExcelOptionBuilder<T> with(Class<T> clazz){
            this.clazz = clazz;
            return this;
        }

        public ExcelOptionBuilder<T> start(int start){
            this.start = start;
            return this;
        }

        public ExcelOptionBuilder<T> end(int end){
            this.end = end;
            return this;
        }

        public ExcelOptionBuilder<T> sheetIndex(int sheetIndex){
            this.sheetIndex = sheetIndex;
            return this;
        }

        public ExcelOptionBuilder<T> dateRegex(String dateRgx){
            this.dateRgx = dateRgx;
            return this;
        }

        public ExcelOptionBuilder<T> dateTimeRgx(String dateTimeRgx){
            this.dateTimeRgx = dateTimeRgx;
            return this;
        }

        public ExcelOptionBuilder<T> listDelimiter(String delimiter){
            this.listDelimiter = delimiter;
            return this;
        }

        public ExcelOption<T> build(){
            return new ExcelOption<T>(clazz, start, end, sheetIndex, listDelimiter, dateRgx, dateTimeRgx);
        }
    }
}
