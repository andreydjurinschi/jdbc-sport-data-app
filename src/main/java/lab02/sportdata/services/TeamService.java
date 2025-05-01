package lab02.sportdata.services;

import lab02.sportdata.dao.teamDAO.TeamDAOImpl;
import lab02.sportdata.dto.team.TeamBaseInfoDTO;
import lab02.sportdata.entities.Team;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    private final TeamDAOImpl teamDAO;

    public TeamService(TeamDAOImpl teamDAO) {
        this.teamDAO = teamDAO;
    }

    public List<TeamBaseInfoDTO> getTeamsByLeague(Long leagueId) throws CloseConnectionException, NotFoundException {
        List<TeamBaseInfoDTO> teams = new ArrayList<>();
        List<Team> teamEntity = teamDAO.getTeamsByLeague(leagueId);
        for(Team team : teamEntity) {
            teams.add(team.mapToDto());
        }
        return teams;
    }
}
