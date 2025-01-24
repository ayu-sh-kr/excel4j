package entities;

import dev.archimedes.excel4j.annotations.ExcelCell;
import dev.archimedes.excel4j.annotations.ExcelSheet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@ToString
@ExcelSheet()
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithTime {

    @ExcelCell(cellNumber = 0, name = "Local Time")
    private LocalTime time;

    @ExcelCell(cellNumber = 1, name = "Local DateTime")
    private LocalDateTime dateTime;

    @ExcelCell(cellNumber = 2, name = "Local Date")
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductWithTime that = (ProductWithTime) o;

        return time.equals(that.time);
    }
}