package zaliczenie.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    GameView gameView;
    View playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = (GameView) findViewById(R.id.gameViewId);


        playButton = findViewById(R.id.playButtonImageView);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.setVisibility(View.INVISIBLE);
                gameView.setVisibility(View.VISIBLE);
                gameView.startGame();

            }
        });


    }


}