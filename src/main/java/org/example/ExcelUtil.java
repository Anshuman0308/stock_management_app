package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    public static List<Stock> importFromExcel(String filePath) throws IOException {
        List<Stock> stocks = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null) {
                    Cell cell0 = row.getCell(0);
                    String idStr = formatter.formatCellValue(cell0).trim();
                    if (idStr.isEmpty()) continue;
                    
                    try {
                        int id = Integer.parseInt(idStr);
                        Stock stock = new Stock();
                        stock.setId(id);
                        
                        Cell cell1 = row.getCell(1);
                        stock.setProductName(cell1 != null ? formatter.formatCellValue(cell1) : "");
                        
                        Cell cell2 = row.getCell(2);
                        String priceStr = formatter.formatCellValue(cell2);
                        stock.setPrice(priceStr.isEmpty() ? 0.0 : Double.parseDouble(priceStr));
                        
                        Cell cell3 = row.getCell(3);
                        String stockLeftStr = formatter.formatCellValue(cell3);
                        stock.setStockLeft(stockLeftStr.isEmpty() ? 0 : Integer.parseInt(stockLeftStr));
                        
                        Cell cell4 = row.getCell(4);
                        String sellStr = formatter.formatCellValue(cell4);
                        stock.setSell(sellStr.isEmpty() ? 0 : Integer.parseInt(sellStr));
                        
                        Cell cell5 = row.getCell(5);
                        String highStr = formatter.formatCellValue(cell5);
                        stock.setHigh(highStr.isEmpty() ? 0.0 : Double.parseDouble(highStr));
                        
                        stocks.add(stock);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
        }
        return stocks;
    }

    public static void exportToExcel(List<Stock> stocks, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Stocks");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Product Name");
            header.createCell(2).setCellValue("Price");
            header.createCell(3).setCellValue("Stock Left");
            header.createCell(4).setCellValue("Sell");
            header.createCell(5).setCellValue("High");

            int rowNum = 1;
            for (Stock stock : stocks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stock.getId());
                row.createCell(1).setCellValue(stock.getProductName());
                row.createCell(2).setCellValue(stock.getPrice());
                row.createCell(3).setCellValue(stock.getStockLeft());
                row.createCell(4).setCellValue(stock.getSell());
                row.createCell(5).setCellValue(stock.getHigh());
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }
}
