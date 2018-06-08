package com.example.lili.animationdemo.DragView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * @author li.li
 * @Description:可拖拽的Gridview
 * @date 18-6-7
 * @copyright MIBC
 */
public class DraggableGridView extends GridView {
    private Context mContext;

    private Vibrator mVibrator;
    /*手指按下屏幕的坐标*/
    private int mDownX, mDownY;

    private int mMoveX, mMoveY;

    /*拖拽的view在GridView中的位置*/
    private int mDragPosition;

    /*刚开始拖拽的item对应的View*/
    private View mStartDragItemView = null;

    /*当GridView多页时，滚页的临界点*/
    private int mLeftScrollBoard, mRightScrollBoard;

    /*拖动的item镜像bitmap*/
    private Bitmap mDragBitmap;

    /*被拖动的imageView*/
    private ImageView mDragImageView;

    /*触摸点距离item项的边缘*/
    private int mOffsetItemTop, mOffsetItemLeft;

    /*GridView距离屏幕原点的偏移量*/
    private int mOffsetOriginTop, mOffsetOriginLeft;

    private WindowManager mWindowManager;
    /*item镜像的布局参数*/
    private WindowManager.LayoutParams mWindowLayoutParams;

    private int mStatusHeight;
    /*是否可以拖拽*/
    private boolean isDrag = false;

    private Handler mHandler;

    /*item长按响应的时间*/
    private long LONG_CLICK_TO_DRAG = 1000;
    /*翻页响应的时间*/
    private long PAGING_TURN_TIME = 25;

    private Runnable mLongClickRunnable;
    private Runnable mScrollRunnable;

    private ItemSwapListener mItemSwapListener;
    private PageTurningListener mPageTurningListener;


    public DraggableGridView(Context context) {
        super(context);
    }

    public DraggableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DraggableGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        mStatusHeight = SystemUtil.getStatusHeight(mContext);

        mHandler = new Handler();
        mLongClickRunnable = new Runnable() {
            @Override
            public void run() {
                isDrag = true;           //设置gridView item项可以拖拽
                mVibrator.vibrate(100); // 震动100毫秒
                if (mStartDragItemView != null) {
                    mStartDragItemView.setVisibility(View.INVISIBLE);//隐藏该item
                }
                createDragImage(mDragBitmap, mDownX, mDownY);
            }
        };

        mScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (mMoveX < mLeftScrollBoard) {
                    if (mPageTurningListener != null) {
                        mPageTurningListener.onPageTurningLeft();
                    }
                    swapGridItem(mMoveX, mMoveY);
                } else if (mMoveX > mRightScrollBoard) {
                    if (mPageTurningListener != null) {
                        mPageTurningListener.onPageTurningRight();
                    }
                    swapGridItem(mMoveX, mMoveY);
                } else {
                    mHandler.removeCallbacks(mScrollRunnable);
                }
            }
        };
    }

    /**
     * 创建拖动的镜像
     */
    private void createDragImage(Bitmap bitmap, int downX, int downY) {
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.flags = PixelFormat.TRANSLUCENT;  //图片之外的其他地方透明
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.alpha = 0.55f; //透明度

        //触摸点相对窗口原点的距离
        mWindowLayoutParams.x = downX - mOffsetItemLeft + mOffsetOriginLeft;
        mWindowLayoutParams.y = downY - mOffsetItemTop + mOffsetOriginTop - mStatusHeight;

        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);
        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();

                mDragPosition = pointToPosition(mDownX, mDownY);  //所点击的item的position

                if (mDragPosition == AdapterView.INVALID_POSITION) {
                    return super.dispatchTouchEvent(ev);
                }

                mHandler.postDelayed(mLongClickRunnable, LONG_CLICK_TO_DRAG);

                mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());

                mOffsetItemLeft = mDownX - mStartDragItemView.getLeft();
                mOffsetItemTop = mDownY - mStartDragItemView.getTop();

                mOffsetOriginLeft = (int) (ev.getRawX() - mDownX);
                mOffsetOriginTop = (int) (ev.getRawY() - mDownY);

                //获取gridView自动滚动的偏移量，大于item宽度的一半，DragGridView向左/右滚动
                mLeftScrollBoard = mStartDragItemView.getWidth() / 2;
                mRightScrollBoard = getWidth() - mStartDragItemView.getWidth() / 2;

                /*将view的绘制进行混存成bitmap*/
                mStartDragItemView.setDrawingCacheEnabled(true);
                mStartDragItemView.buildDrawingCache();
                mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
                /*释放绘图缓存，避免出现重复镜像*/
                mStartDragItemView.destroyDrawingCache();
                mStartDragItemView.setDrawingCacheEnabled(false);
                break;

            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                //当移动的触摸点已经不在原先的item范围内时，移除长按的runnable
                if (!isInTouchItem(mStartDragItemView, moveX, moveY)) {
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                break;

            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacks(mLongClickRunnable);
                mHandler.removeCallbacks(mScrollRunnable);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isDrag && mDragImageView != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mMoveX = (int) ev.getX();
                    mMoveY = (int) ev.getY();

                    dragItem(mMoveX, mMoveY);
                    break;
                case MotionEvent.ACTION_UP:
                    stopDragItem();
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 触摸点是否在item的范围内
     */
    private boolean isInTouchItem(View touchItem, int moveX, int moveY) {
        if (touchItem == null) {
            return false;
        }
        if (moveX < touchItem.getLeft() || moveX > touchItem.getRight()) {
            return false;
        }
        if (moveY < touchItem.getTop() || moveY > touchItem.getBottom()) {
            return false;
        }
        return true;
    }

    /**
     * 拖动镜像imageView
     */
    private void dragItem(int moveX, int moveY) {
        //触摸点相对窗口原点的距离
        mWindowLayoutParams.x = moveX - mOffsetItemLeft + mOffsetOriginLeft;
        mWindowLayoutParams.y = moveY - mOffsetItemTop + mOffsetOriginTop - mStatusHeight;

        //更新镜像的位置
        mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams);

        swapGridItem(moveX, moveY);

        mHandler.post(mScrollRunnable);
    }

    /**
     * 移除拖动的镜像
     */
    private void removeDragImage() {
        if (mDragImageView != null) {
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
    }

    /**
     * item镜像的位置更新
     */
    private void swapGridItem(int moveX, int moveY) {
        int tempPosition = pointToPosition(moveX, moveY);  //移动镜像imageView到新的位置的的item的position

        //如果item的position改变了，且position!=-1
        if (mDragPosition != tempPosition && tempPosition != AdapterView.INVALID_POSITION) {

            if (mItemSwapListener != null) {
                //交换gridView数据
                mItemSwapListener.onSwapItem(mDragPosition, tempPosition);
            }

            getChildAt(tempPosition - getFirstVisiblePosition()).setVisibility(INVISIBLE);
            View dragView = getChildAt(mDragPosition - getFirstVisiblePosition());
            if (dragView != null) {
                dragView.setVisibility(VISIBLE);
            }

            mDragPosition = tempPosition;
        }
    }

    /**
     * 停止拖动镜像imageView
     */
    private void stopDragItem() {
        isDrag = false;
        View view = getChildAt(mDragPosition - getFirstVisiblePosition());
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        removeDragImage();
    }

    public void setItemSwapListener(ItemSwapListener itemSwapListener) {
        this.mItemSwapListener = itemSwapListener;
    }

    public void setPageTurningListener(PageTurningListener pageTurningListener) {
        this.mPageTurningListener = pageTurningListener;
    }

    interface ItemSwapListener {
        /**
         * 当item交换位置的时候回调的方法，在该方法中实现数据的交换
         *
         * @param fromItem 开始拖拽item的position
         * @param toItem   拖拽到新的item的position
         */
        void onSwapItem(int fromItem, int toItem);
    }

    /**
     * 当拖动item到临界值时回调翻页的接口
     */
    interface PageTurningListener {
        void onPageTurningLeft();

        void onPageTurningRight();
    }
}
