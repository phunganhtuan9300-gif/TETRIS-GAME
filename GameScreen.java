import javax.swing.*;
import java.awt.*;

    public class GameScreen extends JPanel{
    public GameManage gameManage;
    private JLabel scoreLabel;
    private JLabel linesLabel;
    private JLabel levelLabel;

    public GameManage getGameManage(){
        return this.gameManage;
    }

    public void updateScore(int score, int lines, int level){
        SwingUtilities.invokeLater(()->{
        levelLabel.setText(String.valueOf(level));
        scoreLabel.setText(String.valueOf(score));
        linesLabel.setText(String.valueOf(lines));
    });
}

    public GameScreen(CardLayout card, JPanel parent, LeaderBoard lb) {
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 144, 255));

        GridBagConstraints gbc = new GridBagConstraints();

        JButton backBtn = new JButton("BACK");
        backBtn.setFocusable(false);
        backBtn.addActionListener(e -> {
           gameManage.stopGame();
            card.show(parent, "START");
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST; 
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0; 
        gbc.insets = new Insets(10, 10, 0, 0); 
        add(backBtn, gbc);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        levelLabel = new JLabel("0");
        scoreLabel = new JLabel("0");
        linesLabel = new JLabel("0");
        leftPanel.add(createUIBox("LEVEL", levelLabel));
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(createUIBox("SCORE", scoreLabel));
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(createUIBox("LINES", linesLabel));
        

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(0, 0, 0, 10);
        add(leftPanel, gbc);

        Board board = new Board(); 
        board.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        gameManage = new GameManage(board, lb, this);
        new Controller(board, gameManage);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(board, gbc);
    }

        private JPanel createUIBox(String titleText, JLabel valueLabel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(50, 50, 50, 180));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel title = new JLabel(titleText);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel value = valueLabel;
        value.setForeground(Color.WHITE);
        value.setFont(new Font("Arial", Font.BOLD, 18));
        value.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        panel.add(value);
        
        panel.setMaximumSize(new Dimension(100, 60));
        
        return panel;
        
    }
}



