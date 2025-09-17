package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SportParticipantsDTO {
    private String sportType;
    private int numberOfParticipants;
}
