package lab02.sportdata.dto.league;

import lab02.sportdata.entities.League;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeagueCreateDTO {

    private String name;
    public League mapToEntity(String name){
        return new League(name);
    }
}
