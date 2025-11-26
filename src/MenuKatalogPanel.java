import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Kelas yang menampilkan antarmuka katalog menu (Header, Banner, Kategori, dan Grid Katalog).
 * Kelas ini difokuskan untuk mereplikasi desain gambar yang diberikan seakurat mungkin.
 * Menggunakan teknik custom painting (paintComponent) untuk Gradien, Shadow, dan Rounded Corners.
 * * Perubahan: Logika penskalaan gambar diubah agar gambar selalu 'fit' di dalam frame 180px 
 * tanpa terpotong (menjaga aspek rasio).
 * * * PERHATIAN:
 * Untuk menggunakan aset gambar lokal, pastikan Anda memiliki folder 'assets' di akar
 * proyek Anda dan gambar-gambar berikut tersedia di dalamnya:
 * - ayam.jpg (digunakan untuk Nasi Goreng dan Sate Ayam)
 * - burger.jpg
 * - lava_cake.jpg
 * - jus_jeruk.jpg
 */
public class MenuKatalogPanel extends JPanel {

    // Ukuran frame gambar kartu menu
    private static final int IMAGE_WIDTH = 250;
    private static final int IMAGE_HEIGHT = 180;

    // Struktur data untuk Item Menu. imageUrl sekarang menunjuk ke jalur lokal.
    private record MenuItem(String name, String description, String price, String category, String imageUrl) {}

    // Data dummy yang diperbarui, menggunakan jalur relatif ke folder 'assets'
    private static final List<MenuItem> DUMMY_MENU = Arrays.asList(
        // Item 1: Nasi Goreng
        new MenuItem("Nasi Goreng Special", 
                     "Nasi goreng dengan telur, ayam, dan sayuran segar", 
                     "Rp 25.000", 
                     "Makanan Utama", 
                     "assets/ayam.jpg"), // Gunakan nama file yang sesuai
        // Item 2: Sate Ayam
        new MenuItem("Sate Ayam", 
                     "10 tusuk sate ayam dengan bumbu kacang dan lontong", 
                     "Rp 35.000", 
                     "Makanan Utama", 
                     "assets/ayam.jpg"), // Gunakan nama file yang sesuai
        // Item 3: Burger
        new MenuItem("Burger Beef Classic", 
                     "Burger daging sapi dengan keju, lettuce, dan saus spesial", 
                     "Rp 45.000", 
                     "Makanan Utama", 
                     "assets/burger.jpg"), // Gunakan nama file yang sesuai
        // Item 4: Dessert (Tambahan)
        new MenuItem("Chocolate Lava Cake", 
                     "Kue cokelat lezat dengan lelehan lava di dalamnya.", 
                     "Rp 25.000", 
                     "Dessert", 
                     "assets/lava_cake.jpg"), // Gunakan nama file yang sesuai
        // Item 5: Minuman (Tambahan)
        new MenuItem("Jus Jeruk Murni", 
                     "Jus jeruk segar yang dibuat dari buah murni", 
                     "Rp 15.000", 
                     "Minuman", 
                     "assets/jus_jeruk.jpg") // Gunakan nama file yang sesuai
    );
    
    // Nama pengguna dummy
    private final String USERNAME = "Fadila Rahmania";
    
    // Komponen UI utama
    private JPanel catalogGrid; 
    private JPanel categoryPanel; 
    private String activeCategory = "Semua";

    // Konstanta Warna dan Gaya
    private static final Color PRIMARY_ORANGE = new Color(255, 100, 0); 
    private static final Color DARK_ORANGE = new Color(240, 50, 0);  
    private static final Color BACKGROUND_GRAY = new Color(245, 245, 245); 
    private static final Color BUTTON_ACTIVE_BG = new Color(30, 30, 30); // Hitam gelap untuk tombol 'Menu'
    private static final Color BUTTON_INACTIVE_BG = Color.WHITE;
    private static final Color CATEGORY_ACTIVE_BG = new Color(235, 235, 235); // Abu-abu muda untuk kategori aktif
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 20); // Shadow yang lebih tipis
    private static final int CORNER_RADIUS = 20;

    /**
     * Konstruktor untuk panel katalog menu.
     */
    public MenuKatalogPanel() {
        // Mengatur properti sistem untuk anti-aliasing
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_GRAY); 
        
        // JScrollPane agar katalog makanan bisa digulir
        JScrollPane scrollPane = new JScrollPane(createMainContentPanel());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        
        add(createHeader(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        filterMenu("Semua");
    }

    // --- 1. HEADER (Logo dan Navigasi) ---
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 50, 15, 50)); // Padding horizontal lebih besar

        // Kiri: Logo dan Info User
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        
        // Logo (Topi Koki Oranye, dibuat dengan paintComponent)
        JPanel logoContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(PRIMARY_ORANGE); 
                // Menggambar kotak oranye dengan sudut melengkung 10
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); 
                g2d.setColor(Color.WHITE);
                // Ikon Topi Koki (Unicode U+1F373 atau simbol custom)
                // Menggunakan simbol Unicode Topi Koki yang mirip gambar
                g2d.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
                g2d.drawString("\uD83C\uDF73", 5, 23); // Topi Koki Emoji
                g2d.dispose();
            }
        };
        logoContainer.setPreferredSize(new Dimension(30, 30));
        logoContainer.setOpaque(false);
        
        // Judul FoodOrder dan Subtitle Selamat datang
        JPanel titlePanel = new JPanel(new BorderLayout(0, 0));
        titlePanel.setOpaque(false);
        
        JLabel title = new JLabel("FoodOrder");
        title.setFont(new Font("Inter", Font.BOLD, 16));
        title.setForeground(PRIMARY_ORANGE); 
        
        JLabel subtitle = new JLabel("Selamat datang, " + this.USERNAME);
        subtitle.setFont(new Font("Inter", Font.PLAIN, 10));
        subtitle.setForeground(Color.GRAY);
        
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        
        leftPanel.add(logoContainer);
        leftPanel.add(titlePanel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Kanan: Navigasi Bar
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        // Menggunakan ikon Unicode yang lebih umum
        rightPanel.add(createNavButton("Menu", "\u2302", true, false));       // Home/Menu (Rumah) - Aktif
        rightPanel.add(createNavButton("Pesanan Saya", "\uD83D\uDCCB", false, false)); // Clipboard
        rightPanel.add(createNavButton("Keranjang", "\uD83D\uDED2", false, true)); // Shopping Cart (Berborder)
        rightPanel.add(createNavButton("Logout", "\u21E8", false, false));           // Arrow (Logout)
        
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }
    
    // Helper untuk tombol navigasi
    private JButton createNavButton(String text, String icon, boolean isActive, boolean hasBorder) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Inter", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20)); 
        
        if (isActive) {
            // Gaya Tombol Aktif (Menu): Hitam dengan sudut melengkung
            button.setBackground(BUTTON_ACTIVE_BG); 
            button.setForeground(Color.WHITE); 
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BUTTON_ACTIVE_BG, 1, true), 
                new EmptyBorder(10, 20, 10, 20)
            ));
        } else if (hasBorder) {
            // Gaya Keranjang: Putih dengan border abu-abu dan sudut melengkung
            button.setBackground(BUTTON_INACTIVE_BG); 
            button.setForeground(Color.BLACK);
            button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true), 
                new EmptyBorder(10, 20, 10, 20)
            ));
        } else {
            // Gaya pasif lainnya: Transparan
            button.setContentAreaFilled(false); 
            button.setBorderPainted(false);
            button.setForeground(Color.BLACK);
            button.setOpaque(false);
            button.setBorder(new EmptyBorder(0, 20, 10, 20));
        }
        
        return button;
    }

    // --- 2. KONTEN UTAMA (Banner, Kategori, Katalog) ---
    private JPanel createMainContentPanel() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(10, 50, 50, 50)); 
        
        mainContent.add(createBanner());
        mainContent.add(Box.createRigidArea(new Dimension(0, 30)));
        categoryPanel = createCategoryBar(); 
        mainContent.add(categoryPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 30))); // Jarak kategori dan grid
        
        // Panel Grid Katalog (tempat menu ditampilkan)
        catalogGrid = new JPanel(new GridLayout(0, 3, 30, 30)); 
        catalogGrid.setOpaque(false);
        catalogGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(catalogGrid);
        
        mainContent.add(Box.createVerticalGlue()); 
        
        return mainContent;
    }

    // --- Banner dan Search Bar (Rounded Corner, Gradien, dan Shadow) ---
    private JPanel createBanner() {
        JPanel banner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // 1. Menggambar Gradien Orange-Red
                // Gradien dari Oranye ke Merah Oranye (diagonal)
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_ORANGE, w, h, DARK_ORANGE.darker());
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, w, h, CORNER_RADIUS, CORNER_RADIUS); 
                
                g2d.dispose();
                super.paintComponent(g); // Pastikan komponen anak tergambar
            }
            
            // Menggambar shadow custom di sini
             @Override
             protected void paintBorder(Graphics g) {
                 Graphics2D g2d = (Graphics2D) g.create();
                 g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 
                 // Menggambar shadow di bagian bawah, mirip CSS box-shadow: 0 5px 15px rgba(0,0,0,.2);
                 // Area Shadow di bawah kotak
                 RoundRectangle2D shadowRect = new RoundRectangle2D.Double(
                     5, 5, getWidth() - 10, getHeight() - 5, 
                     CORNER_RADIUS, CORNER_RADIUS); 
                 
                 g2d.setColor(new Color(0, 0, 0, 30)); // Warna shadow
                 g2d.translate(0, 5); // Geser ke bawah sedikit
                 g2d.fill(shadowRect);

                 g2d.dispose();
             }
        }; 
        
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        banner.setBorder(new EmptyBorder(40, 40, 40, 40));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        banner.setOpaque(false); // Penting agar paintComponent berfungsi

        JLabel welcomeTitle = new JLabel("Selamat Datang di FoodOrder");
        welcomeTitle.setFont(new Font("Inter", Font.BOLD, 30));
        welcomeTitle.setForeground(Color.WHITE);
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeSubtitle = new JLabel("Pilih makanan favoritmu dan pesan dengan mudah");
        welcomeSubtitle.setFont(new Font("Inter", Font.PLAIN, 14));
        welcomeSubtitle.setForeground(Color.WHITE);
        welcomeSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Search Bar Container (Rounded White)
        JPanel searchContainer = new JPanel(new BorderLayout(10, 0));
        searchContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Lebih tinggi sedikit
        searchContainer.setBackground(Color.WHITE);
        searchContainer.setBorder(BorderFactory.createCompoundBorder(
            // Border untuk simulasi sudut melengkung pada container
            new LineBorder(Color.WHITE, 4, true), 
            new EmptyBorder(0, 0, 0, 0) 
        ));
        
        JTextField searchField = new JTextField(" Cari makanan...");
        searchField.setFont(new Font("Inter", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        
        JLabel searchIcon = new JLabel(" \uD83D\uDD0D "); // Kaca Pembesar
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        
        searchContainer.add(searchIcon, BorderLayout.WEST);
        searchContainer.add(searchField, BorderLayout.CENTER);
        
        // Memastikan sudut search bar melengkung sempurna
        searchContainer.setUI(new RoundedPanelUI(CORNER_RADIUS));


        banner.add(welcomeTitle);
        banner.add(Box.createRigidArea(new Dimension(0, 5)));
        banner.add(welcomeSubtitle);
        banner.add(Box.createRigidArea(new Dimension(0, 20)));
        banner.add(searchContainer);

        return banner;
    }

    // --- Klasifikasi Menu (Kategori Bar) ---
    private JPanel createCategoryBar() {
        // Menggunakan Grid Layout untuk 4 kolom
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] categories = {"Semua", "Makanan Utama", "Dessert", "Minuman"};
        for (String cat : categories) {
            JButton btn = createCategoryButton(cat);
            panel.add(btn);
        }
        
        return panel;
    }
    
    // Helper untuk tombol kategori
    private JButton createCategoryButton(String category) {
        JButton button = new JButton(category);
        button.setFont(new Font("Inter", Font.BOLD, 14)); 
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        // Sudut melengkung, radius 10
        int radius = 10;
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true), 
            new EmptyBorder(12, 15, 12, 15) 
        ));
        button.setUI(new RoundedButtonUI(radius)); // Set UI kustom untuk tombol
        
        // Aksi saat kategori di-klik
        button.addActionListener(e -> {
            activeCategory = category;
            filterMenu(category);
            updateCategoryButtonStyles();
        });
        
        // Set style awal
        if (category.equals(activeCategory)) {
            button.setBackground(CATEGORY_ACTIVE_BG); 
            button.setForeground(Color.BLACK);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
        }
        
        return button;
    }
    
    // Helper untuk memperbarui gaya tombol kategori
    private void updateCategoryButtonStyles() {
        if (categoryPanel != null) {
            for (Component comp : categoryPanel.getComponents()) {
                if (comp instanceof JButton btn) {
                    if (btn.getText().equals(activeCategory)) {
                        btn.setBackground(CATEGORY_ACTIVE_BG); 
                        btn.setForeground(Color.BLACK);
                    } else {
                        btn.setBackground(Color.WHITE);
                        btn.setForeground(Color.BLACK);
                    }
                }
            }
        }
    }

    // --- Katalog Makanan (Grid 3 Kolom) ---
    private void filterMenu(String category) {
        catalogGrid.removeAll();
        
        List<MenuItem> filteredList = DUMMY_MENU.stream()
            .filter(item -> category.equals("Semua") || item.category().equals(category))
            .toList();
            
        for (MenuItem item : filteredList) {
            catalogGrid.add(createMenuItemCard(item));
        }

        if (filteredList.isEmpty()) {
            // Jika kosong, gunakan FlowLayout agar pesan berada di tengah
            catalogGrid.setLayout(new FlowLayout(FlowLayout.CENTER)); 
            catalogGrid.add(new JLabel("Tidak ada menu yang tersedia di kategori ini."));
        } else {
            // Jika ada isinya, kembali ke GridLayout 3 kolom
            catalogGrid.setLayout(new GridLayout(0, 3, 30, 30)); 
        }

        catalogGrid.revalidate();
        catalogGrid.repaint();
    }
    
    /**
     * Method untuk membuat kartu menu individu.
     */
    private JPanel createMenuItemCard(MenuItem item) {
        // Menggunakan ukuran kartu menu 250x350
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(IMAGE_WIDTH, 350)); 
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Menggunakan UI Kustom untuk Kartu (Shadow dan Rounded Corner 15)
        card.setUI(new RoundedPanelUI(15)); 
        
        // 1. ATAS: Gambar dan Tag Kategori (Menggunakan JLayeredPane)
        JLayeredPane layeredPane = createCardImageLayer(item);
        card.add(layeredPane, BorderLayout.NORTH);
        
        // 2. BAWAH: Detail Produk (Nama, Deskripsi, Harga, Tombol)
        
        // Panel untuk Konten Teks (Nama dan Deskripsi) - Diposisikan di tengah
        JPanel textContentPanel = new JPanel();
        // Menggunakan BoxLayout Y-Axis untuk menumpuk Name dan Desc
        textContentPanel.setLayout(new BoxLayout(textContentPanel, BoxLayout.Y_AXIS)); 
        textContentPanel.setBorder(new EmptyBorder(15, 15, 20, 15));
        textContentPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(item.name(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Inter", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Pusatkan teks
        
        JLabel descLabel = new JLabel(item.description(), SwingConstants.CENTER);
        descLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY.darker());
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Pusatkan teks
        
        textContentPanel.add(nameLabel);
        textContentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textContentPanel.add(descLabel);
        textContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        
        // Panel Footer untuk Harga dan Tombol (sejajar)
        JPanel footerPanel = new JPanel(new BorderLayout(5, 0));
        footerPanel.setBorder(new EmptyBorder(0, 20, 15, 15)); // Padding ke bawah
        footerPanel.setOpaque(false);
        
        JLabel priceLabel = new JLabel(item.price()); 
        priceLabel.setFont(new Font("Inter", Font.BOLD, 18));
        priceLabel.setForeground(DARK_ORANGE);
        
        JButton addButton = new JButton("\u2795 Tambah");
        addButton.setFont(new Font("Inter", Font.BOLD, 12));
        addButton.setBackground(PRIMARY_ORANGE);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(new EmptyBorder(5, 10, 5, 20));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setUI(new RoundedButtonUI(10)); // Sudut melengkung

        
        // Menggunakan FlowLayout untuk Price agar berada di kiri footer
        JPanel priceWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        priceWrapper.setOpaque(false);
        priceWrapper.add(priceLabel);
        
        // Menggunakan FlowLayout untuk Button agar berada di kanan footer
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(addButton);
        
        footerPanel.add(priceWrapper, BorderLayout.WEST);
        footerPanel.add(buttonWrapper, BorderLayout.EAST);
        
        // Menggabungkan Text Content dan Footer (Harga/Tombol)
        // Menggunakan BorderLayout lagi untuk memisahkan body text dan footer
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setOpaque(false);
        
        // Text Content diletakkan di tengah (CENTER)
        bodyPanel.add(textContentPanel, BorderLayout.CENTER); 
        // Footer (Harga/Tombol) diletakkan di bawah (SOUTH)
        bodyPanel.add(footerPanel, BorderLayout.SOUTH);
        
        card.add(bodyPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    // Helper untuk membuat Layer Gambar Kartu Menu (dengan Tag Kategori)
    private JLayeredPane createCardImageLayer(MenuItem item) {
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setBackground(Color.LIGHT_GRAY); // Warna latar belakang jika gambar lebih kecil
        imageLabel.setOpaque(true); 
        
        // ----------------------------------------------------
        // PERUBAHAN UTAMA: Memuat gambar dengan penskalaan Aspect Fit
        // ----------------------------------------------------
        
        // Mendapatkan URL aset dari classpath (diasumsikan folder 'assets' ada)
        URL imageUrl = getClass().getResource(item.imageUrl());
        
        if (imageUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            Image originalImage = originalIcon.getImage();
            
            int originalWidth = originalImage.getWidth(null);
            int originalHeight = originalImage.getHeight(null);
            
            // Hitung rasio untuk memastikan gambar fit di dalam frame 250x180
            double widthRatio = (double) IMAGE_WIDTH / originalWidth;
            double heightRatio = (double) IMAGE_HEIGHT / originalHeight;
            
            // Pilih rasio yang lebih kecil untuk memastikan gambar fit (aspect fit)
            double ratio = Math.min(widthRatio, heightRatio);
            
            int newWidth = (int) (originalWidth * ratio);
            int newHeight = (int) (originalHeight * ratio);
            
            // Penskalaan gambar
            Image scaledImage = originalImage.getScaledInstance(
                newWidth, newHeight, Image.SCALE_SMOOTH); 
            ImageIcon icon = new ImageIcon(scaledImage);

            imageLabel.setIcon(icon); 
            // Tetapkan ukuran label agar sama dengan ukuran frame (250x180), bukan ukuran gambar yang diskalakan
            imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
            
        } else {
            // Placeholder jika gambar tidak ditemukan
            imageLabel.setText("Gambar Tidak Ditemukan: " + item.imageUrl());
            imageLabel.setFont(new Font("Inter", Font.PLAIN, 10));
            imageLabel.setBackground(new Color(250, 230, 230));
            imageLabel.setOpaque(true);
            imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
            System.err.println("Gagal memuat gambar: " + item.imageUrl() + ". Pastikan folder 'assets' dan file ada.");
        }


        // Panel Pembungkus Gambar untuk Masking Sudut Atas
        JPanel imageWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                // Jangan panggil super.paintComponent(g) di sini karena kita ingin melakukan kliping.
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Klip hanya pada area sudut atas untuk efek border radius 15
                int arc = 15;
                // Menggunakan RoundRectangle2D untuk kliping yang tepat. 
                // Tinggi harus mencakup keseluruhan wrapper, dan lengkungan di bawah diabaikan.
                RoundRectangle2D rect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc); 
                g2d.clip(rect);
                
                // Pastikan label gambar mengisi seluruh wrapper untuk kliping yang tepat
                imageLabel.setSize(this.getSize());
                imageLabel.paint(g2d);

                g2d.dispose();
            }
        };
        imageWrapper.setOpaque(false);
        imageWrapper.add(imageLabel, BorderLayout.CENTER);
        imageWrapper.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        
        // Tag Kategori (Di sudut kanan atas, menggunakan JLayeredPane agar di atas gambar)
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        
        // Menambahkan imageWrapper
        imageWrapper.setBounds(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT); 
        layeredPane.add(imageWrapper, JLayeredPane.DEFAULT_LAYER);
        
        // Tag Kategori (Kotak Merah Kecil)
        JLabel categoryTag = new JLabel(item.category(), SwingConstants.CENTER);
        categoryTag.setBackground(new Color(220, 50, 0)); // Merah oranye
        categoryTag.setForeground(Color.WHITE);
        categoryTag.setFont(new Font("Inter", Font.BOLD, 10));
        categoryTag.setOpaque(true);
        categoryTag.setBorder(new EmptyBorder(4, 8, 4, 8));
        categoryTag.setUI(new RoundedLabelUI(8)); 
        
        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15)); // Jarak dari tepi gambar
        tagPanel.setOpaque(false);
        tagPanel.add(categoryTag);
        
        // Menambahkan tagPanel
        tagPanel.setBounds(0, 0, IMAGE_WIDTH, 50); 
        layeredPane.add(tagPanel, JLayeredPane.PALETTE_LAYER);
        
        return layeredPane;
    }


    // --- CUSTOM UI UNTUK ROUNDED CORNERS DAN SHADOW ---
    
    // UI kustom untuk JLabel (Tag Kategori)
    private static class RoundedLabelUI extends javax.swing.plaf.basic.BasicLabelUI {
        private final int radius;
        RoundedLabelUI(int radius) { this.radius = radius; }
        
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(c.getBackground());
            g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), radius, radius);
            
            super.paint(g2d, c);
            g2d.dispose();
        }
    }
    
    // UI kustom untuk JButton (Kategori dan Tambah)
    private static class RoundedButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
        private final int radius;
        RoundedButtonUI(int radius) { this.radius = radius; }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            c.setOpaque(false);
            c.setBorder(new EmptyBorder(10, 20, 10, 20)); 
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton button = (AbstractButton) c;
            paintBackground(g, button, button.getModel().isPressed() ? 2 : 0);
            super.paint(g, c);
        }

        private void paintBackground(Graphics g, JComponent c, int yOffset) {
            Dimension size = c.getSize();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(c.getBackground());
            g2d.fillRoundRect(0, yOffset, size.width, size.height - yOffset, radius, radius);
        }
    }
    
    // UI kustom untuk JPanel (Banner dan Kartu Menu) - Menggambar Rounded Corner dan Shadow
    private static class RoundedPanelUI extends javax.swing.plaf.PanelUI {
        private final int radius;
        private final boolean drawShadow;

        RoundedPanelUI(int radius) {
            this.radius = radius;
            this.drawShadow = (radius == 15); // Hanya kartu menu yang ada shadow
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = c.getWidth();
            int h = c.getHeight();
            
            // 1. Menggambar Shadow (untuk kartu menu)
            if (drawShadow) {
                g2d.setColor(SHADOW_COLOR); 
                // Shadow di bawah dan kanan
                g2d.fillRoundRect(5, 5, w - 10, h - 10, radius, radius); 
            }
            
            // 2. Menggambar Background Putih (Konten)
            g2d.setColor(c.getBackground());
            g2d.fillRoundRect(0, 0, w, h, radius, radius);
            
            g2d.dispose();
            super.paint(g, c);
        }
    }

    // --- ENTRY POINT (Untuk menjalankan panel ini) ---
    public static void main(String[] args) {
        try {
            // Set Look and Feel yang lebih modern (System L&F)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JFrame frame = new JFrame("FoodOrder - Katalog Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Mengatur ukuran agar lebih mirip tampilan web (maksimal)
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setLocationRelativeTo(null);
        
        frame.add(new MenuKatalogPanel()); 
        frame.setVisible(true);
    }
}
