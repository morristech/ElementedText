package com.yashoid.elementedtext;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.style.ReplacementSpan;
import android.view.MotionEvent;
import android.widget.TextView;

public abstract class ElementSpan extends ReplacementSpan {
	
	private CharSequence mText;
	
	private int mWidth;
	private int mHeight;
	
	private float mLeft;
	
	protected ElementSpan(CharSequence text) {
		mText = text;
	}
	
	public CharSequence getText() {
		return mText;
	}
	
	protected void onRemoved() {
		
	}
	
	protected boolean onClick(TextView v, MotionEvent e) {
		float x = e.getX() - mLeft;
		float y = e.getY();
		
		return onClick(v, x, y);
	}
	
	abstract protected boolean onClick(TextView v, float x, float y);
	
	protected void setWidth(int width) {
		mWidth = width;
	}
	
	protected void setHeight(int height) {
		mHeight = height;
	}
	
	protected int getWidth() {
		return mWidth;
	}
	
	protected int getHeight() {
		return mHeight;
	}
	
	abstract protected void measure(Paint paint);

	@Override
	public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
		measure(paint);
		
		if (fm!=null) {
			fm.ascent = -mHeight;
			fm.descent = 0;
			
			fm.top = fm.ascent;
			fm.bottom = 0;
		}
		
		return mWidth;
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
		mLeft = x;
		
		canvas.save();
		
		canvas.translate(x, bottom - mHeight);
		
		draw(canvas, paint);
		
		canvas.restore();
	}
	
	abstract protected void draw(Canvas canvas, Paint paint);
	
	protected void invalidate(TextView v) {
		v.setText(v.getText());
	}

}
