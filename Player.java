public class Player {  // nguoi choi + highscore
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
