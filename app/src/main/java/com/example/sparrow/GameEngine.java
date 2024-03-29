package com.example.sparrow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {
    private final String TAG = "SPARROW";




    // game thread variables
    private Thread gameThread = null;
    private volatile boolean gameIsRunning;

    // drawing variables
    private Canvas canvas;
    private Paint paintbrush;
    private SurfaceHolder holder;

    // Screen resolution varaibles
    private int screenWidth;
    private int screenHeight;

    // VISIBLE GAME PLAY AREA
    // These variables are set in the constructor
    int VISIBLE_LEFT;
    int VISIBLE_TOP;
    int VISIBLE_RIGHT;
    int VISIBLE_BOTTOM;

    // SPRITES
    Square bullet;
    int SQUARE_WIDTH = 300;

    Square enemy;

    Sprite player;
    Sprite sparrow;
    Sprite cat;
    Square cage;

    int randmX;
    int randmY;

    ArrayList<Square> bullets = new ArrayList<Square>();

    // GAME STATS
    int score = 0;

    public GameEngine(Context context, int screenW, int screenH) {
        super(context);

        // make bullets
        this.bullets.add(new Square(context, 100, 600, 50));
        this.bullets.add(new Square(context, 150, 450, 50));
        this.bullets.add(new Square(context, 200, 300, 50));
        this.bullets.add(new Square(context, 300, 150, 50));


//        //generate random number for sparrow

        final int randomX = new Random().nextInt(600) + 200;
        final int randomY = new Random().nextInt(300) + 400;




        // intialize the drawing variables
        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        // set screen height and width
        this.screenWidth = screenW;
        this.screenHeight = screenH;

        // setup visible game play area variables
        this.VISIBLE_LEFT = 20;
        this.VISIBLE_TOP = 10;
        this.VISIBLE_RIGHT = this.screenWidth - 20;
        this.VISIBLE_BOTTOM = (int) (this.screenHeight * 0.8);


        // initalize sprites
        this.player = new Sprite(this.getContext(), 100, screenHeight - 500, R.drawable.player64);
        this.sparrow = new Sprite(this.getContext(), randomX, randomY, R.drawable.bird64);
        this.cat = new Sprite(this.getContext(), screenWidth/3, screenHeight - 500, R.drawable.cat64);
//        this.enemy = new Square(this.getContext(), screenWidth - 400, 150, SQUARE_WIDTH);

        this.cage = new Square(this.getContext(), screenWidth - 400, 10, SQUARE_WIDTH);


    }

    @Override
    public void run() {
        while (gameIsRunning == true) {
            updateGame();    // updating positions of stuff
            redrawSprites(); // drawing the stuff
            controlFPS();
        }
    }

        boolean movingLeft = true;
        boolean isCatMoving = true;
        boolean isBirdMoving = true;

    // Game Loop methods
    public void updateGame() {

        // Moving cage to left and right
        if(movingLeft == true) {
            this.cage.setxPosition(this.cage.getxPosition() + 20);
            if(this.cage.getxPosition() >= this.screenWidth - 280)
            {
                movingLeft = false;
            }
        }
        if(movingLeft == false)
        {
            this.cage.setxPosition(this.cage.getxPosition() -20);
            if(this.cage.getxPosition()<=0)
            {
                movingLeft = true;
            }
        }

    cage.updateHitbox();
        // Moving cat left and right

        if(isCatMoving == true) {
            this.cat.setxPosition(this.cat.getxPosition() + 30);
            if(this.cat.getxPosition() >= this.screenWidth - 280)
            {
                isCatMoving = false;
            }
        }
        if(isCatMoving == false)
        {
            this.cat.setxPosition(this.cat.getxPosition() - 30);
            if(this.cat.getxPosition()<=0)
            {
                isCatMoving = true;
            }
        }

cat.updateHitbox();
        // Moving bird

        if(isBirdMoving == true) {
            this.sparrow.setxPosition(this.sparrow.getxPosition() + 35);
            if(this.sparrow.getxPosition() >= this.screenWidth - 280)
            {
                isBirdMoving = false;
            }
        }
        if(isBirdMoving == false)
        {
            this.sparrow.setxPosition(this.sparrow.getxPosition() - 35);
            if(this.sparrow.getxPosition()<=0)
            {
                isBirdMoving = true;
            }
        }

        // Moving bullets

         bullet = this.bullets.get(3);
        if(this.bullet.getHitbox().intersect(this.cage.getHitbox()))
        {
            this.cage.setyPosition(this.cage.getxPosition() + 500);
            cage.updateHitbox();

        }
        if(this.cage.getHitbox().intersect(this.cat.getHitbox()))
        {
           // score = "winner";
            pauseGame();


        }
        else
        {
           // score = "looser";

        }








    }


    public void outputVisibleArea() {
        Log.d(TAG, "DEBUG: The visible area of the screen is:");
        Log.d(TAG, "DEBUG: Maximum w,h = " + this.screenWidth +  "," + this.screenHeight);
        Log.d(TAG, "DEBUG: Visible w,h =" + VISIBLE_RIGHT + "," + VISIBLE_BOTTOM);
        Log.d(TAG, "-------------------------------------");
    }



    public void redrawSprites() {
        if (holder.getSurface().isValid()) {

            // initialize the canvas
            canvas = holder.lockCanvas();
            // --------------------------------

            // set the game's background color
            canvas.drawColor(Color.argb(255,255,255,255));

            // setup stroke style and width
            paintbrush.setStyle(Paint.Style.FILL);
            paintbrush.setStrokeWidth(8);

            // --------------------------------------------------------
            // draw boundaries of the visible space of app
            // --------------------------------------------------------
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setColor(Color.argb(255, 0, 128, 0));

            canvas.drawRect(VISIBLE_LEFT, VISIBLE_TOP, VISIBLE_RIGHT, VISIBLE_BOTTOM, paintbrush);
            this.outputVisibleArea();

            // --------------------------------------------------------
            // draw player and sparrow
            // --------------------------------------------------------

            // 1. player
            canvas.drawBitmap(this.player.getImage(), this.player.getxPosition(), this.player.getyPosition(), paintbrush);

            // 2. sparrow
            canvas.drawBitmap(this.sparrow.getImage(), this.sparrow.getxPosition(), this.sparrow.getyPosition(), paintbrush);

            // 3. cat
            canvas.drawBitmap(this.cat.getImage(), this.cat.getxPosition(), this.cat.getyPosition(), paintbrush);

            // 4. Cage
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            canvas.drawRect(this.cage.getxPosition(),
                    this.cage.getyPosition(),
                    this.cage.getxPosition() + this.cage.getWidth(),
                    this.cage.getyPosition() + this.cage.getWidth(),
                    paintbrush);

            // 5. draw bullets

            // DRAW ALL THE BULLETS
//                        for (int i = 0; i < this.bullets.size(); i++) {
//                            // 1. get the (x,y) of the bullet
//                            Square b = this.bullets.get(i);
//
//                            int x = b.getxPosition();
//                            int y = b.getyPosition();
//
//                            // 2. draw the bullet
//                            paintbrush.setColor(Color.BLACK);
//                            paintbrush.setStyle(Paint.Style.FILL);
//                            canvas.drawRect(
//                                    x,
//                                    y,
//                                    x + b.getWidth(),
//                                    y + b.getWidth(),
//                                    paintbrush
//                            );
//
//
//                            // 3. draw the bullet's hitbox
//                            paintbrush.setColor(Color.GREEN);
//                            paintbrush.setStyle(Paint.Style.STROKE);
//                            canvas.drawRect(
//                                    b.getHitbox(),
//                                    paintbrush
//                            );
//                        }





            for (int i = 0; i < this.bullets.size(); i++) {
                // 1. get the (x,y) of the bullet
                Square b = this.bullets.get(i);
                int x = b.getxPosition();
                int y = b.getyPosition();

                // 2. draw the bullet
                paintbrush.setColor(Color.BLACK);
                paintbrush.setStyle(Paint.Style.FILL);
                canvas.drawRect(x, y, x+b.getWidth(), y+b.getWidth(), paintbrush);

                // 3. draw the bullet's hitbox
                paintbrush.setColor(Color.GREEN);
                paintbrush.setStyle(Paint.Style.STROKE);
                canvas.drawRect(
                        b.getHitbox(),
                        paintbrush
                );
            }

//            square = new Sprite(getContext(), 1000,  100, SIZE, SIZE);



            // --------------------------------------------------------
            // draw hitbox on player
            // --------------------------------------------------------
            Rect r = player.getHitbox();
            Rect cageHitbox = cage.getHitbox();
            Rect bulletHitbox = bullet.getHitbox();
            Rect catHitbox = cat.getHitbox();
            paintbrush.setStyle(Paint.Style.STROKE);
            canvas.drawRect(r, paintbrush);
            canvas.drawRect(cageHitbox, paintbrush);
            canvas.drawRect(bulletHitbox, paintbrush);
            canvas.drawRect(catHitbox, paintbrush);



            // --------------------------------------------------------
            // draw hitbox on player
            // --------------------------------------------------------
            paintbrush.setTextSize(60);
            paintbrush.setStrokeWidth(5);
            String screenInfo = "Screen size: (" + this.screenWidth + "," + this.screenHeight + ")";
            canvas.drawText(screenInfo, 10, 100, paintbrush);



            // --------------------------------
            holder.unlockCanvasAndPost(canvas);
        }

    }

    public void controlFPS() {
        try {
            gameThread.sleep(17);
        }
        catch (InterruptedException e) {

        }
    }


//    public void restartGame(){
//        run();
//    }




    // Deal with user input
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
//                square.x = (int) event.getX();
//                square.y = (int) event.getY();
                break;
            case MotionEvent.ACTION_DOWN:
                break;
       }
        return true;
    }

    // Game status - pause & resume
    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        }
        catch (InterruptedException e) {

        }
    }
    public void  resumeGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}

