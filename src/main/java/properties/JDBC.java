package properties;

import java.sql.*;

public class JDBC {
    private static final String url = "jdbc:postgresql://localhost:5432/music_study";
    private static final String username = "postgres";
    private static final String password = "admin";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void getShortedComposition(Connection conn, int minDuration) {

        String SQL = "SELECT c.name as composition_name, c.duration as composition_duration,\n" +
                "       a.name as album_name FROM composition AS c\n" +
                "INNER JOIN album a on a.id = c.album_id\n" +
                "WHERE duration > (?)\n" +
                "ORDER BY duration ASC\n" +
                "LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, minDuration);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(String.format("Composition = {name: %s, duration: %d, album: %s}",
                        rs.getString("composition_name"),
                        rs.getInt("composition_duration"),
                        rs.getString("album_name")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeCompositionByName(Connection conn, String compositionName){
        String SQL = "DELETE FROM composition \n" +
                "WHERE name = (?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, compositionName);
            ResultSet rs = pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertComposition(Connection conn, String compositionName, int compositionDuration, int albumId){
        String SQL = "INSERT INTO composition(name, duration, album_id) VALUES \n" +
                "(?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, compositionName);
            pstmt.setInt(2, compositionDuration);
            pstmt.setInt(3, albumId);
            ResultSet rs = pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void changeCompositionName(Connection conn, String oldCompositionName, String newCompositionName){
        String SQL = "UPDATE composition SET name = ? WHERE name = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, newCompositionName);
            pstmt.setString(2, oldCompositionName);
            ResultSet rs = pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
