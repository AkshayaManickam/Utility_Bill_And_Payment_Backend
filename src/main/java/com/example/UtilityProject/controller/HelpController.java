package com.example.UtilityProject.controller;

import com.example.UtilityProject.model.Help;
import com.example.UtilityProject.model.Status;
import com.example.UtilityProject.repository.HelpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/help")
@CrossOrigin(origins = "http://localhost:4200") // Allow frontend access
public class HelpController {

    @Autowired
    private HelpRepository helpRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Help>> getAllHelpRequests() {
        List<Help> helpRequests = helpRepository.findAll();
        if (helpRequests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(helpRequests);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<Help> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Optional<Help> helpRequest = helpRepository.findById(id);
        if (helpRequest.isPresent()) {
            Help help = helpRequest.get();
            String newStatus = request.get("status");
            if (newStatus == null) {
                return ResponseEntity.badRequest().body(null);
            }

            help.setStatus(Status.valueOf(newStatus));
            helpRepository.save(help);
            return ResponseEntity.ok(help);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
