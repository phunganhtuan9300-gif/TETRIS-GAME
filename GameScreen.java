import javax.swing.*;
import java.awt.*;

    public class GameScreen extends JPanel{
    private GameManage gameManage;
    private JLabel scoreLabel;
    private JLabel linesLabel;

    public void updateScore(int score, int lines){
        SwingUtilities.invokeLater(()->{
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
            gameManage.resetGame();
            gameManage.togglePause();
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

        scoreLabel = new JLabel("0");
        linesLabel = new JLabel("0");
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

class Board extends JPanel{ // bang D x R
    public static final int ROWS = 20;
    public static final int COLUMNS = 10;
    public static final int CELL_SIZE = 40;

    Color[][] grid = new Color[ROWS][COLUMNS];
    Tetromino current;

    public void setCurrent(Tetromino tetro){
        this.current = tetro;
    }

    boolean isEmpty(int row, int col){
        return row >= 0 && row < ROWS && col >= 0 && col < COLUMNS && grid[row][col] == null;
    }
    
    boolean canPlace(Tetromino tetromino, int newRow, int newCol){
        int[][] shape = tetromino.shape;
        for (int row = 0; row < shape.length; row++){
            for (int col = 0; col < shape[row].length; col++){
                if (shape[row][col] != 0){
                    int newRowPos = newRow + row;
                    int newColPos = newCol + col;

                    if (!isEmpty(newRowPos, newColPos)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    void place(Tetromino tetromino){
        for (int row = 0; row < tetromino.shape.length; row++){
            for (int col = 0; col < tetromino.shape[row].length; col++){
                if (tetromino.shape[row][col] != 0){
                    grid[tetromino.row + row][tetromino.column + col] = tetromino.color;
                }
            }
        }
    }
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(COLUMNS * CELL_SIZE, ROWS * CELL_SIZE);
    }

    @Override 
    public void paintComponent(Graphics g){
        super.paintComponent(g);

    g.setColor(Color.BLACK);
    g.fillRect(0, 0, COLUMNS * CELL_SIZE, ROWS * CELL_SIZE);

    for(int row = 0; row <ROWS; row++){
        for(int column = 0; column < COLUMNS; column++){
            if(grid[row][column] != null){
                g.setColor(grid[row][column]);
                g.fillRect(column * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    if(current != null){
        g.setColor(current.color);
        for(int row = 0; row < current.shape.length; row++){
            for(int column = 0; column < current.shape[row].length; column++){
                if(current.shape[row][column] != 0){
                    int x = (current.column + column) * CELL_SIZE;
                    int y = (current.row + row) * CELL_SIZE;

                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }
    g.setColor(Color.GRAY);
    for(int row = 0; row <= ROWS; row++){
        g.drawLine(0, row * CELL_SIZE, COLUMNS * CELL_SIZE, row * CELL_SIZE);
    }
    for(int column = 0; column <= COLUMNS; column++){
        g.drawLine(column * CELL_SIZE, 0, column * CELL_SIZE, ROWS * CELL_SIZE);
    }
}
} 

