package lab02.sportdata.entities;

import lab02.sportdata.dto.team.TeamBaseInfoDTO;
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
public class Team {
    private Long Id;
    private String Name;
    private League league;
    private List<Player> players = new ArrayList<>();

    public TeamBaseInfoDTO mapToDto() {
        return new TeamBaseInfoDTO(Id, Name);
    }
}
