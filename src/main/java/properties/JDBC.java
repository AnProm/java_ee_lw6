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

    public static void getCompositionsByMaxDurationWithArtist(Connection conn, int maxDuration){
        String SQL = "SELECT c.name AS composition_name, c.duration, art.name AS art_name FROM composition AS c \n" +
                "LEFT JOIN album AS a ON c.album_id = a.id \n" +
                "LEFT JOIN artist AS art ON a.artist_id = art.id \n" +
                "WHERE c.duration < (?)";

        try(PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setInt(1, maxDuration);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(String.format("Composition = {name: %s, duration: %d, art_name: %s}",
                        rs.getString("composition_name"),
                        rs.getInt("duration"),
                        rs.getString("art_name")));

            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void getCompositionsFromAlbumWhereCompositionMoreThen(Connection conn, int minCountCompInAlbum){
        String SQL = "SELECT a.name AS alb_name, COUNT(c.id) AS num_of_comp, art.name AS art_name FROM composition AS c \n" +
                "LEFT JOIN album AS a ON c.album_id = a.id \n" +
                "LEFT JOIN artist AS art ON a.artist_id = art.id \n" +
                "GROUP BY alb_name, art_name\n" +
                "HAVING COUNT(c.id) > (?)";

        try(PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setInt(1, minCountCompInAlbum);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(String.format("Album = {alb_name: %s, num_of_comp: %d, art_name: %s}",
                        rs.getString("alb_name"),
                        rs.getInt("num_of_comp"),
                        rs.getString("art_name")));

            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void getCompositionsListeningStory(Connection conn, String compName){
        String SQL = "WITH RECURSIVE temp1(name, id, prev_listening_id, path) AS (\n" +
                "\tSELECT c.name, c.id, c.prev_listening_id, cast (c.name as varchar (50)) as path\n" +
                "\tFROM composition AS c \n" +
                "\tWHERE c.name = (?)\n" +
                "\tUNION\n" +
                "\tSELECT c2.name, c2.id, c2.prev_listening_id, cast (temp1.path || '->'|| c2.name as varchar(50))\n" +
                "\tFROM composition AS c2 INNER JOIN temp1 on (temp1.prev_listening_id = c2.id))\n" +
                "\tSELECT * FROM temp1";

        try(PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.setString(1, compName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(String.format("Album = {name: %s, id: %d, prev_listening_id: %d, path: %s}",
                        rs.getString("name"),
                        rs.getInt("id"),
                        rs.getInt("prev_listening_id"),
                        rs.getString("path")));
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
/*
    public static void getShortedComposition(Connection conn, int minDuration) {

        String SQL = "SELECT c.name as composition_name, c.duration as composition_duration,\n" +
                "       a.name as album_name FROM composition AS c\n" +
                "INNER JOIN album a on a.id = c.album_id\n" +
                "WHERE NOT duration < (?)\n" +
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

    public static void getShortedAlbumComposition(Connection conn, int minDuration, String albumName) {

        String SQL = "SELECT c.name as composition_name, c.duration as composition_duration,\n" +
                "       a.name as album_name FROM composition AS c\n" +
                "INNER JOIN album a on a.id = c.album_id\n" +
                "WHERE NOT duration < (?)\n" +
                "AND a.name = (?)\n" +
                "ORDER BY duration ASC\n" +
                "LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, minDuration);
            pstmt.setString(2, albumName);
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

    //=============================================================================

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
    }*/
}
