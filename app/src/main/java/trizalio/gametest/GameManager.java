package trizalio.gametest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by trizalio on 15.11.14.
 */
public class GameManager extends Thread {

    private static final String LOG_TAG = "GameManager";
    public static final double LINE_SIZE = 150;
    private static final double DISTANCE_TO_HERO = 200;
    private static final double CAM_HEIGHT = 100;
    private static final long OBSTACLE_FREQUENCY = 1000;
    private final int BASE_SIZE = 100;
    private final int SLIDE_SIZE = 100;
    private int mCenterX;
    private int mCenterY;
    private double mScale;

    private Context mContext;

    private Hero mHero;

    private double mCamX;
    private double mCamY;
    private double mCamZ;

    private long mLastTimeGeneratedObstacle;

    private float mTouchStartX;
    private float mTouchStartY;
    /** Область, на которой будем рисовать */
    private SurfaceHolder mSurfaceHolder;

    /** Состояние потока (выполняется или нет. Нужно, чтобы было удобнее прибивать поток, когда потребуется) */
    private boolean mRunning;

    /** Стили рисования */
    private Paint mPaint;

    /** Прямоугольник игрового поля */
    private Rect mField;

    ArrayList<GameObject> mObjects = new ArrayList<GameObject>();

    /**
     * Конструктор
     * @param surfaceHolder Область рисования
     * @param context Контекст приложения
     */
    public GameManager(SurfaceHolder surfaceHolder, Context context)
    {
        Log.v(LOG_TAG, "GameManager");
        mSurfaceHolder = surfaceHolder;
        mRunning = false;
        mContext = context;

        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);

        mField = new Rect();



        mLastTimeGeneratedObstacle = System.currentTimeMillis();

        Resources res = mContext.getResources();
        mHero = new Hero(res);
        mObjects.add(mHero);


        mCamX = mHero.getmCenterX();
        mCamY = mHero.getmCenterY() + CAM_HEIGHT;
        mCamZ = mHero.getmCenterZ() - DISTANCE_TO_HERO;
    }
    public void initSize(int screenHeight, int screenWidth)
    {
        Log.v(LOG_TAG, "initSize");
        int size = 0;
        if(screenHeight > screenWidth){
            size = screenWidth;
        }else{
            size = screenHeight;
        }
        mScale = size/BASE_SIZE;

        mCenterX = screenWidth / 2;
        mCenterY = screenHeight / 2;

        mField.set(0, 0, screenWidth, screenHeight);

        for (GameObject mObject : mObjects) {
            mObject.setVisionPoint(mCenterX, mCenterY);
        }
    }

    /**
     * Задание состояния потока
     * @param running
     */
    public void setRunning(boolean running)
    {
        Log.v(LOG_TAG, "setRunning");
        mRunning = running;
    }


    public void onTouchEvent(MotionEvent event)
    {
        //Log.v(LOG_TAG, "onTouchEvent");
        int action = event.getAction();

        if(action == 0){
            mTouchStartX = event.getX();
            mTouchStartY = event.getY();

        }else if(action == 1){
            float deltaX = event.getX() - mTouchStartX;
            if(deltaX > SLIDE_SIZE){
                Log.v(LOG_TAG, "slide right");
                mHero.slide(true);
            }else if(deltaX < -SLIDE_SIZE){
                Log.v(LOG_TAG, "slide left");
                mHero.slide(false);
            }

            float deltaY = event.getY() - mTouchStartY;
            if(deltaY < -SLIDE_SIZE){
                Log.v(LOG_TAG, "jump");
                mHero.jump();
            }

        }else if(action == 2){

        }

    }
    private void clearBackGround(Canvas canvas){
        canvas.drawColor( 0, PorterDuff.Mode.CLEAR );
    }

    private void drawObjects(Canvas canvas){
        clearBackGround(canvas);
        canvas.drawRect(mField, mPaint);
        for(int i = mObjects.size() - 1; i >= 0; --i){
            mObjects.get(i).draw(canvas, mCamX, mCamY, mCamZ);
        }
        /*for (GameObject mObject : mObjects) {
            //Log.v(LOG_TAG, String.valueOf(mObject.getmCenterZ()));
            mObject.draw(canvas, mCamX, mCamY, mCamZ);
        }*/
    }
    private void updateObjects(){
        for (GameObject mObject : mObjects) {
        }
        for(int i = mObjects.size() - 1; i >= 0; --i){
            GameObject mObject = mObjects.get(i);
            mObject.update();
            if(mObject.getmCenterZ() < mCamZ){
                mObjects.remove(mObject);
            }
            if(mObject != mHero) {
                if(checkCollision(mObject, mHero)){
                    mObjects.remove(mObject);
                    Log.v(LOG_TAG, "hit");
                }
            }
        }

        mCamX = mHero.getmCenterX();
        mCamY = mHero.getmCenterY() + CAM_HEIGHT;
        mCamZ = mHero.getmCenterZ() - DISTANCE_TO_HERO;
        createObstacle();
    }

    private boolean checkCollision(GameObject object1, GameObject object2){
        if(Math.abs(object1.getmCenterX() - object2.getmCenterX()) > (object1.getmSizeX() + object2.getmSizeX())/2){
            return false;
        }
        if(Math.abs(object1.getmCenterY() - object2.getmCenterY()) > (object1.getmSizeY() + object2.getmSizeY())/2){
            return false;
        }
        if(Math.abs(object1.getmCenterZ() - object2.getmCenterZ()) > (object1.getmSizeZ() + object2.getmSizeZ())/2){
            return false;
        }

        return true;
    }

    private void createObstacle(){
        if(System.currentTimeMillis() - mLastTimeGeneratedObstacle < OBSTACLE_FREQUENCY){
            return;
        }
        Log.v(LOG_TAG, "createObstacle done");
        mLastTimeGeneratedObstacle = System.currentTimeMillis();

        Random rnd = new Random(System.currentTimeMillis());

        Resources res = mContext.getResources();
        Obstacle newObstacle = new Obstacle(res);
        newObstacle.setVisionPoint(mCenterX, mCenterY);
        newObstacle.setmCenterZ(mHero.getmCenterZ() + 1000);

        int line = rnd.nextInt(3);
        double posX = (line-1)*LINE_SIZE;
        Log.v(LOG_TAG, String.valueOf(posX));
        newObstacle.setmCenterX(posX);
        mObjects.add(newObstacle);

        //mObjects.add(1, newObstacle);
    }

    @Override
    /** Действия, выполняемые в потоке */
    public void run(){
        while (mRunning)
        {

            Canvas canvas = null;

            try
            {
                // подготовка Canvas-а
                canvas = mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder)
                {
                    updateObjects();
                    drawObjects(canvas);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally
            {
                if (canvas != null)
                {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
