package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private StockRepository repository;

    @Override
    public void run(String... args) throws Exception {
        File excelFile = new File("stocks.xlsx");
        if (excelFile.exists()) {
            List<Stock> stocks = ExcelUtil.importFromExcel(excelFile.getAbsolutePath());
            repository.saveAll(stocks);
            log.info("Imported {} records from Excel on startup", stocks.size());
        }
    }
}
