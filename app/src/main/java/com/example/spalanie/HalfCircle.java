package com.example.spalanie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;

public class HalfCircle extends View {

    float min = 0, max = 0, current = 0;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public HalfCircle(Context context) {
        super(context);

    }

    public HalfCircle(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    public HalfCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }

    public HalfCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    void init(){
        try {
            JSONArray array = FileHelper.openFile(getContext());

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                float consumption = Float.parseFloat(String.valueOf(object.get("consumption")));

                System.out.println(consumption);
                if (min == 0 || min > consumption) {
                    min = consumption;
                }

                if (max == 0 || max < consumption) {
                    max = consumption;
                }

                if (i == array.length() - 1) {
                    JSONObject o = array.getJSONObject(i);
                    current = Float.parseFloat(String.valueOf(o.get("consumption")));
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(widthMeasureSpec,heightMeasureSpec/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = (float) getWidth();
        float height = (float) getHeight();
        float radius = 400;
        float strokeWidth = 100;

//        if (width > height) {
//            radius = height / 4;
//        } else {
//            radius = width / 4;
//        }

//        Path path = new Path();
//        path.addCircle(width / 2,
//                height / 2, radius,
//                Path.Direction.CW);

        Paint paint = new Paint();
        paint.setShader(new LinearGradient(width / 1.5F, height / 2, width, height / 2, getResources().getColor(R.color.secondaryColor), getResources().getColor(R.color.primaryColor), Shader.TileMode.CLAMP));
        paint.setStrokeWidth(strokeWidth);

        //paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        float center_x, center_y;
        final RectF oval = new RectF();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        center_x = width / 2;
        center_y = height / 2;

        oval.set(center_x - radius,
                center_y - radius,
                center_x + radius,
                center_y + radius);

        canvas.translate(0, height / 10);

        canvas.drawArc(oval, 160, 220, false, paint);


        Paint textPaint = new Paint();
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(df.format(min) + " l/100km", oval.left + 100, oval.bottom - 100, textPaint);
        canvas.drawText(df.format(max) + " l/100km", oval.right - 100, oval.bottom - 100, textPaint);
        canvas.drawText(df.format(current) + " l/100km", center_x, oval.top - 100, textPaint);

        ;

        RectF pointer = new RectF();
        pointer.set(center_x, center_y, 10, 100);

        Paint pointerPaint = new Paint();
        pointerPaint.setStrokeWidth(30);
        pointerPaint.setAntiAlias(true);
        pointerPaint.setColor(getResources().getColor(R.color.primaryColor));
        pointerPaint.setStyle(Paint.Style.FILL);
        pointerPaint.setStyle(Paint.Style.STROKE);
        pointerPaint.setStrokeCap(Paint.Cap.ROUND);
        //pointerPaint.setStrokeCap(Paint.Cap.ROUND);

        float percentage = 90f;

        float r = radius - strokeWidth;

        float x = center_x;
        float y = (float) (center_y - r);
        //float a = -90 + ((percentage * 180)/100);
        float a = (4 * ((current - min)/ (max-min))) - 2F;
        float xp = (float) (((x - center_x) * Math.cos(a)) - ((y - center_y) * Math.sin(a))) + center_x;
        float yp = (float) (((x - center_x) * Math.sin(a)) + ((y - center_y) * Math.cos(a))) + center_y;


        canvas.drawLine(center_x, center_y, xp, yp, pointerPaint);


//        float mAngle = (float) (Math.PI * percentage / 30 - Math.PI / 2);
//        canvas.drawLine(center_x,
//                center_y,
//                (float) (center_x + Math.cos( mAngle)* 200),
//                (float)(center_y+Math.sin(mAngle)*200),
//                pointerPaint);

    }
}
