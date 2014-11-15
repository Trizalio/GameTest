package trizalio.gametest;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by trizalio on 15.11.14.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    /**
     * Область рисования
     */
    private static final String LOG_TAG = "GameView";
    private SurfaceHolder mSurfaceHolder;
    private GameManager mThread;

    public GameView(Context context) {
        super(context);
        Log.v(LOG_TAG, "GameView");
        // подписываемся на события Surface
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mThread = new GameManager(mSurfaceHolder, context);
    }

    @Override
    /**
     * Изменение области рисования
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        Log.v(LOG_TAG, "surfaceChanged");
        mThread.initSize(height, width);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.v(LOG_TAG, "onTouchEvent");
        mThread.onTouchEvent(event);
        return true;
    }

    @Override
    /**
     * Создание области рисования
     */
    public void surfaceCreated(SurfaceHolder holder)
    {
        Log.v(LOG_TAG, "surfaceCreated");
        mThread.setRunning(true);
        mThread.start();

    }

    @Override
    /**
     * Уничтожение области рисования
     */
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.v(LOG_TAG, "surfaceDestroyed");
        boolean retry = true;
        mThread.setRunning(false);
        while (retry)
        {
            try
            {
                // ожидание завершение потока
                mThread.join();
                retry = false;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}