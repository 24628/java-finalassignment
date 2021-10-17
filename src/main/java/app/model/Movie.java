package app.model;

public class Movie {

    private int durationInMinutes;
    private String title;
    private double price;

    public Movie(int durationInMinutes, String title, double price) {
        this.durationInMinutes = durationInMinutes;
        this.title = title;
        this.price = price;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
