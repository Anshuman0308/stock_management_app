package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockRepository repository;

    @GetMapping
    public List<Stock> getAll() {
        return repository.findAll();
    }

    @GetMapping("/search")
    public List<Stock> search(@RequestParam(required = false, defaultValue = "") String name) {
        if (name.isEmpty()) {
            return repository.findAll();
        }
        return repository.findByProductNameContainingIgnoreCase(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getById(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Stock create(@RequestBody Stock stock) {
        return repository.save(stock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> update(@PathVariable int id, @RequestBody Stock stock) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setProductName(stock.getProductName());
                    existing.setPrice(stock.getPrice());
                    existing.setStockLeft(stock.getStockLeft());
                    existing.setSell(stock.getSell());
                    existing.setHigh(stock.getHigh());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
