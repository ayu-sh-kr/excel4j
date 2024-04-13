import dev.archimedes.excel4j.options.ExcelOption;
import dev.archimedes.excel4j.processors.ExcelProcessor;
import dev.archimedes.excel4j.service.contracts.ExcelReader;
import dev.archimedes.excel4j.service.contracts.ExcelWriter;
import entities.Product;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WriteTest {

    public List<Product> getProducts(){

        return new ArrayList<>(
                List.of(
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
                )
        );
    }

    @Test
    public void writeWithOverwrite() throws IOException {
        List<Product> products = getProducts();

        ExcelOption<Product> option = ExcelOption.<Product>builder()
                .with(Product.class)
                .overwrite(true)
                .listDelimiter(",")
                .build();

        ExcelWriter<Product> writer = ExcelProcessor.getWriterFromOption(option);
        ExcelReader<Product> reader = ExcelProcessor.getReaderFromOption(option);

        File file = new File("ProductTest.xlsx");

        if (!file.exists()) {
            boolean result = file.createNewFile();
            System.out.println("File created: " + result);
        }

        writer.write(file, products);
        List<Product> products1 = reader.read(file);

        assertEquals(products1.size(), products.size(), "Size of both product list should be equal");

        boolean result = file.delete();

        System.out.println("File deleted: " + result);
    }

    @Test
    public void writeWithoutOverwrite() throws IOException{
        ExcelOption<Product> option = ExcelOption.<Product>builder()
                .overwrite(true)
                .sheetIndex(0)
                .with(Product.class)
                .build();

        List<Product> products = getProducts();

        ExcelWriter<Product> writer = ExcelProcessor.getWriterFromOption(option);
        ExcelReader<Product> reader = ExcelProcessor.getReaderFromOption(option);

        File file = new File("ProductTest.xlsx");

        if (!file.exists()) {
            boolean result = file.createNewFile();
            System.out.println("File created: " + result);
        }

        writer.write(file, products);

//        assertEquals(products.size(), reader.read(file).size(), "Size of both product list should be equal");

        products.addAll(getProducts());

        option.setOverwrite(false);

        writer.write(file, products);
        List<Product> products1 = reader.read(file);

        assertEquals(products.size(), products1.size(), "Size of both product list should be equal");

        boolean result = file.delete();

        System.out.println("File deleted: " + result);
    }
}
