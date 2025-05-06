package lab02.sportdata.controllers;

import lab02.sportdata.dto.player.PlayerBaseInfoDTO;
import lab02.sportdata.dto.player.PlayerCreateDTO;
import lab02.sportdata.dto.player.PlayerFullInfoDTO;
import lab02.sportdata.dto.player.PlayerUpdateDTO;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;
import lab02.sportdata.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;


    @GetMapping("/team/{teamId}/players")
    public ResponseEntity<?> getPlayersByTeam(@PathVariable Long teamId) {
        try{
            List<PlayerBaseInfoDTO> players = playerService.getPlayersFromTeam(teamId);
            return ResponseEntity.status(HttpStatus.OK).body(players);
        } catch (CloseConnectionException | NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getPlayerById(@PathVariable Long playerId) {
        try{
            PlayerFullInfoDTO player = playerService.getPlayerById(playerId);
            return ResponseEntity.ok().body(player);
        } catch (CloseConnectionException | NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable Long id, @RequestBody PlayerUpdateDTO player)
    {
        try{
            playerService.updatePlayer(id, player);
            return ResponseEntity.status(HttpStatus.OK).body("Player updated: \n" + player);
        } catch (CreateEntityException | CloseConnectionException | NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createPlayer(@RequestBody PlayerCreateDTO playerDTO) {
        try {
            playerService.createPlayer(playerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Player created successfully.");
        } catch (CreateEntityException | NotFoundException | CloseConnectionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/remove-from-team")
    public ResponseEntity<?> removePlayerFromTeam(@PathVariable Long id) {
        try {
            playerService.removePlayerFromTeam(id);
            return ResponseEntity.ok("Player with ID " + id + " has been removed from the team.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (CloseConnectionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }





}
