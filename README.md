## Djurinschi Andrei I2302 
### Spring Boot JDBC

### Цель:

Использовать Spring JDBC вместо Hibernate и JPA.
Обеспечить поддержку CRUD-операций через REST API, работая исключительно с DTO, а не с entity.
Реализовать DTO-классы, включающие всю связанную информацию: при выполнении GET-запроса, например, к команде, в ответе должны быть все игроки, тренер, лига и матчи, связанные с этой командой.
При создании основной сущности (POST) передавать так же ID уже существующих связанных сущностей. Новые связанные сущности не должны создаваться вложенно, а привязываться по ID. Например, при создании команды передавать ID тренера, ID лиги и список ID игроков.
Использовать DTO, содержащие более одного поля — недопустимо создание DTO с одним единственным полем.
Реализовать базовую валидацию
Строковые поля — не пустые.
Числовые значения — в допустимых пределах.
При создании или обновлении сущности — проверять существование всех указанных связанных ID (например, нельзя привязать команду к несуществующему тренеру).

### Выполнение

JDBC (Java Database Connectivity) — это стандартный API Java для взаимодействия с реляционными базами данных. Он предоставляет низкоуровневые средства для подключения к БД, выполнения SQL-запросов и обработки результатов.
Основные особенности JDBC:
Прямое выполнение SQL
Вы пишете SQL-запросы вручную: SELECT, INSERT, UPDATE, DELETE.
Явное управление ресурсами
Нужно самостоятельно открывать и закрывать соединения, Statement, ResultSet и т.д.
Гибкость
Вы полностью контролируете SQL — хорошо для оптимизации, плохо для удобства.
Не автоматизирует маппинг
Данные из таблицы нужно вручную преобразовывать в Java-объекты.

Классы для обработки исключений

Исключение выбрасывается при неудачном закрытии соединения с БД
```java
package lab02.sportdata.exception;

public class CloseConnectionException extends Exception{
    public CloseConnectionException(String message) {
        super(message);
    }
}
```

Исключения выбрасывается при неудачном создании сущности

```java
package lab02.sportdata.exception;

public class CreateEntityException extends Exception {
    public CreateEntityException(String message) {
        super(message);
    }
}
```

Исключение выбрасывается при поиске несуществующего объекта

```java
package lab02.sportdata.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String message)
    {
        super(message);
    }
}
```

Класс конфигуратор, содержащий в себе всю информацию о соединении с БД

```java
package lab02.sportdata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5433/sportDataDB");
        dataSource.setUsername("user");
        dataSource.setPassword("pass");
        return dataSource;
    }
}
```

DAO интерфейс (работаем с сущностью TEAM)

```java
package lab02.sportdata.dao.teamDAO;

import lab02.sportdata.entities.Team;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;

import java.util.List;

public interface TeamDAO {
    List<Team> getTeamsByLeague(Long id) throws CloseConnectionException, NotFoundException;
    Team getTeamById(Long id) throws CloseConnectionException, NotFoundException;
    void createTeam(Team team) throws CloseConnectionException, CreateEntityException;
    void addPlayerToTeam(Long teamId, Long playerId) throws CloseConnectionException, NotFoundException;
    void deleteTeam(Long id) throws CloseConnectionException, NotFoundException;
}
```

Имплеминтирующий класс:

```java
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

```

Сущность Team

```java
package lab02.sportdata.entities;

import lab02.sportdata.dto.team.TeamBaseInfoDTO;
import lab02.sportdata.dto.team.TeamFullInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Team {
    private Long Id;
    private String Name;
    private League league;
    private List<Player> players = new ArrayList<>();

    public TeamBaseInfoDTO mapToDto() {
        return new TeamBaseInfoDTO(Id, Name);
    }

    public TeamFullInfoDTO mapToFullInfoDTO() {
        List<String> playerNames = new ArrayList<>();

        for(Player player : players) {
            playerNames.add(player.getName());
        }
        return new TeamFullInfoDTO(Name, league.getName(), playerNames);
    }
}
```

Сервис класс для команды (так же представлены DTO классы сущности TEAM)

```java
package lab02.sportdata.services;

import lab02.sportdata.dao.leagueDAO.LeagueDAOImpl;
import lab02.sportdata.dao.playerDAO.PlayerDAOImpl;
import lab02.sportdata.dao.teamDAO.TeamDAOImpl;
import lab02.sportdata.dto.team.TeamBaseInfoDTO;
import lab02.sportdata.dto.team.TeamCreateDTO;
import lab02.sportdata.dto.team.TeamFullInfoDTO;
import lab02.sportdata.entities.League;
import lab02.sportdata.entities.Player;
import lab02.sportdata.entities.Team;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    private final TeamDAOImpl teamDAO;
    private final LeagueDAOImpl leagueDAO;
    private final PlayerDAOImpl playerDAO;

    public TeamService(TeamDAOImpl teamDAO, LeagueDAOImpl leagueDAO, PlayerDAOImpl playerDAO) {
        this.teamDAO = teamDAO;
        this.leagueDAO = leagueDAO;
        this.playerDAO = playerDAO;
    }

    public List<TeamBaseInfoDTO> getTeamsByLeague(Long leagueId) throws CloseConnectionException, NotFoundException {
        List<TeamBaseInfoDTO> teams = new ArrayList<>();
        League league = leagueDAO.getLeague(leagueId);
        if(league == null) {throw new NotFoundException("League not found");}
        List<Team> teamEntity = teamDAO.getTeamsByLeague(league.getId());
        for(Team team : teamEntity) {
            teams.add(team.mapToDto());
        }
        return teams;
    }

    public void save(TeamCreateDTO teamCreateDTO) throws CreateEntityException, CloseConnectionException, NotFoundException {
        if (teamCreateDTO.getLeagueId() == null) {
            throw new CreateEntityException("League must be set");
        }
        if (teamCreateDTO.getTeamName().isEmpty()) {
            throw new CreateEntityException("Team name must be set");
        }
        if (teamCreateDTO.getTeamName().length() < 2 || teamCreateDTO.getTeamName().length() > 25) {
            throw new CreateEntityException("Team name must be between 2 and 25 characters");
        }

        League league = leagueDAO.getLeague(teamCreateDTO.getLeagueId());
        if (league == null) {
            throw new NotFoundException("League not found");
        }

        teamDAO.createTeam(teamCreateDTO.mapToEntity(league));
    }

    public TeamFullInfoDTO getTeamById(Long id) throws NotFoundException, CloseConnectionException {
        Team team = teamDAO.getTeamById(id);
        if(team == null) {throw new NotFoundException("Team not found");}
        return team.mapToFullInfoDTO();
    }

    public void addPlayerToTeam(Long teamId, Long playerId) throws CloseConnectionException, NotFoundException {
        Team team = teamDAO.getTeamById(teamId);
        Player player = playerDAO.getById(playerId);
        if(team == null) {throw new NotFoundException("Team not found");}
        if(player == null) {throw new NotFoundException("Player not found");}
        teamDAO.addPlayerToTeam(teamId, playerId);
    }

    public void deleteTeamById(Long id) throws NotFoundException, CloseConnectionException {
        Team team = teamDAO.getTeamById(id);
        if(team == null) {throw new NotFoundException("Team not found");}
        teamDAO.deleteTeam(id);
    }
}

```

Сообщения от исключений я использую в качестве вывода результата в контроллере:

```java
package lab02.sportdata.controllers;

import lab02.sportdata.dto.team.TeamBaseInfoDTO;
import lab02.sportdata.dto.team.TeamCreateDTO;
import lab02.sportdata.dto.team.TeamFullInfoDTO;
import lab02.sportdata.exception.CloseConnectionException;
import lab02.sportdata.exception.CreateEntityException;
import lab02.sportdata.exception.NotFoundException;
import lab02.sportdata.services.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    /*private final LeagueService leagueService;*/

    public TeamController(TeamService teamService/*, LeagueService leagueService*/) {
        this.teamService = teamService;
/*        this.leagueService = leagueService;*/
    }

    @GetMapping("/league/{id}")
    public ResponseEntity<?> getTeams(@PathVariable Long id) {
        List<TeamBaseInfoDTO> teams;
        try{
            teams = teamService.getTeamsByLeague(id);
        } catch (CloseConnectionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(teams);
    }

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody TeamCreateDTO teamCreateDTO) {
        try{
            teamService.save(teamCreateDTO);
        }catch (CreateEntityException |NotFoundException |CloseConnectionException e ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Team created successfully: " + teamCreateDTO.getTeamName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeamById(@PathVariable Long id) {
        TeamFullInfoDTO team;
        try{
            team = teamService.getTeamById(id);
        } catch (CloseConnectionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(team);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id){
        try{
            teamService.deleteTeamById(id);
        } catch (CloseConnectionException | NotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Team deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> addPlayerToTeam(@PathVariable Long id, Long playerId) throws CloseConnectionException {
        try{
            teamService.addPlayerToTeam(id , playerId);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Player added successfully\n" + getTeamById(id));
    }
}
```

### Выводы

JDBC — это низкоуровневый инструмент, подходящий для простых задач или когда нужен полный контроль над SQL. В отличие от Hibernate, он не упрощает работу с объектами и связями между таблицами, но даёт точную и предсказуемую работу с БД.







