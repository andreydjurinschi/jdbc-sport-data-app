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
public class PlayerCreateDTO {
    private String name;
    private Player.Position position;
    private int number;
    private Long teamId;
}
