import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AuthPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardContainer;

    private static final Color PRIMARY_ORANGE = new Color(255, 127, 39);
    private static final Color NAVY_DARK = new Color(26, 35, 126);
    private static final Color OFF_WHITE_BG = new Color(250, 250, 250);
    private static final Color LIGHT_GRAY_FIELD = new Color(245, 245, 245);

    private JTextField loginUserField;
    private JPasswordField loginPassField;

    private JTextField regUserField;
    private JPasswordField regPassField;
    private JPasswordField regConfirmField;

    public AuthPanel(RestaurantApp app) {

        setLayout(new GridBagLayout());
        setBackground(PRIMARY_ORANGE);

        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setOpaque(false);

        cardContainer.add(createLoginCard(app), "login");
        cardContainer.add(createRegisterCard(app), "register");

        add(cardContainer);

        cardLayout.show(cardContainer,"login");
    }

    private JPanel createLoginCard(RestaurantApp app) {
    JPanel card = baseCard();
    GridBagConstraints c = baseConstraints();

    // ✅ TITLE SELALU MUNCUL
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.CENTER;

    JLabel title = new JLabel("Rumah Makan Lombok", SwingConstants.CENTER);
    title.setForeground(NAVY_DARK);
    title.setFont(new Font("Segoe UI", Font.BOLD, 32));
    card.add(title, c);

    // FIELD USERNAME
    c.gridy++;
    loginUserField = placeholderField("Masukkan username");
    card.add(loginUserField, c);

    // PASSWORD
    c.gridy++;
    loginPassField = placeholderPassword("Masukkan password");
    card.add(loginPassField, c);

    // LOGIN BUTTON
    c.gridy++;
    JButton loginBtn = modernButton("LOG IN");
    card.add(loginBtn, c);

    // LINK REGISTER
    c.gridy++;
    JLabel link = linkLabel("Daftar di sini");
    card.add(makeLinkPanel("Belum punya akun?", link), c);


        link.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                resetFields();
                cardLayout.show(cardContainer,"register");
            }
        });

        loginBtn.addActionListener(e -> {

            String username = loginUserField.getText().trim();
            String password = new String(loginPassField.getPassword()).trim();

            if(username.equals("Masukkan username")||username.isEmpty()){
                JOptionPane.showMessageDialog(this,"Username harus diisi!");
                return;
            }
            if(password.equals("Masukkan password")||password.isEmpty()){
                JOptionPane.showMessageDialog(this,"Password harus diisi!");
                return;
            }

            int userId = DBConnection.loginUser(username,password);

            if(userId != -1){
                JOptionPane.showMessageDialog(this,"Login berhasil! Selamat datang "+username);
                app.setActiveUser(userId,username);
                app.showMainTabs();
            } else {
                JOptionPane.showMessageDialog(this,"Username atau password salah!");
            }
        });

        return card;
    }

    private JPanel createRegisterCard(RestaurantApp app) {

    JPanel card = baseCard();
    GridBagConstraints c = baseConstraints();

    // ✅ TITLE REGISTER TAMPIL & CENTER
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.CENTER;

    JLabel title = new JLabel("Buat Akun", SwingConstants.CENTER);
    title.setFont(new Font("Segoe UI", Font.BOLD, 32));
    title.setForeground(NAVY_DARK);
    card.add(title, c);

    // USERNAME
    c.gridy++;
    regUserField = placeholderField("Masukkan username");
    card.add(regUserField, c);

    // PASSWORD
    c.gridy++;
    regPassField = placeholderPassword("Masukkan password");
    card.add(regPassField, c);

    // CONFIRM
    c.gridy++;
    regConfirmField = placeholderPassword("Konfirmasi password");
    card.add(regConfirmField, c);

    // BUTTON
    c.gridy++;
    JButton regBtn = modernButton("DAFTAR");
    card.add(regBtn, c);

    // LINK TO LOGIN
    c.gridy++;
    JLabel link = linkLabel("Login di sini");
    card.add(makeLinkPanel("Sudah punya akun?", link), c);

    link.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            resetFields();
            cardLayout.show(cardContainer, "login");
        }
    });

    return card;
}

    private JPanel baseCard(){

        JPanel p = new JPanel(new GridBagLayout());
        p.setPreferredSize(new Dimension(480,520));
        p.setBackground(OFF_WHITE_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220),1),
                new EmptyBorder(30,30,30,30)
        ));
        return p;
    }

    private GridBagConstraints baseConstraints(){

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 2;
        return c;
    }

    private JTextField placeholderField(String text){

        JTextField tf = new JTextField(text);
        tf.setPreferredSize(new Dimension(320,55));
        tf.setForeground(Color.GRAY);

        tf.addFocusListener(new FocusAdapter(){
            @Override
            public void focusGained(FocusEvent e){
                if(tf.getText().equals(text)){
                    tf.setText("");
                    tf.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e){
                if(tf.getText().isEmpty()){
                    tf.setText(text);
                    tf.setForeground(Color.GRAY);
                }
            }
        });

        tf.setFont(new Font("Segoe UI",Font.PLAIN,16));
        tf.setBackground(LIGHT_GRAY_FIELD);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                new EmptyBorder(10,15,10,15)
        ));
        return tf;
    }

    private JPasswordField placeholderPassword(String text){

        JPasswordField pf = new JPasswordField(text);
        pf.setPreferredSize(new Dimension(320,55));
        pf.setEchoChar((char)0);
        pf.setForeground(Color.GRAY);

        pf.addFocusListener(new FocusAdapter(){
            @Override
            public void focusGained(FocusEvent e){
                if(new String(pf.getPassword()).equals(text)){
                    pf.setText("");
                    pf.setForeground(Color.BLACK);
                    pf.setEchoChar('•');
                }
            }

            @Override
            public void focusLost(FocusEvent e){
                if(pf.getPassword().length == 0){
                    pf.setEchoChar((char)0);
                    pf.setText(text);
                    pf.setForeground(Color.GRAY);
                }
            }
        });

        pf.setFont(new Font("Segoe UI",Font.PLAIN,16));
        pf.setBackground(LIGHT_GRAY_FIELD);
        pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                new EmptyBorder(10,15,10,15)
        ));
        return pf;
    }

    private JButton modernButton(String text){

        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(320,55));
        b.setFont(new Font("Segoe UI",Font.BOLD,18));
        b.setBackground(PRIMARY_ORANGE);
        b.setForeground(Color.WHITE);
        b.setBorderPainted(false);
        b.setFocusPainted(false);

        b.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                b.setBackground(PRIMARY_ORANGE.darker());
            }
            @Override
            public void mouseExited(MouseEvent e){
                b.setBackground(PRIMARY_ORANGE);
            }
        });

        return b;
    }

    private JLabel linkLabel(String text){

        JLabel lbl = new JLabel("<html><u>"+text+"</u></html>");
        lbl.setForeground(PRIMARY_ORANGE);
        lbl.setFont(new Font("Segoe UI",Font.BOLD,14));
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return lbl;
    }

    private JPanel makeLinkPanel(String text,JLabel link){

        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setOpaque(false);
        p.add(new JLabel(text));
        p.add(link);
        return p;
    }

    private void resetFields(){

        loginUserField.setText("Masukkan username");
        loginUserField.setForeground(Color.GRAY);

        loginPassField.setText("Masukkan password");
        loginPassField.setForeground(Color.GRAY);
        loginPassField.setEchoChar((char)0);

        regUserField.setText("Masukkan username");
        regUserField.setForeground(Color.GRAY);

        regPassField.setText("Masukkan password");
        regPassField.setForeground(Color.GRAY);
        regPassField.setEchoChar((char)0);

        regConfirmField.setText("Konfirmasi password");
        regConfirmField.setForeground(Color.GRAY);
        regConfirmField.setEchoChar((char)0);
    }
}
