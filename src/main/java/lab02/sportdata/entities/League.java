package lab02.sportdata.entities;

import lab02.sportdata.dto.league.LeagueDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class League {
    private Long id;
    private String name;

    public LeagueDTO mapToDto(String name, int teamCount) {
        return new LeagueDTO(name, teamCount);
    }

    public League(String name){

    }
}
