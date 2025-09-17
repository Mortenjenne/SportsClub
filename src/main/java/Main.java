import dto.*;
import entities.*;
import exception.DatabaseException;
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

        try {
            List<Member> members = memberMapper.getAllMembers();

            //showMembers(members);
            //showMemberById(memberMapper, 13);
            //printNumberOfParcipantsOnEachTeam(statisticsMapper);
            //printNumberOfParcipantsOnEachSportsType(statisticsMapper);
            //printNumberOfWomenAndMenInSportsClub(statisticsMapper);
            //printTotalIncomeFromWholeClub(statisticsMapper);
            //printTotalIncomeForEachTeam(statisticsMapper);
            //printAveragePaymentForEachTeam(statisticsMapper);
            //insertMemberToTeam(registrationMapper);

            //showRegistrations(registrationMapper);
            //showRegistrationsWithNameAndSportsType(registrationMapper);
            getMemberAndSportsById(14,memberMapper);
        } catch (DatabaseException e){

        }



        /*  
            int newMemberId = insertMember(memberMapper);
            deleteMember(newMemberId, memberMapper);
            showMembers(members);
            updateMember(13, memberMapper);
        */
    }

    public static void getMemberAndSportsById(int id,MemberMapper memberMapper) throws DatabaseException {
        MemberAndSportsDTO member = memberMapper.getMemberAndSportsById(id);
        System.out.println(member);
    }

    public static void showRegistrations(RegistrationMapper registrationMapper) throws DatabaseException {
        System.out.println("***** Vis alle medlemskaber *******");
        List<Registration> registrations = registrationMapper.getAllRegistrations();
        registrations.forEach(r -> System.out.println(r));
    }

    public static void showRegistrationsWithNameAndSportsType(RegistrationMapper registrationMapper) throws DatabaseException {
        System.out.println("***** Vis alle medlemskaber *******");
        List<RegistrationWithNameAndSportDTO> registrations = registrationMapper.getAllRegistrationsWithNameAndSportsType();
        registrations.forEach(r -> System.out.println(r));
    }

    private static void insertMemberToTeam(RegistrationMapper registrationMapper) throws DatabaseException {
        Registration registration = registrationMapper.addToTeam(14,"ten01",183);
        System.out.println(registration);
    }

    private static void printAveragePaymentForEachTeam(StatisticsMapper statisticsMapper) throws DatabaseException {
        List<TeamAvgIncomeDTO> avgIncomeDTOS = statisticsMapper.averagePaymentPerTeam();
        avgIncomeDTOS.forEach(System.out::println);
    }

    private static void printTotalIncomeForEachTeam(StatisticsMapper statisticsMapper) throws DatabaseException {
        List<TeamIncomeDTO> incomePrTeam = statisticsMapper.getTotalIncomePrTeam();
        incomePrTeam.forEach(System.out::println);
    }

    private static void printTotalIncomeFromWholeClub(StatisticsMapper statisticsMapper) throws DatabaseException {
        int total = statisticsMapper.getTotalIncome();
        System.out.println("Sum of all teams in the club: " + total);
    }

    private static void printNumberOfWomenAndMenInSportsClub(StatisticsMapper statisticsMapper) throws DatabaseException {
        List<GenderCountDTO> genders = statisticsMapper.getNumberOfWomenAndMenInSportsclub();
        if(genders != null || !genders.isEmpty()){
            genders.forEach(System.out::println);
        }
    }

    private static void printNumberOfParcipantsOnEachSportsType(StatisticsMapper statisticsMapper) throws DatabaseException {
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

    private static void deleteMember(int memberId, MemberMapper memberMapper) throws DatabaseException {
        if (memberMapper.deleteMember(memberId)){
            System.out.println("Member with id = " + memberId + " is removed from DB");
        }
    }

    private static int insertMember(MemberMapper memberMapper) throws DatabaseException {
        Member m1 = new Member("Ole Olsen", "Banegade 2", 3700, "Rønne", "m", 1967);
        Member m2 = memberMapper.insertMember(m1);
        showMemberById(memberMapper, m2.getMemberId());
        return m2.getMemberId();
    }

    private static void updateMember(int memberId, MemberMapper memberMapper) throws DatabaseException {
        Member m1 = memberMapper.getMemberById(memberId);
        m1.setYear(1970);
        if(memberMapper.updateMember(m1)){
            showMemberById(memberMapper, memberId);
        }
    }

    private static void showMemberById(MemberMapper memberMapper, int memberId) throws DatabaseException {
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
