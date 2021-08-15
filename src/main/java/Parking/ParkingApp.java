package Parking;

import java.sql.*;

public class ParkingApp {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "221011")) {

            ParkingFunctions parking = new ParkingFunctions(connection);

            parking.createTableParking();
            parking.createTablePerson();
            parking.setNumberOfParkingSpot();
            parking.addPerson();
            //parking.testGet();
            parking.addPerson();
            parking.addPerson();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*
    Ćwiczenie
    Przygotujcie prosze aplikacje do zarzadzania parkingiem.
     Aplikacja powinna przewidywać możliwość:
    - wprowadzenie dowolnej liczby miejsc parkinowych <1000,
    - rezerwacje wylacznie wolnych miejsc przez kierowcow,
    - zablokowywanie miejsc przez zarzadzajacych parkingiem,
    - rezerwacje miejsca przez zarzadzajacych parkingiem dla okreslonego kierowcy,
    - oznaczenie miejsc dla niepełnosprawnych przez zarzadzajacego parkingiem.
    Każdy kierowca powinien byc identyfikowany przez imie, nazwisko, numer rejestracyjny.
    Ćwiczenie do wykonania z wykorzystaniem DAO.
 */
}