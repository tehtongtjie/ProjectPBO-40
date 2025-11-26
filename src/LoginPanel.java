import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class LoginPanel extends JPanel {
    private Main mainFrame;
    private CardLayout formCardLayout = new CardLayout();
    private JPanel formContainer = new JPanel(formCardLayout); 

    // Komponen UI
    private JTextField loginUsernameField; 
    private JPasswordField loginPasswordField;
    private JButton loginTabButton;

    private JTextField regNamaField;
    private JTextField regEmailField;
    private JPasswordField regPasswordField;
    private JButton registerTabButton;
    private JButton daftarButton;
    private JButton loginSubmitButton; 

    public LoginPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout()); 
        
        // --- Panel Konten Utama (Untuk Sudut Bulat) ---
        JPanel contentArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 30, 30)); 
                g2.dispose();
            }
            @Override
            public Dimension getPreferredSize() { 
                return new Dimension(450, 550); // <-- PERBAIKAN: Tinggi contentArea ditambah sedikit
            }
            @Override
            public Dimension getMaximumSize() { 
                return getPreferredSize();
            }
        };
        contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.Y_AXIS));
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(new EmptyBorder(30, 40, 30, 40)); 

        // --- Header (Logo & Title) ---
        JLabel logo = new JLabel("👨‍🍳", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 48));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
       
        JLabel title = new JLabel("FoodOrder", SwingConstants.CENTER);
        title.setFont(new Font("Montserrat", Font.BOLD, 24)); 
        title.setForeground(new Color(255, 100, 0)); 
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Pesan makanan favoritmu dengan mudah", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        // PERBAIKAN PADDING SUBTITLE: Ditingkatkan dari 20 menjadi 30
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0)); 

        // --- Tabs (Login / Register Button Group) ---
        JPanel tabPanel = createTabPanel(); 
        tabPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tabPanel.setOpaque(false); 

        // --- Form Container ---
        formContainer.setOpaque(false); 
        formContainer.add(createLoginForm(), "LOGIN");
        formContainer.add(createRegisterForm(), "REGISTER"); 
        // PERBAIKAN UKURAN CONTAINER: Ditingkatkan dari 400 menjadi 450 agar form Register muat
        formContainer.setMaximumSize(new Dimension(450, 450)); 
        
        formCardLayout.show(formContainer, "LOGIN");

        // --- Tambahkan Komponen ke Main Content ---
        contentArea.add(logo);
        contentArea.add(Box.createVerticalStrut(5));
        contentArea.add(title);
        contentArea.add(subtitle);
        contentArea.add(Box.createVerticalStrut(10));
        contentArea.add(tabPanel);
        contentArea.add(Box.createVerticalStrut(15)); 
        contentArea.add(formContainer);
        
        add(contentArea); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // PERBAIKAN: Pastikan menggunakan Graphics2D (dengan 'D' kapital)
        Graphics2D g2d = (Graphics2D) g.create(); 
        
        // Gradient background
        GradientPaint gp = new GradientPaint(0, 0, new Color(255, 100, 0), 
                                            getWidth(), getHeight(), new Color(255, 0, 100)); 
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

    // --- Tab Methods (Sama) ---
    private JPanel createTabPanel() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); 
        
        JPanel panel = new JPanel(new GridLayout(1, 2, 0, 0));
        panel.setPreferredSize(new Dimension(350, 45)); 
        panel.setOpaque(false);
        
        panel.setBorder(new RoundedBorder(new Color(220, 220, 220), 1, 25, false)); 
        
        loginTabButton = new JButton("Login");
        registerTabButton = new JButton("Register");
        
        styleTabButton(loginTabButton, true);
        styleTabButton(registerTabButton, false);

        loginTabButton.addActionListener(e -> {
            formCardLayout.show(formContainer, "LOGIN");
            styleTabButton(loginTabButton, true);
            styleTabButton(registerTabButton, false);
            clearRegisterFields();
        });

        registerTabButton.addActionListener(e -> {
            formCardLayout.show(formContainer, "REGISTER");
            styleTabButton(loginTabButton, false);
            styleTabButton(registerTabButton, true);
            clearLoginFields();
        });

        panel.add(loginTabButton);
        panel.add(registerTabButton);
        wrapper.add(panel);
        return wrapper;
    }

    private void styleTabButton(JButton button, boolean isActive) {
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 
        
        if (isActive) {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
            button.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 100, 0))); 
        } else {
            button.setBackground(new Color(245, 245, 245)); 
            button.setForeground(Color.GRAY);
            button.setBorder(null); 
        }
    }

    // --- Bagian Membuat Form Login (Sama) ---
    private JPanel createLoginForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false); 
        form.setBorder(new EmptyBorder(10, 0, 0, 0)); 

        loginUsernameField = new JTextField(20);
        loginPasswordField = new JPasswordField(20);
        
        styleTextField(loginUsernameField, "nama@email.com"); 
        styleTextField(loginPasswordField, "********"); 
        
        loginSubmitButton = new CustomGradientButton("LOGIN SEKARANG", new Color(255, 100, 0), new Color(255, 50, 0));
        styleSubmitButton(loginSubmitButton); 

        // Label Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        form.add(emailLabel);
        form.add(Box.createVerticalStrut(5));
        form.add(loginUsernameField);
        form.add(Box.createVerticalStrut(15));
        
        // Label Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        form.add(passwordLabel);
        form.add(Box.createVerticalStrut(5));
        form.add(loginPasswordField);
        form.add(Box.createVerticalStrut(30));
        form.add(loginSubmitButton);
        
        loginSubmitButton.addActionListener(e -> attemptLogin());
        return form;
    }
    
    // --- Bagian Membuat Form Register (Sama) ---
    private JPanel createRegisterForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false); 
        form.setBorder(new EmptyBorder(10, 0, 0, 0)); 

        regNamaField = new JTextField(20);
        regEmailField = new JTextField(20);
        regPasswordField = new JPasswordField(20);

        styleTextField(regNamaField, "Nama Lengkap");
        styleTextField(regEmailField, "nama@email.com");
        styleTextField(regPasswordField, "********");
        
        daftarButton = new CustomGradientButton("DAFTAR SEKARANG", new Color(255, 100, 0), new Color(255, 50, 0));
        styleSubmitButton(daftarButton); 

        // Label Nama Lengkap
        JLabel namaLabel = new JLabel("Nama Lengkap:");
        namaLabel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        form.add(namaLabel);
        form.add(Box.createVerticalStrut(5));
        form.add(regNamaField);
        form.add(Box.createVerticalStrut(15));
        
        // Label Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        form.add(emailLabel);
        form.add(Box.createVerticalStrut(5));
        form.add(regEmailField);
        form.add(Box.createVerticalStrut(15));
        
        // Label Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        form.add(passwordLabel);
        form.add(Box.createVerticalStrut(5));
        form.add(regPasswordField);
        // PERBAIKAN: Tambahkan spasi di bawah password agar tombol tidak terlalu mepet
        form.add(Box.createVerticalStrut(30)); 
        form.add(daftarButton);
        
        daftarButton.addActionListener(e -> attemptRegister());
        return form;
    }

    // --- Utility Methods untuk Styling (Sama) ---
    private void styleTextField(JTextField field, String placeholder) {
        field.setAlignmentX(Component.LEFT_ALIGNMENT); 
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); 
        field.setBorder(new RoundedBorder(new Color(220, 220, 220), 1, 15, true)); 
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(Color.BLACK);
        field.setBackground(new Color(250, 250, 250)); 
        
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder) || field.getForeground().equals(Color.GRAY)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void styleSubmitButton(JButton button) {
        button.setAlignmentX(Component.LEFT_ALIGNMENT); 
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Montserrat", Font.BOLD, 16));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
    }
    
    // --- Logika Bisnis & Clear Fields (Sama) ---
    private void attemptLogin() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());
        
        if (mainFrame.getDbHelper().login(username, password)) {
            JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat datang.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.showPanel(Main.HOME_VIEW); 
        } else {
            JOptionPane.showMessageDialog(this, "Username atau Password salah.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void attemptRegister() {
        String nama = regNamaField.getText();
        String email = regEmailField.getText();
        String password = new String(regPasswordField.getPassword());

        if (nama.equals("Nama Lengkap")) nama = "";
        if (email.equals("nama@email.com")) email = "";
        if (new String(regPasswordField.getPassword()).equals("********")) password = "";

        if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua kolom harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (mainFrame.getDbHelper().register(nama, email, password)) {
            JOptionPane.showMessageDialog(this, "Registrasi Berhasil! Silakan Login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loginTabButton.doClick(); 
        } else {
             JOptionPane.showMessageDialog(this, "Registrasi Gagal.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearLoginFields() {
        loginUsernameField.setText("nama@email.com"); 
        loginUsernameField.setForeground(Color.GRAY);
        loginPasswordField.setText("");
    }

    private void clearRegisterFields() {
        regNamaField.setText("Nama Lengkap");
        regNamaField.setForeground(Color.GRAY);
        regEmailField.setText("nama@email.com");
        regEmailField.setForeground(Color.GRAY);
        regPasswordField.setText("");
    }
}

// --- Kelas Kustom untuk Border Membulat (RoundedBorder) dan Tombol Gradient (CustomGradientButton) ---
// (Tambahkan kedua kelas ini di bawah kelas LoginPanel)

class RoundedBorder implements Border {
    private int radius;
    private Color color;
    private int thickness;
    private boolean isTextField; 

    public RoundedBorder(Color color, int thickness, int radius, boolean isTextField) {
        this.radius = radius;
        this.color = color;
        this.thickness = thickness;
        this.isTextField = isTextField;
    }

    public Insets getBorderInsets(Component c) {
        if (isTextField) {
            return new Insets(8, 12, 8, 12); 
        }
        return new Insets(this.radius/2 + 2, this.radius/2 + 2, this.radius/2 + 2, this.radius/2 + 2);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        
        g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
        g2.dispose();
    }
}

class CustomGradientButton extends JButton {
    private Color startColor;
    private Color endColor;
    private int radius = 15; 

    public CustomGradientButton(String text, Color startColor, Color endColor) {
        super(text);
        this.startColor = startColor;
        this.endColor = endColor;
        setContentAreaFilled(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        GradientPaint gp = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
        g2.setPaint(gp);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
        
        super.paintComponent(g); 
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Jangan menggambar border default Swing
    }
}