import java.util.Objects; // Perlu untuk List

public abstract class Menu {
    private String nama;
    private double hargaDasar;

    public Menu(String nama, double hargaDasar) {
        this.nama = nama;
        this.hargaDasar = hargaDasar;
    }

    // Metode Abstrak (Abstraction)
    public abstract double hitungHargaJual();

    // Getter dan Setter
    public String getNama() {
        return nama;
    }

    public double getHargaDasar() {
        return hargaDasar;
    }
    
    // Override equals dan hashCode untuk membandingkan item di keranjang
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(nama, menu.nama);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nama);
    }
}
