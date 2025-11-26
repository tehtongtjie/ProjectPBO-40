import java.awt.*;
import javax.swing.*;

public class Main extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    
    // Objek yang dibagikan
    private Pesanan keranjang = new Pesanan();
    private DatabaseHelper dbHelper = new DatabaseHelper();

    // Konstanta Nama View
    public static final String LOGIN_VIEW = "Login";
    public static final String HOME_VIEW = "Home"; 
    public static final String CHART_VIEW = "Cart";
    public static final String PESANAN_VIEW = "Pesanan";

    public Main() {
        setTitle("Sistem Pemesanan Restoran PBO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650); // Ukuran disesuaikan
        setLocationRelativeTo(null);
        
        // Inisialisasi Panel
        mainPanel.add(new LoginPanel(this), LOGIN_VIEW);
        mainPanel.add(new HomePanel(this), HOME_VIEW); // Dummy
        
        add(mainPanel);
    }
    
    // Metode Navigasi
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void showPesananPanel(Pesanan pesanan) {
        // Implementasi penuh nanti
        PesananPanel pesananPanel = new PesananPanel(this, pesanan);
        mainPanel.add(pesananPanel, PESANAN_VIEW);
        cardLayout.show(mainPanel, PESANAN_VIEW);
    }
    
    // Getter objek yang dibagikan
    public Pesanan getKeranjang() { return keranjang; }
    public DatabaseHelper getDbHelper() { return dbHelper; }
    
    public static void main(String[] args) {
        // Atur tampilan agar lebih modern (Opsional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}

// --- DUMMY CLASSES UNTUK KOMPILASI ---
// Ini akan diganti dengan kelas model PBO sesungguhnya nanti
class Pesanan {}
class HomePanel extends JPanel { public HomePanel(Main m) { /* dummy */ } } 
class PesananPanel extends JPanel { public PesananPanel(Main m, Pesanan p) { /* dummy */ } }
