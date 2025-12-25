public class Booking {
    private int id;
    private int userId;
    private int movieId;
    private String bookingDate;

    public Booking(int id, int userId, int movieId, String bookingDate) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.bookingDate = bookingDate;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    @Override
    public String toString() {
        return "Booking ID: " + id +
                ", Movie ID: " + movieId +
                ", Date: " + bookingDate;
    }
}
