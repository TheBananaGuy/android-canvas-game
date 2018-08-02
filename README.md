# Android Pac-Man game using canvas

## Disclaimer

This was an educational projecct during my studies in BAAA (Business Academy Aarhus)

## Overview

This is supposed to be a simple canvas-based android game.
The mechanics are similar to the ones in the game Pac-Man (collect all coins to win).

### Current status

Game starts and works fine.
On a new game instance Pacman does not move until you specify a destination.
The edges are calculated automatically.
Step size is calculated and depends on your device size.

- Pacman
    - speed: 1 step per half a second.
    - position: randomly generated.
- Coins
    - position: randomly generated.
    - amount: 4.
- Enemies
    - speed: 1 step per one whole second.
    - position: randomly generated.
    - AI: random steps in X or Y axis.
    - amount: 2

Resetting the game is possible through the menu.
Collecting all coins will result in a winning message.
Getting struck by an enemy will result in a losing message.

NB!: Collecting the last coin and getting hit by a nearby enemy simultaneously is possible, so be careful to calculate your steps.

### Testing environment

At the beginning, the project was mostly tested on a virtual device. Somewhere mid-project nothing was working on a physical device, so it needed to be resolved. at the end of the project, everything was primarily tested on a physical device.

- Physical
    - LGE Nexus 5X (Android 8.0.0, API 26)
    - LGE Nexus 5 (Android 6.0.1, API 23)
- Virtual
    - Nexus 5X API 26 x86

### Features TODO list

#### Mandatory

- ~~Simple Pacman movement control (buttons)~~
- ~~Coin collection and score display~~
- ~~Enemies in the game (>0)~~
- ~~Game restart~~
- ~~Game pause~~

#### Optional

- Alternative Pacman movement control
- Game highscore
- Levels
- Highscore sharing
- Obstacles

## Code highlights

### Author's notes

#### Enemy moving mechanic

```java
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
```

### Bug encounters

List of bugs encountered during the development process

#### Collision check for the coins

Checking collisions with a foreach loop on the coins array results in a game crash, but only if the coin amount is more than one

```java
public void doCollisionCheck()
{
    for (GoldCoin coin: getCoins()) {
        if (pacx == coin.getCoinx() && pacy == coin.getCoiny()) {
            coins.remove(coin);
        }
    }
}
```

Instead of the foreach loop, we count the amount of coins in the array with a for loop and check by looking up the index in the array according to what we got from its length

```java
public void doCollisionCheck()
{
    for (int i=0; i < getCoins().size(); i++) {
        GoldCoin coin = getCoins().get(i);
        if (pacx == coin.getCoinx() && pacy == coin.getCoiny()) {
            coins.remove(coin);
        }
    }
}
```

#### Resetting coins on a game reset

Since on each redraw of the screen we check if the coin array is empty or not, I tried removing all the coins on a new game function, but it resulted in a game crash

```java
public void newGame()
{
    ...
    for (GoldCoin coin: game.getCoins()) {
        game.getCoins().remove(coin);
    }
    ...
    gameView.invalidate();
}
```

Instead of removing the coins on a new game we just need to reset the coin array itself

```java
public void newGame()
{
    ...
    coins = new ArrayList<>();
    ...
    gameView.invalidate();
}
```
