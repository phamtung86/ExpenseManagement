package com.vti.controller;

import com.vti.dto.MoneySourcesDTO;
import com.vti.entity.MoneySources;
import com.vti.form.MoneySourceForm;
import com.vti.service.IMoneySourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/money-sources")
public class MoneySourceController {

    @Autowired
    private IMoneySourceService moneySourceService;

    @GetMapping
    public ResponseEntity<List<MoneySourcesDTO>> getAllMoneySources() {
        return ResponseEntity.ok(moneySourceService.getAllMoneySources());
    }

    @PostMapping
    public ResponseEntity<?> createNewMoneySource(@RequestBody MoneySourceForm createMoneySourceForm) {
        MoneySources moneySources = moneySourceService.createNewMoneySource(createMoneySourceForm);
        if (moneySources != null) {
            return ResponseEntity.ok().body("Successfully created money source");
        }
        return ResponseEntity.badRequest().body("Failed to create money source");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMoneySource(@PathVariable int id) {
        moneySourceService.deleteMoneySource(id);
        return ResponseEntity.ok().body("Delete money source success");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMoneySource(@PathVariable int id, @RequestBody MoneySourceForm moneySourceForm) {
        boolean isUpdate = moneySourceService.updateMoneySource(id, moneySourceForm);
        if (isUpdate){
            return ResponseEntity.ok().body("Successfully updated money source");
        }
        return ResponseEntity.badRequest().body("Failed to update money source");
    }

}
