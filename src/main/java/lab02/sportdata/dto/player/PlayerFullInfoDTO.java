package lab02.sportdata.dto.player;

import lab02.sportdata.entities.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerFullInfoDTO {
    private Long id;
    private String name;
    private Player.Position position;
    public String teamName;
}
