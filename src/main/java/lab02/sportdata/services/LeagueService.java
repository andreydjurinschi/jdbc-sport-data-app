package lab02.sportdata.services;

import lab02.sportdata.dao.LeagueDAOImpl;
import lab02.sportdata.dto.LeagueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueService {

    @Autowired
    private final LeagueDAOImpl leagueDAO;

    public LeagueService(LeagueDAOImpl leagueDAO) {
        this.leagueDAO = leagueDAO;
    }

/*    public List<LeagueDTO> getLeagues() {
        return le
    }*/
}

