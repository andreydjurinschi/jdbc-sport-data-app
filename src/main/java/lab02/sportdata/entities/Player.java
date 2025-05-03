package lab02.sportdata.entities;

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
}

