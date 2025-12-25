public class Movie {
    private int id;
    private String title;
    private String genre;
    private int tickets;
    private double price;

    public Movie(int id, String title, String genre, int tickets, double price) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.tickets = tickets;
        this.price = price;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getTickets() {
        return tickets;
    }

    public double getPrice() {
        return price;
    }

    // Setters
    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    // For printing readable movie info
    @Override
    public String toString() {
        return id + ". 🎬 " + title + " | Genre: " + genre + " | Tickets: " + tickets + " | ₹" + price;
    }
}
