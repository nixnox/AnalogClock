/*
 * Copyright (C) 2019 Leonard Dizon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nixnox.analogclock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

/**
 * created by NIXNOX
 * 01/09/2020
 */
public class AnalogClockView extends FrameLayout {
    private static final long HOURS = 3600000L;
    private static final long MINUTES = 60000L;
    private static final long SECONDS = 1000L;
    private final AppCompatImageView background;
    private final AppCompatImageView hour;
    private final AppCompatImageView minute;
    private final AppCompatImageView second;
    private final RadiusMeterView drawContainer;
    Type type;
    private float angleHolder = -1;

    public AnalogClockView(Context context) {
        this(context, null);
    }

    public AnalogClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public AnalogClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @SuppressWarnings("WeakerAccess")
    public AnalogClockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.analog_clock, this);

        background = findViewById(R.id.background);
        hour = findViewById(R.id.hour_hand);
        minute = findViewById(R.id.minute_hand);
        second = findViewById(R.id.second_hand);
        drawContainer = findViewById(R.id.view);
        drawContainer.setWidth(second.getDrawable().getIntrinsicWidth());

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.AnalogClockView, defStyleAttr, defStyleRes);

        Drawable backgroundDrawable = typedArray.getDrawable(R.styleable.AnalogClockView_faceDrawable);
        Drawable hourDrawable = typedArray.getDrawable(R.styleable.AnalogClockView_hourDrawable);
        Drawable minuteDrawable = typedArray.getDrawable(R.styleable.AnalogClockView_minuteDrawable);
        Drawable secondDrawable = typedArray.getDrawable(R.styleable.AnalogClockView_secondDrawable);

        setFaceDrawable(backgroundDrawable != null ? backgroundDrawable : ContextCompat.getDrawable(context, R.drawable.clock_background))
                .setHourDrawable(hourDrawable != null ? hourDrawable : ContextCompat.getDrawable(context, R.drawable.clock_hour))
                .setMinuteDrawable(minuteDrawable != null ? minuteDrawable : ContextCompat.getDrawable(context, R.drawable.clock_min))
                .setSecondDrawable(secondDrawable != null ? secondDrawable : ContextCompat.getDrawable(context, R.drawable.clock_second));

        int faceColor = typedArray.getColor(R.styleable.AnalogClockView_faceTint, -1);
        int hourColor = typedArray.getColor(R.styleable.AnalogClockView_hourTint, -1);
        int minuteColor = typedArray.getColor(R.styleable.AnalogClockView_minuteTint, -1);
        int secondColor = typedArray.getColor(R.styleable.AnalogClockView_secondTint, -1);
        if (faceColor != -1) setFaceTint(faceColor);
        if (hourColor != -1) setHourTint(hourColor);
        if (minuteColor != -1) setMinuteTint(minuteColor);
        if (secondColor != -1) setSecondTint(secondColor);

        rotateHourHand(typedArray.getFloat(R.styleable.AnalogClockView_hourRotation, 0));
        rotateMinuteHand(typedArray.getFloat(R.styleable.AnalogClockView_minuteRotation, 0));
        rotateSecondHand(typedArray.getFloat(R.styleable.AnalogClockView_secondRotation, 0));
    }

    @SuppressWarnings("WeakerAccess")
    public AnalogClockView setFaceDrawable(Drawable drawable) {
        background.setImageDrawable(drawable);
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public AnalogClockView setHourDrawable(Drawable drawable) {
        hour.setImageDrawable(drawable);
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public AnalogClockView setMinuteDrawable(Drawable drawable) {
        minute.setImageDrawable(drawable);
        return this;
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public AnalogClockView setSecondDrawable(Drawable drawable) {
        second.setImageDrawable(drawable);
        return this;
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public AnalogClockView rotateHourHand(float angle) {
        if (type == Type.HOUR)
            setRadiusMeter(angle);
        hour.setRotation(angle);
        return this;
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public AnalogClockView rotateMinuteHand(final float angle) {
        if (type == Type.MINUTE)
            setRadiusMeter(angle);
        minute.setRotation(angle);
        return this;
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public AnalogClockView rotateSecondHand(float angle) {
        if (type == Type.SECOND)
            setRadiusMeter(angle);
        second.setRotation(angle);
        return this;
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public AnalogClockView setTime(int hour, int minute, int seconds) {
        long hr = hour * HOURS;
        long min = minute * MINUTES;
        long sec = seconds * SECONDS;

        rotateHourHand((float) 0.0000083 * (hr + min + sec));
        rotateMinuteHand((float) 0.0001 * (min + sec));
        rotateSecondHand((float) 0.006 * sec);
        return this;
    }

    public void setCurrentTime() {
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMin = rightNow.get(Calendar.MINUTE);
        int currentSec = rightNow.get(Calendar.SECOND);
        setTime(currentHour, currentMin, currentSec);
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public AnalogClockView setFaceTint(int color) {
        background.setColorFilter(color);
        return this;
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public AnalogClockView setHourTint(int color) {
        hour.setColorFilter(color);
        return this;
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public AnalogClockView setMinuteTint(int color) {
        minute.setColorFilter(color);
        return this;
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public AnalogClockView setSecondTint(int color) {
        second.setColorFilter(color);
        return this;
    }

    public void setRadiusMeterType(Type type) {
        this.type = type;
    }

    public void setRadiusMeter(float angle) {
        if (angleHolder == -1)
            angleHolder = angle;
        drawContainer.setStartEnd(angleHolder, angle);
    }

    public void setStartEndRadius(long startMills, long endMills, Type type) {


        Calendar sCal = Calendar.getInstance();
        Calendar eCal = Calendar.getInstance();
        sCal.setTimeInMillis(startMills);
        eCal.setTimeInMillis(endMills);
        int startHour = sCal.get(Calendar.HOUR_OF_DAY);
        int endHour = eCal.get(Calendar.HOUR_OF_DAY);
        int startMin = sCal.get(Calendar.MINUTE);
        int endMin = eCal.get(Calendar.MINUTE);
        int startSec = sCal.get(Calendar.SECOND);
        int endSec = eCal.get(Calendar.SECOND);

        switch (type) {
            case HOUR:
                float startHR = (float) 0.0000083 * (startHour * HOURS + startMin * MINUTES + startSec * SECONDS);
                float endHR = (float) 0.0000083 * (endHour * HOURS + endMin * MINUTES + endSec * SECONDS);
                drawContainer.setStartEnd(startHR, endHR);
                break;
            case MINUTE:
                float startMR = (float) 0.0001 * (startMin * MINUTES + startSec * SECONDS);
                float endMR = (float) 0.0001 * (endMin * MINUTES + endSec * SECONDS);
                drawContainer.setStartEnd(startMR, endMR);
                break;
            case SECOND:
                float startSR = (float) 0.006 * startSec * SECONDS;
                float endSR = (float) 0.006 * endSec * SECONDS;
                drawContainer.setStartEnd(startSR, endSR);
                break;
        }
    }

    public enum Type {SECOND, MINUTE, HOUR}
}
