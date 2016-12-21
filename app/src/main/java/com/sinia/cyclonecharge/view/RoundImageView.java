package com.sinia.cyclonecharge.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
 
/**
 * 圆角ImageView
 * 
 * @author skg
 * 
 */
public class RoundImageView extends ImageView {
	
	@SuppressWarnings("unused")
	private Context context;
 
        public RoundImageView(Context context, AttributeSet attrs) {
                super(context, attrs);
                this.context=context;
                init();
        }
        final float scale = getResources().getDisplayMetrics().density;
 
        private final RectF roundRect = new RectF();
//        private float rect_adius = 5 * scale / 2f;
        private float rect_adius =5 * scale / 2f;
        private final Paint maskPaint = new Paint();
        private final Paint zonePaint = new Paint();

		private int center;

		@SuppressWarnings("unused")
	private int innerCircle;

		@SuppressWarnings("unused")
		private int ringWidth;
 
        private void init() {
                maskPaint.setAntiAlias(true);
                maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                //
                zonePaint.setAntiAlias(true);
                zonePaint.setColor(Color.WHITE);
                //
                float density = getResources().getDisplayMetrics().density;
                rect_adius = rect_adius * density;
        }
 
        public void setRectAdius(float adius) {
                rect_adius = adius;
                invalidate();
        }
 
        @Override
        protected void onLayout(boolean changed, int left, int top, int right,
                        int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                int w = getWidth();
                int h = getHeight();
                roundRect.set(0, 0, w, h);
                center = w/2;  
        }
 
        //圆角边的头像 old
//        @Override
//        public void draw(Canvas canvas) {
//                canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
//                canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
//                canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
//                super.draw(canvas);
//                canvas.restore();
//        }
        
        //圆形头像 new
        @Override
        public void draw(Canvas canvas) {
                canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
//                canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
                //Canvas.drawCircle(float cx 距离x的距�?, float cy距离y的距�?, float radius 直径, Paint paint)
                canvas.drawCircle(center, center, center, maskPaint);// center根据像素以图片的中心点为圆心
                canvas.drawCircle(center, center, center, zonePaint);//
                canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
                super.draw(canvas);
                canvas.restore();
        }
        
        /** 
         * 根据手机的分辨率�? dp 的单�? 转成�? px(像素) 
         */  
        public static int dip2px(Context context, float dpValue) {  
            final float scale = context.getResources().getDisplayMetrics().density;  
            return (int) (dpValue * scale + 0.5f);  
        }  
 
}