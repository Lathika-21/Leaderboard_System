import java.util.*;

public class LeaderboardApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PlayerDAO dao = new PlayerDAO();

        while (true) {
            System.out.println("\n=== Leaderboard System Menu ===");
            System.out.println("1. Add Player");
            System.out.println("2. Update Player");
            System.out.println("3. Delete Player");
            System.out.println("4. View All Players");
            System.out.println("5. View Top Scores");
            System.out.println("6. Find Player by ID or Name");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter Player Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Score: ");
                    try {
                        int score = Integer.parseInt(sc.nextLine());
                        dao.addPlayer(name, score);
                        System.out.println("Player added successfully!");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid score. Please enter a number.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Player ID to update: ");
                    try {
                        int uid = Integer.parseInt(sc.nextLine());
                        Player existing = dao.findPlayerById(uid);
                        if (existing == null) {
                            System.out.println("No player found with that ID.");
                            break;
                        }
                        System.out.print("Enter New Name: ");
                        String uname = sc.nextLine();
                        System.out.print("Enter New Score: ");
                        int uscore = Integer.parseInt(sc.nextLine());
                        dao.updatePlayer(uid, uname, uscore);
                        System.out.println("Player updated successfully!");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                    break;

                case 3:
                    System.out.print("Enter Player ID to delete: ");
                    try {
                        int did = Integer.parseInt(sc.nextLine());
                        Player found = dao.findPlayerById(did);
                        if (found == null) {
                            System.out.println("No player found with that ID.");
                        } else {
                            dao.deletePlayer(did);
                            System.out.println("Player deleted successfully!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID. Please enter a valid number.");
                    }
                    break;

                case 4:
                    System.out.println("\n--- All Players ---");
                    List<Player> players = dao.getAllPlayers();
                    if (players.isEmpty()) {
                        System.out.println("No players found.");
                    } else {
                        for (Player p : players) {
                            System.out.printf("ID: %d | Name: %s | Score: %d%n",
                                    p.getId(), p.getName(), p.getScore());
                        }
                    }
                    break;

                case 5:
                    System.out.println("\n--- Top Scores ---");
                    List<Player> topPlayers = dao.getTopPlayers();
                    if (topPlayers.isEmpty()) {
                        System.out.println("No players found.");
                    } else {
                        for (Player p : topPlayers) {
                            System.out.printf("ID: %d | Name: %s | Score: %d%n",
                                    p.getId(), p.getName(), p.getScore());
                        }
                    }
                    break;

                case 6:
                    System.out.print("Enter Player ID or Name: ");
                    String input = sc.nextLine();
                    try {
                        int id = Integer.parseInt(input);
                        Player p = dao.findPlayerById(id);
                        if (p != null)
                            System.out.printf("Found: ID=%d | Name=%s | Score=%d%n",
                                    p.getId(), p.getName(), p.getScore());
                        else
                            System.out.println("No player found with that ID.");
                    } catch (NumberFormatException e) {
                        List<Player> foundPlayers = dao.findPlayerByName(input);
                        if (foundPlayers.isEmpty()) {
                            System.out.println("No players found with that name.");
                        } else {
                            for (Player p : foundPlayers) {
                                System.out.printf("Found: ID=%d | Name=%s | Score=%d%n",
                                        p.getId(), p.getName(), p.getScore());
                            }
                        }
                    }
                    break;

                case 0:
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
