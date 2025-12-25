import java.sql.*;
import java.util.*;

public class MovieDAO {

    // Add new movie
    public void addMovie(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, genre, tickets, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setInt(3, movie.getTickets());
            ps.setDouble(4, movie.getPrice());
            ps.executeUpdate();
        }
    }

    // View all movies
    public void viewMovies() throws SQLException {
        List<Movie> movies = getAllMovies();
        System.out.println("\n🎞️ Available Movies:");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        if (movies.isEmpty()) {
            System.out.println("😕 No movies available yet!");
        }
    }

    // Get all movies (used by viewMovies)
    public List<Movie> getAllMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("tickets"),
                        rs.getDouble("price")
                );
                movies.add(movie);
            }
        }
        return movies;
    }

    // Get movie by ID
    public Movie getMovieById(int id) throws SQLException {
        String sql = "SELECT * FROM movies WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Movie(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getInt("tickets"),
                            rs.getDouble("price")
                    );
                }
            }
        }
        return null;
    }

    // Update available tickets
    public void updateTickets(int movieId, int newTickets) throws SQLException {
        String sql = "UPDATE movies SET tickets = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newTickets);
            ps.setInt(2, movieId);
            ps.executeUpdate();
        }
    }
}