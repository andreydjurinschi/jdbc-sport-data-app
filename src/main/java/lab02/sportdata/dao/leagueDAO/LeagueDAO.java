package lab02.sportdata.dao.leagueDAO;


import lab02.sportdata.entities.League;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;

import java.util.List;

public interface LeagueDAO {
    List<League> getLeagues() throws CloseConnectionException;
    League getLeague(Long id) throws NotFoundException, CloseConnectionException;
    League getAllLeagueInfo(Long id) throws NotFoundException, CloseConnectionException;
    void save(League league) throws CreateEntityException, CloseConnectionException;
    void delete(Long id) throws NotFoundException, CloseConnectionException;
}
