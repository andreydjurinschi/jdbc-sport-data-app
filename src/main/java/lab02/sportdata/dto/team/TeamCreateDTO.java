package lab02.sportdata.dto.team;


import lab02.sportdata.entities.League;
import lab02.sportdata.entities.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreateDTO {

    private String teamName;
    private Long LeagueId;

    public Team mapToEntity(League league)
    {
        Team team = new Team();
        team.setLeague(league);
        team.setName(teamName);
        return team;
    }
}
