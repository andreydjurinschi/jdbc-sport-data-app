package lab02.sportdata.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    private Long Id;
    private String name;
    private Position position;
    private int number;
    private Team team;
}

enum Position{
    GOALKEEPER,
    DEFENDER,
    MIDFIELDER,
    FORWARD
}
