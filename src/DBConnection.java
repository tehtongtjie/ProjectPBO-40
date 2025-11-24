import java.sql.*;

public class DBConnection {

    private static final String URL  = "jdbc:mysql://localhost:3306/restoran?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    // =========================
    // GET KONEKSI DATABASE
    // =========================
    public static Connection getConnection() {
        try {
            // ✅ WAJIB untuk Java 17 + MySQL Connector 8/9
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            return conn;

        } catch (Exception e) {
            System.out.println("[ERROR] Tidak dapat terhubung ke database!");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean registerUser(String username, String password) {

        String cekUser = "SELECT username FROM users WHERE username = ?";
        String insertUser = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = getConnection()) {

            if (conn == null) {
                System.out.println("[ERROR] Koneksi null saat register!");
                return false;
            }

            PreparedStatement ps1 = conn.prepareStatement(cekUser);
            ps1.setString(1, username);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) return false;

            PreparedStatement ps2 = conn.prepareStatement(insertUser);
            ps2.setString(1, username);
            ps2.setString(2, password);
            ps2.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int loginUser(String username, String password) {

        String query = "SELECT user_id FROM users WHERE username = ? AND password = ?";

        try (Connection conn = getConnection()) {

            if (conn == null) {
                System.out.println("[ERROR] Koneksi null saat login!");
                return -1;
            }

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt("user_id");

            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
