import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.awt.event.ActionEvent;


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

class StartMenu extends JPanel{
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

class GameScreen extends JPanel{
    private GameManage gameManage;
    private JLabel scoreLabel;
    private JLabel linesLabel;

    public void updateScore(int score, int lines){
        scoreLabel.setText(String.valueOf(score));
        linesLabel.setText(String.valueOf(lines));
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

class Player{ // nguoi choi + highscore
    private String Name;
    private int score;
    private int highscore;

    public Player(String name, int score, int highscore){
        this.Name = name;
        this.score = score;
        this.highscore = highscore;
    }
    public String getName(){
        return Name;
    }
    public int getScore(){
        return score;
    }
    public int getHighscore(){
        return highscore;
    }
    public void updateHighscore(){
        if (score > highscore){
            highscore = score;
        }
    }
}

class Tetromino{ // khoi
    static final int[][][] SHAPES = {
        {{1, 1, 1, 1}}, // I
        {{1, 1}, {1, 1}}, // O
        {{0, 1, 0}, {1, 1, 1}}, // T
        {{1, 0, 0}, {1, 1, 1}}, // L
        {{0, 0, 1}, {1, 1, 1}}, // J
        {{0, 1, 1}, {1, 1, 0}}, // S
        {{1, 1, 0}, {0, 1, 1}} // Z
    };
    int[][] shape;
    int row;
    int column;
    Color color;
    
    public Tetromino(int type){
        shape = SHAPES[type];
        row = 0;
        column = Board.COLUMNS / 2 - shape[0].length / 2;
        this.color = getColor(type);
    }

    static int[][] copyShape(int[][] base){
        int[][] copy = new int[base.length][];
        for (int i = 0; i < base.length; i++){
            copy[i] = base[i].clone();
        }
        return copy;
    }

    void rotate(){
        int height = shape.length;
        int width = shape[0].length;
        int[][] rotated = new int[width][height];
        for (int row = 0; row < height; row++){
            for (int col = 0; col < width; col++){
                rotated[col][height - 1 - row] = shape[row][col];
            }
        }
        shape = rotated;
    }

    private Color getColor(int type){
        return switch (type){
            case 0 -> Color.CYAN;
            case 1 -> Color.YELLOW;
            case 2 -> Color.MAGENTA;
            case 3 -> Color.ORANGE;
            case 4 -> Color.BLUE;
            case 5 -> Color.GREEN;
            case 6 -> Color.RED;
            default -> Color.GRAY;
         };
        }
    }


class LeaderBoard{ // bang xep hang
    List <Player> players = new ArrayList<Player>();
    
    public void addPlayer(Player player){
        players.add(player);
        sortLeaderboard();
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
}

class LeaderBoardScreen extends JPanel{
    public LeaderBoardScreen(CardLayout card, JPanel parent, LeaderBoard lbLogic) {
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

class Controller { // dieu khien
    private Board board;
    private GameManage gameManage;

    public Controller(Board board, GameManage gameManage){
        this.board=board;
        this.gameManage = gameManage;
        setupKeyBindings();
    }

    private void setupKeyBindings(){
        InputMap im = board.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = board.getActionMap();

        im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        am.put("moveLeft", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (board.current != null &&
                    board.canPlace(board.current, board.current.row, board.current.column - 1)){
                    board.current.column--;
                    board.repaint();
                }
            }
        });

        im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        am.put("moveRight", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (board.current != null &&
                    board.canPlace(board.current, board.current.row, board.current.column + 1)){
                    board.current.column++;
                    board.repaint();
                }
            }
        });

        im.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        am.put("moveDown", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (board.current != null &&
                    board.canPlace(board.current, board.current.row + 1, board.current.column)){
                    board.current.row++;
                    board.repaint();
                }
            }
        });

        im.put(KeyStroke.getKeyStroke("UP"), "rotate");
        am.put("rotate", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (board.current != null){
                    int[][] oldShape = Tetromino.copyShape(board.current.shape);

                    board.current.rotate();

                    if (!board.canPlace(board.current, board.current.row, board.current.column)) {
                        board.current.shape = oldShape;
                    }
                    board.repaint();
                }
            }
        });

        im.put(KeyStroke.getKeyStroke("SPACE"), "drop");
        am.put("drop", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (board.current != null){
                    while (board.canPlace(board.current, board.current.row + 1, board.current.column)){
                        board.current.row++;
                    }
                    board.place(board.current);
                    gameManage.clearLines();
                    board.setCurrent(null);
                    board.repaint();
                    gameManage.timer.start();
                }
            }
        });
    }
}


class GameManage{ // random Tetromino, xoa khoi(hang ngang du 10 o)
    public String Name;
    private Board board;
    public Timer timer;
    private boolean isPause = false;
    private boolean isGameOver = false;
    private GameScreen gameScreen;

    private int score = 0;
    private int lines = 0;

    public int getScore(){
        return score;
    }
    public int getLines(){
        return lines;
    }

    private Random random = new Random();

    private LeaderBoard leaderBoard;

    public GameManage(Board board, LeaderBoard leaderBoard, GameScreen gameScreen){
        this.board = board;
        this.leaderBoard = leaderBoard;
        this.gameScreen = gameScreen;

        Name = JOptionPane.showInputDialog("ENTER YOUR NAME");
        if(Name == null || Name.isEmpty()){
            Name = "Player";
        }
        spawnNewTetromino();
        startGameLoop();
    }

    private void startGameLoop(){
        timer = new Timer(800, e -> update());
        timer.start();
        }

        private void update(){
        if (isPause || isGameOver) return;
        if(board.current == null){
            spawnNewTetromino();
            return;
        }   
        if(board.canPlace(board.current, board.current.row + 1, board.current.column)){
            board.current.row++;
        } else {
            board.place(board.current);
            clearLines();
            board.repaint();
            board.setCurrent(null);
        }
        board.repaint();
    }

    private void spawnNewTetromino(){
        Tetromino t = new Tetromino(random.nextInt(7));

        if(!board.canPlace(t, t.row, t.column)){
           gameOver();
            return;
        }
        board.setCurrent(t);
    }
    private void updateLabels(){
            gameScreen.updateScore(score, lines);
        }

    public void clearLines(){
        int clear = 0;

        for(int row = Board.ROWS - 1; row >= 0; row--){
            boolean fullLine = true;

            for(int col = 0; col < Board.COLUMNS; col++){
                if(board.grid[row][col] == null){
                    fullLine = false;
                    break;
                }
            }
            if(fullLine){
                removeRow(row);
                clear++;  
                row++;      
            }
        }

        if(clear > 0){
            lines += clear;
            score += calculateScore(clear);
            updateLabels();
            board.repaint();
        }
    }

    private void removeRow(int row){
        for (int r = row; r > 0; r--){
            System.arraycopy(board.grid[r - 1], 0, board.grid[r], 0, Board.COLUMNS);
        }
        for(int c = 0; c < Board.COLUMNS; c++){
            board.grid[0][c] = null;
        }
    }

    private int calculateScore(int lines){
        return switch(lines){
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };
    }

    private void gameOver(){
        isGameOver = true;
        timer.stop();

        Player p = new Player(Name, score, score);
        leaderBoard.addPlayer(p);

        int choice = JOptionPane.showConfirmDialog(board,
            "GAME OVER!!\nSCORE: " + score + "\nRETRY?",
            "GAME OVER", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION){
                resetGame();
            } 
        }

            public void resetGame(){
                for (int r = 0; r < Board.ROWS; r++){
                    for (int c = 0; c < Board.COLUMNS; c++){
                        board.grid[r][c] = null;
                    }
                }
                score = 0;
                lines = 0;
                isGameOver = false;
                spawnNewTetromino();
                timer.start();
            }

            public void togglePause(){
            isPause = !isPause;
        }
    }


public class Tetris {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Tetris Game");
        CardLayout card = new CardLayout();
        JPanel container = new JPanel(card);

        LeaderBoard lbLogic = new LeaderBoard();

        StartMenu startMenu = new StartMenu(card, container, lbLogic);
        GameScreen gameScreen = new GameScreen(card, container, lbLogic);
        LeaderBoardScreen leaderboardScreen = new LeaderBoardScreen(card, container, lbLogic);

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



