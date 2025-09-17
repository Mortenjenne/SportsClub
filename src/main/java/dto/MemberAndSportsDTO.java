package dto;

import entities.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class MemberAndSportsDTO {
    private Member member;
    private List<String> sports;
}
