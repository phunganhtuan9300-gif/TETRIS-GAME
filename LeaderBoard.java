import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class LeaderBoard { // bang xep hang
    List <Player> players = new ArrayList<Player>();
    private final String FILE_NAME = "leaderboard.txt";

    public LeaderBoard(){
        loadFromFile();
    }
    public void addPlayer(Player player){
        players.removeIf(p -> p.getName().equalsIgnoreCase(player.getName()) && player.getHighscore() > p.getHighscore());

        boolean exists = false;
        for (Player p : players){
            if(p.getName().equalsIgnoreCase(player.getName())){
                exists = true;
                break;
            }
        }
        if (!exists || player.getHighscore() > 0){
        players.add(player);
    }
        sortLeaderboard();
        saveToFile();
    }

    public void sortLeaderboard(){
        Collections.sort(players, new Comparator<Player>(){
            @Override
            public int compare(Player p1, Player p2){
                return Integer.compare(p2.getHighscore(), p1.getHighscore());
            }
        });
    }

    public void Display(){
        System.out.printf("%-20s | %s\n ", "Player", "Score");
        for (Player player : players){
            System.out.printf("%-20s | %d\n", player.getName(), player.getHighscore());
        }
    }

    private void saveToFile(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))){
            for (Player p : players){
                writer.write(p.getName() + "|" + p.getHighscore());
                writer.newLine();
            }
        } catch (IOException e){
            System.out.println("ERROR" + e.getMessage());
        }
    }

    private void loadFromFile(){
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
            String line;
            players.clear();
            while ((line = reader.readLine()) != null){
                String[] parts = line.split("\\|");
                if (parts.length == 2){
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    players.add(new Player(name, score, score));
                }
            }
            sortLeaderboard();
        } catch (IOException | NumberFormatException e){
            System.out.println("ERROR" + e.getMessage());
        }
    }
}

class LeaderBoardScreen extends JPanel{
    private LeaderBoard lbLogic;
    private JPanel parent;
    private CardLayout card;

    public LeaderBoardScreen(CardLayout card, JPanel parent, LeaderBoard lbLogic) {
        this.card = card;
        this.parent = parent;
        this.lbLogic = lbLogic;
        refreshTable();
    }

        public void refreshTable(){
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 144, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        JButton backBtn = new JButton("BACK");
        backBtn.setFocusable(false);
        backBtn.addActionListener(e -> card.show(parent, "START"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 0, 0);
        add(backBtn, gbc);


        JPanel mainBox = new JPanel();
        mainBox.setLayout(new BorderLayout());
        mainBox.setPreferredSize(new Dimension(Board.COLUMNS * Board.CELL_SIZE, Board.ROWS * Board.CELL_SIZE));
        mainBox.setBackground(Color.BLACK);
        mainBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JPanel header = new JPanel(new GridLayout(1, 3));
        header.setBackground(new Color(70, 70, 70));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        header.add(createLabel("RANK", Font.BOLD, Color.YELLOW));
        header.add(createLabel("PLAYER NAME", Font.BOLD, Color.YELLOW));
        header.add(createLabel("SCORE", Font.BOLD, Color.YELLOW));

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.BLACK);

        listPanel.add(header);

        for(int i = 0; i < lbLogic.players.size(); i++){
            Player player = lbLogic.players.get(i);
            JPanel row = new JPanel(new GridLayout(1, 3));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

            row.add(createLabel(String.valueOf(i + 1), Font.PLAIN, Color.WHITE));
            row.add(createLabel(player.getName(), Font.PLAIN, Color.WHITE));
            row.add(createLabel(String.valueOf(player.getHighscore()), Font.PLAIN, Color.WHITE));

            listPanel.add(row);
        }
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getViewport().setBackground(Color.BLACK);
        mainBox.add(scrollPane, BorderLayout.CENTER);

        
        JPanel userRankPanel = new JPanel(new GridLayout(1, 3));
        userRankPanel.setBackground(new Color(40, 40, 40));
        userRankPanel.setPreferredSize(new Dimension(0, 50));
        userRankPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.WHITE));
        String currentPlayerName = ""; 

        int currentPlayerScore = 0;
        int currentRank = -1;

        try (BufferedReader reader = new BufferedReader(new FileReader("user_setting.txt"))) {
            currentPlayerName = reader.readLine();
        } catch (IOException e) {
            currentPlayerName = "Player";
        }

        for (int i = 0; i < lbLogic.players.size(); i++) {
            if (lbLogic.players.get(i).getName().equalsIgnoreCase(currentPlayerName)) {
                currentRank = i + 1;
                currentPlayerScore = lbLogic.players.get(i).getHighscore();
                break;
            }
        }

        if (currentRank != -1) {
            userRankPanel.add(createLabel("YOU: #" + currentRank, Font.BOLD, Color.GREEN));
            userRankPanel.add(createLabel(currentPlayerName, Font.BOLD, Color.GREEN));
            userRankPanel.add(createLabel(String.valueOf(currentPlayerScore), Font.BOLD, Color.GREEN));
        } else {
            userRankPanel.add(createLabel("-", Font.PLAIN, Color.GRAY));
            userRankPanel.add(createLabel(currentPlayerName, Font.PLAIN, Color.GRAY));
            userRankPanel.add(createLabel("No Score", Font.PLAIN, Color.GRAY));
        }
        mainBox.add(userRankPanel, BorderLayout.SOUTH);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.8; gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(mainBox, gbc);

        gbc.gridx = 2; 
        gbc.weightx = 0.1;
        add(Box.createHorizontalGlue(), gbc);
    }
        private JLabel createLabel(String text, int style, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(color);
        label.setFont(new Font("Arial", style, 14));
        return label;
    }
}

