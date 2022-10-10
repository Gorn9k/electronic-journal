package by.vstu.electronicjournal.controller;

import by.vstu.electronicjournal.dto.requestBodyParams.PatternDTO;
import by.vstu.electronicjournal.service.utils.UtilService;
import by.vstu.electronicjournal.service.utils.exсel.ExcelService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("utils")
public class UtilController {

    @Autowired
    private UtilService utilService;
    @Autowired
    private ExcelService excelService;

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
                                                 @RequestParam("file") MultipartFile file,
                                                 @RequestParam("id_from_source") Long idFromSource){
        if (!file.isEmpty()) {
            try {
                utilService.uploadImage(name, file, idFromSource);
                return "Вы удачно загрузили фото" + name + "в профиль преподавателя!";
            } catch (Exception e) {
                return "Вам не удалось загрузить " + name + " => " + e.getMessage();
            }
        } else {
            return "Вам не удалось загрузить " + name + " потому что файл пустой.";
        }
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("generate")
    public void generate() {
        utilService.generate();
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("update")
    public void update() {
        utilService.generateJournalHeadersEveryDay();
    }

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("myExcel")
    public void getExcel(HttpServletResponse response, @RequestParam String groupName, @RequestParam String period) throws IOException {

        response.setHeader("Content-Disposition", "inline;filename=\"" + URLEncoder.encode("321.xlsx", "UTF-8") + "\"");
        response.setContentType("application/xlsx");

        Workbook workbook = excelService.getPassReport(groupName, period);
        OutputStream outputStream = response.getOutputStream();

        workbook.getCreationHelper().createFormulaEvaluator().clearAllCachedResultValues();
        workbook.setForceFormulaRecalculation(true);

        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
        outputStream.close();

    }

    @Secured({"ROLE_DEAN", "ROLE_HEAD_OF_DEPARTMENT", "ROLE_RECTOR", "ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("mySecondExcel")
    public void getSecondExcel(HttpServletResponse response, @RequestParam String cathedraName, @RequestParam String period) throws IOException {

        response.setHeader("Content-Disposition", "inline;filename=\"" + URLEncoder.encode("123.xlsx", "UTF-8") + "\"");
        response.setContentType("application/xlsx");

        Workbook workbook = excelService.getPerformanceReport(cathedraName, period);
        OutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();

        outputStream.flush();
        outputStream.close();

    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public void autoGenerate(@RequestBody List<PatternDTO> patternDTOList) {
        utilService.autoGenerate(patternDTOList);
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("updateFromRelatedService")
    public void updateFromRelatedService() {
        utilService.updateFromRelatedServiceEveryDay();
    }
}

