import properties.JDBC;

import java.sql.*;

public class App {
    public static void main (String[] args){

        Connection connection = JDBC.connect();
        if (connection !=null) {
            JDBC.getShortedAlbumComposition(connection,5,"album3");

            JDBC.getShortedComposition(connection, 5);
            JDBC.removeCompositionByName(connection, "comp8");
            JDBC.getShortedComposition(connection, 5);
            JDBC.insertComposition(connection, "Composition 99", 6, 2);
            JDBC.getShortedComposition(connection, 5);
            JDBC.changeCompositionName(connection, "Composition 99", "New composition name");
            JDBC.getShortedComposition(connection, 5);
        }
        else{
            System.out.println("Database is not available");
        }
    }
}
