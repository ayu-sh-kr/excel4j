package entities;

import dev.archimedes.excel4j.annotations.ExcelCell;
import dev.archimedes.excel4j.annotations.ExcelSheet;
import lombok.*;

@ExcelSheet(name = "Sheet1")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class Employee {
    @ExcelCell(name = "Employee Name", cellNumber = 7)
    private String name;

    @ExcelCell(name = "Position ID", cellNumber = 0)
    private String positionId;

    @ExcelCell(name = "Position Status", cellNumber = 1)
    private String positionStatus;

    @ExcelCell(name = "Time", cellNumber = 2)
    private String time;

    @ExcelCell(name = "Time Out", cellNumber = 3)
    private String timeOut;

    @ExcelCell(name = "TimeCard", cellNumber = 4)
    private String timeCard;

    @ExcelCell(name = "Pay Cycle Start", cellNumber = 5)
    private String payCycleStart;

    @ExcelCell(name = "Pay Cycle End", cellNumber = 6)
    private String payCycleEnd;

    @ExcelCell(name = "File Number", cellNumber = 8)
    private String fileNumber;
}
