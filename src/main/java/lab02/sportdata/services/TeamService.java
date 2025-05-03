package lab02.sportdata.services;

import lab02.sportdata.dao.leagueDAO.LeagueDAOImpl;
import lab02.sportdata.dao.teamDAO.TeamDAOImpl;
import lab02.sportdata.dto.team.TeamBaseInfoDTO;
import lab02.sportdata.dto.team.TeamCreateDTO;
import lab02.sportdata.dto.team.TeamFullInfoDTO;
import lab02.sportdata.entities.League;
import lab02.sportdata.entities.Team;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    private final TeamDAOImpl teamDAO;
    private final LeagueDAOImpl leagueDAO;

    public TeamService(TeamDAOImpl teamDAO, LeagueDAOImpl leagueDAO) {
        this.teamDAO = teamDAO;
        this.leagueDAO = leagueDAO;
    }

    public List<TeamBaseInfoDTO> getTeamsByLeague(Long leagueId) throws CloseConnectionException, NotFoundException {
        List<TeamBaseInfoDTO> teams = new ArrayList<>();
        League league = leagueDAO.getLeague(leagueId);
        if(league == null) {throw new NotFoundException("League not found");}
        List<Team> teamEntity = teamDAO.getTeamsByLeague(league.getId());
        for(Team team : teamEntity) {
            teams.add(team.mapToDto());
        }
        return teams;
    }

    public void save(TeamCreateDTO teamCreateDTO) throws CreateEntityException, CloseConnectionException {
        if(teamCreateDTO.getLeagueId() == null) {
            throw new CreateEntityException("League must be set");
        }
        if(teamCreateDTO.getTeamName().isEmpty()) {
            throw new CreateEntityException("Team name must be set");
        }
        if(teamCreateDTO.getTeamName().length() < 2 || teamCreateDTO.getTeamName().length() > 25) {
            throw new CreateEntityException("Team name must be between 2 and 25 characters");
        }
        League league;
        try{
            league = leagueDAO.getLeague(teamCreateDTO.getLeagueId());
        } catch (NotFoundException e) {
            throw new CreateEntityException("League not found");
        }
        teamDAO.createTeam(teamCreateDTO.mapToEntity(league));
    }

    public TeamFullInfoDTO getTeamById(Long id) throws NotFoundException, CloseConnectionException {
        Team team = teamDAO.getTeamById(id);
        if(team == null) {throw new NotFoundException("Team not found");}
        return team.mapToFullInfoDTO();
    }

    public void addPlayerToTeam(Long teamId, Long playerId) throws CloseConnectionException, NotFoundException {
        Team team = teamDAO.getTeamById(teamId);
        if(team == null) {throw new NotFoundException("Team not found");}
        teamDAO.addPlayerToTeam(teamId, playerId);
    }

    public void deleteTeamById(Long id) throws NotFoundException, CloseConnectionException {
        Team team = teamDAO.getTeamById(id);
        if(team == null) {throw new NotFoundException("Team not found");}
        teamDAO.deleteTeam(id);
    }
}
