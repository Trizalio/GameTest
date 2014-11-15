package trizalio.gametest;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by trizalio on 15.11.14.
 */
public class Hero extends GameObject {
    private static final double FORWARD_SPEED = 10;
    private static final double SLIDE_SPEED = 90;
    private static final double JUMP_SPEED = 15;


    private static final double SIZE_X = 50;
    private static final double SIZE_Y = 50;
    private static final double SIZE_Z = 50;

    private int mLine;

    public Hero(Resources res) {
        super(res.getDrawable(R.drawable.ic_launcher));
        mCenterSpeedZ = FORWARD_SPEED;
        setmSizeX(SIZE_X);
        setmSizeY(SIZE_Y);
        setmSizeZ(SIZE_Z);
        mLine = 1;
    }

    public void slide(boolean directionIsRight){
        if(directionIsRight && mLine <= 2) {
            mLine++;
        }else if(!directionIsRight && mLine >= 0){
            mLine--;
        }
    }
    public void jump(){
        mCenterSpeedY += JUMP_SPEED;
    }

    @Override
    public void update() {
        super.update();

        double targetX = (mLine-1)*GameManager.LINE_SIZE;
        setmCenterSpeedX(getmCenterSpeedX()+(targetX-getmCenterX())/SLIDE_SPEED);
    }
}
