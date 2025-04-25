package lab02.sportdata.dto.league;

import lab02.sportdata.entities.Team;
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
public class LeagueFullInfoDto {
    private String name;
    private List<Team> teamList = new ArrayList<>();
}
