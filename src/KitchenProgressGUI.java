import java.awt.*;
import java.net.URL;
import java.text.DecimalFormat;
import javax.swing.*; // Import untuk memuat gambar dari URL
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class KitchenProgressGUI extends JFrame {

    // Konstanta Waktu Simulasi
    private static final int ESTIMATION_MINUTES = 10;
    private static final int TOTAL_SIMULATION_SECONDS = ESTIMATION_MINUTES * 60;
    
    // Tema Warna
    private static final Color PRIMARY_GREEN = new Color(46, 125, 50); 
    private static final Color LIGHT_GREEN = new Color(102, 187, 106); 
    private static final Color ACCENT_YELLOW = new Color(255, 193, 7); 
    private static final Color ACCENT_RED = new Color(239, 83, 80); 
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33); 
    private static final Color TEXT_SECONDARY = new Color(117, 117, 117); 
    private static final Color BG_COLOR = Color.WHITE;

    // Font Assets
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28); // Font judul diperbesar sedikit
    private static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_TIMER = new Font("Segoe UI", Font.BOLD, 48); // Timer lebih besar
    private static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font FONT_SMALL = new Font("Segoe UI", Font.ITALIC, 13);

    private JLabel timeLabel;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    
    private int secondsElapsed = 0;
    private Thread progressThread;

    public KitchenProgressGUI() {
        setTitle("Kitchen Status");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500); 
        setMinimumSize(new Dimension(750, 450));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
        
        // Menggunakan GridLayout untuk membagi 2 kolom sama besar
        setLayout(new GridLayout(1, 2)); 

        // --- Bagian Kiri (Teks & Progress) ---
        JPanel leftPanel = createLeftProgressPanel();
        add(leftPanel);

        // --- Bagian Kanan (Gambar Dapur Full) ---
        JPanel rightPanel = createRightImagePanel();
        add(rightPanel);

        startProgressSimulation();
    }
    
    private JPanel createLeftProgressPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        // Padding (Top, Left, Bottom, Right)
        panel.setBorder(new EmptyBorder(80, 50, 80, 50)); 

        // 1. Judul Utama
        JLabel titleLabel = new JLabel("Status Pesanan Anda");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        
        panel.add(Box.createVerticalStrut(10));

        // 2. Sub-judul
        JLabel subTitleLabel = new JLabel("Waktu persiapan makanan di dapur:");
        subTitleLabel.setFont(FONT_SUBTITLE);
        subTitleLabel.setForeground(TEXT_SECONDARY);
        subTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subTitleLabel);
        
        panel.add(Box.createVerticalStrut(40));

        // 3. Timer
        timeLabel = new JLabel(formatTimeRemaining(TOTAL_SIMULATION_SECONDS));
        timeLabel.setFont(FONT_TIMER);
        timeLabel.setForeground(PRIMARY_GREEN);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(timeLabel);
        
        panel.add(Box.createVerticalStrut(30));

        // 4. Status Teks
        statusLabel = new JLabel("Pesanan sedang diproses...");
        statusLabel.setFont(FONT_BODY);
        statusLabel.setForeground(TEXT_PRIMARY);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(statusLabel);
        
        panel.add(Box.createVerticalStrut(15));

        // 5. Progress Bar
        progressBar = new JProgressBar(0, TOTAL_SIMULATION_SECONDS);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setForeground(LIGHT_GREEN);
        progressBar.setBackground(new Color(230, 230, 230)); 
        progressBar.setPreferredSize(new Dimension(300, 10)); 
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
        progressBar.setBorder(BorderFactory.createEmptyBorder());
        
        progressBar.setUI(new BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = progressBar.getWidth();
                int height = progressBar.getHeight();
                
                g2.setColor(progressBar.getBackground());
                g2.fillRoundRect(0, 0, width, height, height, height);
                
                int amountFull = getAmountFull(new Insets(0,0,0,0), width, height);
                g2.setColor(progressBar.getForeground());
                if (amountFull > 0) {
                     g2.fillRoundRect(0, 0, amountFull, height, height, height);
                }
                g2.dispose();
            }
        });
        
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(progressBar);
        
        panel.add(Box.createVerticalStrut(20));
        
        // 6. Footer
        JLabel infoLabel = new JLabel("Kami akan memberi tahu saat pesanan siap.");
        infoLabel.setFont(FONT_SMALL);
        infoLabel.setForeground(TEXT_SECONDARY);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(infoLabel);

        panel.add(Box.createVerticalGlue());

        return panel;
    }
    
    // --- BAGIAN YANG DIUBAH: Panel Kanan dengan Gambar Background ---
    private JPanel createRightImagePanel() {
        // URL Gambar Dapur (Placeholder berkualitas tinggi)
        // Anda bisa mengganti string URL ini dengan path file lokal: "Assets/dapur.jpg"
        String kitchenImageUrl = "Assets/penyet.jpg";
        
        // Menggunakan kelas SideImagePanel baru
        SideImagePanel panel = new SideImagePanel(kitchenImageUrl);
        
        // Opsional: Menambahkan overlay gelap/hijau transparan agar terlihat lebih menyatu
        panel.setBackground(new Color(46, 125, 50, 80)); // Hijau Transparan
        
        return panel;
    }

    private String formatTimeRemaining(int totalSeconds) {
        if (totalSeconds < 0) return "00:00";
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        DecimalFormat df = new DecimalFormat("00");
        return df.format(minutes) + ":" + df.format(seconds);
    }

    private void startProgressSimulation() {
        if (progressThread != null && progressThread.isAlive()) return;

        progressThread = new Thread(() -> {
            try {
                for (secondsElapsed = 0; secondsElapsed <= TOTAL_SIMULATION_SECONDS; secondsElapsed++) {
                    final int remaining = TOTAL_SIMULATION_SECONDS - secondsElapsed;
                    final int progress = secondsElapsed;

                    SwingUtilities.invokeLater(() -> updateGUI(remaining, progress));
                    Thread.sleep(1000);
                }
                SwingUtilities.invokeLater(this::updateFinishedStatus);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        progressThread.start();
    }
    
    private void updateGUI(int remainingSeconds, int progressValue) {
        timeLabel.setText(formatTimeRemaining(remainingSeconds));
        progressBar.setValue(progressValue);
        
        if (remainingSeconds <= 60 && remainingSeconds > 0) { 
            timeLabel.setForeground(ACCENT_YELLOW);
            progressBar.setForeground(ACCENT_YELLOW);
            statusLabel.setText("Hampir siap! Sedang finishing...");
        } else if (remainingSeconds == 0) { 
             timeLabel.setForeground(ACCENT_RED);
             progressBar.setForeground(LIGHT_GREEN);
        } else {
            timeLabel.setForeground(PRIMARY_GREEN);
            progressBar.setForeground(LIGHT_GREEN);
            
            double ratio = (double) progressValue / TOTAL_SIMULATION_SECONDS;
            if (ratio < 0.3) statusLabel.setText("Pesanan diterima, menyiapkan bahan...");
            else if (ratio < 0.7) statusLabel.setText("Sedang dimasak oleh Chef...");
            else statusLabel.setText("Sedang dalam proses plating...");
        }
    }
    
    private void updateFinishedStatus() {
        timeLabel.setText("00:00");
        timeLabel.setForeground(ACCENT_RED); 
        progressBar.setValue(TOTAL_SIMULATION_SECONDS);
        progressBar.setForeground(LIGHT_GREEN); 
        statusLabel.setText("Selesai! Pesanan siap diambil.");
        JOptionPane.showMessageDialog(this, "Makanan Anda Siap!", "Notifikasi Dapur", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // --- KELAS BARU: SideImagePanel ---
    // Kelas ini menggambar gambar memenuhi panel (aspect fill / cover)
    static class SideImagePanel extends JPanel {
        private Image image;

        public SideImagePanel(String imagePath) {
            try {
                // Cek apakah inputnya URL web atau File lokal
                if (imagePath.startsWith("http")) {
                    image = new ImageIcon(new URL(imagePath)).getImage();
                } else {
                    image = new ImageIcon(imagePath).getImage();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // Menggambar background hijau dulu (fallback jika gambar gagal load)
            g2.setColor(new Color(36, 89, 59)); 
            g2.fillRect(0, 0, getWidth(), getHeight());

            if (image != null) {
                // Logika "Object-Fit: Cover" (Agar gambar tidak gepeng)
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                double imageRatio = (double) image.getWidth(null) / image.getHeight(null);
                double panelRatio = (double) panelWidth / panelHeight;

                int newWidth;
                int newHeight;

                if (panelRatio > imageRatio) {
                    newWidth = panelWidth;
                    newHeight = (int) (panelWidth / imageRatio);
                } else {
                    newWidth = (int) (panelHeight * imageRatio);
                    newHeight = panelHeight;
                }

                int x = (panelWidth - newWidth) / 2;
                int y = (panelHeight - newHeight) / 2;

                g2.drawImage(image, x, y, newWidth, newHeight, null);
                
                // Tambahkan Overlay Hijau Transparan agar estetik (opsional)
                g2.setColor(new Color(36, 89, 59, 80)); // Hijau dengan transparansi
                g2.fillRect(0, 0, panelWidth, panelHeight);
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new KitchenProgressGUI().setVisible(true));
    }
}