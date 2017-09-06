package com.example.progessbuttonlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by allensun on 9/5/17.
 * on Tubitv.com, allengotstuff@gmail.com
 */
public class ProgressView extends View {


    private double currentProgress;

    private static String TAG = ProgressView.class.getSimpleName();
    private float height;
    private float width;
    private Paint mProgressPaint;
    private Paint butttonStokePaint;
    private Paint textPaint;
    private String displayText;


    private float outerStrokeWidth;

    private int outerStrokeColor;

    private String buttonText;

    private int buttonTextColor;

    private int progressTextColor;

    private int buttonColor;

    private int maxProgress;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }


    private void init(Context context) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                null,
                R.styleable.ProgressView,
                0, 0);

        try {
            outerStrokeWidth = a.getInt(R.styleable.ProgressView_outerStrokeWidth, 4);

            outerStrokeColor = a.getColor(R.styleable.ProgressView_outerStrokeColor, ContextCompat.getColor(context, R.color.yellow));

            buttonText = a.getString(R.styleable.ProgressView_buttonText);

            if(buttonText==null){
                buttonText = getResources().getString(R.string.download_button_default_text);
            }

            buttonTextColor = a.getColor(R.styleable.ProgressView_buttonTextColor, ContextCompat.getColor(context, R.color.default_not_downloading_text_color));

            progressTextColor = a.getColor(R.styleable.ProgressView_progressTextColor, ContextCompat.getColor(context, R.color.yellow));

            buttonColor  = a.getColor(R.styleable.ProgressView_buttonColor, ContextCompat.getColor(context, R.color.default_progress_color));

            maxProgress = a.getInt(R.styleable.ProgressView_maxProgress, 100);



            //in the begining, the round retangle should display 100%
            currentProgress = maxProgress;

            mProgressPaint = new Paint();
            mProgressPaint.setAntiAlias(true);
            mProgressPaint.setColor(buttonColor);

            textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setColor(buttonTextColor);


            butttonStokePaint = new Paint();
            butttonStokePaint.setAntiAlias(true);
            butttonStokePaint.setStyle(Paint.Style.STROKE);
            //setting the outter layer line is width
            butttonStokePaint.setStrokeWidth(outerStrokeWidth);

            butttonStokePaint.setColor(outerStrokeColor);

            displayText = buttonText;

        } finally {
            a.recycle();
        }


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

        double coverPercentage = 1 - (maxProgress - currentProgress) / maxProgress;

        canvas.clipRect(new RectF(0, 0, (float) (width * coverPercentage), height));
        canvas.drawRoundRect(new RectF(0, 0, width, height), height / 2, height / 2, mProgressPaint);
        canvas.restore();

        drawButtonOuterStroke(canvas);

    }

    private void drawButtonOuterStroke(Canvas canvas) {
        //draw the outter layer of the progressbar.

        // the width of the stroke need to take into account, x , y coordinate use half of size on each size, hense need to divide stroke width for extra spacing.

        canvas.drawArc(new RectF(outerStrokeWidth / 2, outerStrokeWidth / 2, height - outerStrokeWidth / 2, height - outerStrokeWidth / 2), 90, 180, false, butttonStokePaint);

        canvas.drawLine(height / 2, outerStrokeWidth / 2, width - height / 2, outerStrokeWidth / 2, butttonStokePaint);

        canvas.drawArc(new RectF(width - height, outerStrokeWidth / 2, width - outerStrokeWidth / 2, height - outerStrokeWidth / 2), 270, 180, false, butttonStokePaint);

        canvas.drawLine(height / 2, height - outerStrokeWidth / 2, width - height / 2, height - outerStrokeWidth / 2, butttonStokePaint);

        //draw the text in the center.
        Rect bound = new Rect();
        textPaint.getTextBounds(displayText, 0, displayText.length(), bound);
        float textWidth = bound.width() / 2;
        float textHeight = bound.height() / 3;
        canvas.drawText(displayText, ((width / 2) - textWidth), ((height / 2) + textHeight), textPaint);
    }

    public void updateProgress(double progress) {
        if (progress >= maxProgress) {
            currentProgress = maxProgress;
        } else {
            currentProgress = progress;
        }
        // when updateing the progressbar, set the text to black
        invalidate();
    }


}
