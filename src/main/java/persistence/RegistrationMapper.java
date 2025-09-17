package persistence;

import entities.Member;
import entities.Registration;
import entities.RegistrationWithNameAndSportDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationMapper {

        private final Database database;

        public RegistrationMapper(Database database) {
            this.database = database;
        }

        public List<RegistrationWithNameAndSportDTO> getAllRegistrationsWithNameAndSportsType(){
            String sql = "SELECT m.name, s.sport, r.member_id, r.team_id, r.price\n" +
                    "FROM registration r\n" +
                    "JOIN team t\n" +
                    "ON t.team_id = r.team_id\n" +
                    "JOIN sport s\n" +
                    "ON t.sport_id = s.sport_id\n" +
                    "JOIN member m\n" +
                    "ON r.member_id = m.member_id ORDER BY m.name";

            List<RegistrationWithNameAndSportDTO> result = new ArrayList<>();

            try (Connection connection = database.connect()) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String name = rs.getString("name");
                        String sportType = rs.getString("sport");
                        int memberId = rs.getInt("member_id");
                        String teamId = rs.getString("team_id");
                        int price = rs.getInt("price");

                        Registration registration = new Registration(memberId,teamId,price);

                        result.add(new RegistrationWithNameAndSportDTO(name,sportType,registration));
                    }
                } catch (SQLException throwables) {
                    // TODO: Make own throwable exception and let it bubble upwards
                    throwables.printStackTrace();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return result;
        }

        public List<Registration> getAllRegistrations(){
            String sql = "SELECT r.member_id, r.team_id, r.price FROM registration r";

            List<Registration> result = new ArrayList<>();

            try (Connection connection = database.connect()) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int memberId = rs.getInt("member_id");
                        String teamId = rs.getString("team_id");
                        int price = rs.getInt("price");

                        result.add(new Registration(memberId,teamId,price));
                    }
                } catch (SQLException throwables) {
                    // TODO: Make own throwable exception and let it bubble upwards
                    throwables.printStackTrace();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return result;
        }

        public Registration addToTeam(int memberId, String teamId, int price) {
            String sql = "INSERT INTO registration (member_id, team_id, price) VALUES (?, ?, ?)";
            Registration registration = null;

            try (Connection connection = database.connect();
                 PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, memberId);
                ps.setString(2, teamId);
                ps.setInt(3, price);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1) {
                    ResultSet generatedKeys = ps.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int registrationId = generatedKeys.getInt(1);
                        registration = new Registration(memberId, teamId, price);
                    } else {
                        throw new RuntimeException("Failed to retrieve generated registration ID.");
                    }
                } else {
                    throw new RuntimeException("Inserting registration failed, no rows affected.");
                }

            } catch (SQLException e) {
                throw new RuntimeException("Error adding member to team", e);
            }

            return registration;
        }
    }

