package properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBC {

    public static Connection connect(){
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")){
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String drivers = prop.getProperty("jdbc.drivers");
        if (drivers != null){
            try {
                Class.forName(drivers);
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        String url = prop.getProperty("jdbc.url");
        String username = prop.getProperty("jdbc.username");
        String password = prop.getProperty("jdbc.password");

        Connection c = null;
        try {
            c = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return c;
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
