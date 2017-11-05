package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Timer;
import java.util.Random;

/**
 *
 * This class should contain all your game logic
 */

public class Game {

    private Context context;    // context is a reference to the activity
    private int points = 0; // how points do we have

    private Bitmap pacBitmap;   // bitmap of the pacman
    private TextView pointsView;    // textview reference to points
    private Button pauseButton;
    private int pacx, pacy; // pacman coordinates
    private ArrayList<GoldCoin> coins = new ArrayList<>();  // the list of goldcoins - initially empty
    private ArrayList<Enemy> enemies = new ArrayList<>();   // the list of enemies - initially empty
    private GameView gameView;  //a reference to the gameview
    private int h,w;    // height and width of screen
    private int minx, miny, padx, pady; // field dimensions for the steps and padding around the field
    private int coinAmount = 4; // amount of our coins
    private int enemyAmount = 2;    // amount of our enemies

    public String direction;
    public boolean gameState = false;
    public boolean pauseState = false;
    public Timer timer;
    public int speed;
    public int runner = 0;

    public Game(Context context, TextView view)
    {

        this.context = context;
        this.pointsView = view;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        speed = pacBitmap.getWidth();
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    public void newGame()
    {
        Log.d("Game", "New game is set");
        points = 0;
        pacx = 0;     // left
        pacy = 0;     // top
        direction = "none";
        timer = new Timer();
        gameState = true;
        pauseState = false;
        coins = new ArrayList<>();
        enemies = new ArrayList<>();
        pointsView.setText(context.getResources().getString(R.string.points)+points);
        gameView.invalidate(); //redraw screen
    }

    public void setSize(int h, int w)
    {
        Random rand = new Random();
        this.w = w;
        this.h = h;
        padx = (this.w-(this.w/speed)*speed)/2;
        pady = (this.h-(this.h/speed)*speed)/2;
        minx = (this.w-padx*2)/speed;
        miny = (this.h-pady*2)/speed;
        if (pacx == 0 && pacy == 0) {
            pacx = padx + rand.nextInt(minx)*speed;     // random
            pacy = pady + rand.nextInt(miny)*speed;     // random
        }
        // coin placed
        if (coins.size()==0 && gameState) {
            for (int i=0; i < coinAmount; i++) {
                coins.add(new GoldCoin(context, speed, minx, miny, pacx, pacy, padx, pady));
            }
        }
        if (enemies.size()==0) {
            for (int i=0; i < enemyAmount; i++) {
                enemies.add(new Enemy(context, speed, minx, miny, pacx, pacy, padx, pady));
            }
        }
        Log.d("sizzze", "setSize: "+speed+" "+minx+" "+miny);
    }

//    deprecated pacman moving mechanic
//
//    public void movePacmanX(int pixels)
//    {
//        if (pacx+pixels+pacBitmap.getWidth()<w && pacx+pixels>0) {  //still within our boundaries?
//            pacx = pacx + pixels;
//            doCollisionCheck();
//            gameView.invalidate();
//        }
//    }
//
//    public void movePacmanY(int pixels)
//    {
//        if (pacy+pixels+pacBitmap.getHeight()<h && pacy+pixels>0) { //still within our boundaries?
//            pacy = pacy + pixels;
//            doCollisionCheck();
//            gameView.invalidate();
//        }
//    }

    public void pacStep(String vector)
    {
        if(vector == "left") {
            if (pacx-speed+pacBitmap.getWidth()<w && pacx-speed>0) {  //still within our boundaries?
                pacx = pacx - speed;
            }
        }
        if(vector == "right") {
            if (pacx+speed+pacBitmap.getWidth()<w && pacx+speed>0) {  //still within our boundaries?
                pacx = pacx + speed;
            }
        }
        if(vector == "up") {
            if (pacy-speed+pacBitmap.getWidth()<h && pacy-speed>0) {  //still within our boundaries?
                pacy = pacy - speed;
            }
        }
        if(vector == "down") {
            if (pacy+speed+pacBitmap.getWidth()<h && pacy+speed>0) {  //still within our boundaries?
                pacy = pacy + speed;
            }
        }
        doCollisionCheck();
        gameView.invalidate();
    }

    public void enemyStep()
    {
        Random go = new Random(); // declare a random number generator
        for(Enemy enemy :getEnemies())  // get all the enemies and assign for each and one of them
        {
            int step;   // declare the direction of the step;
            boolean dontGoOutside = false;  // to check if enemy would go outside the zone
            do  // while we're not sure if it's stepping within the game zone
            {
                step = go.nextInt(4)+1;   // set a random direction to go to: 1 = left, 2 = right, 3 = up, 4 = down.
                switch(step)  // check the direction it's going
                {   // if it's not going to pass the border - we can continue
                    case 1: if(enemy.getEnemyx()-speed+enemy.getEnemyBitmap().getWidth()<w && enemy.getEnemyx()-speed>0){dontGoOutside=true;}
                        break;
                    case 2: if(enemy.getEnemyx()+speed+enemy.getEnemyBitmap().getWidth()<w && enemy.getEnemyx()+speed>0){dontGoOutside=true;}
                        break;
                    case 3: if(enemy.getEnemyy()-speed+enemy.getEnemyBitmap().getWidth()<h && enemy.getEnemyy()-speed>0){dontGoOutside=true;}
                        break;
                    case 4: if(enemy.getEnemyy()+speed+enemy.getEnemyBitmap().getWidth()<h && enemy.getEnemyy()+speed>0){dontGoOutside=true;}
                        break;
                }
            } while (!dontGoOutside);
            /*
            * if we would use an ordinary while loop, the upcoming switch would not work
            * so we use do while to make sure that our step value is set
            */
            if(dontGoOutside)   // double check the upcoming step
            {
                switch(step)  // check the direction it's going
                {   // and set the step according to the direction
                    case 1: enemy.enemyx -= speed;
                        break;
                    case 2: enemy.enemyx += speed;
                        break;
                    case 3: enemy.enemyy -= speed;
                        break;
                    case 4: enemy.enemyy += speed;
                        break;
                }
            }
        }
    }

    public void doCollisionCheck()
    {
        for (int i=0; i < getCoins().size(); i++) {
            GoldCoin coin = getCoins().get(i);
            if (pacx == coin.getCoinx() && pacy == coin.getCoiny()) {
                Log.d("coinCollision", "got coin at: "+pacx+" "+pacy+" at coin position "+coin.getCoinx()+" "+coin.getCoiny());
                coins.remove(coin);
                points = getPoints() + coin.getCoinPrice();
                pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
                doGameStateCheck();
            }
        }
        for (int i=0; i < getEnemies().size(); i++) {
            Enemy enemy = getEnemies().get(i);
            if (pacx == enemy.getEnemyx() && pacy == enemy.getEnemyy()) {
                Log.d("enemyCollision", "died at: "+pacx+" "+pacy+" at enemy position "+enemy.getEnemyx()+" "+enemy.getEnemyy());
                gameState = false;
                Toast.makeText(context,"You died",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void pauseGame()
    {
        if (!pauseState)
        {
            pauseState = true;
            Toast.makeText(context,"paused",Toast.LENGTH_SHORT).show();
        }
        else
        {
            pauseState = false;
            Toast.makeText(context,"continued",Toast.LENGTH_SHORT).show();
        }
    }

    public void doGameStateCheck()
    {
        if (coins.size()==0) {
            gameState = false;
            Log.d("gameState", "game has ended");
            Toast.makeText(context,"All coins collected",Toast.LENGTH_LONG).show();
        }
    }

    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    public int getPoints()
    {
        return points;
    }

    public ArrayList<GoldCoin> getCoins() { return coins; }

    public ArrayList<Enemy> getEnemies() { return enemies; }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }


}
