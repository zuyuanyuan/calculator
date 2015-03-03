package com.kengdie.calculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class ScreenView extends View {

	private Paint paint, backPaint;
	private int[] colors;
	private RectF displayRectF;
	private RectF backrgound;
	private int backColor;
	private String num;
	private int numSize;
	private Rect numBound;
	Typeface fontFace ;

	public ScreenView(Context context) {
		this(context, null);
	}

	public ScreenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		colors = new int[] { Color.parseColor("#ffca30"),
				Color.parseColor("#ffe753"), Color.parseColor("#ffcc32"),
				Color.parseColor("#ffcf23") };
		paint = new Paint();
		backPaint = new Paint();
		displayRectF = new RectF();
		backrgound = new RectF();
		numBound = new Rect();
		num = "0";
		fontFace = Typeface.createFromAsset(context.getAssets(),
			    "fonts/DroidSansFallback.ttf");
		backColor = Color.parseColor("#e8b315");
		// 设置颜色渐变效果
		LinearGradient lg = new LinearGradient(0, 0, 100, 100, colors, null,
				Shader.TileMode.MIRROR);
		paint.setShader(lg);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		backrgound.set(0, 0, getWidth(), getHeight());
		displayRectF.set(5, 5, getWidth() - 5, getHeight() - 5);
		backPaint.setColor(backColor);
		canvas.drawRoundRect(backrgound, 10, 10, backPaint);
		canvas.drawRoundRect(displayRectF, 10, 10, paint);
		numSize = getHeight() / 3 * 2;
		backPaint.setColor(Color.parseColor("#251c00"));
		backPaint.setTextSize(numSize);
		backPaint.setTypeface(fontFace);
		if (num.length() == 0)
			num = "0";
		backPaint.getTextBounds(num, 0, num.length(), numBound);
		canvas.drawText(num, getWidth() - 40 - numBound.width(),
				(getHeight() + numBound.height()) / 2, backPaint);
	}

	public  void displayRes(String res) {
		num = delete(res);
		if(num.length() > 9)
			num = num.substring(0,9);
		invalidate();
	}
	
	public String delete(String ori)
	{
		System.out.println("ori  = " + ori);
		int length = ori.length();
		int index = -1;
		String ans = "";
		for (int i=0; i<length; i++)
		{
			if (ori.charAt(i) == '.')
			{
				index = i;
				break;
			}
		}
		if (index != -1)
		{
			int i = length - 1;
			while(i >= index && (ori.charAt(i) == '0'))
			{
				i--;
			}
			index = i + 1;
		}
		if (index == -1)
			return ori;
		else {
			return ori.substring(0,index);
		}
	}
	
	

}
