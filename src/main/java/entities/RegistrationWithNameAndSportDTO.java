package entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegistrationWithNameAndSportDTO {
    private String name;
    private String sportType;
    private Registration registration;
}
