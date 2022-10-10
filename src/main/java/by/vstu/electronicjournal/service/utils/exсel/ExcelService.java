package by.vstu.electronicjournal.service.utils.exсel;

import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelService {

    Workbook getPassReport(String groupName, String period);

    Workbook getPerformanceReport(String facultyName, String period);
}
