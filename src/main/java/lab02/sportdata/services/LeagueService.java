package lab02.sportdata.services;

import lab02.sportdata.dao.leagueDAO.LeagueDAOImpl;
import lab02.sportdata.dto.league.LeagueCreateDTO;
import lab02.sportdata.dto.league.LeagueDTO;
import lab02.sportdata.dto.league.LeagueFullInfoDto;
import lab02.sportdata.entities.League;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeagueService {

    private final LeagueDAOImpl leagueDAO;

    public LeagueService(LeagueDAOImpl leagueDAO) {
        this.leagueDAO = leagueDAO;
    }

    public List<LeagueDTO> getLeagues() throws CloseConnectionException {
        List<League> leagues = leagueDAO.getLeagues();
        List<LeagueDTO> dtos = new ArrayList<>();
        for(League league : leagues) {
            dtos.add(league.mapToDto(league.getName(), leagueDAO.getTeamCount(league)));
        }
        return dtos;
    }

    public LeagueFullInfoDto getLeague(Long id) throws NotFoundException, CloseConnectionException {
        League league = leagueDAO.getAllLeagueInfo(id);

        if(league == null) {
            throw new NotFoundException("League not found");
        }
        return league.mapToFullInfoDto();
    }

    public void createLeague(LeagueCreateDTO leagueCreateDTO) throws CreateEntityException, CloseConnectionException {
        if(leagueCreateDTO.getName().isEmpty()) {
            throw new CreateEntityException("League name cannot be empty");
        }
        if(leagueCreateDTO.getName().length() > 10 || leagueCreateDTO.getName().length() < 3) {
            throw new CreateEntityException("League name must be between 3 and 10 characters");
        }
        leagueDAO.save(leagueCreateDTO.mapToEntity(leagueCreateDTO.getName()));
    }

    public void updateLeague(Long id, LeagueCreateDTO leagueCreateDTO) throws NotFoundException, CreateEntityException, CloseConnectionException {
        League league = leagueDAO.getLeague(id);
        if(league == null) {
            throw new NotFoundException("League not found");
        }
        if(leagueCreateDTO.getName().isEmpty()) {
            throw new CreateEntityException("League name cannot be empty");
        }
        if(leagueCreateDTO.getName().length() > 10 || leagueCreateDTO.getName().length() < 3) {
            throw new CreateEntityException("League name must be between 3 and 10 characters");
        }
        league.setName(leagueCreateDTO.getName());
        leagueDAO.update(id, league);
    }

    public void deleteLeague(Long id) throws NotFoundException, CloseConnectionException {
        League league = leagueDAO.getLeague(id);
        if(league == null) {
            throw new NotFoundException("League not found");
        }
        leagueDAO.delete(id);
    }
}

