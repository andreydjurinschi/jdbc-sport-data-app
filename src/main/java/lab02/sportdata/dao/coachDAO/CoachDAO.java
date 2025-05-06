package lab02.sportdata.dao.coachDAO;

import lab02.sportdata.entities.Coach;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;

import java.util.List;

public interface CoachDAO {
    List<Coach> getAll() throws CloseConnectionException;
    Coach getById(Long id) throws NotFoundException, CloseConnectionException;
    void create(Coach coach) throws CreateEntityException, CloseConnectionException;
    void update(Long id, Coach coach) throws NotFoundException, CloseConnectionException;
    void delete(Long id) throws NotFoundException, CloseConnectionException;
}

