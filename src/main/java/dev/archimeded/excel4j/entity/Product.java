package dev.archimeded.excel4j.entity;

import dev.archimeded.excel4j.annotations.ExcelCell;
import dev.archimeded.excel4j.annotations.ExcelSheet;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ExcelSheet()
@ToString
//@HeaderStyle(padding = 2, wrapText = false, fontHeight = 12, font = FontFamily.APTOS_NARROW)
public class Product {

    @ExcelCell(name = "name", cellNumber = 0)
    private String name;

    @ExcelCell(name = "category", cellNumber = 1)
    private String category;

    @ExcelCell(name = "price", cellNumber = 2)
    private double price;

    @ExcelCell(name = "date", cellNumber = 3)
    private Date date;

    public Product(Builder builder){
        this.name = builder.name;
        this.category = builder.category;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String category;

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
    }
}

