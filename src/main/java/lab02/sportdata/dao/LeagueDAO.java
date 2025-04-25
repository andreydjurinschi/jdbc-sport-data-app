package lab02.sportdata.dao;


import lab02.sportdata.entities.League;

import java.util.List;

public interface LeagueDAO {
    public List<League> getLeagues();
    public League getLeague(Long id);
}
