package lab02.sportdata.dao;

import lab02.sportdata.entities.League;
import lab02.sportdata.entities.Team;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
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
    public List<League> getLeagues() {
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
        } catch (SQLException e) {
            e.getMessage();
        }finally {
            try{
                connection.close();
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.getMessage();
            }
        }
        return leagues;
    }

    @Override
    public League getLeague(Long id) {
        return null;
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
    public void update(League league) throws CreateEntityException, CloseConnectionException {
        String query = "update league set name=? where id=?";
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, league.getName());
            preparedStatement.setLong(2, league.getId());
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
