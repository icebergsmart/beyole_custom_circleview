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
 * �Զ��廷�εȴ��ؼ�
 * 
 * @date 2016-01-18
 * @author Iceberg
 * 
 */
public class CustomCircleView extends View {

	// ��һȦ��ɫ
	private int mFirstColor;
	// �ڶ�Ȧ��ɫ
	private int mSecondColor;
	// Բ�����
	private int mCircleWidth;
	// ����
	private Paint mPaint;
	// ��ǰ����
	private int mProgress;
	// �ٶ�
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
		// ��ͼ�߳�
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
					// �����߳����ػ�viewʹ�ô˷���
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
		// ��ȡԲ������
		int center = getWidth() / 2;
		// �뾶
		int radius = center - mCircleWidth / 2;
		// ����Բ���Ŀ��
		mPaint.setStrokeWidth(mCircleWidth);
		// �������
		mPaint.setAntiAlias(true);
		// ���ÿ���
		mPaint.setStyle(Paint.Style.STROKE);
		// ���ڶ���Բ������״�ͽ���
		RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
		// ��һȦ��ɫ�������ڶ�Ȧ��ɫ��
		mPaint.setColor(mFirstColor);// ����Բ����ɫ
		// ����Բ��
		canvas.drawCircle(center, center, radius, mPaint);
		// ����Բ����ɫ
		mPaint.setColor(mSecondColor);
		// ���ݽ��Ȼ�Բ��
		canvas.drawArc(oval, -90, mProgress, false, mPaint);
	}
}
