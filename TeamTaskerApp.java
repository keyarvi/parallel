import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class TeamTaskerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginWindow::new);
    }
}

class LoginWindow extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private final String USERNAME = "admin";
    private final String PASSWORD = "admin123";

    public LoginWindow() {
        setTitle("TeamTasker Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:", SwingConstants.RIGHT));
        userField = new JTextField();
        panel.add(userField);

        panel.add(new JLabel("Password:", SwingConstants.RIGHT));
        passField = new JPasswordField();
        panel.add(passField);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> authenticate());
        panel.add(new JLabel(""));
        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    private void authenticate() {
        String user = userField.getText();
        String pass = new String(passField.getPassword());

        if (user.equals(USERNAME) && pass.equals(PASSWORD)) {
            new CalendarView();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class CalendarView extends JFrame {
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private Calendar calendar;
    private Map<String, List<String>> tasks;
    private String selectedDate = null;
    private JPanel lastSelectedDayPanel = null;

    public CalendarView() {
        setTitle("TeamTasker: TimeTree Style");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        calendar = new GregorianCalendar();
        tasks = new HashMap<>();

        JPanel topPanel = new JPanel(new BorderLayout());
        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topPanel.add(monthLabel, BorderLayout.CENTER);
        topPanel.setBackground(new Color(0, 123, 255));
        monthLabel.setForeground(Color.WHITE);

        JButton prevBtn = new JButton("<");
        JButton nextBtn = new JButton(">");
        prevBtn.addActionListener(e -> changeMonth(-1));
        nextBtn.addActionListener(e -> changeMonth(1));
        topPanel.add(prevBtn, BorderLayout.WEST);
        topPanel.add(nextBtn, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(calendarPanel, BorderLayout.CENTER);

        JButton addButton = new JButton("+");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        addButton.setBackground(new Color(0, 123, 255));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> showAddTaskDialogForSelectedDate());
        add(addButton, BorderLayout.SOUTH);

        displayCalendar();
        setVisible(true);
    }

    private void displayCalendar() {
        calendarPanel.removeAll();

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            dayLabel.setForeground(new Color(0, 123, 255));
            calendarPanel.add(dayLabel);
        }

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int startDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            dayPanel.setBackground(Color.WHITE);

            JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            dayPanel.add(dayLabel, BorderLayout.NORTH);

            String key = getKeyForDate(day);
            if (tasks.containsKey(key)) {
                JLabel taskLabel = new JLabel(" " + String.join(", ", tasks.get(key)), SwingConstants.CENTER);
                dayPanel.add(taskLabel, BorderLayout.CENTER);
                dayPanel.setBackground(new Color(173,216,230)); // highlight if has task
            }

            int finalDay = day;
            dayPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if(lastSelectedDayPanel != null) lastSelectedDayPanel.setBackground(Color.WHITE);
                    dayPanel.setBackground(new Color(173,216,230));
                    lastSelectedDayPanel = dayPanel;
                    selectedDate = getKeyForDate(finalDay);
                }
            });

            calendarPanel.add(dayPanel);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        monthLabel.setText(sdf.format(calendar.getTime()));

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void showAddTaskDialogForSelectedDate() {
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date first.");
            return;
        }
        JTextField taskField = new JTextField();
        JSpinner startDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startDate, "yyyy-MM-dd");
        startDate.setEditor(startEditor);

        JSpinner startTime = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTime, "HH:mm");
        startTime.setEditor(startTimeEditor);

        JSpinner endDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDate, "yyyy-MM-dd");
        endDate.setEditor(endEditor);

        JSpinner endTime = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTime, "HH:mm");
        endTime.setEditor(endTimeEditor);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Task Name:"));
        panel.add(taskField);
        panel.add(new JLabel("Start Date:"));
        panel.add(startDate);
        panel.add(new JLabel("Start Time:"));
        panel.add(startTime);
        panel.add(new JLabel("End Date:"));
        panel.add(endDate);
        panel.add(new JLabel("End Time:"));
        panel.add(endTime);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String task = taskField.getText();
            Calendar startCal = Calendar.getInstance();
            startCal.setTime((Date)startDate.getValue());
            int startDay = startCal.get(Calendar.DAY_OF_MONTH);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime((Date)endDate.getValue());
            int endDay = endCal.get(Calendar.DAY_OF_MONTH);

            for (int d = startDay; d <= endDay; d++) {
                String key = getKeyForDate(d);
                if (!tasks.containsKey(key)) tasks.put(key, new ArrayList<>());
                tasks.get(key).add(task);
            }
            displayCalendar();
        }
    }

    private String getKeyForDate(int day) {
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return year + "-" + month + "-" + day;
    }

    private void changeMonth(int delta) {
        calendar.add(Calendar.MONTH, delta);
        displayCalendar();
    }
}
