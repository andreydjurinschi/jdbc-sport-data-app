package lab02.sportdata.controllers;


import lab02.sportdata.dto.league.LeagueCreateDTO;
import lab02.sportdata.dto.league.LeagueDTO;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.services.LeagueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leagues")
public class LeagueController {

    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping
    public ResponseEntity<?> getLeagues(){
        List<LeagueDTO> leagueDTO;
        try {
            leagueDTO = leagueService.getLeagues();
        } catch (CloseConnectionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(leagueDTO);
    }

    @PostMapping
    public ResponseEntity<?> createLeague(@RequestBody LeagueCreateDTO leagueCreateDTO) {
        try{
            leagueService.createLeague(leagueCreateDTO);
        } catch (CreateEntityException | CloseConnectionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(leagueCreateDTO);
    }

    /*@PutMapping("/{id}")
    public ResponseEntity<?> changeLeague(@RequestBody LeagueDTO leagueCreateDTO, @PathVariable Long id)
    {
        try{
            leagueService.updateLeague(leagueCreateDTO);
        }
    }*/


}
