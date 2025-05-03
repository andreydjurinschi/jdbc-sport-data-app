package lab02.sportdata.dao.playerDAO;

import lab02.sportdata.entities.Player;
import lab02.sportdata.entities.Team;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.swing.text.Position;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlayerDAOImpl implements PlayerDAO{
    @Autowired
    private DataSource dataSource;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public List<Player> getAllFromTeam(Long teamId) throws CloseConnectionException, NotFoundException {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM player WHERE team_id = ?";

        try{
            connection= dataSource.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(1, teamId);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Player player = new Player();
                player.setId(resultSet.getLong("id"));
                player.setName(resultSet.getString("name"));
                String playerPosition = resultSet.getString("position");
                player.setPosition(Player.Position.valueOf(playerPosition.toUpperCase()));
                player.setNumber(resultSet.getInt("number"));
                players.add(player);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try{
                connection.close();
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException e) {
                throw new CloseConnectionException(e.getMessage());
            }
        }
        return players;
    }

    @Override
    public Player getById(Long id) throws CloseConnectionException, NotFoundException {
        Player player = null;
        Team team = null;
        String sql = "SELECT * FROM player WHERE id = ?";
        String sqlForTeam = "SELECT * FROM player WHERE team_id = ?";
        try{
            connection = dataSource.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                player = new Player();
                player.setId(resultSet.getLong("id"));
                player.setName(resultSet.getString("name"));
                String playerPosition = resultSet.getString("position");
                player.setPosition(Player.Position.valueOf(playerPosition.toUpperCase()));
                player.setNumber(resultSet.getInt("number"));
                int teamId = resultSet.getInt("team_id");
                preparedStatement = connection.prepareStatement(sqlForTeam);
                preparedStatement.setLong(1, teamId);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    team = new Team();
                    team.setId(resultSet.getLong("id"));
                    team.setName(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }finally {
            try{
                connection.close();
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException e) {
                throw new CloseConnectionException(e.getMessage());
            }
        }
        return player;
    }

    @Override
    public void create(Player player) throws CloseConnectionException, CreateEntityException {

    }

    @Override
    public void update(Long id, Player player) throws CreateEntityException, CloseConnectionException, NotFoundException {

    }

    @Override
    public void deleteFromTeam(Long id) throws CloseConnectionException, NotFoundException {

    }
}
