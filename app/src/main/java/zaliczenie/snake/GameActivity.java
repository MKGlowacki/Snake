package zaliczenie.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.telephony.ims.RcsUceAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {


    GameView gameView;
    TextView text;
    TextView scoreText;

    boolean gameStart = false;

    public int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        text = findViewById(R.id.startTextView);
        text.setVisibility(View.VISIBLE);

        scoreText = findViewById(R.id.scoreTextView);
        scoreText.setText(""+score);

        gameView = findViewById(R.id.gameViewId);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //po tapnieciu w ekran odpalam moje customowe view zawierajace gre

        if(!gameStart){
            text.setVisibility(View.INVISIBLE);
            gameView.startGame();
        }

        gameStart = true;

        return super.onTouchEvent(event);
    }


    //dodaje punkciki na wyswietlaczu
    public void addPoint(int score){
        this.score = score;
        scoreText.setText(""+this.score);
    }

    //przekazuje GameOverActivity wynik i odpalam GameOverActivity
    public void gameOver(int score){
        Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
        intent.putExtra("Score", score);
        startActivity(intent);

    }



}