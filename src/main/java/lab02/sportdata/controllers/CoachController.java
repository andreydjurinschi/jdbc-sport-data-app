package lab02.sportdata.controllers;

import lab02.sportdata.dto.coach.CoachCreateDTO;
import lab02.sportdata.dto.coach.CoachUpdateDTO;
import lab02.sportdata.exception.NotFoundException;
import lab02.sportdata.services.CoachService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coaches")
public class CoachController {

    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @PostMapping
    public ResponseEntity<?> createCoach(@RequestBody CoachCreateDTO dto) {
        try {
            coachService.createCoach(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Coach created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCoach(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(coachService.getCoachById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCoaches() {
        try {
            return ResponseEntity.ok(coachService.getAllCoaches());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCoach(@PathVariable Long id, @RequestBody CoachUpdateDTO dto) {
        try {
            coachService.updateCoach(id, dto);
            return ResponseEntity.ok("Coach updated");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoach(@PathVariable Long id) {
        try {
            coachService.deleteCoach(id);
            return ResponseEntity.ok("Coach deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

