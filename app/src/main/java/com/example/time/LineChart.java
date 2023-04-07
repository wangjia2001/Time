package com.example.time;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class LineChart extends View {
    public int XPoint=20;    //原点的X坐标
    public int YPoint=Constant.point.y/4;     //原点的Y坐标
    public int XLength=Constant.point.x-50;        //X轴的长度
    public int YLength=Constant.point.y/4-150;        //y轴的长度
    public int XScale;     //X的刻度长度
    public String[] XLabel;    //X的刻度
    public String[] Data;      //数据
    public String sumTitle;    //总时间标题
    public String avgTitle;    //平均时间标题
    public float Ymax;     //y轴最大值

    public LineChart(Context context){
        super(context);
    }
    public LineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void SetInfo(String[] XLabels, String[] AllData, String strsumTitle, String stravgTitle){
        XLabel=XLabels;
        Data=AllData;
        sumTitle=strsumTitle;
        avgTitle=stravgTitle;
        if (AllData!=null){
            XScale=XLength/(AllData.length-1);//实际X的刻度长度
            float max=0;
            for(int i=0;i<AllData.length;i++) {
                if (Float.parseFloat(AllData[i]) > max)
                    max = Float.parseFloat(AllData[i]);
            }
            Ymax = max;//这组数值的最大值设为y轴最大值
        }


    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);//重写onDraw方法

        Paint paint= new Paint();
        paint.setColor(Color.BLACK);//颜色
        paint.setTextSize(30);

        //画x轴
        canvas.drawLine(XPoint,YPoint,XPoint+XLength,YPoint,paint);   //轴线

        paint.setStrokeWidth(5); //设置线条粗细
        for(int i=0;i*XScale<XLength;i++) {
            //画x轴刻度
            canvas.drawLine(XPoint+i*XScale, YPoint, XPoint+i*XScale, YPoint-5, paint);  //刻度
            try {
                //x轴日期
                drawText(canvas,XLabel[i], XPoint + i * XScale,
                        YPoint + 55, paint,-45); // 文字
                // 数据值
                if (i > 0 ) // 保证有效数据
                    canvas.drawLine(XPoint + (i - 1) * XScale,
                            Ycount(Data[i - 1]), XPoint + i * XScale,
                            Ycount(Data[i]), paint);
                canvas.drawCircle(XPoint + i * XScale, Ycount(Data[i]), 2,
                        paint);
            } catch (Exception e) {
            }
        }

        //设置标题位置
        paint.setTextSize(40);
        canvas.drawText(sumTitle, 20, 50, paint);
        canvas.drawText(avgTitle, 20, 100, paint);

    }

    //设置文字显示方向
    void drawText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle){
        if(angle != 0){
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if(angle != 0){
            canvas.rotate(-angle, x, y);
        }
    }

    private int Ycount(String y0) { //计算绘制时的Y坐标
        int y;
        y = Integer.parseInt(y0);
        y= YPoint -( int ) (y * YLength / Ymax);
        return y;
    }
}