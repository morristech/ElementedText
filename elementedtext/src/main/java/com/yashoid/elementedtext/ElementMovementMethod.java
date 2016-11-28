package com.yashoid.elementedtext;

import android.text.Spannable;
import android.text.Spanned;
import android.text.method.BaseMovementMethod;
import android.text.method.MovementMethod;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

public class ElementMovementMethod extends BaseMovementMethod {
	
//	private static final String TAG = "ElementMovementMethod";

	private TextView mTextView;
	
	private MovementMethod mCoreMovementMethod;
	
	private GestureDetector mGestureDetector;
	
	private boolean mCoreWantsTouch = false;
	private boolean mDetectorWantsTouch = false;
	
	public ElementMovementMethod(TextView widget, MovementMethod coreMovementMethod) {
		mCoreMovementMethod = coreMovementMethod;

		mTextView = widget;
		
		mGestureDetector = new GestureDetector(widget.getContext(), mOnGestureListener);
	}

	@Override
	public boolean canSelectArbitrarily() {
		if (mCoreMovementMethod==null) {
			return super.canSelectArbitrarily();
		}
		
		return mCoreMovementMethod.canSelectArbitrarily();
	}

	@Override
	public void initialize(TextView widget, Spannable text) {
		if (mCoreMovementMethod==null) {
			super.initialize(widget, text);
		}
		else {
			mCoreMovementMethod.initialize(widget, text);
		}
	}

	@Override
	public boolean onGenericMotionEvent(TextView widget, Spannable text, MotionEvent event) {
		if (mCoreMovementMethod==null) {
			return super.onGenericMotionEvent(widget, text, event);
		}
		
		return mCoreMovementMethod.onGenericMotionEvent(widget, text, event);
	}

	@Override
	public boolean onKeyDown(TextView widget, Spannable text, int keyCode, KeyEvent event) {
		if (mCoreMovementMethod==null) {
			return super.onKeyDown(widget, text, keyCode, event);
		}
		
		return mCoreMovementMethod.onKeyDown(widget, text, keyCode, event);
	}

	@Override
	public boolean onKeyOther(TextView view, Spannable text, KeyEvent event) {
		if (mCoreMovementMethod==null) {
			return super.onKeyOther(view, text, event);
		}
		
		return mCoreMovementMethod.onKeyOther(view, text, event);
	}

	@Override
	public boolean onKeyUp(TextView widget, Spannable text, int keyCode, KeyEvent event) {
		if (mCoreMovementMethod==null) {
			return super.onKeyUp(widget, text, keyCode, event);
		}
		
		return mCoreMovementMethod.onKeyUp(widget, text, keyCode, event);
	}

	@Override
	public void onTakeFocus(TextView widget, Spannable text, int direction) {
		if (mCoreMovementMethod==null) {
			super.onTakeFocus(widget, text, direction);
		}
		else {
			mCoreMovementMethod.onTakeFocus(widget, text, direction);
		}
	}

	@Override
	public boolean onTouchEvent(TextView widget, Spannable text, MotionEvent event) {
		if (event.getAction()==MotionEvent.ACTION_DOWN) {
			mCoreWantsTouch = true;
			mDetectorWantsTouch = true;
		}
		
//		if (mCoreWantsTouch) {
			if (mCoreMovementMethod==null) {
				mCoreWantsTouch = super.onTouchEvent(widget, text, event);
			}
			else {
				mCoreWantsTouch = mCoreMovementMethod.onTouchEvent(widget, text, event);
			}
//		}
		
		if (mDetectorWantsTouch) {
			mDetectorWantsTouch = mGestureDetector.onTouchEvent(event);
		}
		
		return true;//mCoreWantsTouch || mDetectorWantsTouch;
	}

	@Override
	public boolean onTrackballEvent(TextView widget, Spannable text, MotionEvent event) {
		if (mCoreMovementMethod==null) {
			return super.onTouchEvent(widget, text, event);
		}
		
		return mCoreMovementMethod.onTouchEvent(widget, text, event);
	}
	
	private OnGestureListener mOnGestureListener = new OnGestureListener() {
		
		private ElementSpan mTouchedSpan;
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return mTouchedSpan.onClick(mTextView, e);
		}
		
		@Override
		public void onShowPress(MotionEvent e) { }
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent e) { }
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			int position = mTextView.getOffsetForPosition(e.getX(), e.getY());
			
			Spanned text = (Spanned) mTextView.getText();
			ElementSpan[] spans = text.getSpans(0, position, ElementSpan.class);
			
			for (ElementSpan span: spans) {
				int start = text.getSpanStart(span);
				int end = text.getSpanEnd(span);
				
//				Log.d(TAG, "start:"+start+" end:"+end+" position:"+position);
				
				if (position>=start && position<=end) {
					mTouchedSpan = span;
					return true;
				}
			}
			
			return false;
		}
	};
	
}
