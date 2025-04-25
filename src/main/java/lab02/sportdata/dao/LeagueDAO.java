package lab02.sportdata.dao;


import lab02.sportdata.entities.League;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;

import java.util.List;

public interface LeagueDAO {
    List<League> getLeagues();
    League getLeague(Long id);
    void save(League league) throws CreateEntityException, CloseConnectionException;
    public void update(League league) throws CreateEntityException, CloseConnectionException;
}
