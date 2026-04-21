import java.awt.*;


public class Tetromino { // khoi
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

