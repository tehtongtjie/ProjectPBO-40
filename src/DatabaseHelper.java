
public class DatabaseHelper {
    
    // --- Simulasi Login ---
    public boolean login(String username, String password) {
        // Ganti dengan koneksi JDBC nyata.
        // Simulasi: Login berhasil jika username="user" dan password="123"
        return "user".equals(username) && "123".equals(password);
    }
    
    // --- Simulasi Register ---
    public boolean register(String nama, String email, String password) {
        // Ganti dengan logika penyimpanan data user baru (JDBC INSERT)
        System.out.println("User Baru terdaftar:");
        System.out.println("Nama: " + nama + ", Email: " + email);
        return true; // Asumsi registrasi selalu berhasil
    }
    
    // Metode lain... (getDaftarMenu, savePesanan, dll.)
}