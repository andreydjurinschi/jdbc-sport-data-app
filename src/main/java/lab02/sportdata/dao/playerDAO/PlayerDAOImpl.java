package lab02.sportdata.dao.playerDAO;

import lab02.sportdata.entities.Player;
import lab02.sportdata.entities.Team;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
        String sqlForTeam = "SELECT * FROM team WHERE id = ?";

        try {
            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                player = new Player();
                player.setId(resultSet.getLong("id"));
                player.setName(resultSet.getString("name"));

                String playerPosition = resultSet.getString("position");
                if (playerPosition != null) {
                    player.setPosition(Player.Position.valueOf(playerPosition.toUpperCase()));
                }

                player.setNumber(resultSet.getInt("number"));
                long teamId = resultSet.getLong("team_id");
                resultSet.close();
                preparedStatement.close();

                preparedStatement = connection.prepareStatement(sqlForTeam);
                preparedStatement.setLong(1, teamId);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    team = new Team();
                    team.setId(resultSet.getLong("id"));
                    team.setName(resultSet.getString("name"));
                }
                player.setTeam(team);
            }

        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        } finally {
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                throw new CloseConnectionException(e.getMessage());
            }
        }

        return player;
    }


    @Override
    public void create(Player player) throws CloseConnectionException, CreateEntityException {
        String sql = "INSERT INTO player (name, position, number, team_id) VALUES (?, ?, ?, ?)";
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, player.getPosition().toString());
            preparedStatement.setInt(3, player.getNumber());
            preparedStatement.setLong(4, player.getTeam().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CreateEntityException(e.getMessage());
        }finally {
            try{
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new CloseConnectionException(e.getMessage());
            }
        }
    }

    @Override
    public void update(Long id, Player player) throws CreateEntityException, CloseConnectionException, NotFoundException {
        String sql = "UPDATE player SET name = ?, position = ?, number = ?, team_id = ? WHERE id = ?";
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, player.getPosition().toString());
            preparedStatement.setInt(3, player.getNumber());
            preparedStatement.setLong(4, player.getTeam().getId());
            preparedStatement.setLong(5, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }finally {
            try{
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new CloseConnectionException(e.getMessage());
            }
        }
    }

    @Override
    public void deleteFromTeam(Long id) throws CloseConnectionException, NotFoundException {
        String sql = "Update player SET team_id = null WHERE id = ?";
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }finally {
            try{
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new CloseConnectionException(e.getMessage());
            }
        }
    }
}
