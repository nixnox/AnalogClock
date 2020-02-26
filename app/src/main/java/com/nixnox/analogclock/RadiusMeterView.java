package com.nixnox.analogclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

/**
 * created by NIXNOX
 * 01/09/2020
 */
public class RadiusMeterView extends View {

    float start, end;
    float startSR = -1, endSR = -1;
    float outwidth = -1;
    Context context;
    RectF oval;
    private Paint circlePaint;
    private int radiusMeterColor = R.color.colorRadiusMeter;

    public RadiusMeterView(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initDraw();
        canvas.translate(1, 1);

        end = start == end ? end = 0 : ((start < end) ? (end - start) : ((360 + end) - start));

        canvas.drawArc(oval, start, end, true, circlePaint);
        //    canvas.drawArc(oval, start, end, true, circleStrokePaint);
        if (startSR != -1 && endSR != -1) {
            endSR = startSR == endSR ? endSR = 0 : ((startSR < endSR) ? (endSR - startSR) : ((360 + endSR) - startSR));
            canvas.drawArc(oval, startSR, endSR, true, circlePaint);
        }
    }

    public void initDraw() {
        float width = (float) getWidth();
        float height = (float) getHeight();
        float radius = outwidth == -1 ? width > height ? height / 6 : width / 6 : outwidth + 20;
        float center_x = width / 2, center_y = height / 2;
        oval = new RectF();
        oval.set(center_x - radius,
                center_y - radius,
                center_x + radius,
                center_y + radius);
    }

    public void initPaint() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(ContextCompat.getColor(context, radiusMeterColor));

    }

    public void setStartEnd(float start, float end) {
        this.start = start;
        this.end = end;
        invalidate();
    }

    public void setStartEndSecondRound(float start, float end) {
        this.startSR = start;
        this.endSR = end;
        invalidate();
    }

    public void setWidth(float width) {
        this.outwidth = width;
        invalidate();
    }

    public void setRadiusMeterColor(@ColorInt int radiusMeterColor) {
        this.radiusMeterColor = radiusMeterColor;
        invalidate();
    }
}