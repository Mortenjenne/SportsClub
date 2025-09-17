package entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TeamParticipantsDTO {
    private String teamId;
    private String sportType;
    private int numberOfParticipants;
}
