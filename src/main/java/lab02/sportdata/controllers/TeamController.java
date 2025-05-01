package lab02.sportdata.controllers;

import lab02.sportdata.dto.team.TeamBaseInfoDTO;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.NotFoundException;
import lab02.sportdata.services.LeagueService;
import lab02.sportdata.services.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}")
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
}
