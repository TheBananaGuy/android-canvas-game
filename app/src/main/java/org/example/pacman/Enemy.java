package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

/**
 * Created by Sam Mullen on 02-Nov-17.
 *
 *
 */

public class Enemy {
    private Bitmap enemyBitmap;
    public int enemyx, enemyy;

    public Enemy(Context context, int speed, int minx, int miny, int pacx, int pacy, int padx, int pady) {
        Random rand = new Random();
        do {
            enemyx = padx + (rand.nextInt(minx))*speed;
            enemyy = pady + (rand.nextInt(miny))*speed;
        } while(enemyx == pacx && enemyy== pacy);
        enemyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
    }

    public Bitmap getEnemyBitmap() {
        return enemyBitmap;
    }

    public int getEnemyx() {
        return enemyx;
    }

    public int getEnemyy() {
        return enemyy;
    }
}
