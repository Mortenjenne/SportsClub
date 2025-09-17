package entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TeamAvgIncomeDTO {
    private String teamId;
    private double averagePayment;
}
