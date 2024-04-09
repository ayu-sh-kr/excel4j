package dev.archimeded.excel4j.entity;

import dev.archimeded.excel4j.annotations.ExcelCell;
import dev.archimeded.excel4j.annotations.ExcelSheet;
import dev.archimeded.excel4j.annotations.HeaderStyle;
import dev.archimeded.excel4j.enums.FontFamily;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ExcelSheet
@ToString
@HeaderStyle(padding = 2, wrapText = false, fontHeight = 12, font = FontFamily.APTOS_NARROW)
public class Product {

    @ExcelCell(name = "name", cellNumber = 0)
    private String name;

    @ExcelCell(name = "category", cellNumber = 1)
    private String category;

    @ExcelCell(name = "price", cellNumber = 2)
    private double price;

    @ExcelCell(name = "date", cellNumber = 3)
    private Date date;

    @ExcelCell(name = "List", cellNumber = 4)
    private List<Double> list = new ArrayList<>();

    public Product(Builder builder){
        this.name = builder.name;
        this.category = builder.category;
        this.date = builder.date;
        this.list = builder.list;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String category;

        private Date date;

        private List<Double> list;

        public Product build(){
            return new Product(this);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder date(Date date){
            this.date = date;
            return this;
        }

        public Builder list(List<Double> list){
            this.list = list;
            return this;
        }
    }
}

