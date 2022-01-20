package zaliczenie.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);



        TextView finalScore = findViewById(R.id.finalScoreTextView);
        TextView highScore = findViewById(R.id.highScoreTextView);
        View playButton = findViewById(R.id.playAgainButton);

        //score jest przekazywany z GameActivity
        int score = getIntent().getIntExtra("Score", 0);

        finalScore.setText("Score: "+score );

        SharedPreferences sharedPreferences = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);

        //ustawiam najwyzszy wynik

        int hScore = sharedPreferences.getInt("HIGH_SCORE", 0);

        if (score > hScore) {


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.apply();
            hScore = score;

        }
        highScore.setText("HIGH SCORE: " + hScore);



        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameActivity.class));

            }
        });
    }
}