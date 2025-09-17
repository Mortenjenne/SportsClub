package persistence;

import entities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticsMapper {
    private Database database;

    public StatisticsMapper(Database database){
        this.database = database;
    }

    //Find the number of participants on each team
    public List<TeamParticipantsDTO> getNumberOfParticipantsOnEachTeam(){

        List<TeamParticipantsDTO> result = new ArrayList<>();

        String sql = "SELECT \n" +
                "    t.team_id AS team,\n" +
                "    s.sport,\n" +
                "    COUNT(r.member_id) AS participants\n" +
                "FROM team t\n" +
                "JOIN sport s\n" +
                "  ON s.sport_id = t.sport_id\n" +
                "JOIN registration r\n" +
                "  ON t.team_id = r.team_id\n" +
                "GROUP BY t.team_id, s.sport;\n";

        try(Connection connection = database.connect()){
            try(PreparedStatement ps = connection.prepareStatement(sql)) {

                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()){
                    String teamId = resultSet.getString("team");
                    String sportType = resultSet.getString("sport");
                    int numberOfParcipants = resultSet.getInt("participants");
                    result.add(new TeamParticipantsDTO(teamId,sportType,numberOfParcipants));
                }

            } catch (SQLException e){
                System.out.println("Couldnt find anything");
            }
        }catch (SQLException e){
            System.out.println("Couldnt connect");
        }
        return result;

    }

    //Find the number of participants for each sport
    public List<SportParticipantsDTO> numberOfParticipantsOnEachSportsType (){
        String sql = "SELECT s.sport, COUNT(s.sport_id) AS participants\n" +
                "FROM team t\n" +
                "INNER JOIN registration r\n" +
                "ON t.team_id = r.team_id\n" +
                "INNER JOIN sport s\n" +
                "ON t.sport_id = s.sport_id\n" +
                "GROUP BY s.sport";

        List<SportParticipantsDTO> result = new ArrayList<>();

        try(Connection connection = database.connect()){
            try(PreparedStatement ps = connection.prepareStatement(sql)) {

                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()){
                    String sportType = resultSet.getString("sport");
                    int numberOfParcipants = resultSet.getInt("participants");
                    result.add(new SportParticipantsDTO(sportType,numberOfParcipants));
                }

            } catch (SQLException e){
                System.out.println("Couldnt find anything");
            }
        }catch (SQLException e){
            System.out.println("Couldnt connect");
        }
        return result;

    }

    //Find the number of men and women in the club
    public List<GenderCountDTO> getNumberOfWomenAndMenInSportsclub(){
        String sql = "SELECT m.gender, COUNT(*) AS count\n" +
                "FROM member m\n" +
                "JOIN registration r\n" +
                "  ON m.member_id = r.member_id\n" +
                "GROUP BY m.gender";

        List<GenderCountDTO> result = new ArrayList<>();
        try(Connection connection = database.connect()){
            try(PreparedStatement ps = connection.prepareStatement(sql)) {

                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()){
                    String gender = resultSet.getString("gender");
                    int count = resultSet.getInt("count");
                    result.add(new GenderCountDTO(gender,count));
                }

            } catch (SQLException e){
                System.out.println("Couldnt find anything");
            }
        }catch (SQLException e){
            System.out.println("Couldnt connect");
        }
        return result;
    }

    public int getTotalIncome(){
        String sql = "SELECT SUM(r.price) AS total_income\n" +
                "FROM registration r";

        int totalIncome = 0;

        try(Connection connection = database.connect()){
            try(PreparedStatement ps = connection.prepareStatement(sql)) {

                ResultSet resultSet = ps.executeQuery();

                if(resultSet.next()){
                    totalIncome = resultSet.getInt("total_income");
                }


            } catch (SQLException e){
                System.out.println("Couldnt find anything");
            }
        }catch (SQLException e){
            System.out.println("Couldnt connect");
        }
        return totalIncome;
    }

    //Find the total sum of income for each team
    public List<TeamIncomeDTO> getTotalIncomePrTeam(){
        String sql = "SELECT r.team_id, SUM(r.price) AS total_income\n" +
                "FROM registration r\n" +
                "GROUP BY r.team_id";

        List<TeamIncomeDTO> result = new ArrayList<>();

        try(Connection connection = database.connect()){
            try(PreparedStatement ps = connection.prepareStatement(sql)) {

                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()){
                    String teamId = resultSet.getString("team_id");
                    int sum = resultSet.getInt("total_income");
                    result.add(new TeamIncomeDTO(teamId,sum));
                }

            } catch (SQLException e){
                System.out.println("Couldnt find anything");
            }
        }catch (SQLException e){
            System.out.println("Couldnt connect");
        }

        return result;
    }

    //Find the average payment for each team
    public List<TeamAvgIncomeDTO> averagePaymentPerTeam(){
        String sql = "    SELECT r.team_id, AVG(r.price) AS avg_pr_team\n" +
                "    FROM registration r\n" +
                "    GROUP BY r.team_id";

        List<TeamAvgIncomeDTO> result = new ArrayList<>();

        try(Connection connection = database.connect()){
            try(PreparedStatement ps = connection.prepareStatement(sql)) {

                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()){
                    String teamId = resultSet.getString("team_id");
                    double average = resultSet.getInt("avg_pr_team");
                    result.add(new TeamAvgIncomeDTO(teamId,average));
                }

            } catch (SQLException e){
                System.out.println("Couldnt find anything");
            }
        }catch (SQLException e){
            System.out.println("Couldnt connect");
        }

        return result;
    }


}
