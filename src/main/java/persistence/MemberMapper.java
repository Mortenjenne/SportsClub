package persistence;

import dto.MemberAndSportsDTO;
import entities.Member;
import exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberMapper {

        private Database database;

        public MemberMapper(Database database) {
            this.database = database;
        }

        public List<Member> getAllMembers() throws DatabaseException {

            List<Member> memberList = new ArrayList<>();

            String sql = "select member_id, name, address, m.zip, gender, city, year " +
                         "from member m inner join zip using(zip) " +
                         "order by member_id";

            try (Connection connection = database.connect()) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        int memberId = rs.getInt("member_id");
                        String name = rs.getString("name");
                        String address = rs.getString("address");
                        int zip = rs.getInt("zip");
                        String city = rs.getString("city");
                        String gender = rs.getString("gender");
                        int year = rs.getInt("year");
                        memberList.add(new Member(memberId, name, address, zip, city, gender, year));
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Fejl ved hentning af alle medlemmer: " + e.getMessage(), e);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Kunne ikke oprette forbindelse til databasen: " + e.getMessage(), e);
            }
            return memberList;
        }

        public Member getMemberById(int memberId) throws DatabaseException {
            Member member = null;

            String sql =  "select member_id, name, address, m.zip, gender, city, year " +
            "from member m inner join zip using(zip) " +
            "where member_id = ?";

            try (Connection connection = database.connect()) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, memberId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        memberId = rs.getInt("member_id");
                        String name = rs.getString("name");
                        String address = rs.getString("address");
                        int zip = rs.getInt("zip");
                        String city = rs.getString("city");
                        String gender = rs.getString("gender");
                        int year = rs.getInt("year");
                        member = new Member(memberId, name, address, zip, city, gender, year);
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Fejl ved hentning af alle medlemmer: " + e.getMessage(), e);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Kunne ikke oprette forbindelse til databasen: " + e.getMessage(), e);
            }
            int a = 1;
            return member;
        }

        public boolean deleteMember(int member_id) throws DatabaseException {
            boolean result = false;
            String sql = "delete from member where member_id = ?";
            try (Connection connection = database.connect()) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, member_id);
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected == 1){
                        result = true;
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Fejl ved hentning af alle medlemmer: " + e.getMessage(), e);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Kunne ikke oprette forbindelse til databasen: " + e.getMessage(), e);
            }
            return result;
        }

        public Member insertMember(Member member) throws DatabaseException {
            boolean result = false;
            int newId = 0;
            String sql = "insert into member (name, address, zip, gender, year) values (?,?,?,?,?)";
            try (Connection connection = database.connect()) {
                try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )) {
                    ps.setString(1, member.getName());
                    ps.setString(2, member.getAddress());
                    ps.setInt(3, member.getZip());
                    ps.setString(4, member.getGender());
                    ps.setInt(5, member.getYear());
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected == 1){
                        result = true;
                    }
                    ResultSet idResultset = ps.getGeneratedKeys();
                    if (idResultset.next()){
                        newId = idResultset.getInt(1);
                        member.setMemberId(newId);
                    } else {
                        member = null;
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Fejl ved hentning af alle medlemmer: " + e.getMessage(), e);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Kunne ikke oprette forbindelse til databasen: " + e.getMessage(), e);
            }
            return member;
        }

        public boolean updateMember(Member member) throws DatabaseException {
            boolean result = false;
            String sql =    "update member " +
                            "set name = ?, address = ?, zip = ?, gender = ?, year = ? " +
                            "where member_id = ?";
            try (Connection connection = database.connect()) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, member.getName());
                    ps.setString(2, member.getAddress());
                    ps.setInt(3, member.getZip());
                    ps.setString(4, member.getGender());
                    ps.setInt(5, member.getYear());
                    ps.setInt(6, member.getMemberId());
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected == 1){
                        result = true;
                    }
                } catch (SQLException e) {
                    throw new DatabaseException("Fejl ved hentning af alle medlemmer: " + e.getMessage(), e);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Kunne ikke oprette forbindelse til databasen: " + e.getMessage(), e);
            }
            return result;
        }

        public MemberAndSportsDTO getMemberAndSportsById(int id) throws DatabaseException {
            String sql = "SELECT m.member_id, m.name, m.address, m.zip, m.gender, m.year, s.sport\n" +
                    "FROM registration r\n" +
                    "JOIN member m\n" +
                    "ON r.member_id = m.member_id \n" +
                    "JOIN team t\n" +
                    "ON t.team_id = r.team_id\n" +
                    "JOIN sport s \n" +
                    "ON s.sport_id = t.sport_id\n" +
                    "WHERE m.member_id = ?";

            List<String> sports = new ArrayList<>();
            Member member = null;

            try(Connection connection = database.connect();
            PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1,id);
                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()){

                    if(member == null) {
                        int memberId = resultSet.getInt("member_id");
                        String memberName = resultSet.getString("name");
                        String address = resultSet.getString("address");
                        int zip = resultSet.getInt("zip");
                        String gender = resultSet.getString("gender");
                        int year = resultSet.getInt("year");
                        member = new Member(memberId,memberName,address,zip,gender,year);
                    }
                    String sport = resultSet.getString("sport");
                    sports.add(sport);
                }

            } catch (SQLException e){
                throw new DatabaseException("Couldnt find or connect");
            }
            if(member == null){
                return null;
            }
            return new MemberAndSportsDTO(member,sports);
        }


}
