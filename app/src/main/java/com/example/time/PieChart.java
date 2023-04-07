package com.example.time;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.annotation.Nullable;

import java.util.List;


public class PieChart extends View {

    //画笔
    private Paint mPaint;
    private Paint mTvPaint;

    //饼状图宽高
    private int mWidth, mHeight;

    //饼状图起始角度
    private float mStartAngle = 0f;

    //动画时间
    private static final long ANIMATION_DURATION = 1000;

    //用户数据
    private List<PieData> mData;

    // 绘制方式
    private int mDrawWay = PART;
    public static final int PART = 0;//分布绘制
    public static final int COUNT = 1;//连续绘制
    private int centerX, centerY;//中心坐标
    private int offset = 6;

    // 自定义动画
    private PieChartAnimation mAnimation;
    private float sumValue = 0;//数据值的总和

    public PieChart(Context context) {
        super(context);
        init();
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防止抖动
        mPaint.setStyle(Paint.Style.FILL);//画笔为填充
        mTvPaint = new Paint();
        mTvPaint.setStyle(Paint.Style.FILL);
        mTvPaint.setColor(Color.GRAY);
        mTvPaint.setTextSize(30);
        //初始化动画
        mAnimation = new PieChartAnimation();
        mAnimation.setDuration(ANIMATION_DURATION);
    }


    //设置起始角度
    public PieChart setmStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();//刷新
        return this;
    }

    //设置数据
    public void setData(List<PieData> mData) {
        setmData(mData);
    }


    //设置数据和绘制方式
    public void setData(List<PieData> mData, int mDrawWay) {
        setmData(mData);
        this.mDrawWay = mDrawWay;
    }

    //设置数据
    private void setmData(List<PieData> mData) {
        sumValue = 0;
        this.mData = mData;
        initData(mData);
        startAnimation(mAnimation);
        invalidate();
    }

    //总和
    public PieChart setSumValue(float sumValue) {
        this.sumValue = sumValue;
        invalidate();//刷新
        return this;
    }

    //初始化数据
    private void initData(List<PieData> mData) {
        if (mData == null || mData.size() == 0) {
            return;
        }

        //计算数据总和确定颜色
        for (int i = 0; i < mData.size(); i++) {
            i=i%10;
            PieData data = mData.get(i);
            sumValue += data.getValue();
            data.setColor(COLORS[i]);
        }

        //计算百分比和角度
        float currentStartAngle = mStartAngle;
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            data.setCurrentStartAngle(currentStartAngle);
            //通过总和来计算百分比
            float percentage = data.getValue() / sumValue;
            //通过百分比来计算对应的角度
            float angle = percentage * 360;
            //设置用户数据
            data.setPercentage(percentage);
            data.setAngle(angle);
            currentStartAngle += angle;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mData == null) {
            return;
        }

        centerX = (getRight() - getLeft()) / 2;
        centerY = (getBottom() - getTop()) / 2;
        //1.移动画布到中心点
        canvas.translate(mWidth / 2, mHeight / 2);
        //2.设置当前起始角度
        float currentStartAngle = mStartAngle;
        //3.确定饼图的半径
        float r = (float) (Math.min(mWidth, mHeight) / 3);
        float r1 = r / 2;
        float r2 = r / 1.8f;
        float stopX = 0;
        float stopY = 0;
        //4.确定饼图的矩形大小
        RectF rectF = new RectF(-r, -r, r, r);
        RectF rectF1 = new RectF(-r1, -r1, r1, r1);
        RectF rectF2 = new RectF(-r2, -r2, r2, r2);
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            //5.设置颜色
            mPaint.setColor(data.getColor());
            //6.绘制饼图

            canvas.drawArc(rectF, currentStartAngle, data.getAngle(), true, mPaint);
            //7.绘制下一块扇形时先将角度加上当前扇形的角度
            mPaint.setColor(data.getColor());
            stopX = (float) ((r + 30) * Math.cos((2 * currentStartAngle + data.getAngle()) / 2 * Math.PI / 180));
            stopY = (float) ((r + 30) * Math.sin((2 * currentStartAngle + data.getAngle()) / 2 * Math.PI / 180));
            //canvas.drawPoint(stopX, stopY, TPaint);
            canvas.drawCircle(stopX, stopY, 10, mPaint);
            currentStartAngle += data.getAngle();

            //画横线
            int dx;
            // 判断横线是画在左边还是右边
            int endX;
            if (stopX > 0) {
                endX = (centerX - getPaddingRight() - 20);
            } else {
                endX = (-centerX + getPaddingLeft() + 20);
            }
            //画横线
            canvas.drawLine(stopX, stopY,
                    endX, stopY, mPaint);
            dx = (int) (endX - stopX);
            //测量文字大小
            Rect rect = new Rect();
            String text = data.getText();
            mTvPaint.getTextBounds(text, 0, text.length(), rect);
            int w = rect.width();
            int h = rect.height();
            canvas.drawText(text, 0, text.length(), dx > 0 ? endX - offset - w : endX + offset, stopY + h + offset, mTvPaint);
            Rect rect2 = new Rect();
            String value = data.getPercentage();
            mTvPaint.getTextBounds(value, 0, value.length(), rect2);
            int w2 = rect2.width();
            int h2 = rect2.height();
            canvas.drawText(value, 0, value.length(), dx > 0 ? endX - offset - w2 : endX + offset, stopY - offset, mTvPaint);
        }
        //绘制中心空白处
        mPaint.setColor(0x11000000);
        canvas.drawArc(rectF2, currentStartAngle, 360f, true, mPaint);
        //绘制中心阴影部分
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawArc(rectF1, currentStartAngle, 360f, true, mPaint);
        //绘制文字
        mPaint.setColor(0xEEFF4567);
        mPaint.setTextSize(80);
//        Rect textRect = new Rect();
//        mPaint.getTextBounds(str, 0, str.length(), textRect);
        // canvas.drawText("", -textRect.width() / 2, 0, mPaint);
    }


    /**
     * 自定义动画
     */
    public class PieChartAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                for (int i = 0; i < mData.size(); i++) {
                    PieData data = mData.get(i);
                    //通过总和来计算百分比
                    float percentage = data.getValue() / sumValue;
                    //通过百分比来计算对应的角度
                    float angle = percentage * 360;
                    //根据插入时间来计算角度
                    angle = angle * interpolatedTime;
                    data.setAngle(angle);
                }
            } else {//默认显示效果
                for (int i = 0; i < mData.size(); i++) {
                    //通过总和来计算百分比
                    PieData data = mData.get(i);
                    float percentage = data.getValue() / sumValue;
                    //通过百分比来计算对应的角度
                    float angle = percentage * 360;
                    data.setPercentage(percentage);
                    data.setAngle(angle);
                }
            }
            invalidate();
        }
    }

    public final static int[] COLORS = {
            Color.rgb(192, 255, 140), Color.rgb(140, 234, 255)
            , Color.rgb(255, 247, 140), Color.rgb(255, 208, 140)
            , Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)
            , Color.rgb(45, 192, 252), Color.rgb(53, 194, 209)
            , Color.rgb(52, 152, 219), Color.rgb(255, 255, 240)};
}
