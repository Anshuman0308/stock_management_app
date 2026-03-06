package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private StockRepository repository;

    @Override
    public void run(String... args) throws Exception {
        File excelFile = new File("stocks.xlsx");
        if (excelFile.exists()) {
            List<Stock> stocks = ExcelUtil.importFromExcel(excelFile.getAbsolutePath());
            repository.saveAll(stocks);
            System.out.println("Imported " + stocks.size() + " records from Excel on startup");
        }
    }
}
