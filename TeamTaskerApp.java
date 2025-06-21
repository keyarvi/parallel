// --- TeamTaskerApp.java (All-in-One, Fixed and Enhanced) ---

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class TeamTaskerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen());
    }
}

// --- RoundedPanel class ---
class RoundedPanel extends JPanel {
    private final int cornerRadius;
    private final Color bgColor;

    public RoundedPanel(int radius, Color bgColor) {
        this.cornerRadius = radius;
        this.bgColor = bgColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
    }
}

// --- RoundedBorder class ---
class RoundedBorder implements Border {
    private final int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius + 1, radius + 1, radius + 2, radius);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}

// --- LoginScreen class ---
class LoginScreen extends JFrame {
    private final JTextField userField;
    private final JPasswordField passField;
    private final JLabel feedback;
    private final String USERNAME = "admin";
    private final String PASSWORD = "admin123";

    public LoginScreen() {
        setTitle("TeamTasker Login");
        setSize(480, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        JPanel bg = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x6a11cb), 0, getHeight(), new Color(0x2575fc));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(32, new Color(255, 255, 255, 235));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(350, 420));
        card.setBorder(new EmptyBorder(32, 32, 32, 32));

        JLabel logo = new JLabel(new ImageIcon(drawLogo(64, 64)));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(logo);

        JLabel title = new JLabel("TeamTasker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(0x2575fc));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(24));

        userField = new JTextField(16);
        passField = new JPasswordField(16);
        styleField(userField, "Username");
        styleField(passField, "Password");

        card.add(userField);
        card.add(Box.createVerticalStrut(16));
        card.add(passField);

        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setForeground(new Color(0x2575fc));
        showPass.setOpaque(false);
        showPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        showPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        showPass.addActionListener(_e -> passField.setEchoChar(showPass.isSelected() ? (char) 0 : '•'));
        card.add(showPass);

        card.add(Box.createVerticalStrut(16));
        feedback = new JLabel(" ");
        feedback.setForeground(new Color(255, 80, 80));
        feedback.setFont(new Font("Segoe UI", Font.BOLD, 13));
        feedback.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(feedback);

        JButton loginButton = new JButton("Login");
        styleButton(loginButton, new Color(0x2575fc));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(_e -> authenticate());
        card.add(Box.createVerticalStrut(12));
        card.add(loginButton);

        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        closeBtn.setForeground(new Color(0x2575fc));
        closeBtn.setBackground(new Color(0, 0, 0, 0));
        closeBtn.setBorder(null);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeBtn.addActionListener(_e -> System.exit(0));
        card.add(Box.createVerticalStrut(8));
        card.add(closeBtn);

        bg.add(card, new GridBagConstraints());
        setContentPane(bg);
        setVisible(true);
    }

    private void authenticate() {
        String user = userField.getText();
        String pass = new String(passField.getPassword());
        if (user.equals(USERNAME) && pass.equals(PASSWORD)) {
            new CalendarUI(user);
            dispose();
        } else {
            feedback.setText("Invalid username or password.");
        }
    }

    private void styleField(JTextField field, String placeholder) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setMaximumSize(new Dimension(340, 40));
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(0x2575fc), 2, true),
                new EmptyBorder(8, 16, 8, 16)
        ));
        field.setBackground(new Color(245, 245, 255));
        field.setForeground(new Color(30, 30, 30));
        field.setCaretColor(new Color(0x2575fc));
        field.putClientProperty("JTextField.placeholderText", placeholder);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new RoundedBorder(16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 40));
    }

    private Image drawLogo(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0x2575fc));
        g.fillOval(0, 0, w, h);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, w / 2));
        FontMetrics fm = g.getFontMetrics();
        String s = "T";
        int sw = fm.stringWidth(s);
        int sh = fm.getAscent();
        g.drawString(s, (w - sw) / 2, (h + sh) / 2 - 4);
        g.dispose();
        return img;
    }
}

// --- CalendarUI class ---
class CalendarUI extends JFrame {
    private final JPanel calendarPanel;
    private final JLabel monthLabel;
    private final Calendar calendar = Calendar.getInstance();
    private final Map<String, java.util.List<String>> taskMap = new HashMap<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");

    public CalendarUI(String username) {
        setTitle("TeamTasker Calendar - " + username);
        setSize(920, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 255));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        headerPanel.setBackground(new Color(0x2575fc));

        JButton prevMonth = new JButton("◀");
        JButton nextMonth = new JButton("▶");
        styleHeaderButton(prevMonth);
        styleHeaderButton(nextMonth);

        monthLabel = new JLabel();
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        monthLabel.setForeground(Color.WHITE);
        updateMonthLabel();

        prevMonth.addActionListener(e -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        nextMonth.addActionListener(e -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        headerPanel.add(prevMonth);
        headerPanel.add(monthLabel);
        headerPanel.add(nextMonth);

        // Calendar grid
        calendarPanel = new JPanel(new GridLayout(0, 7));
        calendarPanel.setBackground(Color.WHITE);
        updateCalendar();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(calendarPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        setVisible(true);
    }

    private void updateMonthLabel() {
        monthLabel.setText(dateFormat.format(calendar.getTime()));
    }

    private void updateCalendar() {
        calendarPanel.removeAll();
        updateMonthLabel();

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel lbl = new JLabel(day, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lbl.setForeground(new Color(0x2575fc));
            calendarPanel.add(lbl);
        }

        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int startDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < startDay; i++) calendarPanel.add(new JLabel(""));

        for (int day = 1; day <= maxDay; day++) {
            Calendar dateCal = (Calendar) calendar.clone();
            dateCal.set(Calendar.DAY_OF_MONTH, day);
            String key = new SimpleDateFormat("yyyy-MM-dd").format(dateCal.getTime());

            JButton dayBtn = new JButton("<html><center>" + day +
                    (taskMap.containsKey(key) ? "<br>📌" + taskMap.get(key).size() : "") +
                    "</center></html>");
            dayBtn.setBackground(new Color(230, 240, 255));
            dayBtn.setFocusPainted(false);
            dayBtn.setBorder(new LineBorder(new Color(0x2575fc), 1));
            dayBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            int finalDay = day;
            dayBtn.addActionListener(e -> openTaskDialog(finalDay));
            calendarPanel.add(dayBtn);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void openTaskDialog(int day) {
        String key = getSelectedDate(day);
        java.util.List<String> tasks = taskMap.getOrDefault(key, new ArrayList<>());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        DefaultListModel<String> model = new DefaultListModel<>();
        tasks.forEach(model::addElement);
        JList<String> taskList = new JList<>(model);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(taskList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        controls.add(addBtn);
        controls.add(editBtn);
        controls.add(delBtn);
        panel.add(controls, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(this, "Tasks on " + key, true);
        dialog.setContentPane(panel);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        addBtn.addActionListener(_e -> {
            String task = promptForTask(null);
            if (task != null) {
                model.addElement(task);
                tasks.add(task);
                taskMap.put(key, tasks);
                updateCalendar();
            }
        });

        editBtn.addActionListener(_e -> {
            int idx = taskList.getSelectedIndex();
            if (idx >= 0) {
                String updated = promptForTask(tasks.get(idx));
                if (updated != null) {
                    tasks.set(idx, updated);
                    model.set(idx, updated);
                    updateCalendar();
                }
            }
        });

        delBtn.addActionListener(_e -> {
            int idx = taskList.getSelectedIndex();
            if (idx >= 0) {
                int confirm = JOptionPane.showConfirmDialog(dialog, "Delete selected task?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tasks.remove(idx);
                    model.remove(idx);
                    if (tasks.isEmpty()) taskMap.remove(key);
                    updateCalendar();
                }
            }
        });

        dialog.setVisible(true);
    }

    private String promptForTask(String existing) {
        JTextField title = new JTextField();
        JTextField desc = new JTextField();
        JComboBox<String> start = createTimePicker();
        JComboBox<String> end = createTimePicker();

        if (existing != null) {
            // Parse existing text to try to prefill
            // This is optional and basic
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Title:"));
        panel.add(title);
        panel.add(new JLabel("Description:"));
        panel.add(desc);
        panel.add(new JLabel("Start Time:"));
        panel.add(start);
        panel.add(new JLabel("End Time:"));
        panel.add(end);

        int result = JOptionPane.showConfirmDialog(this, panel, "Task Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            return String.format("📌 %s (%s-%s): %s", title.getText(), start.getSelectedItem(), end.getSelectedItem(), desc.getText());
        }
        return null;
    }

    private String getSelectedDate(int day) {
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, day);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    private JComboBox<String> createTimePicker() {
        String[] times = new String[48];
        for (int i = 0; i < 48; i++) {
            int hour = i / 2;
            int minute = (i % 2) * 30;
            times[i] = String.format("%02d:%02d", hour, minute);
        }
        return new JComboBox<>(times);
    }

    private void styleHeaderButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0x6a11cb));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setBorder(new EmptyBorder(4, 10, 4, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
