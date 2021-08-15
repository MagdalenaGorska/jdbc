package Parking;

import java.sql.*;
import java.util.Scanner;

public class ParkingFunctions {

    //PROCEDURA ZAGNIEŻDŻONA?

    private Connection connection;
    private Statement statement;
    private String query;
    private Scanner scanner;
    private PreparedStatement prepareStatementUpdate;

    public ParkingFunctions(Connection connection) {
        this.connection = connection;
    }

    public void createTableParking() {
        try {
            statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS PARKING");
            query = "CREATE TABLE IF NOT EXISTS PARKING (id INTEGER AUTO_INCREMENT PRIMARY KEY" +
                    ",isDisabled BOOLEAN" +
                    ",idPerson INTEGER)";

            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // nie wybrałaś opcji, jaki rodzaj miejsca chcesz: normalne czy niepełnosprawne?
    public void createTablePerson() {
        try {
            statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS PERSON");
            query = "CREATE TABLE IF NOT EXISTS PERSON (id INTEGER AUTO_INCREMENT PRIMARY KEY" +
                    ",firstName VARCHAR(30)" +
                    ",surname VARCHAR(30)" +
                    ",registrationNumber varchar(10));";
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setNumberOfParkingSpot() {
        try {
            scanner = new Scanner(System.in);
            System.out.println("Podaj ilość miejsc parkingowych");
            int howManySlots = scanner.nextInt();
            for (int i = 0; i < howManySlots; i++) {
                statement.execute("INSERT INTO PARKING (isDisabled, idPerson) VALUES (false,0)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setSpotForDisabled() {

    }

    public void addPerson() {
        System.out.println("Dodawanie osoby: Podaj imię, nazwisko i numer rejestracyjny");
        try {
            prepareStatementUpdate = connection.prepareStatement("INSERT INTO PERSON (firstName, surname,registrationNumber) VALUES (?,?,?)");
            //wpisywanie danych osobowych
            prepareStatementUpdate.setString(1, scanner.next());
            prepareStatementUpdate.setString(2, scanner.next());
            prepareStatementUpdate.setString(3, scanner.next());
            prepareStatementUpdate.executeUpdate();

            //wyszukiwanie ostatniego ID Person
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() from person");
            if (resultSet.next()) {
                int chosenParkingSpot;
                int x = 0;
                int IdPerson = resultSet.getInt("LAST_INSERT_ID()");

                //wybieranie miejsca wolnego -> jeśli jest wolne, to do tabeli Parking dodawany jest ID Person
                //                           -> jeśli nie, to następuje prośba o ponowne wybranie miejsca
                do {
                    System.out.println("Podaj wolne miejsce");
                    chosenParkingSpot = scanner.nextInt();
                    resultSet = statement.executeQuery("SELECT idPerson FROM PARKING WHERE id = " + chosenParkingSpot);
                    if (resultSet.next()) {
                        x = resultSet.getInt("idPerson");

                    }
                }
                while (x != 0);

                //Jeśli miejsce zostało prawidłowo wybrane, to nastąpi update tabelki Parking
                prepareStatementUpdate = connection.prepareStatement("UPDATE PARKING SET idPerson = " + IdPerson + " WHERE id = ?");
                prepareStatementUpdate.setInt(1, chosenParkingSpot);
                prepareStatementUpdate.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deletePerson() {
        System.out.println("Usuwanie osoby: Podaj id osoby");
        try {
            prepareStatementUpdate = connection.prepareStatement("DELETE FROM PERSON WHERE id = ?");
            String chosenPersonToDelete = scanner.next();
            prepareStatementUpdate.setString(1, chosenPersonToDelete);
            prepareStatementUpdate.executeUpdate();

            prepareStatementUpdate = connection.prepareStatement("UPDATE PARKING SET idPerson = 0 WHERE idPerson = " + chosenPersonToDelete);
            prepareStatementUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    Dodać do ParkingSpot?
//    public void testGet() {
//        try {
//            ResultSet resultSet = statement.executeQuery("SELECT id FROM PARKING WHERE idPerson = 1;");
//            if (resultSet.next()) {
//                int x = resultSet.getInt("id");
//                System.out.println(x);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    //
//    }

}

