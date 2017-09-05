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

    private static double TOTAL_PROGRESS = 100;
    private static String TAG = ProgressView.class.getSimpleName();
    private float height;
    private float weight;
    private Paint mProgressPaint;
    private Paint outterLayer;
    private Paint textPaint;
    private String displayText;
    private double progressWidth = 0;
    private boolean isUpdating;
    private int beginProgress = 0;
    private float strockeWidth = 4;
    private double firstRectangleProgressMax;
    private double middleRectangleProgressMax;
    private double thirdRectangleProgressMax;
    private boolean setWallPaper;

    public ProgressView(Context context) {
        super(context);
        init();
    }

    public ProgressView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public String getDisplayText() {
        return displayText;
    }


    public void displaySetWallPaper(boolean boo) {
        setWallPaper = boo;
        invalidate();
    }

    /**
     * when updaing the progress, there are three steps:
     * 1. increase display portion of  the left round rectangle
     * 2. increase display portion of the middle round rectangle
     * 3. increase display portion of the right round rectangle
     *
     * @return: tells the view, whice round rectangle to update base on progress level
     * @param: the current progress level
     */
    private int upDateProgressStep() {
        double currentPortion = progressWidth / TOTAL_PROGRESS;

        double sideRectanglePortion = (((double) height / 2) / (double) weight);

        double middleRectanglePortion = 1 - sideRectanglePortion;

        if (0 < currentPortion && currentPortion < sideRectanglePortion) {
            return 1;
        } else if (currentPortion > sideRectanglePortion && currentPortion < middleRectanglePortion) {
            return 2;
        } else if (currentPortion == 0 || currentPortion == sideRectanglePortion) {
            return 1;
        } else {
            return 3;
        }
    }

    private void updateProgressBar(Canvas canvas) {
//        Log.e(TAG,upDateProgressStep() +"" );
        switch (upDateProgressStep()) {
            case 1:
                canvas.save();
                //clips the round rectangle to show the progress level %%%%
                double firstPorition = progressWidth / firstRectangleProgressMax;

                canvas.clipRect(new RectF(0, strockeWidth, (float) (firstPorition * height / 2), height - strockeWidth));

                //draw the unifom rond rectangle, or circle.
                canvas.drawRoundRect(new RectF(0, strockeWidth, height, height - strockeWidth), height / 2, height / 2, mProgressPaint);
                canvas.restore();
                break;

            case 2:
                //draw the full round rectangle on the left
                canvas.save();
                canvas.clipRect(new RectF(0, 0, height / 2, height));
                canvas.drawRoundRect(new RectF(0, strockeWidth, height, height - strockeWidth), height / 2, height / 2, mProgressPaint);
                canvas.restore();

                canvas.save();
                double middlePortion = progressWidth / middleRectangleProgressMax;
                canvas.clipRect(new RectF(height / 2, strockeWidth, (float) middlePortion * (weight - height / 2), height - strockeWidth));
                canvas.drawRoundRect(new RectF(height / 2, strockeWidth, weight - height / 2, height - strockeWidth), 0, 0, mProgressPaint);
                canvas.restore();

                break;


            case 3:
                //draw the full round rectangle on the left
                canvas.save();
                canvas.clipRect(new RectF(0, strockeWidth, height / 2, height - strockeWidth));
                canvas.drawRoundRect(new RectF(0, strockeWidth, height, height - strockeWidth), height / 2, height / 2, mProgressPaint);
                canvas.restore();

                // draw the full rectangle on the middle
                canvas.drawRoundRect(new RectF(height / 2, strockeWidth, weight - height / 2, height - strockeWidth), 0, 0, mProgressPaint);

                //draw the full round rectangel on the right
                canvas.save();
                double rightPortion = (progressWidth - middleRectangleProgressMax) / (100 - middleRectangleProgressMax);

                canvas.clipRect(new RectF((weight - height / 2), strockeWidth, (weight - height / 2) + (float) rightPortion * height / 2, height - strockeWidth));
                canvas.drawRoundRect(new RectF(weight - height, strockeWidth, weight, height - strockeWidth), height / 2, height / 2, mProgressPaint);
                canvas.restore();
                break;
        }
    }

    public void clearProgress() {
        isUpdating = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        /**
         * drawing order :
         *
         * 1. draw the progress rect
         * 2. draw the outter layer
         */
        if (isUpdating) {
            textPaint.setColor(getResources().getColor(R.color.default_downloading_text_color));
            displayText = (int) progressWidth + "%";
        } else {
            textPaint.setColor(getResources().getColor(R.color.default_not_downloading_text_color));
            displayText = getResources().getString(R.string.download_button_default_text);
        }

        if (setWallPaper) {
            textPaint.setColor(getResources().getColor(R.color.default_not_downloading_text_color));
            displayText = getResources().getString(R.string.set_wallpaper);
        }

        if (isUpdating) {
            updateProgressBar(canvas);
        } else {
            canvas.drawRoundRect(new RectF(0, strockeWidth, weight, height - strockeWidth), height / 2, height / 2, mProgressPaint);
        }


        //draw the outter layer of the progressbar.
        canvas.drawArc(new RectF(strockeWidth, strockeWidth, height, height - strockeWidth), 90, 180, false, outterLayer);
        canvas.drawArc(new RectF(weight - height, strockeWidth, weight - strockeWidth, height - strockeWidth), 270, 180, false, outterLayer);
        canvas.drawLine(height / 2, strockeWidth, weight - height / 2, strockeWidth, outterLayer);
        canvas.drawLine(height / 2, height - strockeWidth, weight - height / 2, height - strockeWidth, outterLayer);

        //draw the text in the center.
        Rect bound = new Rect();
        textPaint.getTextBounds(displayText, 0, displayText.length(), bound);
        float textWidth = bound.width() / 2;
        float textHeight = bound.height() / 3;

        canvas.drawText(displayText, ((weight / 2) - textWidth), ((height / 2) + textHeight), textPaint);
    }

    public void setProgress(double progress) {
        if (progress >= 100) {
            isUpdating = false;
            setWallPaper = true;
        }
        isUpdating = true;
        progressWidth = progress;
        // when updateing the progressbar, set the text to black
        invalidate();
    }

    private void init() {
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(getResources().getColor(R.color.default_progress_color));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(getResources().getColor(R.color.default_not_downloading_text_color));


        outterLayer = new Paint();
        outterLayer.setAntiAlias(true);
        outterLayer.setStyle(Paint.Style.STROKE);
        //setting the outter layer line is width
        outterLayer.setStrokeWidth(strockeWidth);
        outterLayer.setColor(getResources().getColor(R.color.default_progress_color));

        isUpdating = false;
        setWallPaper = false;

        displayText = getResources().getString(R.string.download_button_default_text);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        height = getMeasuredHeight();
        weight = getMeasuredWidth();

        textPaint.setTextSize(height / 2);

        progressWidth = weight;

        //calculate the progress increase interval for three parts of the rectangle
        firstRectangleProgressMax = (((double) height / 2) / (double) weight) * 100;

        middleRectangleProgressMax = 100 - firstRectangleProgressMax;

    }

}
