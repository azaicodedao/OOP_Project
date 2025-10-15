package Model;

import java.time.LocalDateTime;

public class Invoice {
    private int id;
    private LocalDateTime Create_date;
    double Total;

    public Invoice(int id, LocalDateTime Create_date, double Total) {
        this.id = id;
        this.Create_date = Create_date;
        this.Total = Total;
    }
    public Invoice(double Total, LocalDateTime Create_date) {
        this.Total = Total;
        this.Create_date = Create_date;
    }
    public int getId() {
        return id;
    }
    public LocalDateTime getDate() {
        return Create_date;
    }
    public double getTotal() {
        return Total;
    }
}
