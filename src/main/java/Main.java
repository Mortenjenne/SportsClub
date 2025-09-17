import entities.*;
import persistence.Database;
import persistence.MemberMapper;
import persistence.RegistrationMapper;
import persistence.StatisticsMapper;

import java.util.List;

public class Main {

    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";
    private final static String URL = "jdbc:postgresql://localhost:5432/sportsclub";

    public static void main(String[] args) {

        Database db = new Database(USER, PASSWORD, URL);
        MemberMapper memberMapper = new MemberMapper(db);
        StatisticsMapper statisticsMapper = new StatisticsMapper(db);
        RegistrationMapper registrationMapper = new RegistrationMapper(db);
        List<Member> members = memberMapper.getAllMembers();

        //showMembers(members);
        //showMemberById(memberMapper, 13);
        //printNumberOfParcipantsOnEachTeam(statisticsMapper);
        //printNumberOfParcipantsOnEachSportsType(statisticsMapper);
        //printNumberOfWomenAndMenInSportsClub(statisticsMapper);
        //printTotalIncomeFromWholeClub(statisticsMapper);
        //printTotalIncomeForEachTeam(statisticsMapper);
        //printAveragePaymentForEachTeam(statisticsMapper);
        insertMemberToTeam(registrationMapper);


        /*  
            int newMemberId = insertMember(memberMapper);
            deleteMember(newMemberId, memberMapper);
            showMembers(members);
            updateMember(13, memberMapper);
        */
    }

    private static void insertMemberToTeam(RegistrationMapper registrationMapper){
        Registration registration = registrationMapper.addToTeam(14,"ten01",183);
        System.out.println(registration);
    }

    private static void printAveragePaymentForEachTeam(StatisticsMapper statisticsMapper){
        List<TeamAvgIncomeDTO> avgIncomeDTOS = statisticsMapper.averagePaymentPerTeam();
        avgIncomeDTOS.forEach(System.out::println);
    }

    private static void printTotalIncomeForEachTeam(StatisticsMapper statisticsMapper){
        List<TeamIncomeDTO> incomePrTeam = statisticsMapper.getTotalIncomePrTeam();
        incomePrTeam.forEach(System.out::println);
    }

    private static void printTotalIncomeFromWholeClub(StatisticsMapper statisticsMapper){
        int total = statisticsMapper.getTotalIncome();
        System.out.println("Sum of all teams in the club: " + total);
    }

    private static void printNumberOfWomenAndMenInSportsClub(StatisticsMapper statisticsMapper){
        List<GenderCountDTO> genders = statisticsMapper.getNumberOfWomenAndMenInSportsclub();
        if(genders != null || !genders.isEmpty()){
            genders.forEach(System.out::println);
        }
    }

    private static void printNumberOfParcipantsOnEachSportsType(StatisticsMapper statisticsMapper){
        List<SportParticipantsDTO> teams = statisticsMapper.numberOfParticipantsOnEachSportsType();
        if(teams != null || !teams.isEmpty()){
            teams.forEach(System.out::println);
        }
    }


    private static void printNumberOfParcipantsOnEachTeam(StatisticsMapper statisticsMapper){
        List<TeamParticipantsDTO> teams = statisticsMapper.getNumberOfParticipantsOnEachTeam();
        if(teams != null || !teams.isEmpty()){
            teams.forEach(System.out::println);
        }
    }

    private static void deleteMember(int memberId, MemberMapper memberMapper) {
        if (memberMapper.deleteMember(memberId)){
            System.out.println("Member with id = " + memberId + " is removed from DB");
        }
    }

    private static int insertMember(MemberMapper memberMapper) {
        Member m1 = new Member("Ole Olsen", "Banegade 2", 3700, "Rønne", "m", 1967);
        Member m2 = memberMapper.insertMember(m1);
        showMemberById(memberMapper, m2.getMemberId());
        return m2.getMemberId();
    }

    private static void updateMember(int memberId, MemberMapper memberMapper) {
        Member m1 = memberMapper.getMemberById(memberId);
        m1.setYear(1970);
        if(memberMapper.updateMember(m1)){
            showMemberById(memberMapper, memberId);
        }
    }

    private static void showMemberById(MemberMapper memberMapper, int memberId) {
        System.out.println("***** Vis medlem nr. 13: *******");
        System.out.println(memberMapper.getMemberById(memberId).toString());
    }

    private static void showMembers(List<Member> members) {
        System.out.println("***** Vis alle medlemmer *******");
        for (Member member : members) {
            System.out.println(member.toString());
        }
    }


}
