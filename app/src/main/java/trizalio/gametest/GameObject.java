package trizalio.gametest;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by trizalio on 15.11.14.
 */
public class GameObject {
    private static final String LOG_TAG = "GameObject";
    private static final double BASE_DISTANCE = 1000;
    private static final double GRAVITY = 0.7;
    private static final double FRICTION = 0.93;
    protected double mCenterX;
    protected double mCenterY;
    protected double mCenterZ;
    protected double mCenterSpeedX;
    protected double mCenterSpeedY;
    protected double mCenterSpeedZ;

    protected int mVisionX;
    protected int mVisionY;

    protected double mSizeX;
    protected double mSizeY;
    protected double mSizeZ;

    public double getmSizeZ() {
        return mSizeZ;
    }

    public void setmSizeZ(double mSizeZ) {
        this.mSizeZ = mSizeZ;
    }

    public double getmSizeX() {
        return mSizeX;
    }

    public void setmSizeX(double mSizeX) {
        this.mSizeX = mSizeX;
    }

    public double getmSizeY() {
        return mSizeY;
    }

    public void setmSizeY(double mSizeY) {
        this.mSizeY = mSizeY;
    }


    protected int mImageWidth;
    protected int mImageHeight;


    private Drawable mImage;

    public GameObject(Drawable image)
    {
        Log.v(LOG_TAG, "GameObject");
        mImage = image;
        mCenterX = 0;
        mCenterY = 0;
        mCenterZ = 0;
        mCenterSpeedX = 0;
        mCenterSpeedY = 0;
        mCenterSpeedZ = 0;
        mImageWidth = image.getIntrinsicWidth();
        mImageHeight = image.getIntrinsicHeight();
    }

    public void setVisionPoint(int x, int y){
        Log.v(LOG_TAG, "setVisionPoint");
        mVisionX = x;
        mVisionY = y;
    }


    public void update(){
        mCenterX += mCenterSpeedX;
        mCenterY += mCenterSpeedY;
        mCenterZ += mCenterSpeedZ;
        if(mCenterY > 0){
            mCenterSpeedY -= GRAVITY;
        }else if(mCenterY < 0){
            mCenterY = 0;
        }
        mCenterSpeedX *= FRICTION;

        if(mCenterX > GameManager.LINE_SIZE){
            mCenterX = GameManager.LINE_SIZE;
            mCenterSpeedX = -mCenterSpeedX;
        }else if(mCenterX < -GameManager.LINE_SIZE) {
            mCenterX = -GameManager.LINE_SIZE;
            mCenterSpeedX = -mCenterSpeedX;
        }
    }
    private void projectImage(double shift, double height, double distance){
        double visionDistance = (mCenterZ-distance)/BASE_DISTANCE;
        double baseX = mVisionX + (mCenterX-shift)/visionDistance;
        double baseY = mVisionY - (mCenterY-height)/visionDistance;
        double sizeX = mImageWidth/visionDistance/2;
        double sizeY = mImageHeight/visionDistance/2;

        mImage.setBounds((int) (baseX - sizeX), (int)(baseY - sizeY),(int)(baseX + sizeX), (int)(baseY + sizeY));
    }

    public void draw(Canvas canvas, double shift, double height, double distance)
    {
        //Log.v(LOG_TAG, "draw");
        //Log.v(LOG_TAG, String.valueOf(distance));
        projectImage(shift, height, distance);
        mImage.draw(canvas);
    }

    public double getmCenterX() {
        return mCenterX;
    }

    public void setmCenterX(double mCenterX) {
        this.mCenterX = mCenterX;
    }

    public double getmCenterY() {
        return mCenterY;
    }

    public void setmCenterY(double mCenterY) {
        this.mCenterY = mCenterY;
    }

    public double getmCenterZ() {
        return mCenterZ;
    }

    public void setmCenterZ(double mCenterZ) {
        this.mCenterZ = mCenterZ;
    }

    public double getmCenterSpeedX() {
        return mCenterSpeedX;
    }

    public void setmCenterSpeedX(double mCenterSpeedX) {
        this.mCenterSpeedX = mCenterSpeedX;
    }

    public double getmCenterSpeedY() {
        return mCenterSpeedY;
    }

    public void setmCenterSpeedY(double mCenterSpeedY) {
        this.mCenterSpeedY = mCenterSpeedY;
    }

    public double getmCenterSpeedZ() {
        return mCenterSpeedZ;
    }

    public void setmCenterSpeedZ(double mCenterSpeedZ) {
        this.mCenterSpeedZ = mCenterSpeedZ;
    }
}
