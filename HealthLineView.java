package com.db.williamchartdemo.health;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.ChartEntry;
import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.util.Tools;
import com.db.chart.view.ChartView;

import java.util.ArrayList;
import java.util.Random;

import static com.db.chart.util.Preconditions.checkNotNull;

public class HealthLineView extends View {


    private static final float SMOOTH_FACTOR = 0.15f;


    /// 每个横向的大的网格宽度 20dp
    public static final float PER_MONTH_GRID_WIDTH_DP = 35.0f;
    // 每个纵向的大的网格高度 30dp
    public static final float PER_GRID_HEIGHT_DP = 35.0f;
    private static final int FULL_ALPHA = 255;

    /// 每个横向的大的网格宽度
    public static float PER_MONTH_GRID_WIDTH_PX;
    // 每个纵向的大的网格高度
    public static float PER_GRID_HEIGHT_PX;


    /// 默认73个月份
    private static int DEF_MONTH_COUNT = 72;

    /// 每个小网格宽度
    private static float PER_GRID_WIDTH;


    /// 每个月份分为4个小网格， 每周一个小网格
    private static int PER_MONTH_GRID_COUNT = 4;


    /// 垂直方向上 每 10cm 对应的小网格数量 5
    private static final int PER_VERTICAL_GRID_COUNT = 5;

    /// 每个小网格的高度
    private static float PER_GRID_HEIGHT;

    private static final int DEF_HEIGHT_COUNT = 11;


    //// 默认宽度
    private static int DEF_WIDTH = 0;

    /// 默认高度
    private static int DEF_HEIGHT = 0;
    private GestureDetector mGestureDetector;

    private int paddingLeft = 0;
    private Path smoothLinePath;
    private PathMeasure pathMeasure;
    private float mClickableRadius;
    private Paint mDotLinePaint;

    /// 用户记录数据线
    private LineSet dataLineSet;
    private Paint mDotLineStrokePaint;
    private ArrayList<Region> dataLineRegions;

    @Override
    public int getPaddingTop() {
        return paddingTop;
    }


    private int paddingTop = 0;
    private int paddingRight = 0;

    private int paddingBottom = 0;


    private int axisY[] = {40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150};


    public static final float simulateData[] = {
            50.4F, 54.8F, 58.7f, 62f, 64.6f,
            66.7F, 68.4F, 69.8f, 71.2f, 72.6f,
            74F, 75.3F, 76.5f, 79.8f, 82.7f,
            85.6F, 60.6F, 60.75f, 60.85f, 61.f,
            61.5F, 61.6F, 61.75f, 61.85f, 62.f,
            62.5F, 62.6F, 62.75f, 62.85f, 63.f,
            63.5F, 63.6F, 63.75f, 63.85f, 64.f,
            64.5F, 64.6F, 64.75f, 64.85f, 65.f,
            65.5F, 65.6F, 65.75f, 65.85f, 66.f,
            66.5F, 66.6F, 66.75f, 66.85f, 67.f,
            68.5F, 68.6F, 68.75f, 68.85f, 68.f,
            69.5F, 69.6F, 69.75f, 89.85f, 95.6f,
            100.5F, 105.6F, 110.115f, 110.85f, 111.f,
            111.5F, 111.65F, 111.115f, 111.85f, 112.f,
            112.5F, 112.65F, 112.115f
    };

    /// 负方向
    public static final float simulateDataneg[] = {
            40.4F, 44.8F, 48.7f, 52f, 54.6f,
            56.7F, 58.4F, 59.8f, 61.2f, 62.6f,
            64F, 65.3F, 66.5f, 69.8f, 72.7f,
            75.6F, 50.6F, 50.75f, 50.85f, 51.f,
            51.5F, 51.6F, 51.75f, 51.85f, 52.f,
            52.5F, 52.6F, 52.75f, 52.85f, 53.f,
            53.5F, 53.6F, 53.75f, 53.85f, 54.f,
            54.5F, 54.6F, 54.75f, 54.85f, 55.f,
            55.5F, 55.6F, 55.75f, 55.85f, 56.f,
            56.5F, 56.6F, 56.75f, 56.85f, 57.f,
            58.5F, 58.6F, 58.75f, 58.85f, 58.f,
            59.5F, 59.6F, 59.75f, 79.85f, 85.6f,
            90.5F, 95.6F, 100.115f, 100.85f, 101.f,
            101.5F, 101.65F, 101.115f, 101.85f, 102.f,
            102.5F, 102.65F, 102.115f
    };

    // 坐标轴画笔
    private Paint axisPaint;
    private Paint axisPermairyPaint;
    private Paint axisSecPPaint;
    private TextPaint axisLablePaint;
    private int xAxisTxtPadding;
    private float bottomTxtHeight;
    private float bottomContentHeight;
    private int innerStartLeft;
    private Paint baseNormalPaint;
    private float pxPerScore;
    private LineSet lineSet;
    private Path linePath;
    private LineSet lineSetNeg;
    private Paint mFillPaint;

    public float getInnerStartBottom() {
        return innerStartBottom;
    }

    private float innerStartBottom;

    private int leftTxtWidth;

    private int yAxisTxtPadding;


    public HealthLineView(Context context) {
        super(context);
        init();
    }


    public HealthLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {

        PER_MONTH_GRID_WIDTH_PX = Tools.fromDpToPx(PER_MONTH_GRID_WIDTH_DP);
        PER_GRID_HEIGHT_PX = Tools.fromDpToPx(PER_GRID_HEIGHT_DP);


        PER_GRID_WIDTH = PER_MONTH_GRID_WIDTH_PX / PER_MONTH_GRID_COUNT;

        PER_GRID_HEIGHT = PER_GRID_HEIGHT_PX / PER_VERTICAL_GRID_COUNT;


        /// 月数 * 大网格宽度(每个小网格宽度 * 每个大网格的有几个小网格)
        DEF_WIDTH = (int) (DEF_MONTH_COUNT * PER_GRID_WIDTH * PER_MONTH_GRID_COUNT);

        /// 纵向一共 纵向的大网格数 * 大网格宽度(每个小网格高度 * 每个大网格的有几个小网格)
        DEF_HEIGHT = (int) (DEF_HEIGHT_COUNT * PER_GRID_HEIGHT * PER_VERTICAL_GRID_COUNT);


        xAxisTxtPadding = (int) Tools.fromDpToPx(5.0f);

        yAxisTxtPadding = (int) Tools.fromDpToPx(0.0f);

        paddingLeft = (int) Tools.fromDpToPx(0.0f);

        innerStartLeft = paddingLeft + leftTxtWidth + yAxisTxtPadding;

        paddingTop = (int) Tools.fromDpToPx(5.f);


        /// 坐标轴画笔
        axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setColor(Color.GRAY);
        axisPaint.setStrokeWidth(Tools.fromDpToPx(1));
        axisPaint.setStyle(Paint.Style.FILL);

        /// 坐标轴 文字画笔
        axisLablePaint = new TextPaint(axisPaint);
        axisLablePaint.setTextSize(Tools.fromDpToPx(12.f));
        axisLablePaint.measureText("10");

        Paint.FontMetrics fontMetrics = axisLablePaint.getFontMetrics();
        bottomTxtHeight = fontMetrics.descent - fontMetrics.ascent;

        bottomContentHeight = bottomTxtHeight + xAxisTxtPadding;

        DEF_WIDTH = DEF_WIDTH + innerStartLeft;

        DEF_HEIGHT = (int) (DEF_HEIGHT + bottomContentHeight + paddingBottom + paddingTop);


        /// 内部开始画的地方
        innerStartBottom = DEF_HEIGHT - paddingBottom - bottomContentHeight;


        // 每分占用多少像素 Y轴方向上
        pxPerScore = innerStartBottom / (150.f - 40.f);


        /// 关键轴 大网格 画笔
        axisPermairyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPermairyPaint.setColor(Color.GRAY);
        axisPermairyPaint.setDither(true);
        axisPermairyPaint.setStrokeWidth(Tools.fromDpToPx(.75f));
        axisPermairyPaint.setStyle(Paint.Style.FILL);


        /// sec关键轴 小网格 画笔
        axisSecPPaint = new Paint(axisPermairyPaint);
        axisSecPPaint.setColor(Color.LTGRAY);
        axisSecPPaint.setDither(true);
        axisSecPPaint.setStrokeWidth(Tools.fromDpToPx(.45f));
        axisSecPPaint.setStyle(Paint.Style.FILL);

        //// 画常模线的画笔  ，
        baseNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        baseNormalPaint.setColor(Color.GRAY);
        baseNormalPaint.setDither(true);
        baseNormalPaint.setStyle(Paint.Style.STROKE);
        baseNormalPaint.setStrokeWidth(Tools.fromDpToPx(1f));


        /// 填充画渐变色的画笔
        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFillPaint.setColor(Color.GREEN);
        mFillPaint.setDither(true);
        mFillPaint.setStyle(Paint.Style.FILL);

        /// 画点图
        mDotLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotLinePaint.setColor(Color.RED);
        mDotLinePaint.setDither(true);
        mDotLinePaint.setStyle(Paint.Style.FILL);
        mDotLinePaint.setStrokeWidth(Tools.fromDpToPx(4.f));

        /// 画线
        mDotLineStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotLineStrokePaint.setColor(Color.RED);
        mDotLineStrokePaint.setDither(true);
        mDotLineStrokePaint.setStyle(Paint.Style.STROKE);
        mDotLineStrokePaint.setStrokeWidth(Tools.fromDpToPx(1.f));


        mClickableRadius = Tools.fromDpToPx(18.f);

        pathMeasure = new PathMeasure();

        linePath = new Path();
        lineSet = new LineSet();

        for (int i = 0; i < simulateData.length; i++) {

            Point point = new Point("" + i, simulateData[i]);
            point.setCoordinates(getXBaseByMonth(i), getYCorrByScore(simulateData[i]));
            lineSet.addPoint(point);
        }

        lineSetNeg = new LineSet();

        for (int i = 0; i < simulateDataneg.length; i++) {

            Point point = new Point("" + i, simulateDataneg[i]);
            point.setCoordinates(getXBaseByMonth(i), getYCorrByScore(simulateDataneg[i]));
            lineSetNeg.addPoint(point);
        }

        dataLineSet = new LineSet();
        dataLineRegions = new ArrayList<Region>(dataLineSet.size());
        for (int i = 10; i < simulateDataneg.length / 2; i++) {

            float simpointY = simulateDataneg[i] + 20;
            Point point = new Point("" + i, simpointY);
            point.setCoordinates(getXBaseByMonth(i), getYCorrByScore(simpointY));
            point.setRadius(Tools.fromDpToPx(2.f));
            point.setVisible(true);
            dataLineSet.addPoint(point);
            dataLineRegions.add(new Region());
        }

        defineRegions(dataLineRegions, dataLineSet);

        mGestureDetector = new GestureDetector(getContext(), new GestureListener());

    }


    /**
     * Listener callback on entry click
     */
    private OnEntryClickListener mEntryListener;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawChartBackGround(canvas);
        drawChartBaseData(canvas);
        drawChartDotline(canvas);
    }

    private void drawChartDotline(Canvas canvas) {

        drawPoints(canvas, dataLineSet);

        canvas.drawPath(createLinePath(dataLineSet), mDotLineStrokePaint);
    }


    /**
     * 画基础数据 常模图
     *
     * @param canvas
     */
    private void drawChartBaseData(Canvas canvas) {

        smoothLinePath = createSmoothLinePath(lineSet);

        canvas.drawPath(smoothLinePath, baseNormalPaint);


        Path secLinePath = createSmoothLinePath(lineSetNeg);

        canvas.drawPath(secLinePath, baseNormalPaint);


        //Draw background
//        if (lineSet.hasFill() || lineSet.hasGradientFill())
        canvas.drawPath(createBackgroundPath(new Path(smoothLinePath), new Path(secLinePath), lineSet, lineSetNeg), mFillPaint);

    }

    /**
     * 画图表的背景
     *
     * @param canvas
     */
    private void drawChartBackGround(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        /// 画坐标轴
        drawAxiss(canvas);

        //// 画关键轴
        drawPermiaryAxiss(canvas);
        //// 画关键轴labels
        drawPermiaryAxissLabels(canvas);

    }


    //// 画关键轴labels X
    private void drawPermiaryAxissLabels(Canvas canvas) {

        String firstLabel = "0";

//        float txtw = axisLablePaint.measureText(firstLabel);
        float sx = innerStartLeft;
        float sy = innerStartBottom + xAxisTxtPadding + bottomTxtHeight / 2;

        canvas.drawText(firstLabel, sx, sy, axisLablePaint);

        for (int i = 1; i < DEF_MONTH_COUNT; i++) {

            String currLabel = i + "";
            float txtWidth = axisLablePaint.measureText(currLabel);
            float txtsx = innerStartLeft + i * PER_MONTH_GRID_WIDTH_PX - (txtWidth / 2);
            float txtsy = innerStartBottom + xAxisTxtPadding + bottomTxtHeight / 2;

            canvas.drawText(currLabel, txtsx, txtsy, axisLablePaint);
        }


        String lastLabel = "" + DEF_MONTH_COUNT;

        float lasttxtw = axisLablePaint.measureText(lastLabel);
        float lastsx = DEF_WIDTH - lasttxtw;
        float lastsy = innerStartBottom + xAxisTxtPadding + bottomTxtHeight / 2;

        canvas.drawText(lastLabel, lastsx, lastsy, axisLablePaint);


    }

    /**
     * 画关键轴
     * <p>
     * 1. X 轴方向上  DEF_MONTH_COUNT *
     * 2. Y 轴方向上
     *
     * @param canvas
     */
    private void drawPermiaryAxiss(Canvas canvas) {

        drawPermiaryXAxis(canvas);
        drawPermiaryYAxis(canvas);

    }

    /**
     * 关键轴 Y方向-----
     * ----------------
     * --------------
     *
     * @param canvas
     */
    private void drawPermiaryYAxis(Canvas canvas) {


        for (int i = 1; i < DEF_HEIGHT_COUNT; i++) {

            canvas.drawLine(innerStartLeft, innerStartBottom - i * PER_GRID_HEIGHT_PX, DEF_WIDTH, innerStartBottom - i * PER_GRID_HEIGHT_PX, axisPermairyPaint);
        }


        int gridcount = DEF_HEIGHT_COUNT * PER_VERTICAL_GRID_COUNT;

        for (int i = 1; i < gridcount; i++) {
            if (i % PER_VERTICAL_GRID_COUNT != 0) {

                canvas.drawLine(innerStartLeft, innerStartBottom - i * PER_GRID_HEIGHT, DEF_WIDTH, innerStartBottom - i * PER_GRID_HEIGHT, axisSecPPaint);

            }
        }

    }

    /**
     * 关键轴X方向 ||||||||
     *
     * @param canvas
     */
    private void drawPermiaryXAxis(Canvas canvas) {

        for (int i = 1; i < DEF_MONTH_COUNT; i++) {
            canvas.drawLine(innerStartLeft + i * PER_MONTH_GRID_WIDTH_PX, paddingTop, innerStartLeft + i * PER_MONTH_GRID_WIDTH_PX, innerStartBottom, axisPermairyPaint);

        }

        int gridcount = DEF_MONTH_COUNT * PER_MONTH_GRID_COUNT;

        for (int i = 1; i < gridcount; i++) {
            if (i % PER_MONTH_GRID_COUNT != 0) {
                canvas.drawLine(innerStartLeft + i * PER_GRID_WIDTH, paddingTop, innerStartLeft + i * PER_GRID_WIDTH, innerStartBottom, axisSecPPaint);
            }
        }

    }

    /**
     * 上下左右 坐标轴
     *
     * @param canvas
     */
    private void drawAxiss(Canvas canvas) {

        //// 上
        canvas.drawLine(innerStartLeft, paddingTop, DEF_WIDTH, paddingTop, axisPaint);

        //// 下
        canvas.drawLine(innerStartLeft, innerStartBottom, DEF_WIDTH, innerStartBottom, axisPaint);

        //// 左
        canvas.drawLine(innerStartLeft, paddingTop, innerStartLeft, innerStartBottom, axisPaint);

        //// 右
        canvas.drawLine(DEF_WIDTH, paddingTop, DEF_WIDTH, innerStartBottom, axisPaint);

    }

    /**
     * 设定可点击区域
     *
     * @param regions Empty list of regions where result of this method must be assigned
     */
    void defineRegions(ArrayList<Region> regions, LineSet dataLineSet) {

        float x;
        float y;

        int dataSize = dataLineSet.size();
        for (int i = 0; i < dataSize; i++) {
            x = dataLineSet.getEntry(i).getX();
            y = dataLineSet.getEntry(i).getY();
            regions.get(i).set((int) (x - mClickableRadius), (int) (y - mClickableRadius),
                    (int) (x + mClickableRadius), (int) (y + mClickableRadius));
        }
    }


    /**
     * Responsible for drawing points
     */
    private void drawPoints(Canvas canvas, LineSet set) {


        int begin = set.getBegin();
        int end = set.getEnd();
        Point dot;
        for (int i = begin; i < end; i++) {

            dot = (Point) set.getEntry(i);

            if (dot.isVisible()) {

                // Style dot
//                mDotLinePaint.setColor(dot.getColor());
//                mDotLinePaint.setAlpha((int) (set.getAlpha() * FULL_ALPHA));
                applyShadow(mDotLinePaint, set.getAlpha(), dot.getShadowDx(), dot
                        .getShadowDy(), dot.getShadowRadius(), dot.getShadowColor());

                // Draw dot
                canvas.drawCircle(dot.getX(), dot.getY(), dot.getRadius(), mDotLinePaint);

                //Draw dots stroke
                if (dot.hasStroke()) {

                    // Style stroke
//                    mDotLineStrokePaint.setStrokeWidth(dot.getStrokeThickness());
//                    mDotLineStrokePaint.setColor(dot.getStrokeColor());
//                    mDotLineStrokePaint.setAlpha((int) (set.getAlpha() * FULL_ALPHA));
                    applyShadow(mDotLineStrokePaint, set.getAlpha(), dot.getShadowDx(), dot
                            .getShadowDy(), dot.getShadowRadius(), dot.getShadowColor());

                    canvas.drawCircle(dot.getX(), dot.getY(), dot.getRadius(), mDotLineStrokePaint);
                }

                // Draw drawable
//                if (dot.getDrawable() != null) {
//                    Bitmap dotsBitmap = Tools.drawableToBitmap(dot.getDrawable());
//                    canvas.drawBitmap(dotsBitmap, dot.getX() - dotsBitmap.getWidth() / 2,
//                            dot.getY() - dotsBitmap.getHeight() / 2, mDotLinePaint);
//                }

            }
        }

    }


    /**
     * Applies an alpha to the paint object.
     *
     * @param paint  {@link android.graphics.Paint} object to apply alpha
     * @param alpha  Alpha value (opacity)
     * @param dx     Dx
     * @param dy     Dy
     * @param radius Radius
     * @param color  Color
     */
    protected void applyShadow(Paint paint, float alpha, float dx, float dy, float radius,
                               int[] color) {

        paint.setAlpha((int) (alpha * FULL_ALPHA));
        paint.setShadowLayer(radius, dx, dy,
                Color.argb(((int) (alpha * FULL_ALPHA) < color[0]) ? (int) (alpha * FULL_ALPHA) : color[0],
                        color[1], color[2], color[3]));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modew = MeasureSpec.getMode(widthMeasureSpec);
        int mswidth = MeasureSpec.getSize(widthMeasureSpec);

        int modeh = MeasureSpec.getMode(heightMeasureSpec);
        int msheight = MeasureSpec.getSize(heightMeasureSpec);

        int viewWidth;
        if (modew == MeasureSpec.UNSPECIFIED || modew == MeasureSpec.AT_MOST) {
            viewWidth = DEF_WIDTH;
        } else {
            viewWidth = mswidth;
        }

        int viewHeight;
        if (modeh == MeasureSpec.UNSPECIFIED || modeh == MeasureSpec.AT_MOST) {
            viewHeight = DEF_HEIGHT;
        } else {
            viewHeight = msheight;
        }

        // 每分占用多少像素 Y轴方向上
        pxPerScore = innerStartBottom / (150.f - 40.f);

        Log.d("onMeasure", "pxPerScore: " + pxPerScore);

        setMeasuredDimension(viewWidth, viewHeight);

    }


    /**
     * 分数 对应在Y轴上显示占用多少Y坐标
     *
     * @param score
     * @return
     */
    public float getYCorrByScore(float score) {
        return innerStartBottom - (score - 40) * pxPerScore;
    }

    /**
     * 根据月份获取精准的X轴坐标
     *
     * @param month
     * @return
     */
    public float getXBaseByMonth(int month) {
        return innerStartLeft + month * PER_MONTH_GRID_WIDTH_PX;
    }


    /**
     * 获取X轴方向上某点对应的Y轴坐标
     *
     * @param scrollX
     * @return
     */
    public float[] getPositionByScorllX(float scrollX) {

        float[] pos = new float[2];
        float[] tan = new float[2];
        if (smoothLinePath != null && pathMeasure != null) {

            pathMeasure.setPath(smoothLinePath, false);

            float length = pathMeasure.getLength();

            float distance = scrollX * length / (DEF_WIDTH - innerStartLeft);

            pathMeasure.getPosTan(distance, pos, tan);
        }
        return pos;
    }


    /**
     * 根据X坐标计算月份
     *
     * @param xAxispx
     * @return
     */
    public float getBaseMonthByPx(int xAxispx) {

        return (xAxispx - innerStartLeft) / PER_MONTH_GRID_WIDTH_PX;
    }


    /**
     * Responsible for drawing a (non smooth) line.
     *
     * @param set {@link LineSet} object
     * @return {@link Path} object containing line
     */
    Path createLinePath(LineSet set) {

        Path res = new Path();
        res.setFillType(Path.FillType.EVEN_ODD);

        int begin = set.getBegin();
        int end = set.getEnd();
        for (int i = begin; i < end; i++) {
            if (i == begin) res.moveTo(set.getEntry(i).getX(), set.getEntry(i).getY());
            else res.lineTo(set.getEntry(i).getX(), set.getEntry(i).getY());
        }

        return res;
    }


    /**
     * Credits: http://www.jayway.com/author/andersericsson/
     * Method responsible to draw a smooth line with the parsed screen points.
     *
     * @param set {@link LineSet} object.
     * @return {@link Path} object containing smooth line
     */
    Path createSmoothLinePath(LineSet set) {

        float thisPointX;
        float thisPointY;
        float nextPointX;
        float nextPointY;
        float startDiffX;
        float startDiffY;
        float endDiffX;
        float endDiffY;
        float firstControlX;
        float firstControlY;
        float secondControlX;
        float secondControlY;

        Path res = new Path();
        /// move to first position
        res.moveTo(set.getEntry(set.getBegin()).getX(), set.getEntry(set.getBegin()).getY());

        int begin = set.getBegin();
        int end = set.getEnd();
        for (int i = begin; i < end - 1; i++) {

            thisPointX = set.getEntry(i).getX();
            thisPointY = set.getEntry(i).getY();

            nextPointX = set.getEntry(i + 1).getX();
            nextPointY = set.getEntry(i + 1).getY();

            startDiffX = (nextPointX - set.getEntry(si(set.size(), i - 1)).getX());
            startDiffY = (nextPointY - set.getEntry(si(set.size(), i - 1)).getY());

            endDiffX = (set.getEntry(si(set.size(), i + 2)).getX() - thisPointX);
            endDiffY = (set.getEntry(si(set.size(), i + 2)).getY() - thisPointY);

            firstControlX = thisPointX + (SMOOTH_FACTOR * startDiffX);
            firstControlY = thisPointY + (SMOOTH_FACTOR * startDiffY);

            secondControlX = nextPointX - (SMOOTH_FACTOR * endDiffX);
            secondControlY = nextPointY - (SMOOTH_FACTOR * endDiffY);

            res.cubicTo(firstControlX, firstControlY, secondControlX, secondControlY, nextPointX,
                    nextPointY);
        }

        return res;

    }


    /**
     * Responsible for drawing line background
     *
     * @param secSet {@link LineSet} object.
     * @return {@link Path} object containing background
     */
    private Path createBackgroundPath(Path first, Path secPath, LineSet firstSet, LineSet secSet) {

//        mFillPaint.setAlpha((int) (secSet.getAlpha() * FULL_ALPHA));

        mFillPaint.setAlpha(150);

        if (secSet.hasFill()) mFillPaint.setColor(secSet.getFillColor());
//        if (secSet.hasGradientFill())
// mFillPaint.setShader(
//                new LinearGradient(innerStartLeft, ,
//                        super.getInnerChartLeft(), super.getInnerChartBottom(),
//                        secSet.getGradientColors(), secSet.getGradientPositions(), Shader.TileMode.MIRROR));

        Path fillPath = new Path();
        fillPath.setFillType(Path.FillType.EVEN_ODD);

        fillPath.addPath(first);

        /// 移到第一个数据集的最右一个位置
//        fillPath.moveTo(firstSet.getEntry(firstSet.getEnd() - 1).getX(), firstSet.getEntry(firstSet.getEnd() - 1).getY());

        /// 连接第二个数据集的最右一个位置
        fillPath.lineTo(secSet.getEntry(secSet.getEnd() - 1).getX(), secSet.getEntry(secSet.getEnd() - 1).getY());

        /// 添加第二个路径
        fillPath.addPath(secPath);

        ///移到第二个数据集的第一个位置
//        fillPath.moveTo(secSet.getEntry(secSet.getBegin()).getX(), secSet.getEntry(secSet.getBegin()).getY());

        /// 连接第一个数据集的第一个位置
        fillPath.lineTo(firstSet.getEntry(firstSet.getBegin()).getX(), firstSet.getEntry(firstSet.getBegin()).getY());

        return fillPath;
    }


    /**
     * Credits: http://www.jayway.com/author/andersericsson/
     * Given an index in points, it will make sure the the returned index is
     * within the array.
     */
    private static int si(int setSize, int i) {

        if (i > setSize - 1) return setSize - 1;
        else if (i < 0) return 0;
        return i;
    }

    public void setmEntryListener(OnEntryClickListener mEntryListener) {
        this.mEntryListener = mEntryListener;
    }


    /**
     * Interface to define a listener when an chart entry has been clicked
     */
    public interface OnEntryClickListener {

        /**
         * Abstract method to define the code when an entry has been clicked
         *
         * @param rect a Rect covering the entry area.
         */
        void onClick(int dataSetIndex, ChartEntry chartEntry, Rect rect);

    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent ev) {
            Log.d("onSingleTapConfirmed", "ev: " + ev);

            if (mEntryListener != null) { // Check if tap on any entry
                int nSets = dataLineRegions.size();
                for (int i = 0; i < nSets; i++)
                    if (dataLineRegions.get(i).contains((int) ev.getX(), (int) ev.getY())) {
                        if (mEntryListener != null)  // Trigger entry callback
                            mEntryListener.onClick(i, dataLineSet.getEntry(i), getEntryRect(dataLineRegions.get(i)));
                        return true;
                    }
            }

            return false;

        }

//        @Override
//        public boolean onSingleTapUp(MotionEvent ev) {
//
//            if (mEntryListener != null) { // Check if tap on any entry
//                int nSets = dataLineRegions.size();
//                for (int i = 0; i < nSets; i++)
//                    if (dataLineRegions.get(i).contains((int) ev.getX(), (int) ev.getY())) {
//                        if (mEntryListener != null)  // Trigger entry callback
//                            mEntryListener.onClick(i, i, getEntryRect(dataLineRegions.get(i)));
//                        return true;
//                    }
//            }
//
//            return true;
//        }

        @Override
        public boolean onDown(MotionEvent e) {
//            Log.d("onDown", "e: " + e);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            Log.d("onScroll", "e1: " + e1);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /**
     * Get the area, {@link android.graphics.Rect}, of an entry from the entry's {@link
     * android.graphics.Region}
     *
     * @param region Region covering {@link ChartEntry} area
     * @return {@link android.graphics.Rect} specifying the area of an {@link ChartEntry}
     */
    Rect getEntryRect(@NonNull Region region) {
        checkNotNull(region);
        // Subtract the view left/top padding to correct position
        return new Rect(region.getBounds().left - getPaddingLeft(),
                region.getBounds().top - getPaddingTop(), region.getBounds().right - getPaddingLeft(),
                region.getBounds().bottom - getPaddingTop());
    }


    /**
     * The method listens for chart clicks and checks whether it intercepts
     * a known Region. It will then use the registered Listener.onClick
     * to return the region's index.
     */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (mGestureDetector == null) {
            return super.onTouchEvent(event);
        }
        return mGestureDetector.onTouchEvent(event);
    }


    /**
     * Class responsible to style the LineChart!
     * Can be instantiated with or without attributes.
     */
    class Style {

        static final int FULL_ALPHA = 255;

        /**
         * Paint variables
         */
        private Paint mDotsPaint;

        private Paint mDotsStrokePaint;

        private Paint mLinePaint;

        private Paint mFillPaint;


        Style() {
        }


        Style(TypedArray attrs) {
        }

        private void init() {

            mDotsPaint = new Paint();
            mDotsPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mDotsPaint.setAntiAlias(true);

            mDotsStrokePaint = new Paint();
            mDotsStrokePaint.setStyle(Paint.Style.STROKE);
            mDotsStrokePaint.setAntiAlias(true);

            mLinePaint = new Paint();
            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setAntiAlias(true);

            mFillPaint = new Paint();
            mFillPaint.setStyle(Paint.Style.FILL);
        }

        private void clean() {

            mLinePaint = null;
            mFillPaint = null;
            mDotsPaint = null;
        }

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
