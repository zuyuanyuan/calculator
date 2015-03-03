package com.kengdie.calculator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.kengdie.calculator.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class KeyView extends View {

	private int color = Color.parseColor("#EEEEEE");
	private int rectColor;
	private Paint mPaint;
	private RectF backRect;
	private float scale;
	private float eachLength;
	private float screenWidth, screenHeight;
	private float flag;
	private String type;
	private static Uri uri;

	// private static Bitmap MainActivity.backBitmap;
	private String index;
	private int backLength;

	public KeyView(Context context) {
		this(context, null);
	}

	public KeyView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public KeyView(Context context, AttributeSet attrs, int defstyle) {
		super(context, attrs, defstyle);
		color = Color.parseColor("#eeeeee");
		rectColor = Color.parseColor("#b7b7b7");
		mPaint = new Paint();
		index = "";
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.NumView);
		index = a.getString(R.styleable.NumView_index);
		mPaint.setColor(color);
		scale = getResources().getDisplayMetrics().density;
		screenHeight = getResources().getDisplayMetrics().heightPixels;
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		flag = screenHeight / screenWidth;
		eachLength = (screenWidth / scale - 60) / 4;
		eachLength *= scale;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (flag < 1.7 && flag > 1.5)
			setMeasuredDimension((int) eachLength, (int) eachLength - 20);
		else if (flag > 1.7) {
			setMeasuredDimension((int) eachLength, (int) eachLength);
		} else if (flag < 1.5) {
			setMeasuredDimension((int) eachLength, (int) eachLength);
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bitmap tmp = null;
		if (MainActivity.backBitmap != null) {
			tmp = getImageBitmap(MainActivity.backBitmap);
			int[] co = new int[2];
			getBack(co, index);
			tmp = Bitmap.createBitmap(tmp, co[1], co[0],
					backLength, backLength);
		}
		backRect = new RectF(1, 0, (float) (getWidth() - 1), getHeight() - 4.5f);
		RectF outRectF = new RectF(0, 0, getWidth(), getHeight());
		mPaint.setColor(rectColor);
		canvas.drawRoundRect(outRectF, 7, 7, mPaint);
		mPaint.setColor(color);
		if (MainActivity.backBitmap != null) {
			Rect rect = new Rect(2, 2, getWidth() - 2, getHeight() - 5);
			canvas.drawBitmap(tmp, null, rect, mPaint);
//			tmp.recycle();
		} else {
			canvas.drawRoundRect(backRect, 7, 7, mPaint);
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void changeBackground() {
		invalidate();
	}

	public void getBack(int[] coor, String index) {
		int flag = 0;
		for (int i = 0; i < 20; i++) {
			if (index.equals(i + "")) {
				flag = i;
				break;
			}
		}
		int x = flag / 4;
		int y = flag % 4;
		coor[0] = x * backLength;
		coor[1] = y * backLength;
	}

	public Bitmap getImageBitmap(Bitmap backBitmap) {
		Bitmap res = null;
		float width = backBitmap.getWidth();
		float height = backBitmap.getHeight();
		float ratio = height / width;
		int x = 0, y = 0;
		if (ratio > 1.25) {
			height = width / 4 * 5;
			x = 0;
			y = (int) ((backBitmap.getHeight() - height) / 2);
		} else if (ratio < 0.8) {
			width = height / 5 * 4;
			y = 0;
			x = (int) ((backBitmap.getWidth() - width) / 2);
		} else if (ratio >= 0.8 && ratio < 1.0) {
			height = width / 4 * 5;
			x = 0;
			y = 0;
		} else if (ratio >= 1 && ratio <= 1.25) {
			width = height / 5 * 4;
			x = 0;
			y = 0;
		}
		res = Bitmap.createBitmap(backBitmap, x, y, (int) width, (int) height);
		backLength = (int) (res.getWidth() / 4);
		return res;
	}

}
