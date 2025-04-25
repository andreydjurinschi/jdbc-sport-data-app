package lab02.sportdata.dao;


import lab02.sportdata.entities.League;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;

import java.util.List;

public interface LeagueDAO {
    List<League> getLeagues() throws CloseConnectionException;
    League getLeague(Long id) throws NotFoundException, CloseConnectionException;
    void save(League league) throws CreateEntityException, CloseConnectionException;
    public void update(League league) throws CreateEntityException, CloseConnectionException;
}
