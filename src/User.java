
public class User {
    private int id;
    private String nama;
    private String email;

    // Tambahkan konstruktor
    public User(int id, String nama, String email) {
        this.id = id;
        this.nama = nama;
        this.email = email;
    }

    // Tambahkan getters
    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }
}