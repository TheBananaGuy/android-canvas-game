package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin {
    private Bitmap coinBitmap;
    private int coinx, coiny, coinPrice;

    public GoldCoin(Context context, int speed, int minx, int miny, int pacx, int pacy, int padx, int pady) {
        Random rand = new Random();
        do {
            coinx = padx + (rand.nextInt(minx))*speed;
            coiny = pady + (rand.nextInt(miny))*speed;
        } while(coinx == pacx && coiny== pacy);
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);
        coinPrice = 10;
    }

    public Bitmap getCoinBitmap() {
        return coinBitmap;
    }

    public int getCoinx() {
        return coinx;
    }

    public int getCoiny() {
        return coiny;
    }

    public int getCoinPrice() {
        return coinPrice;
    }
}
