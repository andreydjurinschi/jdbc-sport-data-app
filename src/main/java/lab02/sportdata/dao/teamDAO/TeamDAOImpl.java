package lab02.sportdata.dao.teamDAO;
import lab02.sportdata.entities.League;
import lab02.sportdata.entities.Player;
import lab02.sportdata.entities.Team;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TeamDAOImpl implements TeamDAO {

    private final DataSource dataSource;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public TeamDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<Team> getTeamsByLeague(Long id) throws CloseConnectionException, NotFoundException {
        String query = "SELECT * FROM team WHERE league_id = ?";
        List<Team> teams = new ArrayList<>();
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Team team = new Team();
                team.setId(resultSet.getLong("id"));
                team.setName(resultSet.getString("name"));
                teams.add(team);
            }
        } catch (SQLException e) {
            throw new NotFoundException("League not found");
        } finally {
            try{
                connection.close();
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException e) {
                throw new CloseConnectionException(e.getMessage());
            }
        }
        return teams;
    }

    @Override
    public Team getTeamById(Long id) throws CloseConnectionException, NotFoundException {
        Team team = null;
        League league = null;
        String sqlTeam = "SELECT * FROM team WHERE id = ?";
        String sqlPlayer = "SELECT * FROM player WHERE team_id = ?";
        String sqlLeague = "SELECT * FROM league WHERE id = ?";

        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sqlTeam);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                team = new Team();
                team.setId(resultSet.getLong("id"));
                team.setName(resultSet.getString("name"));
                Long leagueId = resultSet.getLong("league_id");
                PreparedStatement psLeague = connection.prepareStatement(sqlLeague);
                psLeague.setLong(1, leagueId);
                ResultSet resLeague = psLeague.executeQuery();
                while (resLeague.next()) {
                    league = new League();
                    league.setId(resLeague.getLong("id"));
                    league.setName(resLeague.getString("name"));
                }
                team.setLeague(league);
                psLeague.close();
                resLeague.close();
            }
            if(team != null){
                preparedStatement = connection.prepareStatement(sqlPlayer);
                preparedStatement.setLong(1, id);
                resultSet = preparedStatement.executeQuery();
                List<Player> players = new ArrayList<>();
                while (resultSet.next()) {
                    Player player = new Player();
                    player.setId(resultSet.getLong("id"));
                    player.setName(resultSet.getString("name"));
                    players.add(player);
                }
                team.setPlayers(players);
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
        return team;
    }

    @Override
    public void createTeam(Team team) throws CloseConnectionException, CreateEntityException {
        String query = "INSERT INTO team (name, league_id) VALUES (?, ?)";
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, team.getName());
            preparedStatement.setLong(2, team.getLeague().getId());
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
    public void addPlayerToTeam(Long teamId, Long playerId) throws CloseConnectionException, NotFoundException {
        String sql = "update player set team_id = ? where id = ?";
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, teamId);
            preparedStatement.setLong(2, playerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
        finally{
            try{
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new CloseConnectionException(e.getMessage());
            }
        }
    }

    @Override
    public void deleteTeam(Long id) throws CloseConnectionException, NotFoundException {
        String deleteTeamQuery = "DELETE FROM team WHERE id = ?";
        String playerToFreeAgent = "update player set team_id = null where team_id = ?";
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(playerToFreeAgent);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(deleteTeamQuery);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
        finally {
            try{
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new CloseConnectionException(e.getMessage());
            }
        }




    }
}
