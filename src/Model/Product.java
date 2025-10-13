package Model;

public class Product {
    private int id;
    private String maSP;
    private String tenSP;
    private String donvi;
    private double gia;
    private int soluong;
//    Đạng hiển thị trong Table
    public Product(int id, String maSP, String tenSP, String donvi, double gia, int soluong) {
        this.id = id;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donvi = donvi;
        this.gia = gia;
        this.soluong = 0;
    }
//    Dạng khi tạo trong UI.Product.Add_Frame
    public Product(String tenSP, String donvi) {
        this.tenSP = tenSP;
        this.maSP = setMaSP(tenSP);
        this.donvi = donvi;
        this.gia = 0.0;
        this.soluong = 0;

    }
    private String setMaSP(String tenSP) {
        String [] res = tenSP.trim().split("\\s+");
        StringBuilder ma =  new StringBuilder();
        for(String s: res ){
            ma.append(s.substring(0,1).toUpperCase());
        }
        return ma.toString();
    }
    public int getId(){
        return this.id;
    }
    public String getMaSP(){
        return this.maSP;
    }
    public String getTenSP(){
        return this.tenSP;
    }
    public String getDonvi(){
        return this.donvi;
    }
    public double getGia(){
        return this.gia;
    }
    public int getSoluong(){
        return this.soluong;
    }
    public String toString(){
        return this.tenSP;
    }


}
