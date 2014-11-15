package trizalio.gametest;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by trizalio on 15.11.14.
 */
public class Obstacle extends GameObject  {
    private static final double SIZE_X = 50;
    private static final double SIZE_Y = 50;
    private static final double SIZE_Z = 50;
    public Obstacle(Resources res) {
        super(res.getDrawable(R.drawable.ic_launcher));
        setmSizeX(SIZE_X);
        setmSizeY(SIZE_Y);
        setmSizeZ(SIZE_Z);
    }
}
