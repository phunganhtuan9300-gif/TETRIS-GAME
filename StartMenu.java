import javax.swing.*;
import java.awt.*;


    public class StartMenu extends JPanel{
    private LeaderBoard lbLogic;
    public StartMenu(CardLayout card, JPanel parent, LeaderBoard lbLogic){
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 144, 255));
        this.lbLogic = lbLogic;

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setPreferredSize(new Dimension(Board.COLUMNS * Board.CELL_SIZE, Board.ROWS * Board.CELL_SIZE));
        box.setBackground(Color.BLACK);

        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("TETRIS");
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton play = new JButton("PLAY");
        play.setAlignmentX(Component.CENTER_ALIGNMENT);
        play.setBackground(Color.GREEN);
        play.setForeground(Color.WHITE);

        JButton leaderboard = new JButton("LEADERBOARD");
        leaderboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboard.setBackground(Color.BLUE);
        leaderboard.setForeground(Color.WHITE);

        box.add(Box.createVerticalGlue());
        box.add(title);
        box.add(Box.createVerticalStrut(50));
        box.add(play);
        box.add(Box.createVerticalStrut(20));
        box.add(leaderboard);
        box.add(Box.createVerticalGlue());

        add(box);

        play.addActionListener(e -> card.show(parent, "GAME"));
        leaderboard.addActionListener(e -> {
            card.show(parent, "LEADERBOARD");
        });
    }
}

