
import javax.swing.*;
import java.util.Random;


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
