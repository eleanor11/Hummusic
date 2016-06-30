package com.hummusic.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.hummusic.R;

import java.util.ArrayList;

/**
 * Created by bluemaple on 2016/6/17.
 */
public class StaffView {

    private ArrayList<Annotation> staff = new ArrayList<>();

    private Context mContext;
    private final int STAFF_RADIX = 5;
    private final int GAP_DISTANCE = 2*STAFF_RADIX;
    private final int HORIZON_DISTANCE = STAFF_RADIX*5;
    private final int TOTAL_LINE_HEIGHT = GAP_DISTANCE*4;
    private final int START_POS_X = 50;
    private final int START_FIRST_X = 300;
    private final int MIDDLE_POS_X = 550;
    private final int START_THIRD_X = 800;
    private final int END_POS_X = 1050;
    private final int ROTATE_THRESHOLD = 9;
    private final float scaleTime = 0.3f;

    private final int PER_WIDTH = 50;
    private float cntPosX = START_POS_X;
    private float cntPosY = 30;

    private Paint paint;
    //TODO draw annotation

    public StaffView(Context context,String noteString) {
        paint = new Paint();
        this.mContext = context;
        paint.setColor(Color.BLACK);
        //paint.setStrokeJoin(Paint.Join.ROUND);
        //paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(2);
        String[] notes = noteString.split(",");
        Log.i("aicitel", noteString);
        for (String note:notes){
            String cntNote = note.split("-")[0];
            String lnoteInOctave = note.split("-")[1];
            if(cntNote.length()>0 && lnoteInOctave.length()>0) {
                int noteValue = Integer.parseInt(cntNote);
                int levelValue = Integer.parseInt(lnoteInOctave);
                staff.add(new Annotation(noteValue+(levelValue-3)*7));
            }
        }
    }

    /**
     * draw an annotation directly
     * @param annotation class of annotation
     * @param canvas canvas obj
     * @param basePosX startx of annotation
     * @param basePosY starty of annotation (bottom in the staff lines)
     */
    private void drawAnnotation(Annotation annotation,Canvas canvas,float basePosX, float basePosY){
        if(annotation.isHollow())
            paint.setStyle(Paint.Style.STROKE);
        else
            paint.setStyle(Paint.Style.FILL);
        float annotationPosY = -getAnnotationHeight(annotation.getNote())+basePosY;
        float annotationPosX = basePosX-3*STAFF_RADIX;
        Bitmap bmpBg = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.minim);
        Matrix matrix = new Matrix();
        if(annotation.getNote()>ROTATE_THRESHOLD) {
            matrix.setRotate(180);
            annotationPosY+=(scaleTime*bmpBg.getHeight()-4*STAFF_RADIX);
        }
        matrix.postScale(scaleTime, scaleTime);
        Bitmap dstbmp = Bitmap.createBitmap(bmpBg, 0, 0, bmpBg.getWidth(),bmpBg.getHeight(), matrix, true);
        canvas.drawBitmap(dstbmp, annotationPosX, annotationPosY, paint);
    }

    public void drawTrebleclef(Canvas canvas,float cntPosX, float cntPosY){
        Bitmap bmpBg = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.trebleclef);
        float annotationPosY = cntPosY-3.5f*STAFF_RADIX;
        float annotationPosX = cntPosX-3*STAFF_RADIX;
        Matrix matrix = new Matrix();
        matrix.postScale(0.2f, 0.2f);
        Bitmap dstbmp = Bitmap.createBitmap(bmpBg, 0, 0, bmpBg.getWidth(),bmpBg.getHeight(), matrix, true);
        canvas.drawBitmap(dstbmp, annotationPosX, annotationPosY, paint);
    }


    public void drawStaffLine(Canvas canvas,float cntPosY){
        for(int i=0;i<5;i++)
            canvas.drawLine(START_POS_X-STAFF_RADIX*3,i*GAP_DISTANCE+cntPosY,END_POS_X,i*GAP_DISTANCE+cntPosY,paint);
    }
    public void drawVerticalLine(Canvas canvas, float cntPosX,float cntPosY){
            canvas.drawLine(cntPosX,cntPosY,cntPosX,cntPosY+TOTAL_LINE_HEIGHT,paint);
    }

    private float getAnnotationHeight(int annotationType){
        //float result = 0;
        return (annotationType-2)*STAFF_RADIX;
    }


    private float getAnnotationWidth(int type){
        //TODO xiaxiede!!
        return type<4?PER_WIDTH:2*PER_WIDTH;
    }

    public void onDraw(Canvas canvas) {
        //TODO 1.draw staff line
        if(canvas==null)
            return;
        Paint paint = new Paint();

        Log.i("aicitel","on draw here");
        // 绘制矩形区域-实心矩形
        // 设置颜色
        paint.setColor(Color.WHITE);
        // 设置样式-填充
        paint.setStyle(Paint.Style.FILL);
        // 绘制一个矩形
        Rect backRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawRect(backRect, paint);
        //TODO 2.posY change
        cntPosX = START_POS_X;
        for( int i=0;i<staff.size();i++){
            //Log.i("aicitel","on draw here "+i);
            if(cntPosX==START_POS_X){
                drawTrebleclef(canvas,cntPosX,cntPosY);
                drawVerticalLine(canvas,START_FIRST_X,cntPosY);
                drawVerticalLine(canvas,MIDDLE_POS_X,cntPosY);
                drawVerticalLine(canvas,START_THIRD_X,cntPosY);
                drawVerticalLine(canvas,END_POS_X,cntPosY);
                drawStaffLine(canvas, cntPosY);
                cntPosX+=PER_WIDTH;
                i--;
            }
            else if(cntPosX>=END_POS_X) {
                cntPosY += GAP_DISTANCE * 10;
                cntPosX = START_POS_X;
                i--;
            }
            else if(cntPosX==MIDDLE_POS_X||cntPosX==START_FIRST_X||cntPosX==START_THIRD_X){
                //drawVerticalLine(canvas,cntPosX,cntPosY);
                cntPosX+=PER_WIDTH;
                i--;
            }
            else {
                drawAnnotation(staff.get(i), canvas, cntPosX, cntPosY);
                cntPosX += getAnnotationWidth(staff.get(i).getType());
            }
        }

    }
}
