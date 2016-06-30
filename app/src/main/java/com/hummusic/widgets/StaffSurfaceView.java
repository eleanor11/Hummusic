package com.hummusic.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by bluemaple on 2016/6/25.
 */

public class StaffSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder holder;
    private StaffView mStaffView;
    public void setStaffView(StaffView staffView){
        this.mStaffView = staffView;
    }
    public void reDraw(){
        surfaceCreated(holder);
    }
    //private DrawThread mDrawThread;
    public StaffSurfaceView(Context context,AttributeSet attributeSet) {
        super(context,attributeSet);
        // TODO Auto-generated constructor stub
        holder = this.getHolder();
        holder.addCallback(this);
        //mDrawThread = new DrawThread(holder);//创建一个绘图线程
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //mDrawThread.isRun = true;
        //mDrawThread.start();
        Canvas c = null;
        try
        {
            synchronized (this.holder)
            {
                c = this.holder.lockCanvas();//锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                if(mStaffView==null)
                    Log.i("aicitel","here staff null");
                else
                    mStaffView.onDraw(c);
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally
        {
            if(c!= null)
            {
                holder.unlockCanvasAndPost(c);//结束锁定画图，并提交改变。
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //mDrawThread.isRun = false;
    }

}