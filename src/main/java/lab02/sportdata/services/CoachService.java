package lab02.sportdata.services;

import lab02.sportdata.dao.coachDAO.CoachDAO;
import lab02.sportdata.dto.coach.CoachCreateDTO;
import lab02.sportdata.dto.coach.CoachDTO;
import lab02.sportdata.dto.coach.CoachUpdateDTO;
import lab02.sportdata.entities.Coach;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CoachService {

    private final CoachDAO coachDAO;

    public CoachService(CoachDAO coachDAO) {
        this.coachDAO = coachDAO;
    }

    public void createCoach(CoachCreateDTO dto) throws SQLException, CreateEntityException, CloseConnectionException {
        Coach coach = new Coach();
        coach.setName(dto.getName());
        coach.setTeamId(dto.getTeamId());
        coachDAO.create(coach);
    }

    public CoachDTO getCoachById(Long id) throws SQLException, CloseConnectionException, NotFoundException {
        Coach coach = coachDAO.getById(id);
        if (coach == null) throw new NotFoundException("Coach not found");
        return mapToDTO(coach);
    }

    public List<CoachDTO> getAllCoaches() throws SQLException, CloseConnectionException {
        return coachDAO.getAll().stream().map(this::mapToDTO).toList();
    }

    public void updateCoach(Long id, CoachUpdateDTO dto) throws SQLException, CloseConnectionException, NotFoundException {
        Coach existing = coachDAO.getById(id);
        if (existing == null) throw new NotFoundException("Coach not found");
        existing.setName(dto.getName());
        existing.setTeamId(dto.getTeamId());
        coachDAO.update(id, existing);
    }

    public void deleteCoach(Long id) throws SQLException, CloseConnectionException, NotFoundException {
        coachDAO.delete(id);
    }

    private CoachDTO mapToDTO(Coach coach) {
        CoachDTO dto = new CoachDTO();
        dto.setId(coach.getId());
        dto.setName(coach.getName());
        dto.setTeamId(coach.getTeamId());
        return dto;
    }
}

