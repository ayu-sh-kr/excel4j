import dev.archimedes.excel4j.options.ExcelOption;
import dev.archimedes.excel4j.processors.ExcelProcessor;
import dev.archimedes.excel4j.service.contracts.ExcelReader;
import dev.archimedes.excel4j.service.contracts.ExcelWriter;
import entities.Product;
import entities.ProductWithTime;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        File file = new File("product_test.xlsx");

        if (!file.exists()) {
            boolean result = file.createNewFile();
            System.out.println("File created: " + result);
        }

        writer.write(file, products);
        List<Product> products1 = reader.read(file);

        assertEquals(products.size(), products1.size(), "Size of both product list should be equal");
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

        File file = new File("product_test.xlsx");

        System.out.println("File exists: " + file.exists());

        if (!file.exists()) {
            boolean result = file.createNewFile();
            System.out.println("File created: " + result);
        }

        products.addAll(getProducts());

        option.setOverwrite(false);

        writer.write(file, products);
        List<Product> products1 = reader.read(file);

        assertEquals(products.size(), products1.size(), "Size of both product list should be equal");

        boolean result = file.delete();

        System.out.println("File deleted: " + result);
    }

    @Test
    public void writeInBetween() throws IOException {

        ExcelOption<Product> option = ExcelOption.<Product>builder()
                .with(Product.class)
                .sheetIndex(0)
                .overwrite(true)
                .listDelimiter(",")
                .build();
        List<Product> products = getProducts();

        File file = new File("product_test.xlsx");

        if(!file.exists()){
            boolean result = file.createNewFile();
            System.out.println("File created: " + result);
        }

        ExcelWriter<Product> writer = ExcelProcessor.getWriterFromOption(option);
        ExcelReader<Product> reader = ExcelProcessor.getReaderFromOption(option);
        writer.write(file, products);

        assertEquals(products.size(), reader.read(file).size(), "Product size and Size of read should be equal");

        Product product = Product.builder()
                .name("Apple Pie").category("Cake").quantity(3).date(new Date()).list(List.of("Honey", "Apple", "Corn flower"))
                .build();

        option.setOverwrite(false);
        option.setStart(2);
        option.setEnd(3);
        writer.write(file, List.of(product));

        option.setStart(1);

        Product product1 = reader.read(file).get(1);
        boolean test = product.equals(product1);
        System.out.println(product);
        System.out.println(product1);

        assertTrue(test, "It should be true");

        boolean result = file.delete();

        System.out.println("File deleted: " + result);
    }

    @Test
    public void writeOne() throws IOException {
        Product product = Product.builder()
                .name("Apple Pie").category("Cake").quantity(3).date(new Date()).list(List.of("Honey", "Apple", "Corn flower"))
                .build();

        File file = new File("product_test.xlsx");

        if(!file.exists()){
            boolean result = file.createNewFile();
            System.out.println("File created: " + result);
        }

        ExcelOption<Product> option = ExcelOption.<Product>builder()
                .start(1)
                .listDelimiter(";")
                .overwrite(true)
                .with(Product.class)
                .build();

        ExcelWriter<Product> writer = ExcelProcessor.getWriterFromOption(option);
        ExcelReader<Product> reader = ExcelProcessor.getReaderFromOption(option);

        writer.write(file, product);

        Product product1 = reader.readOne(file);

        assertEquals(product, product1, "Both products should be equal");

        boolean result = file.delete();

        System.out.println("File deleted: " + result);
    }

    @Test
    public void writeJavaTime() throws IOException {


        ProductWithTime product = new ProductWithTime(
                LocalTime.of(19, 30),
                LocalDateTime.of(2025, 1, 25, 7, 34),
                LocalDate.of(2025, 1, 25)
        );
        System.out.println(product);

        File file = new File("product_test_localtime.xlsx");

        if (!file.exists()) {
            boolean result = file.createNewFile();
            System.out.println("File created: " + result);
        }

        ExcelOption<ProductWithTime> option = ExcelOption.<ProductWithTime>builder()
                .sheetIndex(0)
                .start(1)
                .listDelimiter(";")
                .dateRegex("dd-MM-yyyy")
                .timeRgx("HH:mm")
                .dateTimeRgx("dd:MM:yyyy HH:mm")
                .overwrite(true)
                .with(ProductWithTime.class)
                .build();

        ExcelWriter<ProductWithTime> writer = ExcelProcessor.getWriterFromOption(option);
        ExcelReader<ProductWithTime> reader = ExcelProcessor.getReaderFromOption(option);

        writer.write(file, product);

        ProductWithTime product1 = reader.readOne(file);
        System.out.println(product1);

        assertEquals(product.getTime(), product1.getTime(), "Both LocalTime values should be equal");
        assertEquals(product.getDate(), product1.getDate(), "Both LocalDate values should be equal");
        assertEquals(product.getDateTime(), product1.getDateTime(), "Both LocalDateTime values should be equal");

        boolean result = file.delete();
        System.out.println("File deleted: " + result);
    }
}
