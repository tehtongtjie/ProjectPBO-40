import javax.swing.*;

public class RestaurantApp extends JFrame {

    private int activeUserId = -1;
    private String activeUsername = "";

    public RestaurantApp() {
        setTitle("Aplikasi Pemesanan Makanan");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ✅ tampilkan login / register
        setContentPane(new AuthPanel(this));
    }

    public void setActiveUser(int userId, String username) {
        this.activeUserId = userId;
        this.activeUsername = username;
    }

    public int getActiveUserId() {
        return activeUserId;
    }

    public String getActiveUsername() {
        return activeUsername;
    }

    // ✅ setelah login buka MenuGUI
    public void showMainTabs() {
        // Tutup window login
        this.dispose();

        // Buka UI menu modern
        new MenuGUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RestaurantApp().setVisible(true));
    }
}
