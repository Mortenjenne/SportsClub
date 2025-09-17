package entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GenderCountDTO {
    private String gender;
    private int count;
}
