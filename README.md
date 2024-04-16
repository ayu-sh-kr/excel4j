# Excel4j 

> **Java Library** to read and write POJOs

### Working of Excel4j

In order to identify what fields to write and read from Excel you need Provide meta-data.
We use annotations to provide those meta-data to the Excel4j. Excel4j provide annotations: 
> *@ExcelSheet* to mark the Pojo class 
 ```Java

// default sheet name will be the class name and sheet number is 0
@ExcelSheet(name = "", sheetNumber = 0)
public class Product {
}
```

> *@ExcelCell* to mark fields of Class

```Java
import dev.archimedes.excel4j.annotations.ExcelCell;
import dev.archimedes.excel4j.annotations.ExcelSheet;

@ExcelSheet(name = "Product", sheetNumber = 1)
public class Product {
    @ExcelCell(name = "Product Name", cellNumber = 0)
    private String name;
}
```

#### Read Operation

```Java
import dev.archimedes.excel4j.options.ExcelOption;
import dev.archimedes.excel4j.processors.ExcelProcessor;
import dev.archimedes.excel4j.service.contracts.ExcelReader;
import dev.archimedes.entities.Product;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // create option to supply addition information to read and writer
        ExcelOption<Product> option = ExcelOption.<Product>builder()
                .with(Product.class) // with Java Object to read/write
                .sheetIndex(0) // default value is 0, but you can provide your own.
                .dateRegex("dd/MM/yyyy") // date format in Excel cell
                .listDelimiter(";") // if cell have value separated by delimiter, here ';'
                .start(5) // default 1, indicate the row number from where to start operation
                .build();

        // create the file object to refer your Excel File.

        File file = new File("your_file_location.xlsx");

        ExcelReader<Product> reader = ExcelProcessor.getReaderFromOption(option);

        // ExcelProcessor have static methods to return writer and reader instance

        // read the whole file
        List<Product> products = reader.read(file);
        
        // read one row at the given position (start in option)
        Product product = reader.readOne(file);
    }
}
```


#### Write Operation

```Java

import dev.archimedes.excel4j.options.ExcelOption;
import dev.archimedes.excel4j.processors.ExcelProcessor;
import dev.archimedes.excel4j.service.contracts.ExcelReader;
import dev.archimedes.excel4j.service.contracts.ExcelWriter;
import dev.archimedes.entities.Product;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ExcelOption<Product> option = ExcelOption.<Product>builder()
                .with(Product.class) // Java Pojo to write
                .start(1) // row index where write operation start.
                .dateRegex("dd/MM/yyyy")
                .sheetIndex(0) // sheet where ExcelWriter will write to.
                .overwrite(true) // overwrite the file if set true
                .build();

        ExcelWriter<Product> writer = ExcelProcessor.getWriterFromOption(option);

        File file = new File("product_data.xlsx");

        List<Product> products = List.of(
                Product.builder()
                        .name("Product 1")
                        .category("Juice")
                        .date(new Date())
                        .quantity(7)
                        .list(List.of("Something", "Another Thing", "Other Thing"))
                        .build(),

                Product.builder()
                        .name("Product 2")
                        .category("Cake")
                        .date(new Date())
                        .quantity(2)
                        .list(List.of("Other Thing", "Something", "Another Thing"))
                        .build(),

                Product.builder()
                        .name("Product 3")
                        .category("Cookie")
                        .date(new Date())
                        .quantity(5)
                        .list(List.of("Another Thing", "Something", "Other Thing"))
                        .build()
        );
        writer.write(file, products);
    }
}
```

#### Resolve Sheet name to write from ClassName

*If no name parameter is specified in @ExcelSheet()* **Excel4j** has a different approach to name the sheet.

```Java

import dev.archimedes.excel4j.annotations.ExcelSheet;

@ExcelSheet() // no name specified, sheet number default to 0
public class ProductDescription {
}
```

In this case sheet name will be *'Product Description'*

This behavior is due to the Approach of naming classes based on *'CamelCase'* Convention

If your class name does not follow *'CamelCase'* then do provide the name value,


