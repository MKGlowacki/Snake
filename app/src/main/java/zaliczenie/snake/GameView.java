package zaliczenie.snake;


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
    private int points = 0;
    private int[][] snake = new int[1000][2];
    private int[] gameBoardX = new int[14], gameBoardY = new int[22];
    private int snakeLength = 1;
    float lastTouchX;
    boolean ifHead = true;
    int tempX, tempY;
    double appleXF, appleYF;
    int appleX, appleY;
    boolean appleDiff;

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
        direction = 0;
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


                //System.out.println(snake.length);

            }
        });


    }

    public void startGame(){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                snakeLength = points+1;
                snakeMove(direction);
                if(snakeHeadRect.top < 0 || snakeHeadRect.bottom > getHeight() || snakeHeadRect.left < 0 || snakeHeadRect.right > getWidth()){
                    return;
                }

                if (snake[0][0] == appleX && snake[0][1] == appleY){
                    points++;

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

        appleRect.left = (int)appleX;
        appleRect.right = (int)appleX + snakeBodySize;
        appleRect.top = (int)appleY;
        appleRect.bottom = (int)appleY + snakeBodySize;

        canvas.drawRect(appleRect, applePaint);

        for(int i=0;i<snakeLength;i++ ){

            snakeHeadRect.left = snake[i][0];
            snakeHeadRect.right = snake[i][0]+snakeBodySize;
            snakeHeadRect.top = snake[i][1];
            snakeHeadRect.bottom = snake[i][1]+snakeBodySize;

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

