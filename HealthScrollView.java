package com.db.williamchartdemo.health;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.db.chart.model.ChartEntry;
import com.db.chart.util.Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class HealthScrollView extends HorizontalScrollView implements ViewTreeObserver.OnDrawListener {


    private static float PER_GRID_HEIGHT_PX = 0.0f;
    private List<Integer> axisYLables;

    private Paint axisPaint;
    private float maxAisyLablesWidth;
    private float paddingLeftWidth;
    private float axisLeftPaddingTxtLeft;
    private float axisLeftPaddingTxtRight;
    private float axisRightPaddingTxtLeft;
    private float axisRightPaddingTxtRight;
    private float paddingRightWidth;
    private float axisTopPadding;
    private float axisBottomPadding;
    private HealthLineView healthLineView;
    private int lineviewHeight;
    private int healthviewWidth;
    private float innerStartBottom;
    private Paint axisYPaint;
    private float labelYTxtHeight;
    private int innerPaddingTop;

    String willdrawtxt = "正常范围";

    public HealthScrollView(Context context) {
        super(context);
        init(context);
    }


    public HealthScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HealthScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {


        axisYLables = new ArrayList<>();
        for (int i = 40; i < 160; i += 10) {
            axisYLables.add(i);
        }

        axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setStyle(Paint.Style.FILL);
        axisPaint.setColor(Color.GRAY);
        axisPaint.setDither(true);
        axisPaint.setStrokeWidth(Tools.fromDpToPx(1.2f));
        axisPaint.setTextSize(Tools.fromDpToPx(12f));


        axisYPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisYPaint.setStyle(Paint.Style.FILL);
        axisYPaint.setColor(Color.DKGRAY);
        axisYPaint.setTextSize(Tools.fromDpToPx(12f));

        Paint.FontMetrics fontMetrics = axisYPaint.getFontMetrics();

        labelYTxtHeight = fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading;


        maxAisyLablesWidth = maxAisyLablesWidth();

        axisLeftPaddingTxtLeft = Tools.fromDpToPx(10.0f);
        axisLeftPaddingTxtRight = Tools.fromDpToPx(5.0f);

        paddingLeftWidth = maxAisyLablesWidth + axisLeftPaddingTxtLeft + axisLeftPaddingTxtRight;

        axisRightPaddingTxtLeft = Tools.fromDpToPx(10.0f);
        axisRightPaddingTxtRight = Tools.fromDpToPx(10.0f);

        paddingRightWidth = maxAisyLablesWidth + axisRightPaddingTxtLeft + axisRightPaddingTxtRight;

        axisTopPadding = Tools.fromDpToPx(10.0f);

        axisBottomPadding = Tools.fromDpToPx(2.0f);


        healthLineView = new HealthLineView(context);
        addView(healthLineView, new HorizontalScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        healthLineView.getViewTreeObserver().addOnDrawListener(this);
        healthLineView.setmEntryListener(new HealthLineView.OnEntryClickListener() {
            @Override
            public void onClick(int dataSetIndex, ChartEntry chartEntry, Rect rect) {
                Log.d("onClick", "dataSetIndex: " + dataSetIndex + " chartEntry: " + chartEntry + " rect: " + rect);
                Toast.makeText(getContext(), "val: " + chartEntry.getValue() + "cm", Toast.LENGTH_SHORT).show();
            }
        });
        setPadding((int) paddingLeftWidth, (int) axisTopPadding, (int) paddingRightWidth, (int) axisBottomPadding);

        setWillNotDraw(false);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() > 0) {
            healthLineView = (HealthLineView) getChildAt(0);
            lineviewHeight = healthLineView.getMeasuredHeight();

            PER_GRID_HEIGHT_PX = HealthLineView.PER_GRID_HEIGHT_PX;

            /// 内部view 从下到上 ，底部开始的地方
            innerStartBottom = healthLineView.getInnerStartBottom();

            innerPaddingTop = healthLineView.getPaddingTop();

            int selfwidth = getMeasuredWidth();

            healthviewWidth = (int) (selfwidth - paddingLeftWidth - paddingRightWidth);

            Log.d("onMeasure", "selfwidth: " + selfwidth + "  lineviewHeight: " + lineviewHeight + " healthviewWidth: " + healthviewWidth);

        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawAxis(canvas);

        drawLables(canvas);

        drawRightLabel(canvas);

    }

    private void drawRightLabel(Canvas canvas) {

        float[] positionY = getPosByScrollX(getScrollX());
        // 微调
        positionY[1] += 5;

        float txtX = paddingLeftWidth + healthviewWidth + axisRightPaddingTxtLeft + getScrollX();

        canvas.drawText("" + willdrawtxt.charAt(0), txtX, positionY[1] - labelYTxtHeight * 1, axisYPaint);
        canvas.drawText("" + willdrawtxt.charAt(1), txtX, positionY[1] - labelYTxtHeight * 0, axisYPaint);
        canvas.drawText("" + willdrawtxt.charAt(2), txtX, positionY[1] - labelYTxtHeight * -1, axisYPaint);
        canvas.drawText("" + willdrawtxt.charAt(3), txtX, positionY[1] - labelYTxtHeight * -2, axisYPaint);

//        canvas.drawCircle(txtX, positionY[1], 10.f, axisYPaint);

    }

    private void drawLables(Canvas canvas) {

        List<Integer> axisYLables = getAxisYLables();
        int size = axisYLables.size();
        for (int i = 0; i < size; i++) {
            int labels = axisYLables.get(i);
            String currLable = "" + labels;
            float currLablewidth = axisYPaint.measureText(currLable);
            canvas.drawText(currLable, paddingLeftWidth + getScrollX() - axisLeftPaddingTxtRight - currLablewidth, innerStartBottom + axisTopPadding - PER_GRID_HEIGHT_PX * i + labelYTxtHeight / 2, axisYPaint);
        }

    }

    private void drawAxis(Canvas canvas) {
        /// left
        canvas.drawLine(paddingLeftWidth + getScrollX(), axisTopPadding + innerPaddingTop, paddingLeftWidth + getScrollX(), innerStartBottom + axisTopPadding, axisPaint);
        /// top
//        canvas.drawLine(getPaddingLeft(),getPaddingTop(),healthviewWidth,getPaddingTop(),axisPaint);
        /// right
        canvas.drawLine(paddingLeftWidth + healthviewWidth + getScrollX(), axisTopPadding + innerPaddingTop, paddingLeftWidth + healthviewWidth + getScrollX(), innerStartBottom + axisTopPadding, axisPaint);

        /// bottom 不需要绘制
    }


//    @Override
//    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
////        float[] positionY = getPosByScrollX(scrollX);
//
////        Log.d("onScrollChange", "scrollX: " + scrollX + " pos, x: " + positionY[0] + " y: " + positionY[1]);
//
//
//    }

    public float[] getPosByScrollX(int scrollX) {

        return healthLineView.getPositionByScorllX(paddingLeftWidth + healthviewWidth + scrollX);
    }


    public List<Integer> getAxisYLables() {
        return axisYLables;
    }

    public void setAxisYLables(List<Integer> axisYLables) {
        this.axisYLables = axisYLables;
    }

    public float maxAisyLablesWidth() {
        if (axisYLables != null && !axisYLables.isEmpty()) {
            Integer max = Collections.max(axisYLables);
            float lablesMaxwidth = axisPaint.measureText(max.toString());
            return lablesMaxwidth;
        } else {
            return 0.f;
        }
    }


    @Override
    public void onDraw() {
        postInvalidate();
    }
}
