package com.sinia.cyclonecharge.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class EditViewPager extends ViewPager {
	private PointF mDownP = new PointF();

	private PointF mCurrentP = new PointF();

	OnItemTouchListener mOnItemTouchListener;

	public EditViewPager(Context context) {
		super(context);
	}

	public EditViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		mCurrentP.x = arg0.getX();
		mCurrentP.y = arg0.getY();
		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			mDownP.x = arg0.getX();
			mDownP.y = arg0.getY();
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
			float aMoveX = mDownP.x - mCurrentP.x;
			float aMoveY = mDownP.y - mCurrentP.y;
			if (Math.abs(aMoveX) > Math.abs(aMoveY * 2)) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}else {
				getParent().requestDisallowInterceptTouchEvent(false);
			}
		}
		if (arg0.getAction() == MotionEvent.ACTION_UP) {
			if (Math.abs(mDownP.x - mCurrentP.x) < 5 && Math.abs(mDownP.y - mCurrentP.y) < 5) {
				onItemTouch();
				return true;
			}
		}
		return super.onTouchEvent(arg0);
	}

	public void onItemTouch() {
		if (mOnItemTouchListener != null) {
			mOnItemTouchListener.onItemTouch(getCurrentItem());
		}
	}

	public interface OnItemTouchListener {
		public void onItemTouch(int position);
	}

	/**
	 * 
	 * @param
	 */
	public void setOnItemTouchListener(OnItemTouchListener onItemTouchListener) {
		this.mOnItemTouchListener = onItemTouchListener;
	}

}

