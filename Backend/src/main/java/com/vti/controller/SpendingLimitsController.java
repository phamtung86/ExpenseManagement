package com.vti.controller;

import com.vti.dto.SpendingLimits.SpendingLimitsCreateRequest;
import com.vti.dto.SpendingLimits.SpendingLimitsDTO;
import com.vti.dto.SpendingLimits.SpendingLimitsUpdateRequest;
import com.vti.service.ISpendingLimitsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/spending-limits")
@RequiredArgsConstructor
public class SpendingLimitsController {

    private final ISpendingLimitsService spendingLimitsService;

    // GET ALL
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllSpendingLimits(@PathVariable int id) {
        try {
            List<SpendingLimitsDTO> list = spendingLimitsService.getAll(id);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch spending limits");
        }
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSpendingLimitById(@PathVariable Integer id) {
        try {
            SpendingLimitsDTO dto = spendingLimitsService.getById(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Spending limit not found");
        }
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createSpendingLimit(@RequestBody SpendingLimitsCreateRequest request) {
        try {
            SpendingLimitsDTO created = spendingLimitsService.create(request);
            if (created != null) {
                return ResponseEntity.ok("Created spending limit successfully");
            }
            return ResponseEntity.status(500).body("Failed to create spending limit");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Create spending limit failed");
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSpendingLimit(@PathVariable Integer id,
                                                 @RequestBody SpendingLimitsUpdateRequest request) {
        try {
            SpendingLimitsDTO updated = spendingLimitsService.update(id, request);
            if (updated != null) {
                return ResponseEntity.ok("Updated spending limit successfully");
            }
            return ResponseEntity.status(500).body("Failed to update spending limit");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Spending limit not found");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Update spending limit failed");
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpendingLimit(@PathVariable Integer id) {
        try {
            spendingLimitsService.delete(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Delete failed");
        }
    }

}
