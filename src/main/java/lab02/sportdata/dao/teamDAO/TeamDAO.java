package lab02.sportdata.dao.teamDAO;

import lab02.sportdata.entities.Team;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;

import java.util.List;

public interface TeamDAO {
    List<Team> getTeamsByLeague(Long id) throws CloseConnectionException, NotFoundException;
    Team getTeamById(Long id) throws CloseConnectionException, NotFoundException;
    void createTeam(Team team) throws CloseConnectionException, CreateEntityException;
    void addPlayerToTeam(Long teamId, Long playerId) throws CloseConnectionException, NotFoundException;
    void deleteTeam(Long id) throws CloseConnectionException, NotFoundException;
}
