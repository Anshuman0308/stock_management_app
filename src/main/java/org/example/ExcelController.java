package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private StockRepository repository;

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportToExcel() throws Exception {
        List<Stock> stocks = repository.findAll();
        String tempFile = "temp_export.xlsx";
        ExcelUtil.exportToExcel(stocks, tempFile);
        
        File file = new File(tempFile);
        InputStreamResource resource = new InputStreamResource(new java.io.FileInputStream(file));
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=stocks.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importFromExcel(@RequestParam("file") MultipartFile file) throws Exception {
        String tempFile = "temp_import.xlsx";
        file.transferTo(new File(tempFile));
        
        List<Stock> stocks = ExcelUtil.importFromExcel(tempFile);
        repository.saveAll(stocks);
        
        new File(tempFile).delete();
        return ResponseEntity.ok("Imported " + stocks.size() + " records");
    }
}
