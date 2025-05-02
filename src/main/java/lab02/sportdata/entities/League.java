package lab02.sportdata.entities;

import lab02.sportdata.dto.league.LeagueDTO;
import lab02.sportdata.dto.league.LeagueFullInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class League {
    private Long id;
    private String name;
    private List<Team> teams = new ArrayList<>();

    public LeagueDTO mapToDto(String name, int teamCount) {
        return new LeagueDTO(name, teamCount);
    }

    public LeagueFullInfoDto mapToFullInfoDto() {
        List<String> teams = new ArrayList<>();
        if (this.teams != null) {
            for (Team team : this.teams) {
                teams.add(team.getName());
            }
        }
        return new LeagueFullInfoDto(this.name, teams);
    }

    public League(String name){

    }

}
