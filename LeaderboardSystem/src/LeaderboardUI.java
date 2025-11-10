import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LeaderboardUI extends JFrame {

    private PlayerDAO dao;
    private DefaultTableModel tableModel;
    private JTable table;

    public LeaderboardUI() {
        dao = new PlayerDAO();
        setTitle("Leaderboard System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center window
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        // Title
        JLabel titleLabel = new JLabel("Leaderboard System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(33, 37, 41));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Score"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 12, 12));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 20, 60));

        JButton addBtn = createButton("Add Player", new Color(46, 204, 113));
        JButton updateBtn = createButton("Update Player", new Color(52, 152, 219));
        JButton deleteBtn = createButton("Delete Player", new Color(231, 76, 60));
        JButton viewBtn = createButton("View All", new Color(155, 89, 182));
        JButton topBtn = createButton("Top Scores", new Color(241, 196, 15));
        JButton findBtn = createButton("Find Player", new Color(230, 126, 34));

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(topBtn);
        buttonPanel.add(findBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load all players initially
        loadAllPlayers();

        // Button actions
        addBtn.addActionListener(e -> addPlayerDialog());
        updateBtn.addActionListener(e -> updatePlayerDialog());
        deleteBtn.addActionListener(e -> deletePlayerDialog());
        viewBtn.addActionListener(e -> loadAllPlayers());
        topBtn.addActionListener(e -> loadTopPlayers());
        findBtn.addActionListener(e -> findPlayerDialog());
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    private void loadAllPlayers() {
        List<Player> players = dao.getAllPlayers();
        refreshTable(players);
    }

    private void loadTopPlayers() {
        List<Player> topPlayers = dao.getTopPlayers();
        refreshTable(topPlayers);
    }

    private void refreshTable(List<Player> players) {
        tableModel.setRowCount(0);
        for (Player p : players) {
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getScore()});
        }
    }

    private void addPlayerDialog() {
        JTextField nameField = new JTextField();
        JTextField scoreField = new JTextField();

        Object[] message = {
                "Name:", nameField,
                "Score:", scoreField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Player", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            try {
                int score = Integer.parseInt(scoreField.getText().trim());
                dao.addPlayer(name, score);
                JOptionPane.showMessageDialog(this, "Player added successfully!");
                loadAllPlayers();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid score. Please enter a number.");
            }
        }
    }

    private void updatePlayerDialog() {
        String idInput = JOptionPane.showInputDialog(this, "Enter Player ID to update:");
        if (idInput == null) return;

        try {
            int id = Integer.parseInt(idInput);
            Player p = dao.findPlayerById(id);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "No player found with that ID.");
                return;
            }

            JTextField nameField = new JTextField(p.getName());
            JTextField scoreField = new JTextField(String.valueOf(p.getScore()));

            Object[] message = {
                    "New Name:", nameField,
                    "New Score:", scoreField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Update Player", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String newName = nameField.getText().trim();
                int newScore = Integer.parseInt(scoreField.getText().trim());
                dao.updatePlayer(id, newName, newScore);
                JOptionPane.showMessageDialog(this, "Player updated successfully!");
                loadAllPlayers();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID or Score. Please enter a valid number.");
        }
    }

    private void deletePlayerDialog() {
        String idInput = JOptionPane.showInputDialog(this, "Enter Player ID to delete:");
        if (idInput == null) return;

        try {
            int id = Integer.parseInt(idInput);
            Player p = dao.findPlayerById(id);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "No player found with that ID.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete player: " + p.getName() + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dao.deletePlayer(id);
                JOptionPane.showMessageDialog(this, "Player deleted successfully!");
                loadAllPlayers();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID.");
        }
    }

    private void findPlayerDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter Player ID or Name:");
        if (input == null || input.isBlank()) return;

        try {
            int id = Integer.parseInt(input);
            Player p = dao.findPlayerById(id);
            if (p != null) {
                refreshTable(List.of(p));
            } else {
                JOptionPane.showMessageDialog(this, "No player found with that ID.");
            }
        } catch (NumberFormatException e) {
            List<Player> found = dao.findPlayerByName(input);
            if (found.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No players found with that name.");
            } else {
                refreshTable(found);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LeaderboardUI().setVisible(true));
    }
}
