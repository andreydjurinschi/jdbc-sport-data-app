package lab02.sportdata.dto.team;
import lab02.sportdata.entities.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamBaseInfoDTO {
    private Long Id;
    private String name;
    public Team mapToEntity(TeamBaseInfoDTO teamBaseInfoDTO) {
        return new Team(teamBaseInfoDTO.getId(), teamBaseInfoDTO.getName());
    }
}
