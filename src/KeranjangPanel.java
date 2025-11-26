import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.PanelUI;

/**
 * Kelas yang menampilkan antarmuka Keranjang Belanja (Sidebar).
 * Menampilkan item, kuantitas, harga, dan ringkasan pembayaran.
 */
public class KeranjangPanel extends JPanel {

    // --- Data dan Model ---
    
    // Kelas data untuk Item dalam Keranjang
    // Diambil dari MenuItem record di MenuKatalogPanel
    public record CartItem(String name, String priceString, String imageUrl, double pricePerItem) {}

    // Objek yang menyimpan item keranjang saat ini
    private final List<CartEntry> cartItems = new ArrayList<>(); 
    // Format mata uang Rupiah
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    // Komponen UI
    private final JPanel cartListPanel; // Container untuk daftar item
    private final JLabel subtotalLabel;
    private final JLabel taxLabel;
    private final JLabel totalLabel;
    private final JLabel itemCountLabel;
    private final JButton checkoutButton;
    
    // Konstanta Warna dan Gaya (Mengambil dari MenuKatalogPanel)
    private static final Color PRIMARY_ORANGE = new Color(255, 100, 0); 
    private static final Color DARK_ORANGE = new Color(240, 50, 0); 
    private static final Color BACKGROUND_GRAY = new Color(245, 245, 245); 
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 20); 
    private static final int CORNER_RADIUS = 20;

    /**
     * Entry Cart: Menyimpan item dan kuantitasnya.
     */
    public class CartEntry {
        public CartItem item;
        public int quantity;

        public CartEntry(CartItem item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }
        
        // Menghitung total harga untuk entry ini
        public double getTotalPrice() {
            return item.pricePerItem * quantity;
        }
    }

    /**
     * Konstruktor untuk KeranjangPanel.
     * @param checkoutListener ActionListener yang akan dipanggil saat tombol Checkout ditekan.
     */
    public KeranjangPanel(ActionListener checkoutListener) {
        // Mengatur properti tampilan sidebar (mirip JDialog atau JLayeredPane)
        setPreferredSize(new Dimension(350, 800)); 
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Header Panel (Keranjang Belanja, Jumlah Item, Tombol Tutup)
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Konten Utama: Daftar Item yang bisa di-scroll
        cartListPanel = new JPanel();
        cartListPanel.setLayout(new BoxLayout(cartListPanel, BoxLayout.Y_AXIS));
        cartListPanel.setOpaque(false);
        cartListPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JScrollPane scrollPane = new JScrollPane(cartListPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        add(scrollPane, BorderLayout.CENTER);

        // Footer Panel: Ringkasan Harga dan Tombol Checkout
        JPanel footerPanel = createFooterPanel(checkoutListener);
        add(footerPanel, BorderLayout.SOUTH);

        // Terapkan shadow dan rounded corner ke panel utama (jika di dalam JDialog/JLayeredPane)
        // Jika digunakan sebagai komponen di dalam JFrame, ini mungkin perlu disesuaikan di MainFrame.
        // Untuk saat ini, kita akan membuat tampilan yang bersih.
        // setUI(new RoundedPanelUI(CORNER_RADIUS)); 
        
        // Inisialisasi tampilan keranjang
        updateCartDisplay();
    }
    
    // --- UI Helpers ---

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(5, 0));
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        headerPanel.setBackground(Color.WHITE);

        // Ikon dan Judul
        JLabel titleLabel = new JLabel("\uD83D\uDCCB Keranjang Belanja");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 18));
        
        // Jumlah Item
        itemCountLabel = new JLabel("0 item dalam keranjang");
        itemCountLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        itemCountLabel.setForeground(Color.GRAY.darker());

        JPanel titleContainer = new JPanel(new BorderLayout(0, 5));
        titleContainer.setOpaque(false);
        titleContainer.add(titleLabel, BorderLayout.NORTH);
        titleContainer.add(itemCountLabel, BorderLayout.CENTER);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        
        // Tombol Tutup (X) - Placeholder, karena biasanya diimplementasikan di JFrame/JDialog
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Inter", Font.PLAIN, 24));
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setFocusPainted(false);
        closeButton.setContentAreaFilled(false);
        // Tindakan menutup akan diatur di kelas Main yang memanggil KeranjangPanel

        headerPanel.add(closeButton, BorderLayout.EAST);
        
        // Tambahkan separator di bawah header
        headerPanel.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);

        return headerPanel;
    }
    
    private JPanel createFooterPanel(ActionListener checkoutListener) {
        JPanel footerPanel = new JPanel(new BorderLayout(10, 10));
        footerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        footerPanel.setBackground(Color.WHITE);

        // 1. Ringkasan Harga
        JPanel summaryPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        summaryPanel.setOpaque(false);
        
        // Baris Subtotal
        summaryPanel.add(new JLabel("Subtotal"));
        subtotalLabel = new JLabel("Rp 0", SwingConstants.RIGHT);
        subtotalLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        summaryPanel.add(subtotalLabel);
        
        // Baris Pajak (Dummy 10%)
        summaryPanel.add(new JLabel("Pajak (10%)"));
        taxLabel = new JLabel("Rp 0", SwingConstants.RIGHT);
        taxLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        summaryPanel.add(taxLabel);

        // Baris Total
        summaryPanel.add(new JLabel("Total"));
        totalLabel = new JLabel("Rp 0", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Inter", Font.BOLD, 18));
        totalLabel.setForeground(DARK_ORANGE.darker());
        summaryPanel.add(totalLabel);
        
        // 2. Tombol Checkout
        checkoutButton = new JButton("Checkout");
        checkoutButton.setFont(new Font("Inter", Font.BOLD, 16));
        checkoutButton.setBackground(PRIMARY_ORANGE);
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.setBorder(new EmptyBorder(15, 20, 15, 20));
        checkoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutButton.setUI(new RoundedButtonUI(15)); // Sudut melengkung
        checkoutButton.addActionListener(checkoutListener); // Tambahkan listener dari konstruktor

        footerPanel.add(summaryPanel, BorderLayout.NORTH);
        footerPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER); // Spasi
        footerPanel.add(checkoutButton, BorderLayout.SOUTH);

        return footerPanel;
    }
    
    /**
     * Membuat tampilan baris untuk satu item di keranjang.
     */
    private JPanel createCartItemRow(CartEntry entry) {
        JPanel rowPanel = new JPanel(new BorderLayout(15, 0));
        rowPanel.setOpaque(false);
        rowPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); 
        
        // 1. Gambar
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(70, 70));
        imageLabel.setBackground(BACKGROUND_GRAY);
        imageLabel.setOpaque(true);
        // Menggunakan UI kustom untuk gambar ber-rounded corner
        imageLabel.setUI(new RoundedLabelUI(10)); 
        
        // Memuat dan menskalakan gambar
        URL imageUrl = getClass().getResource(entry.item.imageUrl());
        if (imageUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(70, 70, Image.SCALE_SMOOTH); 
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
             imageLabel.setText("Foto");
             imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        rowPanel.add(imageLabel, BorderLayout.WEST);

        // 2. Detail Item (Nama dan Harga per Unit)
        JPanel detailPanel = new JPanel(new BorderLayout(0, 5));
        detailPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(entry.item.name());
        nameLabel.setFont(new Font("Inter", Font.BOLD, 14));
        
        JLabel priceLabel = new JLabel(entry.item.priceString()); 
        priceLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        priceLabel.setForeground(DARK_ORANGE);
        
        detailPanel.add(nameLabel, BorderLayout.NORTH);
        detailPanel.add(priceLabel, BorderLayout.CENTER);
        
        rowPanel.add(detailPanel, BorderLayout.CENTER);

        // 3. Kontrol Kuantitas dan Hapus
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        controlPanel.setOpaque(false);

        // Tombol Hapus Item
        JButton deleteButton = new JButton("\uD83D\uDDD1"); // Ikon Tempat Sampah
        deleteButton.setFont(new Font("Inter", Font.BOLD, 14));
        deleteButton.setForeground(Color.RED.darker());
        deleteButton.setBackground(BACKGROUND_GRAY);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(new EmptyBorder(5, 8, 5, 8));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setUI(new RoundedButtonUI(8));

        // Tombol Kurang Kuantitas
        JButton minusButton = new JButton("-");
        minusButton.setFont(new Font("Inter", Font.BOLD, 14));
        minusButton.setForeground(Color.BLACK);
        minusButton.setBackground(BACKGROUND_GRAY);
        minusButton.setFocusPainted(false);
        minusButton.setBorder(new EmptyBorder(5, 8, 5, 8));
        minusButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minusButton.setUI(new RoundedButtonUI(8));
        
        // Label Kuantitas
        JLabel quantityLabel = new JLabel(String.valueOf(entry.quantity), SwingConstants.CENTER);
        quantityLabel.setFont(new Font("Inter", Font.BOLD, 14));
        quantityLabel.setPreferredSize(new Dimension(20, 30));
        
        // Tombol Tambah Kuantitas
        JButton plusButton = new JButton("+");
        plusButton.setFont(new Font("Inter", Font.BOLD, 14));
        plusButton.setForeground(Color.BLACK);
        plusButton.setBackground(BACKGROUND_GRAY);
        plusButton.setFocusPainted(false);
        plusButton.setBorder(new EmptyBorder(5, 8, 5, 8));
        plusButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        plusButton.setUI(new RoundedButtonUI(8));
        
        // Aksi Tombol
        minusButton.addActionListener(e -> updateQuantity(entry, entry.quantity - 1));
        plusButton.addActionListener(e -> updateQuantity(entry, entry.quantity + 1));
        deleteButton.addActionListener(e -> removeItem(entry));

        // Grup Kontrol Kuantitas
        JPanel quantityControl = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        quantityControl.setOpaque(false);
        quantityControl.add(minusButton);
        quantityControl.add(quantityLabel);
        quantityControl.add(plusButton);
        
        // Gabungkan Control dan Delete Button
        controlPanel.add(quantityControl);
        controlPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Jarak ke tombol hapus
        controlPanel.add(deleteButton); 
        
        rowPanel.add(controlPanel, BorderLayout.EAST);
        
        // Tambahkan separator di bawah baris item
        JPanel separatorWrapper = new JPanel(new BorderLayout());
        separatorWrapper.setOpaque(false);
        separatorWrapper.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.CENTER);
        rowPanel.add(separatorWrapper, BorderLayout.SOUTH);

        return rowPanel;
    }
    
    // --- Logika Keranjang ---

    /**
     * Menambahkan item ke keranjang. Jika sudah ada, kuantitasnya ditambah.
     */
    public void addItem(CartItem item) {
        for (CartEntry entry : cartItems) {
            if (entry.item.name().equals(item.name())) {
                entry.quantity++;
                updateCartDisplay();
                return;
            }
        }
        // Item baru
        cartItems.add(new CartEntry(item, 1));
        updateCartDisplay();
    }
    
    /**
     * Memperbarui kuantitas item.
     */
    private void updateQuantity(CartEntry entry, int newQuantity) {
        if (newQuantity <= 0) {
            removeItem(entry);
        } else {
            entry.quantity = newQuantity;
            updateCartDisplay();
        }
    }
    
    /**
     * Menghapus item dari keranjang.
     */
    private void removeItem(CartEntry entry) {
        cartItems.remove(entry);
        updateCartDisplay();
    }
    
    /**
     * Menghitung total harga keranjang.
     */
    private double calculateTotal() {
        double subtotal = cartItems.stream()
                                   .mapToDouble(CartEntry::getTotalPrice)
                                   .sum();
        double tax = subtotal * 0.10; // Pajak 10%
        
        // Update Ringkasan Harga
        subtotalLabel.setText(currencyFormat.format(subtotal));
        taxLabel.setText(currencyFormat.format(tax));
        totalLabel.setText(currencyFormat.format(subtotal + tax));
        
        return subtotal + tax;
    }
    
    /**
     * Memperbarui tampilan KeranjangPanel berdasarkan data cartItems.
     */
    public void updateCartDisplay() {
        cartListPanel.removeAll();
        
        if (cartItems.isEmpty()) {
            // Tampilan keranjang kosong
            JPanel emptyPanel = new JPanel(new GridBagLayout());
            emptyPanel.setOpaque(false);
            JLabel emptyLabel = new JLabel("Keranjang Anda kosong. Ayo pesan makanan!");
            emptyLabel.setFont(new Font("Inter", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyPanel.add(emptyLabel);
            emptyPanel.setPreferredSize(new Dimension(300, 300));
            
            cartListPanel.add(emptyPanel);
            
            // Non-aktifkan tombol checkout
            checkoutButton.setEnabled(false);
        } else {
            // Tampilan daftar item
            cartItems.forEach(entry -> cartListPanel.add(createCartItemRow(entry)));
            checkoutButton.setEnabled(true);
        }

        int totalItems = cartItems.stream().mapToInt(e -> e.quantity).sum();
        itemCountLabel.setText(totalItems + " item dalam keranjang");
        
        calculateTotal(); // Hitung dan perbarui ringkasan harga
        
        cartListPanel.revalidate();
        cartListPanel.repaint();
    }

    
    // UI kustom untuk JLabel (Digunakan untuk Gambar Item agar Rounded)
    private static class RoundedLabelUI extends javax.swing.plaf.basic.BasicLabelUI {
        private final int radius;
        RoundedLabelUI(int radius) { this.radius = radius; }
        
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(c.getBackground());
            g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), radius, radius);
            
            // Kliping untuk memastikan icon/text juga rounded
            RoundRectangle2D rect = new RoundRectangle2D.Double(0, 0, c.getWidth(), c.getHeight(), radius, radius);
            g2d.clip(rect);

            super.paint(g2d, c);
            g2d.dispose();
        }
    }
    
    // UI kustom untuk JButton 
    private static class RoundedButtonUI extends BasicButtonUI {
        private final int radius;
        RoundedButtonUI(int radius) { this.radius = radius; }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            c.setOpaque(false); 
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton button = (AbstractButton) c;
            paintBackground(g, button, button.getModel().isPressed() ? 1 : 0);
            super.paint(g, c);
        }

        private void paintBackground(Graphics g, JComponent c, int yOffset) {
            Dimension size = c.getSize();
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(c.getBackground());
            g2d.fillRoundRect(0, yOffset, size.width, size.height - yOffset, radius, radius);
            g2d.dispose();
        }
    }
    
    // UI kustom untuk JPanel 
    private static class RoundedPanelUI extends PanelUI {
        private final int radius;
        RoundedPanelUI(int radius) { this.radius = radius; }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = c.getWidth();
            int h = c.getHeight();
            
            // Menggambar Background Utama
            g2d.setColor(c.getBackground());
            g2d.fillRoundRect(0, 0, w, h, radius, radius);

            g2d.dispose();
            super.paint(g, c);
        }
    }
}
