import java.sql.*;

public class DBConnection {

    // KONFIGURASI DATABASE
    private static final String URL  = "jdbc:mysql://localhost:3306/restoran";
    private static final String USER = "root";
    private static final String PASS = "";

    // GET KONEKSI DATABASE
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("[ERROR] Tidak dapat terhubung ke database!");
            e.printStackTrace();
            return null;
        }
    }

    // REGISTER USER BARU
    public static boolean registerUser(String username, String password) {

        String cekUser = "SELECT username FROM users WHERE username = ?";
        String insertUser = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps1 = conn.prepareStatement(cekUser);
             PreparedStatement ps2 = conn.prepareStatement(insertUser)) {

            // cek apakah username sudah terpakai
            ps1.setString(1, username);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                System.out.println("[INFO] Username sudah digunakan!");
                return false;
            }

            // simpan user baru
            ps2.setString(1, username);
            ps2.setString(2, password);
            ps2.executeUpdate();

            System.out.println("[INFO] Registrasi berhasil!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static int loginUser(String username, String password) {
        String query = "SELECT user_id FROM users WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                System.out.println("[INFO] Login berhasil! user_id = " + id);
                return id;
            } else {
                System.out.println("[INFO] Login gagal!");
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // SIMPAN PESANAN (HEADER)

    public static int simpanPesanan(int userId, double totalHarga) {

        String query = "INSERT INTO pesanan (user_id, total_harga) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setDouble(2, totalHarga);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int idPesanan = rs.getInt(1);
                System.out.println("[INFO] Pesanan berhasil disimpan. ID = " + idPesanan);
                return idPesanan;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // SIMPAN DETAIL PESANAN
    public static boolean simpanDetailPesanan(int idPesanan, int idMenu, int jumlah, double subtotal) {

        String query = "INSERT INTO detail_pesanan (id_pesanan, id_menu, jumlah, subtotal) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idPesanan);
            ps.setInt(2, idMenu);
            ps.setInt(3, jumlah);
            ps.setDouble(4, subtotal);
            ps.executeUpdate();

            System.out.println("[INFO] Detail pesanan ditambahkan!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // TES KONEKSI
    public static void main(String[] args) {
        Connection c = getConnection();
        System.out.println(c != null ? "Koneksi OK!" : "Koneksi GAGAL!");
    }
}
