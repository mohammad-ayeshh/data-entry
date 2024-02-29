package com.example.data.entry.grade;

import com.example.data.entry.Auth.AuthController;
import com.example.data.entry.Auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/grades")
public class GradeController {
    @Autowired
    private AuthService authService;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private RestTemplate restTemplate;

    private Boolean verify() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authService.getAuthToken()); // Set the bearer token in the header

            String verifyServiceUrl = "http://auth-app:8081/verify";
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Boolean> response = restTemplate.exchange(verifyServiceUrl, HttpMethod.GET, entity, Boolean.class);


            boolean b = response.getBody() != null && response.getBody();
            System.out.println("verified");
            return b;
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized request");
            return false;
        } catch (Exception e) {
            System.out.println("An error occurred during the GET request to /verify");
            e.printStackTrace();
            return false;
        }
    }
    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        if (verify()) {
            return ResponseEntity.ok().body(gradeRepository.findAll());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping
    public ResponseEntity<Grade> createGrade(@RequestBody Grade grade) {
        if (!verify()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Grade savedGrade = gradeRepository.save(grade);
        return ResponseEntity.ok(savedGrade);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        if (!verify()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Grade grade = gradeRepository.findById(id).orElseThrow(() -> new RuntimeException("Grade not found"));
        return ResponseEntity.ok(grade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody Grade gradeDetails) {
        if (!verify()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Grade grade = gradeRepository.findById(id).orElseThrow(() -> new RuntimeException("Grade not found"));
        grade.setValue(gradeDetails.getValue());
        final Grade updatedGrade = gradeRepository.save(grade);
        return ResponseEntity.ok(updatedGrade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        if (!verify()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        gradeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}