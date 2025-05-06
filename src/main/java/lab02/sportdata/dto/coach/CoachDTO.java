package lab02.sportdata.dto.coach;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class CoachDTO {
    private Long id;
    private String name;
    private Long teamId;
}

