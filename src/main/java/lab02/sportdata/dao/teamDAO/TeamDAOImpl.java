package lab02.sportdata.dao.teamDAO;
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
        List<Team> teams = new ArrayList<Team>();
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
    public void getTeamById(Long id) throws CloseConnectionException, NotFoundException {

    }

    @Override
    public void createTeam(Team team) throws CloseConnectionException, CreateEntityException {

    }

    @Override
    public void updateTeam(Team team) throws CloseConnectionException, NotFoundException {

    }
}
