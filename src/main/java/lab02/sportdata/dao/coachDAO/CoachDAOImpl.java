package lab02.sportdata.dao.coachDAO;

import lab02.sportdata.entities.Coach;
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
public class CoachDAOImpl implements CoachDAO {

    private final DataSource dataSource;

    public CoachDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Coach> getAll() throws CloseConnectionException {
        List<Coach> coaches = new ArrayList<>();
        String sql = "SELECT * FROM coach";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                coaches.add(new Coach(rs.getLong("id"), rs.getString("name"), rs.getLong("team_id")));
            }
        } catch (SQLException e) {
            throw new CloseConnectionException(e.getMessage());
        }
        return coaches;
    }

    @Override
    public Coach getById(Long id) throws NotFoundException, CloseConnectionException {
        String sql = "SELECT * FROM coach WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Coach(rs.getLong("id"), rs.getString("name"), rs.getLong("team_id"));
            } else {
                throw new NotFoundException("Coach not found");
            }
        } catch (SQLException e) {
            throw new CloseConnectionException(e.getMessage());
        }
    }

    @Override
    public void create(Coach coach) throws CreateEntityException, CloseConnectionException {
        String sql = "INSERT INTO coach (name, team_id) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, coach.getName());
            ps.setLong(2, coach.getTeamId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new CreateEntityException("Failed to create coach: " + e.getMessage());
        }
    }

    @Override
    public void update(Long id, Coach coach) throws NotFoundException, CloseConnectionException {
        String sql = "UPDATE coach SET name = ?, team_id = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, coach.getName());
            ps.setLong(2, coach.getTeamId());
            ps.setLong(3, id);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new NotFoundException("Coach not found");
            }
        } catch (SQLException e) {
            throw new CloseConnectionException(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws NotFoundException, CloseConnectionException {
        String sql = "DELETE FROM coach WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new NotFoundException("Coach not found");
            }
        } catch (SQLException e) {
            throw new CloseConnectionException(e.getMessage());
        }
    }
}

