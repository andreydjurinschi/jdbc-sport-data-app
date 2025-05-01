package lab02.sportdata.entities;

import lab02.sportdata.dto.team.TeamBaseInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    private Long Id;
    private String Name;
    private League league;

    public Team(Long id, String name) {
        this.Id = id;
        this.Name = name;
    }

    public TeamBaseInfoDTO mapToDto() {
        return new TeamBaseInfoDTO(Id, Name);
    }
}
