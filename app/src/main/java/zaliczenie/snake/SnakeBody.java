package zaliczenie.snake;

public class SnakeBody {
    public int x1,x2,y1,y2,size;


    public SnakeBody(int x, int y, int s) {
        x1 = x;
        y1 = y;
        size = s;
        x2 = x1 + size;
        y2 = y1 + size;
    }
}
