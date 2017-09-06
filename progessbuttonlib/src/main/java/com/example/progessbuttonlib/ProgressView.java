package com.example.progessbuttonlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by allensun on 9/5/17.
 * on Tubitv.com, allengotstuff@gmail.com
 */
public class ProgressView extends View {

    private static double MAX_PROGRESS = 100;
    private double currentProgress;

    private static String TAG = ProgressView.class.getSimpleName();
    private float height;
    private float width;
    private Paint mProgressPaint;
    private Paint butttonStokePaint;
    private Paint textPaint;
    private String displayText;


    private float button_stroke_width = 1;

    public ProgressView(Context context) {
        super(context);
        init();
    }

    public ProgressView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    private void init() {
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(getResources().getColor(R.color.default_progress_color));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(getResources().getColor(R.color.default_not_downloading_text_color));


        butttonStokePaint = new Paint();
        butttonStokePaint.setAntiAlias(true);
        butttonStokePaint.setStyle(Paint.Style.STROKE);
        //setting the outter layer line is width
        butttonStokePaint.setStrokeWidth(button_stroke_width);
        butttonStokePaint.setColor(getResources().getColor(R.color.yellow));

        displayText = getResources().getString(R.string.download_button_default_text);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        height = getMeasuredHeight();
        width = getMeasuredWidth();

        textPaint.setTextSize(height / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * drawing order :
         *
         * 1. draw the outter layer
         *
         * 2. draw the progress roundRect.
         */

        canvas.save();

        double coverPercentage = 1 - (MAX_PROGRESS - currentProgress) / MAX_PROGRESS;

        canvas.clipRect(new RectF(0, 0, (float) (width * coverPercentage), height));
        canvas.drawRoundRect(new RectF(0, 0, width, height), height / 2, height / 2, mProgressPaint);
        canvas.restore();

        drawButtonOutterStroke(canvas);

    }

    private void drawButtonOutterStroke(Canvas canvas) {
        //draw the outter layer of the progressbar.

        // the width of the stroke need to take into account, x , y coordinate use half of size on each size, hense need to divide stroke width for extra spacing.

        canvas.drawArc(new RectF(button_stroke_width / 2, button_stroke_width / 2, height - button_stroke_width / 2, height - button_stroke_width / 2), 90, 180, false, butttonStokePaint);

        canvas.drawLine(height / 2, button_stroke_width / 2, width - height / 2, button_stroke_width / 2, butttonStokePaint);

        canvas.drawArc(new RectF(width - height, button_stroke_width / 2, width - button_stroke_width / 2, height - button_stroke_width / 2), 270, 180, false, butttonStokePaint);

        canvas.drawLine(height / 2, height - button_stroke_width / 2, width - height / 2, height - button_stroke_width / 2, butttonStokePaint);

        //draw the text in the center.
        Rect bound = new Rect();
        textPaint.getTextBounds(displayText, 0, displayText.length(), bound);
        float textWidth = bound.width() / 2;
        float textHeight = bound.height() / 3;
        canvas.drawText(displayText, ((width / 2) - textWidth), ((height / 2) + textHeight), textPaint);
    }

    public void updateProgress(double progress) {
        if (progress >= MAX_PROGRESS) {
            currentProgress = MAX_PROGRESS;
        } else {
            currentProgress = progress;
        }
        // when updateing the progressbar, set the text to black
        invalidate();
    }


}
