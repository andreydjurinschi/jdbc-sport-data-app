package lab02.sportdata.entities;

import lab02.sportdata.dto.player.PlayerBaseInfoDTO;
import lab02.sportdata.dto.player.PlayerFullInfoDTO;
import lab02.sportdata.dto.player.PlayerUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Player {
    private Long Id;
    private String name;
    private Position position;
    private int number;
    private Team team;

    public enum Position{
        GOALKEEPER,
        DEFENDER,
        MIDFIELDER,
        FORWARD
    }

    public PlayerBaseInfoDTO mapToDTO() {
        return new PlayerBaseInfoDTO(Id, name, position);
    }

    public PlayerFullInfoDTO mapToFullInfoDTO() {
        return new PlayerFullInfoDTO(Id, name, position, team.getName() );
    }

}

