package lab02.sportdata.services;

import lab02.sportdata.dao.playerDAO.PlayerDAOImpl;
import lab02.sportdata.dao.teamDAO.TeamDAOImpl;
import lab02.sportdata.dto.player.PlayerBaseInfoDTO;
import lab02.sportdata.dto.player.PlayerCreateDTO;
import lab02.sportdata.dto.player.PlayerFullInfoDTO;
import lab02.sportdata.dto.player.PlayerUpdateDTO;
import lab02.sportdata.entities.Player;
import lab02.sportdata.entities.Team;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerDAOImpl playerDAO;

    @Autowired
    private TeamDAOImpl teamDAO;

    public List<PlayerBaseInfoDTO> getPlayersFromTeam(Long teamId) throws CloseConnectionException, NotFoundException {
        List<Player> players = playerDAO.getAllFromTeam(teamId);
        List<PlayerBaseInfoDTO> playerBaseInfoDTOs = new ArrayList<>();
        if(players.isEmpty()) {
            throw new NotFoundException("Player list is empty or not such club");
        }
        for (Player player : players) {
            playerBaseInfoDTOs.add(player.mapToDTO());
        }
        return playerBaseInfoDTOs;
    }

    public PlayerFullInfoDTO getPlayerById(Long id) throws CloseConnectionException, NotFoundException
    {
        Player player = playerDAO.getById(id);
        if(player == null) {
            throw new NotFoundException("Player not found");
        }
        return player.mapToFullInfoDTO();
    }

    public void updatePlayer(Long id, PlayerUpdateDTO playerDTO) throws CreateEntityException, CloseConnectionException, NotFoundException {
        Player playerToUpdate = playerDAO.getById(id);
        if (playerToUpdate == null) {
            throw new NotFoundException("Player not found");
        }

        if (playerDTO.getName() == null || playerDTO.getName().isEmpty() ||
                playerDTO.getPosition() == null ||
                playerDTO.getNumber() <= 0) {
            throw new CreateEntityException("Player name, position and number are required");
        }

        if (playerDTO.getNumber() > 100) {
            throw new CreateEntityException("Player number must be between 1 and 100");
        }

        playerToUpdate.setName(playerDTO.getName());
        playerToUpdate.setPosition(playerDTO.getPosition());
        playerToUpdate.setNumber(playerDTO.getNumber());


        Team team = teamDAO.getTeamById(playerDTO.getTeamId());
        if (team == null) {
            throw new NotFoundException("Team not found");
        }
        playerToUpdate.setTeam(team);

        playerDAO.update(id, playerToUpdate);
    }

    public void createPlayer(PlayerCreateDTO dto) throws CreateEntityException, NotFoundException, CloseConnectionException {
        if (dto.getName() == null || dto.getName().isEmpty() ||
                dto.getPosition() == null ||
                dto.getNumber() <= 0 || dto.getNumber() > 100 ||
                dto.getTeamId() == null) {
            throw new CreateEntityException("All fields are required, and number must be between 1 and 100");
        }

        Team team = teamDAO.getTeamById(dto.getTeamId());
        if (team == null) {
            throw new NotFoundException("Team not found");
        }

        Player player = new Player();
        player.setName(dto.getName());
        player.setPosition(dto.getPosition());
        player.setNumber(dto.getNumber());
        player.setTeam(team);

        playerDAO.create(player);
    }

    public void removePlayerFromTeam(Long id) throws CloseConnectionException, NotFoundException {
        Player player = playerDAO.getById(id);
        if (player == null) {
            throw new NotFoundException("Player with ID " + id + " not found.");
        }
        playerDAO.deleteFromTeam(id);
    }






}
