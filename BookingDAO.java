import java.sql.*;

public class BookingDAO {

    // ✅ Book ticket
    public boolean bookTicket(int userId, int movieId) throws SQLException {
        String insertSql = "INSERT INTO bookings (user_id, movie_id) VALUES (?, ?)";
        String selectSql = "SELECT tickets FROM movies WHERE id = ?";
        String updateSql = "UPDATE movies SET tickets = tickets - 1 WHERE id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            // 1️⃣ Check available tickets
            int tickets = 0;
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, movieId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        tickets = rs.getInt("tickets");
                    } else {
                        System.out.println("❌ Invalid movie ID!");
                        return false;
                    }
                }
            }

            if (tickets <= 0) {
                System.out.println("🎟️ No tickets available!");
                return false;
            }

            // 2️⃣ Book ticket
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, userId);
                ps.setInt(2, movieId);
                ps.executeUpdate();
            }

            // 3️⃣ Decrease ticket count
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, movieId);
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Ticket booked successfully!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Cancel ticket
    public boolean cancelBooking(int bookingId, int userId) throws SQLException {
        String getSql = "SELECT movie_id FROM bookings WHERE id = ? AND user_id = ?";
        String deleteSql = "DELETE FROM bookings WHERE id = ?";
        String updateSql = "UPDATE movies SET tickets = tickets + 1 WHERE id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            int movieId = -1;

            // 1️⃣ Get movie ID
            try (PreparedStatement ps = conn.prepareStatement(getSql)) {
                ps.setInt(1, bookingId);
                ps.setInt(2, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        movieId = rs.getInt("movie_id");
                    } else {
                        System.out.println("❌ Booking not found or not yours!");
                        return false;
                    }
                }
            }

            // 2️⃣ Delete booking
            try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                ps.setInt(1, bookingId);
                ps.executeUpdate();
            }

            // 3️⃣ Increase ticket count
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, movieId);
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Ticket cancelled successfully!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ View bookings for user
    public void viewUserBookings(int userId) {
        String query = "SELECT b.id AS booking_id, m.title, m.genre, m.price, b.booking_date " +
                       "FROM bookings b " +
                       "JOIN movies m ON b.movie_id = m.id " +
                       "WHERE b.user_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n🎟️ Your Booked Tickets:");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("Booking ID: " + rs.getInt("booking_id"));
                System.out.println("🎬 Movie: " + rs.getString("title") +
                                   " | Genre: " + rs.getString("genre") +
                                   " | Price: ₹" + rs.getDouble("price"));
                System.out.println("📅 Date: " + rs.getTimestamp("booking_date"));
                System.out.println("--------------------------------");
            }

            if (!found) {
                System.out.println("😕 You haven’t booked any tickets yet!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Top Booked Movies Report (Analytics Feature)
    public void showTopMoviesReport() {
        String sql = "SELECT m.title, COUNT(b.id) AS total_bookings " +
                     "FROM bookings b " +
                     "JOIN movies m ON b.movie_id = m.id " +
                     "GROUP BY m.id " +
                     "ORDER BY total_bookings DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n📊 Top Booked Movies:");
            int rank = 1;
            boolean found = false;

            while (rs.next()) {
                found = true;
                String title = rs.getString("title");
                int total = rs.getInt("total_bookings");
                System.out.println(rank + ". " + title + " — " + total + " tickets");
                rank++;
            }

            if (!found) {
                System.out.println("😕 No bookings found yet!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
