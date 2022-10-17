import properties.JDBC;

import java.sql.*;

public class App {
    public static void main (String[] args){

        Connection connection = JDBC.connect();
        JDBC.getShortedComposition(connection, 5);
        JDBC.removeCompositionByName(connection, "comp4");
        JDBC.getShortedComposition(connection, 5);
        JDBC.insertComposition(connection, "Composition 99", 6, 2);
        JDBC.getShortedComposition(connection, 5);
        JDBC.changeCompositionName(connection, "Composition 99", "New composition name");
        JDBC.getShortedComposition(connection, 5);
    }
}
