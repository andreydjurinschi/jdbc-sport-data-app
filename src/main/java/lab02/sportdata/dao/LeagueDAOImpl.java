package lab02.sportdata.dao;

import lab02.sportdata.entities.League;
import lab02.sportdata.entities.Team;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LeagueDAOImpl implements LeagueDAO {

    private final DataSource dataSource;

    public LeagueDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public List<League> getLeagues() {
        String sql = "select l.id as league_id, l.name as league_name, " +
                "t.id as team_id, t.name as team_name, t.league_id as team_league_id " +
                "from league l left join team t on l.id = t.league_id";

        List<League> leagues = new ArrayList<>();
        Map<Long, League> leagueMap;
        leagueMap = new HashMap<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Long leagueId = resultSet.getLong("league_id");
                League league = leagueMap.get(leagueId);
                if (league == null) {
                    league = new League();
                    league.setId(leagueId);
                    league.setName(resultSet.getString("league_name"));
                    league.setTeams(new ArrayList<>());
                    leagueMap.put(leagueId, league);
                    leagues.add(league);
                }

                Long teamId = resultSet.getLong("team_id");
                if (teamId != 0) {
                    Team team = new Team();
                    team.setId(teamId);
                    team.setName(resultSet.getString("team_name"));
                    team.setLeagueId(resultSet.getLong("team_league_id"));
                    league.getTeams().add(team);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return leagues;
    }


    @Override
    public League getLeague(Long id) {
        return null;
    }
}
