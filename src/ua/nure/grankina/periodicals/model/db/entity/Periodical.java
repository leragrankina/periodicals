package ua.nure.grankina.periodicals.model.db.entity;

/**
 * Periodical entity
 *
 * Created by Valeriia on 01.01.2017.
 */
public class Periodical extends Entity{
    private String title;
    private Double price;
    private Theme theme;
    private Period period;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Periodical that = (Periodical) o;

        if (!title.equals(that.title)) return false;
        if (!price.equals(that.price)) return false;
        if (!theme.equals(that.theme)) return false;
        return period.equals(that.period);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + theme.hashCode();
        result = 31 * result + period.hashCode();
        return result;
    }
}
