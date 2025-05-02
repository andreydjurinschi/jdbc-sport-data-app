package lab02.sportdata.dao.leagueDAO;

import lab02.sportdata.entities.League;
import lab02.sportdata.entities.Team;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LeagueDAOImpl implements LeagueDAO {

    private final DataSource dataSource;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public LeagueDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<League> getLeagues() throws CloseConnectionException {
        String sql = "select * from league";
        List<League> leagues = new ArrayList<>();
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
           while (resultSet.next()) {
               League league = new League();
               league.setId(resultSet.getLong("id"));
               league.setName(resultSet.getString("name"));
               leagues.add(league);
           }
        } catch (SQLException ignored) {

        }finally {
            try{
                connection.close();
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException e) {
                throw new CloseConnectionException(e.getMessage());
            }
        }
        return leagues;
    }

    @Override
    public League getLeague(Long id) throws NotFoundException, CloseConnectionException {
        String sql = "select * from league where id = ?";
        League league = null;
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                league = new League();
                league.setId(resultSet.getLong("id"));
                league.setName(resultSet.getString("name"));
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
        return league;
    }

    @Override
    public League getAllLeagueInfo(Long id) throws NotFoundException, CloseConnectionException {
        League league = null;
        String leagueSql = "select * from league where id = ?";
        String teamSql = "select * from team where league_id = ?";

        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(leagueSql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                league = new League();
                league.setId(resultSet.getLong("id"));
                league.setName(resultSet.getString("name"));
            }
            if(league != null){
                preparedStatement = connection.prepareStatement(teamSql);
                preparedStatement.setLong(1, id);
                resultSet = preparedStatement.executeQuery();
                List<Team> leagueTeams = new ArrayList<>();
                while (resultSet.next()) {
                    Team team = new Team();
                    team.setId(resultSet.getLong("id"));
                    team.setName(resultSet.getString("name"));
                    team.setLeague(league);
                    leagueTeams.add(team);
                }
                league.setTeams(leagueTeams);
            }
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
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
        return league;
    }

    @Override
    public void save(League league) throws CreateEntityException, CloseConnectionException {
        String query = "insert into league(name) values(?)";
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, league.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CreateEntityException(e.getMessage());
        }finally {
            try {
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new CloseConnectionException(e.getMessage());
            }
        }
    }

    @Override
    public void delete(Long id) throws NotFoundException, CloseConnectionException {
        String deleteLeague = "delete from league where id = ?";
        String deleteTeam = "delete from team where league_id = ?";
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(deleteTeam);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(deleteLeague);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }finally {
            try {
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new CloseConnectionException(e.getMessage());
            }
        }
    }

    @Override
    public void update(Long id, League leagueToUpdate) throws CloseConnectionException, NotFoundException {
        String sql = "update league set name = ? where id = ?";
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, leagueToUpdate.getName());
            preparedStatement.setLong(2, id);
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

    public int getTeamCount(League league) {
        String sql = "select count(*) from team where league_id = ?";
        int count = 0;
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, league.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try{
                connection.close();
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return count;
    }
}
