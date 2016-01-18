package com.beyole.view;

import com.beyole.customcircleview.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 自定义环形等待控件
 * 
 * @date 2016-01-18
 * @author Iceberg
 * 
 */
public class CustomCircleView extends View {

	// 第一圈颜色
	private int mFirstColor;
	// 第二圈颜色
	private int mSecondColor;
	// 圆环宽度
	private int mCircleWidth;
	// 画笔
	private Paint mPaint;
	// 当前进度
	private int mProgress;
	// 速度
	private int mSpeed;

	public CustomCircleView(Context context) {
		this(context, null);
	}

	public CustomCircleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomCircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomCircleView, defStyle, 0);
		int n = array.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = array.getIndex(i);
			switch (attr) {
			case R.styleable.CustomCircleView_firstColor:
				mFirstColor = array.getColor(attr, Color.CYAN);
				break;
			case R.styleable.CustomCircleView_secondColor:
				mSecondColor = array.getColor(attr, Color.RED);
				break;
			case R.styleable.CustomCircleView_circleWidth:
				mCircleWidth = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
				break;
			case R.styleable.CustomCircleView_speed:
				mSpeed = array.getInt(attr, 20);
				break;
			}
		}
		array.recycle();
		mPaint = new Paint();
		// 绘图线程
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					mProgress++;
					if (mProgress == 360) {
						mProgress = 0;
						int temp=mFirstColor;
						mFirstColor=mSecondColor;
						mSecondColor=temp;
					}
					// 在子线程中重绘view使用此方法
					postInvalidate();
					try {
						Thread.sleep(mSpeed);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 获取圆心坐标
		int center = getWidth() / 2;
		// 半径
		int radius = center - mCircleWidth / 2;
		// 设置圆环的宽度
		mPaint.setStrokeWidth(mCircleWidth);
		// 消除锯齿
		mPaint.setAntiAlias(true);
		// 设置空心
		mPaint.setStyle(Paint.Style.STROKE);
		// 用于定义圆弧的形状和界限
		RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
		// 第一圈颜色完整，第二圈颜色跑
		mPaint.setColor(mFirstColor);// 设置圆环颜色
		// 画出圆环
		canvas.drawCircle(center, center, radius, mPaint);
		// 设置圆环颜色
		mPaint.setColor(mSecondColor);
		// 根据进度画圆弧
		canvas.drawArc(oval, -90, mProgress, false, mPaint);
	}
}
