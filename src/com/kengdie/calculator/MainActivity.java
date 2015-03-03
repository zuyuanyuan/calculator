package com.kengdie.calculator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import com.kengdie.calculator.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

public class MainActivity extends Activity {
	private NumView one, two, three, four, five, six, seven, eight, zero, nine,
			dian, zhengfu, baifen;
	private SimbolView add, minus, cheng, chu, dengyu, AC;
	private Beijing beijing;
	private double num1, num2, ans;
	private String num, simbol, preSimbol, real;
	private int flag;
	private ScreenView screenView = null;
	private boolean isNum, isfirst;
	public static Bitmap backBitmap;
	private MyHandler myHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		flag = 0;
		num = "";
		simbol = "";
		isfirst = true;
		preSimbol = "=";
		myHandler = new MyHandler();
		screenView = (ScreenView) findViewById(R.id.screenView);
		dian = (NumView) findViewById(R.id.dian);
		one = (NumView) findViewById(R.id.one);
		two = (NumView) findViewById(R.id.two);
		three = (NumView) findViewById(R.id.thr);
		four = (NumView) findViewById(R.id.four);
		five = (NumView) findViewById(R.id.five);
		six = (NumView) findViewById(R.id.six);
		seven = (NumView) findViewById(R.id.seven);
		eight = (NumView) findViewById(R.id.eight);
		zero = (NumView) findViewById(R.id.zero);
		nine = (NumView) findViewById(R.id.nine);
		add = (SimbolView) findViewById(R.id.add);
		minus = (SimbolView) findViewById(R.id.minus);
		cheng = (SimbolView) findViewById(R.id.cheng);
		chu = (SimbolView) findViewById(R.id.chu);
		dengyu = (SimbolView) findViewById(R.id.dengyu);
		zhengfu = (NumView) findViewById(R.id.zhengfu);
		beijing = (Beijing) findViewById(R.id.beijing);
		AC = (SimbolView) findViewById(R.id.AC);
		baifen = (NumView) findViewById(R.id.baifen);
		AC.setOnClickListener(new MyListener());
		baifen.setOnClickListener(new MyListener());
		screenView.setOnClickListener(new MyListener());
		dian.setOnClickListener(new MyListener());
		one.setOnClickListener(new MyListener());
		two.setOnClickListener(new MyListener());
		three.setOnClickListener(new MyListener());
		four.setOnClickListener(new MyListener());
		five.setOnClickListener(new MyListener());
		six.setOnClickListener(new MyListener());
		seven.setOnClickListener(new MyListener());
		eight.setOnClickListener(new MyListener());
		nine.setOnClickListener(new MyListener());
		zero.setOnClickListener(new MyListener());
		add.setOnClickListener(new MyListener());
		minus.setOnClickListener(new MyListener());
		cheng.setOnClickListener(new MyListener());
		chu.setOnClickListener(new MyListener());
		dengyu.setOnClickListener(new MyListener());
		zhengfu.setOnClickListener(new MyListener());
		beijing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupWindow(MainActivity.this, beijing);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null && requestCode == 100) {
			Uri uri = data.getData();
			ContentResolver cr = this.getContentResolver();
			Cursor cursor = cr.query(uri, null, null, null, null);

			if (cursor.moveToFirst()) {
				try {
					getBitmap(cr, uri);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				backBitmap = scale(backBitmap);
				Message msg = new Message();

				myHandler.sendMessage(msg);
			}
		} else {
			if (backBitmap != null)
			backBitmap.recycle();
			backBitmap = null;
		}
	}

	// 图片放大实现
	private int primaryWidth; // 原图片宽
	private int primaryHeight; // 原图片高
	private double scaleWidth, scaleHeight; // 高宽比例

	private Bitmap scale(Bitmap bmp) {

		double scale_width = 0;
		double scale_height = 0;
		// 创建BitMap对象，用于显示图片
		// 原始大小
		primaryWidth = bmp.getWidth();
		primaryHeight = bmp.getHeight();
		scale_width = scale_height = 1000.0 / primaryHeight;

		scaleWidth = scaleHeight = 1;
		scaleWidth = scaleWidth * scale_width; // 缩放到原来的*倍
		scaleHeight = scaleHeight * scale_height;

		Matrix matrix = new Matrix(); // 矩阵，用于图片比例缩放
		matrix.postScale((float) scaleWidth, (float) scaleHeight); // 设置高宽比例（三维矩阵）

		// 缩放后的BitMap
		return Bitmap.createBitmap(bmp, 0, 0, primaryWidth, primaryHeight,
				matrix, true);

	}

	public void getBitmap(ContentResolver cr, Uri url)
			throws FileNotFoundException, IOException {
		InputStream input = cr.openInputStream(url);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inPurgeable = true;
		backBitmap = BitmapFactory.decodeStream(input, null, options);
		input.close();
	}

	class MyListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			KeyView ori = (KeyView) v;
			String type = ori.getType();
			String numType;
			DecimalFormat decimalFormat = new DecimalFormat("0.00000000");// 格式化设置
			if (type.equals("num")) {
				numType = ((NumView) v).getNum();
				if (numType.equals("+/-")) {
					num2 = 0 - num2;
					screenView.displayRes(decimalFormat.format(num2));
				} else if (numType.equals("%")) {
					num2 = num2 / 100;
					screenView.displayRes(decimalFormat.format(num2));
				} else {
					num += ((NumView) v).getNum();
					num2 = testToNum(num);
					screenView.displayRes(decimalFormat.format(num2));
				}
				num1 = num2;
			} else {
				simbol = ((SimbolView) v).getSilbol();
				num = "0";
				if (flag == 0) {
					flag = 1;
					ans = num1;
				} else {
					if (simbol.equals("=") == false
							&& simbol.equals("AC") == false) {
						if (preSimbol.equals("=") == false)
							getAns(num2, real);
					}
					screenView.displayRes(decimalFormat.format(ans));
				}
				if (simbol.equals("=")) {
					System.out.println("ans = " + ans);
					getAns(num2, real);
					screenView.displayRes(decimalFormat.format(ans));
				}
				if (simbol.equals("=") == false && simbol.equals("AC") == false)
					real = simbol;
				preSimbol = simbol;

				if (simbol.equals("AC")) {
					screenView.displayRes("0");
					ans = 0;
					num1 = 0;
					num2 = 0;
					flag = 0;
					preSimbol = "=";
				}
			}
		}
	}

	public void getAns(double num, String simbol) {
		num1 = num;
		if (simbol.equals("+")) {
			ans = ans + num1;
		}
		if (simbol.equals("-")) {
			ans = ans - num1;
		}
		if (simbol.equals("*")) {
			ans = ans * num1;
		}
		if (simbol.equals("/")) {
			if (Math.abs(num1 - 0) < 0.000001)
				ans = 888888888;
			else {
				ans = ans / num1;
			}
		}

		System.out.println("ths ans = " + ans);

	}

	public double testToNum(String ori) {
		int length = ori.length();
		if (length > 9) {
			ori = ori.substring(0, 9);
		}
		length = ori.length();
		int index = length - 1;
		double pre = 0, now = 0;
		for (int i = 0; i < length; i++) {
			if (ori.charAt(i) == '.') {
				index = i;
				break;
			}
		}
		for (int i = 0; i < index; i++) {
			pre *= 10;
			pre += (ori.charAt(i) - '0');
		}
		for (int i = length - 1; i > index; i--) {
			now /= 10;
			now += (ori.charAt(i) - '0');
		}
		if (index == length - 1 && ori.charAt(length - 1) != '.') {
			pre *= 10;
			pre += ori.charAt(length - 1) - '0';
		}
		now /= 10;
		pre += now;
		return pre;
	}

	public void showPopupWindow(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.popwindow, null, false);
		int screenWidth = getResources().getDisplayMetrics().widthPixels;
		final PopupWindow pw = new PopupWindow(view, screenWidth - 50, 300,
				true);
		pw.setFocusable(true);
		pw.setTouchable(true);
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.setOutsideTouchable(true);

		Button clear = null;
		Button choose = null;
		clear = (Button) view.findViewById(R.id.clear);
		choose = (Button) view.findViewById(R.id.choose);
		final Message msg = new Message();
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
				backBitmap = null;
				myHandler.sendMessage(msg);
			}
		});
		choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				MainActivity.this.startActivityForResult(intent, 100);
			}
		});
		
		pw.showAtLocation(MainActivity.this.findViewById(R.id.main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		// 显示popupWindow对话框
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			dian.changeBackground();
			one.changeBackground();
			two.changeBackground();
			three.changeBackground();
			four.changeBackground();
			five.changeBackground();
			six.changeBackground();
			seven.changeBackground();
			eight.changeBackground();
			nine.changeBackground();
			zero.changeBackground();
			add.changeBackground();
			minus.changeBackground();
			cheng.changeBackground();
			chu.changeBackground();
			dengyu.changeBackground();
			zhengfu.changeBackground();
			AC.changeBackground();
			beijing.changeBackground();
			baifen.changeBackground();
		}
	}
}
