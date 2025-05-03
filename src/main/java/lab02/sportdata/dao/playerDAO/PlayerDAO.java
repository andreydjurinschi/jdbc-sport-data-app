package lab02.sportdata.dao.playerDAO;

import lab02.sportdata.entities.Player;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;

import java.util.List;

public interface PlayerDAO {
    List<Player> getAllFromTeam(Long teamId) throws CloseConnectionException, NotFoundException;
    Player getById(Long id) throws CloseConnectionException, NotFoundException;
    void create(Player player) throws CloseConnectionException, CreateEntityException;
    void update(Long id, Player player) throws CreateEntityException, CloseConnectionException, NotFoundException;
    void deleteFromTeam(Long id) throws CloseConnectionException, NotFoundException;
}
