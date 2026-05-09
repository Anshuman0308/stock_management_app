package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private static void validatePath(String filePath) throws IOException {
        String canonical = new File(filePath).getCanonicalPath();
        String allowedDir = new File("").getCanonicalPath();
        if (!canonical.startsWith(allowedDir)) {
            throw new IOException("Access denied: path outside allowed directory");
        }
    }

    private static Stock parseRow(Row row, DataFormatter formatter) {
        String idStr = formatter.formatCellValue(row.getCell(0)).trim();
        if (idStr.isEmpty()) return null;
        try {
            Stock stock = new Stock();
            stock.setId(Integer.parseInt(idStr));
            stock.setProductName(row.getCell(1) != null ? formatter.formatCellValue(row.getCell(1)) : "");
            String priceStr = formatter.formatCellValue(row.getCell(2));
            stock.setPrice(priceStr.isEmpty() ? 0.0 : Double.parseDouble(priceStr));
            String stockLeftStr = formatter.formatCellValue(row.getCell(3));
            stock.setStockLeft(stockLeftStr.isEmpty() ? 0 : Integer.parseInt(stockLeftStr));
            String sellStr = formatter.formatCellValue(row.getCell(4));
            stock.setSell(sellStr.isEmpty() ? 0 : Integer.parseInt(sellStr));
            String highStr = formatter.formatCellValue(row.getCell(5));
            stock.setHigh(highStr.isEmpty() ? 0.0 : Double.parseDouble(highStr));
            return stock;
        } catch (NumberFormatException e) {
            org.slf4j.LoggerFactory.getLogger(ExcelUtil.class).warn("Skipping row due to parse error: {}", e.getMessage());
            return null;
        }
    }

    public static List<Stock> importFromExcel(InputStream inputStream) throws IOException {
        List<Stock> stocks = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null) {
                    Stock stock = parseRow(row, formatter);
                    if (stock != null) stocks.add(stock);
                }
            }
        }
        return stocks;
    }

    public static List<Stock> importFromExcel(String filePath) throws IOException {
        validatePath(filePath);
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return importFromExcel(fis);
        }
    }

    public static void exportToExcel(List<Stock> stocks, String filePath) throws IOException {
        validatePath(filePath);
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
            String canonicalPath = new File(filePath).getCanonicalPath();
            try (FileOutputStream fos = new FileOutputStream(canonicalPath)) {
                workbook.write(fos);
            }
        }
    }
}
