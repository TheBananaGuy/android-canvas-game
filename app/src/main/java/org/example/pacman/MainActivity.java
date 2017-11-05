package org.example.pacman;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.TimerTask;

import static android.R.attr.button;

public class MainActivity extends Activity {
    GameView gameView;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        gameView =  findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);


        game = new Game(this,textView);
        game.setGameView(gameView);
        gameView.setGame(game);
        game.newGame();
        game.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        }, 0, 500);

        // move left
        Button buttonLeft = findViewById(R.id.moveLeft);
        buttonLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.direction = "left";
            }
        });
        // move right
        Button buttonRight = findViewById(R.id.moveRight);
        buttonRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.direction = "right";
            }
        });
        // move up
        Button buttonUp = findViewById(R.id.moveUp);
        buttonUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.direction = "up";
            }
        });
        // move down
        Button buttonDown = findViewById(R.id.moveDown);
        buttonDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.direction = "down";
            }
        });
        // pause
        Button buttonPause = findViewById(R.id.pause);
        buttonPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.pauseGame();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        //just to make sure if the app is killed, that we stop the timer.
        game.timer.cancel();
    }

    private void TimerMethod()
    {
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        @Override
        public void run() {
            if (game.gameState && !game.pauseState)
            {
                game.runner++;
                if (game.runner%2 == 0)
                {
                    game.enemyStep();
                }
                game.pacStep(game.direction);

            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"Sorry, we don't have any settings yet",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_restart) {
            game.newGame();
            Toast.makeText(this,"game is reset",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
