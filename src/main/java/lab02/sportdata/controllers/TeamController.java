package lab02.sportdata.controllers;

import lab02.sportdata.dto.team.TeamBaseInfoDTO;
import lab02.sportdata.dto.team.TeamCreateDTO;
import lab02.sportdata.dto.team.TeamFullInfoDTO;
import lab02.sportdata.entities.Team;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;
import lab02.sportdata.services.LeagueService;
import lab02.sportdata.services.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    /*private final LeagueService leagueService;*/

    public TeamController(TeamService teamService/*, LeagueService leagueService*/) {
        this.teamService = teamService;
/*        this.leagueService = leagueService;*/
    }

    @GetMapping("/league/{id}")
    public ResponseEntity<?> getTeams(@PathVariable Long id) {
        List<TeamBaseInfoDTO> teams;
        try{
            teams = teamService.getTeamsByLeague(id);
        } catch (CloseConnectionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(teams);
    }

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody TeamCreateDTO teamCreateDTO) {
        try{
            teamService.save(teamCreateDTO);
        }catch (CreateEntityException |NotFoundException |CloseConnectionException e ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Team created successfully: " + teamCreateDTO.getTeamName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeamById(@PathVariable Long id) {
        TeamFullInfoDTO team;
        try{
            team = teamService.getTeamById(id);
        } catch (CloseConnectionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(team);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id){
        try{
            teamService.deleteTeamById(id);
        } catch (CloseConnectionException | NotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Team deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> addPlayerToTeam(@PathVariable Long id, Long playerId) throws CloseConnectionException {
        try{
            teamService.addPlayerToTeam(id , playerId);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Player added successfully\n" + getTeamById(id));
    }
}
