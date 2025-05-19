package DB;

import model.Customer;
import model.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDB implements ReservationDBIF {

    // SQL-forespørgsler til at hente alle reservationer eller en specifik
    private static final String FIND_ALL_Q = 
        "SELECT r.reservationId, r.date, r.amount, r.customerId, " +
        "c.firstName, c.lastName, c.email, c.amount AS customerAmount " +
        "FROM Reservation r " +
        "JOIN Customer c ON r.customerId = c.customerId";

    private static final String FIND_BY_ID_Q = FIND_ALL_Q + " WHERE r.reservationId = ?";

    private PreparedStatement findAll;
    private PreparedStatement findById;

    // Initialiserer databasen og forbereder SQL-statements
    public ReservationDB() throws DataAccessException {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            findAll = con.prepareStatement(FIND_ALL_Q);
            findById = con.prepareStatement(FIND_BY_ID_Q);
        } catch (SQLException e) {
            throw new DataAccessException(e, "Kunne ikke forberede statements i ReservationDB");
        }
    }

    @Override
    // Henter alle reservationer fra databasen
    public List<Reservation> findAll() throws DataAccessException {
        try {
            ResultSet rs = findAll.executeQuery();
            return buildObjects(rs);
        } catch (SQLException e) {
            throw new DataAccessException(e, "Kunne ikke hente alle reservationer");
        }
    }

    @Override
    // Finder én reservation ud fra reservationId
    public Reservation findByReservationId(int reservationId) throws DataAccessException {
        try {
            findById.setInt(1, reservationId);
            ResultSet rs = findById.executeQuery();
            if (rs.next()) {
                return buildObject(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e, "Kunne ikke finde reservation med ID = " + reservationId);
        }
        return null;
    }

    // Konverterer resultatet fra databasen til en liste af Reservation-objekter
    private List<Reservation> buildObjects(ResultSet rs) throws SQLException {
        List<Reservation> list = new ArrayList<>();
        while (rs.next()) {
            list.add(buildObject(rs));
        }
        return list;
    }

    // Bygger et enkelt Reservation-objekt ud fra en række i resultatet
    private Reservation buildObject(ResultSet rs) throws SQLException {
        int id = rs.getInt("reservationId");
        LocalDate date = rs.getDate("date").toLocalDate();
        int amount = rs.getInt("amount");

        // Opretter kundeobjektet ud fra oplysninger fra resultatet
        Customer customer = new Customer(
            rs.getInt("customerId"),
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getString("email"),
            rs.getInt("customerAmount")
        );

        // Returnerer en komplet Reservation med kundeinformation
        return new Reservation(id, date, amount, customer);
    }
}
