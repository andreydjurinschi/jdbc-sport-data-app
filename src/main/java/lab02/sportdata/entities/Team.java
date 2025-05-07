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
