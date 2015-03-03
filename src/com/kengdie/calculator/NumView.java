package com.kengdie.calculator;

import com.kengdie.calculator.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;

public class NumView extends KeyView {

	private String num = "";
	private int numSize;
	private int numColor;
	private Paint numPaint;
	private Rect numBound;

	public NumView(Context context) {
		this(context, null);
	}

	public NumView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NumView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.NumView);
		num = a.getString(R.styleable.NumView_num);
		numSize = a.getDimensionPixelSize(R.styleable.NumView_numsize,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
						getResources().getDisplayMetrics()));
		a.recycle();
		numColor = Color.parseColor("#5f5f5f");
		numPaint = new Paint();
		numBound = new Rect();
		setType("num");
	}

	@Override
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		numPaint.setColor(numColor);
		numPaint.setTextSize(numSize);
		numPaint.getTextBounds(num, 0, num.length(), numBound);
		int x = (getMeasuredWidth() - numBound.width()) / 2;
		int y = (getHeight() + numBound.height()) / 2;
		if (num.equals("1"))
			x -= 5;
		if (num.equals("="))
		{
			y += 8;
			x -= 2;
		}
		if (num.equals("+"))
			y += 3;
		if (num.equals("-"))
			y += 13;
		if (num.equals("*"))
			y += 6;
		canvas.drawText(num,x,y, numPaint);
		// canvas.drawText(num, x, y, paint);
	}
	
	public String getNum()
	{
		return this.num;
	}
}
