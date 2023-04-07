package com.example.time;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class NavView extends View {
    private Paint textPaint = new Paint();
    private String[] s = new String[]{
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    //鼠标点击、滑动时选择的字母
    private int choose = -1;
    //中间的文本
    private TextView tv;
    public NavView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NavView(Context context) {
        super(context);
    }
    public NavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initPaint() {
        textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画字母
        drawText(canvas);
    }
    /**
     * 画字母
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        //获取View的宽高
        int width = getWidth();
        int height = getHeight();
        //获取每个字母的高度
        int singleHeight = height / s.length;
        //画字母
        for (int i = 0; i < s.length; i++) {
            //画笔默认颜色
            initPaint();
            //高亮字母颜色
            if (choose == i) {
                textPaint.setColor(Color.WHITE);
            }
            //计算每个字母的坐标
            float x = (width - textPaint.measureText(s[i])) / 2;
            float y = (i + 1) * singleHeight;
            canvas.drawText(s[i], x, y, textPaint);
            //重置颜色
            textPaint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //计算选中字母
        int index = (int) (event.getY() / getHeight() * s.length);
        //防止脚标越界
        if (index >= s.length) {
            index = s.length - 1;
        } else if (index < 0) {
            index = 0;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setBackgroundColor(Color.GRAY);
                //选中字母高亮
                choose = index;
                //出现中间文字
                tv.setVisibility(VISIBLE);
                tv.setText(s[choose]);
                //调用ListView连动接口
                if (listener != null) {
                    listener.touchCharacterListener(s[choose]);
                }
                //重绘
                invalidate();
                break;
            default:
                setBackgroundColor(Color.TRANSPARENT);
                //取消选中字母高亮
                choose = -1;
                //隐藏中间文字
                tv.setVisibility(GONE);
                //重绘
                invalidate();
                break;
        }
        return true;
    }
    public onTouchCharacterListener listener;
    public interface onTouchCharacterListener {
        void touchCharacterListener(String s);
    }
    public void setListener(onTouchCharacterListener listener) {
        this.listener = listener;
    }
    /**
     * 传进来一个TextView
     *
     * @param tv
     */
    public void setTextView(TextView tv) {
        this.tv = tv;
    }

}

