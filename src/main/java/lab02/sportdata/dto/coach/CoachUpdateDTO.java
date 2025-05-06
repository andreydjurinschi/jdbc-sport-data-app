package lab02.sportdata.dto.coach;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CoachUpdateDTO {
    private String name;
    private Long teamId;
}

