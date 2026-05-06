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

