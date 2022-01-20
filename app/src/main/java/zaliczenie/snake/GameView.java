package zaliczenie.snake;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameView extends View {

    private static final int SQUARE_SIZE_DEF = 300;
    private Rect snakeHeadRect, appleRect;
    private Paint snakeHeadPaint, applePaint;
    private int snakeBodySize;
    private int gameBgColor;
    private int direction;
    private SnakeBody snakeHead;
    private int points;
    private int[][] snake = new int[1000][2];
    private int[] gameBoardX = new int[14], gameBoardY = new int[22];
    private int snakeLength;
    float lastTouchX;
    double appleXF, appleYF;
    int appleX, appleY;
    boolean appleDiff, snakeDiff;
    GameActivity gameActivity;


    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public GameView(Context context) {
        super(context);
        init(null);
    }

    public void init(@Nullable AttributeSet set ) {
        snakeLength = 1;
        points = 0;
        direction = 3;
        snakeHeadRect = new Rect();
        snakeHeadPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        appleRect = new Rect();

        gameBoardX[0] = 0;
        gameBoardY[0] = 0;


        applePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        applePaint.setColor(Color.RED);



        snakeHeadPaint.setColor(Color.GREEN);


        if (set == null) return;

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.GameView);
        gameBgColor = ta.getColor(R.styleable.GameView_game_bg_color, Color.YELLOW);
        snakeBodySize = ta.getDimensionPixelSize(R.styleable.GameView_snake_body_size, SQUARE_SIZE_DEF);
        ta.recycle();




        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);


                for(int i = 1; i<(getHeight()/snakeBodySize); i++){

                    if(i<(getWidth()/snakeBodySize)){
                        gameBoardX[i] = gameBoardX[i-1] + snakeBodySize;
                    }

                    gameBoardY[i] = gameBoardY[i-1] + snakeBodySize;
                }

                // F = float
                appleXF = Math.random() * (gameBoardX.length-1);
                appleYF= Math.random() * (gameBoardY.length-1);

                appleX = gameBoardX[(int) Math.round(appleXF)] ;
                appleY = gameBoardY[(int) Math.round(appleYF)];

                snakeHead = new SnakeBody(gameBoardX[gameBoardX.length/2],gameBoardY[gameBoardY.length/2], snakeBodySize);



                snake[0][0] = snakeHead.x1;
                snake[0][1] = snakeHead.y1;




            }
        });


    }

    public void startGame(){

        Timer gameTimer = new Timer();
        TextView pointCounter = findViewById(R.id.text);

        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                snakeDiff = true;
                snakeLength = points+1;
                snakeMove(direction);

                for (int i = 1; i < snakeLength; i++){
                    if(snake[0][0]==snake[i][0] && snake[0][1]==snake[i][1]){
                        System.out.println("Snake diff: "+snakeDiff);
                        snakeDiff = false;
                    }
                }
                System.out.println("snake[0][0]: "+snake[0][0]+" snake[0][1]: "+ snake[0][1] +" MaxH: "+ getHeight() + " MaxW: "+ getWidth());
                if(snakeDiff == false || snake[0][0] < 0 || snake[0][0] >= getWidth() || snake[0][1] < 0 || snake[0][1] >= getHeight()){
                    gameTimer.cancel();
                    gameTimer.purge();
                    ((GameActivity)getContext()).gameOver(points);
                    return;
                }

                if (snake[0][0] == appleX && snake[0][1] == appleY){
                    points++;
                    ((GameActivity)getContext()).addPoint(points);

                    newPoint();
                }

                postInvalidate();
            }
        },1500,600);


    }




    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        canvas.drawColor(gameBgColor);

        appleRect.left = (int)appleX +5;
        appleRect.right = (int)appleX + snakeBodySize-10;
        appleRect.top = (int)appleY+5;
        appleRect.bottom = (int)appleY + snakeBodySize-10;

        canvas.drawRect(appleRect, applePaint);

        for(int i=0;i<snakeLength;i++ ){

            snakeHeadRect.left = snake[i][0] + 5;
            snakeHeadRect.right = snake[i][0]+snakeBodySize - 10;
            snakeHeadRect.top = snake[i][1] + 5;
            snakeHeadRect.bottom = snake[i][1]+snakeBodySize - 10;

            canvas.drawRect(snakeHeadRect,snakeHeadPaint);
        }

    }




    private void snakeMove(int direction) {

        for (int i = snakeLength-1; i>0;i--){
            snake[i][0] = snake[i-1][0];
            snake[i][1] = snake[i-1][1];
        }

        switch (direction) {
            case 0:
                snake[0][1] -= snakeBodySize;
                break;
            case 1:
                snake[0][0] += snakeBodySize;
                break;
            case 2:
                snake[0][1] += snakeBodySize;
                break;
            case 3:
                snake[0][0] -= snakeBodySize;
                break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        lastTouchX = event.getX();

        if(lastTouchX > getWidth()/2) {
            if(direction == 3){
                direction = 0;
            } else{
                direction++;
            }
        } else{
            if (direction == 0){
                direction = 3;
            } else {
                direction--;
            }
        }

        return super.onTouchEvent(event);
    }




    public void newPoint(){

        appleDiff = true;

        do{
            appleXF = Math.random() * (gameBoardX.length-1);
            appleYF = Math.random() * (gameBoardY.length-1);
            appleX = gameBoardX[(int) Math.round(appleXF)];
            appleY = gameBoardY[(int) Math.round(appleYF)];
            appleDiff = true;
            for (int i = 0; i < snakeLength; i++){
                if(appleX == snake[i][0] && appleY== snake[i][1]){
                    appleDiff = false;
                }
            }
        }while (!appleDiff);






    }

}

