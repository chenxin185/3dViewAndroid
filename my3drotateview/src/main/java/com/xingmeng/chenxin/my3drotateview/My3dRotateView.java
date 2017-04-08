package com.xingmeng.chenxin.my3drotateview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chenxin on 2017/3/30.
 * O(∩_∩)O~
 * 自己摸索写了一个很简易的动画效果，原理非常简单：
 * 就是让所有的子view围绕着一个椭圆的轨迹旋转。首先有一个子view的数组，然后有一个存放它们当前的角度的数组，动画开始时，
 * 通过判断是顺时针还是逆时针来动态改变角度数组里的值，然后在invalidate()。
 */

public class My3dRotateView extends ViewGroup implements View.OnTouchListener, GestureDetector.OnGestureListener {

    public static final String TAG = "My3DView";

    private int mWidth;
    private int mHeight;
    private int mChildWidth;
    private int mChildHeight;
    private int mChildCount;


    double currentX;
    double currentY;

    /**
     * 动画实际上就是围绕着椭圆的轨迹来旋转的。
     */
    private int mOvalWidthMargin;//椭圆的宽距离父容器两边的宽度
    private int mOvalHeightMargin;//椭圆的高距离父容器两边的长度
    private int mOvalWidth;//椭圆的宽度
    private int mOvalHeight;//椭圆的高度
    private View[] mChildrenView;//所有的子view
    private double[] mAngles;//所有view角度的集合

    private boolean flag = true;//是否初始化过子view
    private boolean animate = false;//是否正在动画
    private boolean clockwise = true;//是否顺时针旋转

    private float animateTime = 5000;//默认的动画持续时间
    private float currentTime = 0;//当前动画执行的时间
    private GestureDetector mGesture;


    private My3dInterpolate interpolate;//自定义的插值器
    private OnItemClickListener itemClickListener;//自定义的子view的点击监听器

    public void setInterpolate(My3dInterpolate interpolate) {
        this.interpolate = interpolate;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };


    public My3dRotateView(Context context) {
        this(context, null);
    }

    public My3dRotateView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public My3dRotateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attributeSet, int defStyleAttr) {
        setWillNotDraw(false);
        setPersistentDrawingCache(PERSISTENT_NO_CACHE);
        mGesture = new GestureDetector(context, this);
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.My3dRotateView, defStyleAttr, 0);
        mChildHeight = (int) a.getDimension(R.styleable.My3dRotateView_childHeight, -1);
        mChildWidth = (int) a.getDimension(R.styleable.My3dRotateView_childWidth, -1);
        mOvalWidth = (int) a.getDimension(R.styleable.My3dRotateView_ovalWidth, -1);
        mOvalHeight = (int) a.getDimension(R.styleable.My3dRotateView_ovalHeight, -1);
        mOvalHeightMargin = (int) a.getDimension(R.styleable.My3dRotateView_margin_ovalHeight, -1);
        mOvalWidthMargin = (int) a.getDimension(R.styleable.My3dRotateView_margin_ovalWidth, -1);
        a.recycle();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //Log.e(TAG, "onLayout l = " + l + " t = " + t + " r = " + r + " b = " + b);
        if (flag) {
            //进行一些初始化的操作
/*            if ((mOvalHeight == -1) || (mOvalWidth == -1)) {
                if (mWidth > mHeight) {
                    mOvalHeight = (mHeight - 100) / 2;
                    mOvalWidth = mOvalHeight + 100;
                } else {
                    mOvalWidth = (mWidth - 100) / 2;
                    mOvalHeight = mOvalWidth + 100;
                }
            }else {
                mOvalWidth /= 2;
                mOvalHeight /= 2;
            }*/

            if (mOvalHeightMargin != -1) {
                mOvalHeight = (mHeight - mOvalHeightMargin * 2) / 2;
            } else if (mOvalHeight == -1) {
                mOvalHeight = mHeight / 2;
            }
            if (mOvalWidthMargin != -1) {
                mOvalWidth = (mWidth - mOvalWidthMargin * 2) / 2;
            } else if (mOvalWidth == -1) {
                mOvalWidth = mWidth / 2;
            }


            if ((mChildWidth == -1) && (mChildHeight == -1)) {
                mChildWidth = 50;
                mChildHeight = 50;
            }

            //下面是获取所有的子view，给子view分配初始位置，默认第一个是270度，相当于y轴负半轴。
            mChildCount = getChildCount();
            mChildrenView = new View[mChildCount];
            mAngles = new double[mChildCount];
            mAngles[0] = 270;
            double j = 360 / mChildCount;
            for (int i = 0; i < mChildCount; i++) {
                mChildrenView[i] = getChildAt(i);
                final int finalI = i;
                mChildrenView[i].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickListener == null)
                            Log.e(TAG, "你点击了的position = " + finalI + ",但是itemClickListener == null");
                        else {
                            itemClickListener.onItemClick(v, finalI, true);
                        }
                    }
                });
                if (i > 0) {
                    if ((mAngles[i] + j) <= 360) {
                        mAngles[i] = mAngles[i - 1] + j;
                    } else {
                        mAngles[i] = mAngles[i - 1] + j - 360;
                    }
                }
            }
            flag = false;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public String toString() {
        return "My3dView{" +
                "mWidth=" + mWidth +
                ", mHeight=" + mHeight +
                ", mChildWidth=" + mChildWidth +
                ", mChildHeight=" + mChildHeight +
                ", mChildCount=" + mChildCount +
                ", mOvalWidth=" + mOvalWidth +
                ", mOvalHeight=" + mOvalHeight +
                '}';
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mChildCount; i++) {
            int x = (int) (mOvalWidth * Math.cos(mAngles[i] * Math.PI / 180));
            int y = (int) (mOvalHeight * Math.sin(mAngles[i] * Math.PI / 180));
            mChildrenView[i].layout(mWidth / 2 - x - mChildWidth / 2, mHeight / 2 - y - mChildHeight / 2, mWidth / 2 - x + mChildWidth / 2, mHeight / 2 - y + mChildHeight / 2);
        }

    }

    /**
     * 开始动画
     *
     * @return 是否正在执行动画
     */
    public boolean startAnimator() {
        if (animate)
            return true;
        animate = true;
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (currentTime < animateTime && animate) {
                    if (interpolate != null)
                        calculateAngle(interpolate.getInterpolation(currentTime / animateTime));
                    else
                        calculateAngle(getInterpolate(currentTime / animateTime));
                    handler.sendEmptyMessage(0);
                    try {
                        currentTime += 10;
                        sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                currentTime = 0;
                animate = false;
            }
        }.start();
        return false;
    }

    /**
     * 默认的插值器方法
     *
     * @param timing 当前动画时间占总动画时间的百分比
     * @return 返回每一帧应当改变的角度
     */
    private double getInterpolate(float timing) {
        if (timing < 0.4) {
            return 3;
        } else if (timing < 0.5) {
            return 2.4;
        } else if (timing < 0.6) {
            return 1.9;
        } else if (timing < 0.7) {
            return 1.5;
        } else if (timing < 0.8) {
            return 1.2;
        } else if (timing < 0.85) {
            return 1;
        } else {
            return 0.8;
        }
    }


    /**
     * 自定义的插值器
     */
    public interface My3dInterpolate {
        /**
         * 自定义的插值器方法,返回下一帧要增加的角度，因为旋转菜单是根据角度来计算位移的。
         *
         * @param timing 当前动画的时间占总动画时间的百分比
         * @return 角度
         */
        double getInterpolation(float timing);
    }

    public interface OnItemClickListener {
        /**
         * @param view     点击的view
         * @param position 点击的position
         * @param isFirst  是否是最前面的那个view(暂时没有写 <(￣︶￣)> )
         */
        void onItemClick(View view, int position, boolean isFirst);
    }


    /**
     * 通过插值器返回的值来改变当前每个子View的角度
     *
     * @param j 改变的角度
     */
    public void calculateAngle(double j) {
        if (clockwise) {
            for (int i = 0; i < mChildCount; i++) {
                if ((mAngles[i] + j) <= 360) {
                    mAngles[i] += j;
                } else {
                    mAngles[i] += j - 360;
                }
            }
        } else {
            for (int i = 0; i < mChildCount; i++) {
                if ((mAngles[i] + j) >= 0) {
                    mAngles[i] -= j;
                } else {
                    mAngles[i] -= j - 360;
                }
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        onTouch(this, ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGesture.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //  Log.e(TAG, "onDown");
        currentX = e.getX();
        currentY = e.getY();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // Log.e(TAG, "showpress()");
        if (animate) {
            animate = false;
            currentTime = 0;
        }

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // Log.e(TAG, "onSingleTapUp");
        return true;
    }

    /**
     * 通过滑动来判断应该是顺时针还是逆时针
     *
     * @param e1        是手指触碰到屏幕时的event
     * @param e2        是手指当前所在的event
     * @param distanceX x距离
     * @param distanceY y距离
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //  Log.e(TAG, "onScroll");
        double x = e2.getX();
        double y = e2.getY();
        if (y > mHeight / 2) {
            if (x > currentX) {
                clockwise = false;
                calculateAngle((x - currentX) / mOvalWidth * 180);
                //Log.e("infoo", "角度为  = " + (x - currentX) / mOvalWidth * 180);
            } else if (x < currentX) {
                clockwise = true;
                calculateAngle((currentX - x) / mOvalWidth * 180);
                // Log.e("infoo", "角度为  = " + (currentX - x) / mOvalWidth * 180);
            }
            invalidate();
        }

        currentX = x;
        currentY = y;
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //  Log.e(TAG, "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //  Log.e(TAG, "onFling");

        if (startAnimator()) {
            //当手指快速滑动时，如果正在进行动画，就将当前的滑动时间置为0。
            currentTime = 0;
        }
        return false;
    }

}
