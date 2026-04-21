import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;


public class Controller {
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
