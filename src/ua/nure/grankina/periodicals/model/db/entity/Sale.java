package ua.nure.grankina.periodicals.model.db.entity;

/**
 * Created by USER-PC on 21.01.2017.
 */
public class Sale {
    private Periodical periodical;
    private int count;
    private double sum;

    public Periodical getPeriodical() {
        return periodical;
    }

    public void setPeriodical(Periodical periodical) {
        this.periodical = periodical;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
