import javax.swing.*;
import java.awt.*;


public class Tetris {
    public static void main(String[] args){
        
        SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Tetris Game");
        CardLayout card = new CardLayout();
        JPanel container = new JPanel(card);

        LeaderBoard lbLogic = new LeaderBoard();

        GameScreen gameScreen = new GameScreen(card, container, lbLogic);

        LeaderBoardScreen leaderboardScreen = new LeaderBoardScreen(card, container, lbLogic);

        StartMenu startMenu = new StartMenu(card, container, lbLogic, gameScreen, leaderboardScreen);
        

        container.add(startMenu, "START");
        container.add(gameScreen, "GAME");
        container.add(leaderboardScreen, "LEADERBOARD");

        frame.add(container);
        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        });
    }
}



