import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MenuGUI extends JFrame {

    private static final Color BANNER_BG_COLOR = new Color(36, 89, 59); 
    private static final Color BANNER_ACCENT_COLOR = new Color(255, 215, 0); 

    private List<MenuItem> allMenuItems = new ArrayList<>();
    private JPanel menuPanel = new JPanel();
    private JTextField searchField;

    public MenuGUI() {
        setTitle("QuickServe - Menu Makanan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 850); 
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        initializeMenuData();
        add(createHeaderPanel(), BorderLayout.NORTH);
        JPanel mainCenterContainer = new JPanel(new BorderLayout()); 
        mainCenterContainer.setBackground(Theme.BACKGROUND_COLOR);

        JPanel bannerPanel = createBannerPanel();
        mainCenterContainer.add(bannerPanel, BorderLayout.NORTH);

        JScrollPane menuScrollPane = createMenuPanel();
        mainCenterContainer.add(menuScrollPane, BorderLayout.CENTER);

        add(mainCenterContainer, BorderLayout.CENTER);
        filterMenu("");
        setVisible(true);
    }


    private JPanel createMenuItemCard(MenuItem item) {
        int cardWidth = 250; 
        int cardHeight = 280; 
        int cornerRadius = 20; 

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.CARD_BACKGROUND);
        card.setPreferredSize(new Dimension(cardWidth, cardHeight));
        card.setBorder(new RoundedCornerBorder(cornerRadius)); 
        
        int imageHeight = 140;
        ImageRounderPanel imageRounderPanel = new ImageRounderPanel(cornerRadius, true, true, false, false);
        imageRounderPanel.setPreferredSize(new Dimension(cardWidth, imageHeight));
        imageRounderPanel.setBackground(Theme.CARD_BACKGROUND);

        try {
            ImageIcon originalIcon = new ImageIcon(item.getImageUrl());
            if (originalIcon.getIconWidth() > 0) {
                Image scaled = originalIcon.getImage().getScaledInstance(cardWidth, imageHeight, Image.SCALE_SMOOTH);
                imageRounderPanel.setImage(new ImageIcon(scaled));
                card.add(imageRounderPanel, BorderLayout.NORTH); 
            } else {
                throw new Exception("Image not found");
            }
        } catch (Exception e) {
            JLabel imagePlaceholder = new JLabel("<html><center style='color:gray'>Gambar<br>" + item.getName() + "</center></html>",
                                     SwingConstants.CENTER);
            imagePlaceholder.setOpaque(true);
            imagePlaceholder.setBackground(Theme.BACKGROUND_COLOR);
            imagePlaceholder.setPreferredSize(new Dimension(cardWidth, imageHeight));
            card.add(imagePlaceholder, BorderLayout.NORTH);
        }
        
        JPanel detailContainer = new JPanel(new BorderLayout());
        detailContainer.setOpaque(false);
        detailContainer.setBorder(new EmptyBorder(10, 15, 10, 15)); 
        
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        namePanel.setOpaque(false);
        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18)); 
        nameLabel.setForeground(Theme.TEXT_DARK);
        namePanel.add(nameLabel);
        
        detailContainer.add(namePanel, BorderLayout.NORTH);
        
        JPanel priceAndButtonPanel = new JPanel(new BorderLayout());
        priceAndButtonPanel.setOpaque(false);
        priceAndButtonPanel.setBorder(new EmptyBorder(0, 0, 0, 0)); 

        String formattedPrice = String.format("Rp %,d", (int)item.getPrice()).replace(",", ".");
        JLabel priceLabel = new JLabel(formattedPrice);
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 18)); 
        priceLabel.setForeground(Theme.TEXT_DARK); 
        priceAndButtonPanel.add(priceLabel, BorderLayout.WEST);

        JButton addButton = createAddButton(Theme.RED_ADD_BUTTON, 30); 
        priceAndButtonPanel.add(addButton, BorderLayout.EAST);
        
        detailContainer.add(priceAndButtonPanel, BorderLayout.SOUTH);
        card.add(detailContainer, BorderLayout.CENTER);    
        return card;
    }

    private String getCustomDescription(String itemName) {
        return "Deskripsi tidak ditampilkan";
    }

    private JPanel createBannerPanel() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(BANNER_BG_COLOR); 
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));
        banner.setPreferredSize(new Dimension(1200, 230));
        
        banner.setBorder(new EmptyBorder(30, 40, 25, 40));

        JPanel leftTextPanel = new JPanel();
        leftTextPanel.setLayout(new BoxLayout(leftTextPanel, BoxLayout.Y_AXIS));
        leftTextPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("<html><div style='font-family:Sans-serif;'>"
                + "<span style='font-size:26px; color:white; font-weight:bold;'>Pedasnya Nendang, <br></span>"
                + "<span style='font-size:26px; color:white; font-weight:bold;'>Cita Rasa </span>"
                + "<span style='font-size:26px; color:#FFD700; font-weight:bold;'>Asli Lombok</span>"
                + "</div></html>");

        JLabel subTitleLabel = new JLabel("<html><div style='width:450px; color:#E0E0E0; font-size:13px; margin-top:10px;'>"
                + "Nikmati kelezatan Ayam Taliwang, Pelecing Kangkung, dan Sate Rembiga "
                + "yang diracik dengan bumbu rempah warisan leluhur."
                + "</div></html>");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0)); 
        leftTextPanel.add(titleLabel);
        leftTextPanel.add(subTitleLabel);
        leftTextPanel.add(buttonPanel);

        banner.add(leftTextPanel, BorderLayout.WEST);

        JComponent rightImagePanel = new JComponent() {
            ImageIcon burgerIcon = new ImageIcon("Assets/Ayam.jpg"); 

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();

                if (burgerIcon.getIconWidth() > 0) {
                    Image img = burgerIcon.getImage();
                    int imgH = 180; 
                    int imgW = (int) (((double)imgH / burgerIcon.getIconHeight()) * burgerIcon.getIconWidth());
                    g2.drawImage(img, width - imgW - 50, (height - imgH) / 2, imgW, imgH, this);
                } else {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(width - 300, 10, 280, 200, 20, 20);
                }
                
                g2.dispose();
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(400, 230);
            }
        };
        banner.add(rightImagePanel, BorderLayout.EAST);
        return banner;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0)); 
        headerPanel.setBackground(Color.WHITE); 
    
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR),
            new EmptyBorder(30, 30, 10, 30)
        ));
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        leftPanel.setOpaque(false);

        JLabel appTitle = new JLabel("Rumah Makan Sasak");
        appTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        appTitle.setForeground(Theme.TEXT_DARK);
        
        leftPanel.add(appTitle);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        JPanel searchContainerBar = new JPanel(new BorderLayout(10, 0));
        searchContainerBar.setBackground(Theme.SEARCH_BG_COLOR);
        
        searchContainerBar.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(Theme.SEARCH_BG_COLOR, 25), 
            new EmptyBorder(0, 15, 0, 15) 
        ));
        searchContainerBar.setPreferredSize(new Dimension(650, 45));
        searchContainerBar.setMaximumSize(new Dimension(600, 45));
        
        JLabel searchIconLbl = new JLabel("🔍");
        searchIconLbl.setForeground(Color.GRAY);
        searchContainerBar.add(searchIconLbl, BorderLayout.WEST);

        searchField = new JTextField();
        searchField.setBorder(null); 
        searchField.setOpaque(false); 
        searchField.setFont(Theme.FONT_BODY);
        
        String placeholderText = "cari makanan";
        searchField.setForeground(Color.GRAY);
        searchField.setText(placeholderText);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals(placeholderText)) {
                    searchField.setText("");
                    searchField.setForeground(Theme.TEXT_DARK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText(placeholderText);
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!searchField.getText().equals(placeholderText)) {
                    filterMenu(searchField.getText());
                }
            }
        });
        
        searchContainerBar.add(searchField, BorderLayout.CENTER);
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(searchContainerBar);
        headerPanel.add(centerWrapper, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        JLabel cartIcon = new JLabel("🛒");
        cartIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        cartIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel profileIcon = new JLabel("👤");
        profileIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        profileIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rightPanel.add(cartIcon);
        rightPanel.add(profileIcon);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JScrollPane createMenuPanel() {
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); 
        menuPanel.setBorder(new EmptyBorder(5, 20, 15, 20));
        menuPanel.setBackground(Theme.BACKGROUND_COLOR);
    
        menuPanel.setPreferredSize(new Dimension(70, 70)); 
        JScrollPane menuScrollPane = new JScrollPane(menuPanel);
        menuScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        menuScrollPane.setBorder(BorderFactory.createEmptyBorder());
        menuScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        return menuScrollPane;
    }

    private void initializeMenuData() {
        allMenuItems.add(new MenuItem("Ayam Taliwang", 35000, 4.8, "Assets/Ayam.jpg"));
        allMenuItems.add(new MenuItem("Pelecing Kangkung", 15000, 4.5, "Assets/Pelecing.jpg"));
        allMenuItems.add(new MenuItem("Sate Rembiga", 40000, 4.9, "Assets/Sate.jpg"));
        allMenuItems.add(new MenuItem("Nasi Balap Puyung", 25000, 4.7, "Assets/Puyung.jpg"));
        allMenuItems.add(new MenuItem("Beberuk Terong", 12000, 4.4, "Assets/Ayam.jpg"));
        allMenuItems.add(new MenuItem("Sayur Ares", 18000, 4.3, "Assets/Ayam.jpg"));
        allMenuItems.add(new MenuItem("Cilok Kuah", 10000, 4.0, "Assets/Ayam.jpg"));
        allMenuItems.add(new MenuItem("Mie Ayam Ceker", 22000, 4.6, "Assets/Ayam.jpg"));
        allMenuItems.add(new MenuItem("Sop Buntut", 55000, 4.9, "Assets/Ayam.jpg"));
        allMenuItems.add(new MenuItem("Es Kelapa Muda", 10000, 4.5, "Assets/Ayam.jpg"));
        allMenuItems.add(new MenuItem("Kopi Sasak", 14000, 4.7, "Assets/Ayam.jpg"));
        allMenuItems.add(new MenuItem("Jus Jeruk", 13000, 4.2, "Assets/Ayam.jpg"));
    }

    private void filterMenu(String query) {
        menuPanel.removeAll();
        String lowerCaseQuery = query.toLowerCase();

        for (MenuItem item : allMenuItems) {
            if (item.getName().toLowerCase().contains(lowerCaseQuery)) {
                menuPanel.add(createMenuItemCard(item));
            }
        }
        if (menuPanel.getComponentCount() == 0) {
            JLabel notFound = new JLabel("Menu tidak ditemukan.", SwingConstants.CENTER);
            notFound.setFont(Theme.FONT_HEADER);
            menuPanel.add(notFound);
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private JButton createAddButton(Color bgColor, int size) {
        JButton button = new JButton("+");
        button.setFont(new Font("SansSerif", Font.BOLD, 18)); 
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(size, size));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    // --- KELAS CUSTOM BORDER ---

    static class RoundedCornerBorder extends LineBorder {
        private int radius;

        public RoundedCornerBorder(int radius) {
            super(Theme.BORDER_COLOR, 1);
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(c.getBackground());
            g2.fillRoundRect(x, y, width, height, radius, radius);

            g2.setColor(getLineColor());
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            
            g2.dispose();
        }
    }

    static class ImageRounderPanel extends JPanel {
        private ImageIcon imageIcon;
        private int radius;
        private boolean roundTopLeft;
        private boolean roundTopRight;
        private boolean roundBottomLeft;
        private boolean roundBottomRight;

        public ImageRounderPanel(int radius, boolean roundTopLeft, boolean roundTopRight, boolean roundBottomLeft, boolean roundBottomRight) {
            this.radius = radius;
            this.roundTopLeft = roundTopLeft;
            this.roundTopRight = roundTopRight;
            this.roundBottomLeft = roundBottomLeft;
            this.roundBottomRight = roundBottomRight;
            setOpaque(false); 
        }

        public void setImage(ImageIcon imageIcon) {
            this.imageIcon = imageIcon;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imageIcon == null) return;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, width, height, radius, radius);
            Area area = new Area(roundRect);

            if (!roundTopLeft) {
                area.add(new Area(new Rectangle(0, 0, radius, radius)));
            }
            if (!roundTopRight) {
                area.add(new Area(new Rectangle(width - radius, 0, radius, radius)));
            }
            if (!roundBottomLeft) {
                area.add(new Area(new Rectangle(0, height - radius, radius, radius)));
            }
            if (!roundBottomRight) {
                area.add(new Area(new Rectangle(width - radius, height - radius, radius, radius)));
            }
            
            g2.setClip(area);

            Image img = imageIcon.getImage();
            g2.drawImage(img, 0, 0, width, height, this);

            g2.dispose();
        }
    }
    
    static class RoundedBorder extends LineBorder {
        private int radius;

        public RoundedBorder(Color color, int radius) {
            super(color, 1); 
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getLineColor());
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2 + 1, radius/2 + 1, radius/2 + 1, radius/2 + 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.bottom = insets.top = radius/2 + 1;
            return insets;
        }
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        SwingUtilities.invokeLater(() -> new MenuGUI());
    }
}
