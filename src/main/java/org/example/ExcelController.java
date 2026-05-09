package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private StockRepository repository;

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportToExcel() throws Exception {
        List<Stock> stocks = repository.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Stocks");
            Row header = sheet.createRow(0);
            String[] cols = {"ID", "Product Name", "Price", "Stock Left", "Sell", "High"};
            for (int i = 0; i < cols.length; i++) header.createCell(i).setCellValue(cols[i]);
            int rowNum = 1;
            for (Stock s : stocks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(s.getId());
                row.createCell(1).setCellValue(s.getProductName());
                row.createCell(2).setCellValue(s.getPrice());
                row.createCell(3).setCellValue(s.getStockLeft());
                row.createCell(4).setCellValue(s.getSell());
                row.createCell(5).setCellValue(s.getHigh());
            }
            workbook.write(out);
        }
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=stocks.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importFromExcel(@RequestParam("file") MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            List<Stock> stocks = ExcelUtil.importFromExcel(is);
            repository.saveAll(stocks);
            return ResponseEntity.ok("Imported " + stocks.size() + " records");
        }
    }
}
