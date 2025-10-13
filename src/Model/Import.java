package Model;

import java.time.LocalDateTime;

public class Import {
    private int id;
    private final double total;
    private final LocalDateTime ngayNhap;

    public Import(int id, double total, LocalDateTime ngayNhap){
        this.total = total;
        this.ngayNhap = ngayNhap;
    }

    public int getId(){
        return id;
    }

    public double getTotal(){
        return total;
    }

    public LocalDateTime getNgayNhap(){
        return ngayNhap;
    }

    public void setId(int id) {
        this.id = id;
    }
}