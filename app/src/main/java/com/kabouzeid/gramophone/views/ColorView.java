package com.kabouzeid.gramophone.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.kabouzeid.gramophone.R;
import com.kabouzeid.gramophone.util.Util;

public class ColorView extends FrameLayout {

    private final Bitmap mCheck;
    @NonNull
    private final Paint paint;
    @NonNull
    private final Paint paintBorder;
    @Nullable
    private Paint paintCheck;
    private final int borderWidth;

    public ColorView(@NonNull Context context) {
        this(context, null, 0);
    }

    public ColorView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final int checkSize = (int) context.getResources().getDimension(R.dimen.circle_view_check);
        mCheck = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_checkbox_marked_circle_outline_white_24dp), checkSize, checkSize);
        borderWidth = (int) getResources().getDimension(R.dimen.circle_view_border);

        paint = new Paint();
        paint.setAntiAlias(true);

        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);

        setWillNotDraw(false);
    }

    private static Bitmap getResizedBitmap(@NonNull Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        if (bm != resizedBitmap) {
            bm.recycle();
        }
        return resizedBitmap;
    }

    @Override
    public void setBackgroundColor(int color) {
        paint.setColor(color);
        paintBorder.setColor(Util.shiftColorDown(color));
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //noinspection SuspiciousNameCombination
            int height = width;
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
            }
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int canvasSize = canvas.getWidth();
        if (canvas.getHeight() < canvasSize)
            canvasSize = canvas.getHeight();

        int circleCenter = (canvasSize - (borderWidth * 2)) / 2;
        canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, ((canvasSize - (borderWidth * 2)) / 2) + borderWidth - 4.0f, paintBorder);
        canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, ((canvasSize - (borderWidth * 2)) / 2) - 4.0f, paint);

        if (isActivated()) {
            final int offset = (canvasSize / 2) - (mCheck.getWidth() / 2);
            if (paint.getColor() == Color.WHITE) {
                if (paintCheck == null) {
                    paintCheck = new Paint();
                    paintCheck.setAntiAlias(true);
                    paintCheck.setColorFilter(new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN));
                }
            } else {
                paintCheck = null;
            }
            canvas.drawBitmap(mCheck, offset, offset, paintCheck);
        }
    }
}