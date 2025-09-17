package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TeamIncomeDTO {
    private String teamId;
    private int totalIncome;
}
