package persistence;

import entities.Registration;
import dto.RegistrationWithNameAndSportDTO;
import exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationMapper {

        private final Database database;

        public RegistrationMapper(Database database) {
            this.database = database;
        }

        public List<RegistrationWithNameAndSportDTO> getAllRegistrationsWithNameAndSportsType() throws DatabaseException {
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
                } catch (SQLException e) {
                    throw new DatabaseException("Fejl ved hentning af alle medlemmer: " + e.getMessage(), e);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Kunne ikke oprette forbindelse til databasen: " + e.getMessage(), e);
            }
            return result;
        }

        public List<Registration> getAllRegistrations() throws DatabaseException {
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
                } catch (SQLException e) {
                    throw new DatabaseException("Fejl ved hentning af alle medlemmer: " + e.getMessage(), e);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Kunne ikke oprette forbindelse til databasen: " + e.getMessage(), e);
            }
            return result;
        }

    public Registration addToTeam(int memberId, String teamId, int price) throws DatabaseException {
        String sql = "INSERT INTO registration (member_id, team_id, price) VALUES (?, ?, ?)";

        try (Connection connection = database.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ps.setString(2, teamId);
            ps.setInt(3, price);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                return new Registration(memberId, teamId, price);
            } else {
                throw new DatabaseException("Indsættelse af registrering fejlede - ingen rækker påvirket");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved tilføjelse af medlem til hold: " + e.getMessage(), e);
        }
    }

    public boolean isAlreadyRegistered(int memberId, String teamId) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM registration WHERE member_id = ? AND team_id = ?";

        try (Connection connection = database.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ps.setString(2, teamId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved tjek af eksisterende registrering: " + e.getMessage(), e);
        }
    }


    public Registration addToTeamSafe(int memberId, String teamId, int price) throws DatabaseException {

        if (isAlreadyRegistered(memberId, teamId)) {
            throw new DatabaseException("Medlem med ID " + memberId + " er allerede registreret på hold " + teamId);
        }

        return addToTeam(memberId, teamId, price);
    }
    }

