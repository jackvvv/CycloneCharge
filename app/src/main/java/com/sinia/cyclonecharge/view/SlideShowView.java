package com.sinia.cyclonecharge.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;


import com.bumptech.glide.Glide;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果； 既支持自动轮播页面也支持手势滑动切换页面
 * 
 * 
 */
@SuppressLint("HandlerLeak")
public class SlideShowView extends FrameLayout {
	// 自定义轮播图的资源
	private List<String> imageUrls = new ArrayList<String>();
	// 放轮播图片的ImageView 的list
	private List<ImageView> imageViewsList;
	// 放圆点的View的list
	private List<View> dotViewsList;

	private EditViewPager viewPager;
	// 当前轮播页
	private int currentItem = 0;

	private Context context;

	private ViewPagerItemCLickListener itemClickListener;

	private Timer time;
	private LinearLayout dotLayout;

	public SlideShowView(Context context) {
		this(context, null);
	}

	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this,
				true);
		dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		viewPager = (EditViewPager) findViewById(R.id.viewPager);
		initData();
		startPlay();
	}

	/**
	 * 开始轮播图切换
	 */
	public void startPlay() {
		autogallery();
	}

	public void stopPlay(){
		time.cancel();
	}
	
	/**
	 * 初始化相关Data
	 */
	private void initData() {
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();
	}

	/**
	 * 初始化Views等UI
	 */
	private void initUI() {
		if (imageUrls.size() == 0) {
			imageUrls.add("");
		}
		currentItem = 0;
		dotLayout.removeAllViews();
		imageViewsList.clear();
		dotViewsList.clear();
		for (int i = 0; i < imageUrls.size(); i++) {
			ImageView dotView = new ImageView(context);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					Utils.dip2px(context, 6), Utils.dip2px(context, 6));
			lp.setMargins(Utils.dip2px(context, 3), Utils.dip2px(context, 0),
					Utils.dip2px(context, 3), Utils.dip2px(context, 0));
			dotView.setLayoutParams(lp);
			dotLayout.addView(dotView);
			dotViewsList.add(dotView);
		}
		viewPager.setFocusable(true);
		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		currentItem = imageUrls.size() * 10;
		viewPager.setCurrentItem(currentItem);

	}

	/**
	 * 填充ViewPager的页面适配器
	 * 
	 */
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View container, int position, Object object) {
			if (position >= imageViewsList.size()) {
				return;
			}
			((ViewPager) container).removeView(imageViewsList.get(position));
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			final int pos = position % imageUrls.size();
			ImageView view = new ImageView(context);
//			view.setBackgroundResource(R.drawable.lunbo);
			view.setScaleType(ScaleType.CENTER_INSIDE);
			view.setAdjustViewBounds(true);
//				BitmapUtilsHelp.getImage(context, R.drawable.img_loading)
//						.display(view, imageUrls.get(pos));
			Glide.with(context).load(imageUrls.get(pos)).placeholder(R.mipmap.default_img).crossFade().centerCrop().into(view);
			imageViewsList.add(view);
			((ViewPager) container).addView(view);
			return view;
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	private void autogallery() {
		if (time != null) {
			time.cancel();
			time = null;
		}
		time = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (imageUrls.size() > 1) {
					Message m = new Message();
					handler.sendMessage(m);
				}
			}
		};
		time.schedule(task, 5000, 8000);
	}

	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
			viewPager.setCurrentItem(currentItem + 1);
		}
	};

	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:// 手势滑动，空闲中
				if (time != null) {
					time.cancel();
					time = null;
				}
				break;
			case 2:// 界面切换中
				if (time == null) {
					autogallery();
				}
				break;
			case 0:
				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			int pos = position % imageUrls.size();
			currentItem = position;
			for (int i = 0; i < dotViewsList.size(); i++) {
				if (i == pos) {
					((View) dotViewsList.get(pos))
							.setBackgroundResource(R.mipmap.carousel_point_select);
				} else {
					((View) dotViewsList.get(i))
							.setBackgroundResource(R.mipmap.carousel_point);
				}
			}
		}
	}

	/**
	 * 必须在setImagePath(List<String>)调用之后调用
	 * 
	 * @param viewPagerItemCLickListener
	 */
	public void setOnItemClickListener(
			ViewPagerItemCLickListener viewPagerItemCLickListener) {
		itemClickListener = viewPagerItemCLickListener;
		viewPager.setOnItemTouchListener(new EditViewPager.OnItemTouchListener() {

			@Override
			public void onItemTouch(int position) {
				if (itemClickListener != null) {
					itemClickListener.onItemClick(null,
							position % imageUrls.size());
				}
			}
		});
	}

	public void setImagePath(List<String> images) {
		imageUrls.clear();
		for (int i = 0; i < images.size(); i++) {
			imageUrls.add(images.get(i));
		}
		initUI();
	}

	public interface ViewPagerItemCLickListener {
		public abstract void onItemClick(View pager, int position);
	}
}