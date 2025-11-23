import java.awt.*;
import javax.swing.*;

public class RestaurantApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel rootPanel;

    private int activeUserId = -1;
    private String activeUsername = "";

    private JTabbedPane mainTabs;

    private MenuPanel menuPanel;
    private CartPanel cartPanel;
    private OrdersPanel ordersPanel;
    private KitchenPanel kitchenPanel;

    public RestaurantApp() {
        setTitle("Aplikasi Pemesanan Makanan");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        rootPanel = new JPanel(cardLayout);

        // sekarang hanya 1 panel auth
        AuthPanel authPanel = new AuthPanel(this);

        rootPanel.add(authPanel, "auth");

        add(rootPanel);

        showAuthPanel();
    }

    // tampilkan login/register gabungan
    public void showAuthPanel() {
        cardLayout.show(rootPanel, "auth");
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

    // buat tab utama setelah login
    public void showMainTabs() {

        if (mainTabs == null) {
            mainTabs = new JTabbedPane();

            menuPanel = new MenuPanel(this);
            cartPanel = new CartPanel(this);
            ordersPanel = new OrdersPanel(this);
            kitchenPanel = new KitchenPanel(this);

            mainTabs.addTab("Menu", menuPanel);
            mainTabs.addTab("Keranjang", cartPanel);
            mainTabs.addTab("Pesanan Saya", ordersPanel);
            mainTabs.addTab("Dapur", kitchenPanel);

            rootPanel.add(mainTabs, "tabs");
        }

        cardLayout.show(rootPanel, "tabs");
    }

    public CartPanel getCartPanel() {
        return cartPanel;
    }

    public OrdersPanel getOrdersPanel() {
        return ordersPanel;
    }

    public KitchenPanel getKitchenPanel() {
        return kitchenPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RestaurantApp().setVisible(true));
    }
}
