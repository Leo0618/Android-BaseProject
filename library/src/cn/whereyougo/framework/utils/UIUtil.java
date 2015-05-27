package cn.whereyougo.framework.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import cn.whereyougo.framework.WYGApplication;

public class UIUtil {

    /** 获取全局上下文 */
    public static Context getContext() {
        return WYGApplication.getApplication();
    }

    /** 获取主线程 */
    public static Thread getMainThread() {
        return WYGApplication.getMainThread();
    }

    /** 获取主线程ID */
    public static long getMainThreadId() {
        return WYGApplication.getMainThreadId();
    }

    /** 获取主线程的handler */
    public static Handler getHandler() {
        return WYGApplication.getMainThreadHandler();
    }

    /** 在主线程中延时一定时间执行runnable */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    /** 在主线程执行runnable */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /** 从主线程looper里面移除runnable */
    public static void removeCallbacksFromMainLooper(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    /** 获取资源 */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /** 获取文字 */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /** 获取文字数组 */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /** 获取dimen */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /** 获取drawable */
    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /** 获取颜色 */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /** 获取颜色选择器 */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    /** 判断当前的线程是否为主线程 */
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    /** 在主线程中运行任务 */
    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            UIUtil.post(runnable);
        }
    }

    /** dip转换为px */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /** px转换为dip */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /** ----------------------toast封装------start------------------------- */

    /** 弹出长时间Toast */
    public static void showToastLong(String msg) {
        buildToast(msg, Toast.LENGTH_LONG).show();
    }

    /** 弹出长时间Toast */
    public static void showToastLong(final String msg, final String bgColor) {
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                buildToast(msg, Toast.LENGTH_LONG, bgColor).show();
            }
        });
    }

    /** 弹出长时间Toast */
    public static void showToastLong(final String msg, final String bgColor, final int textSp) {
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                buildToast(msg, Toast.LENGTH_LONG, bgColor, textSp).show();
            }
        });
    }

    /** 弹出短时间Toast */
    public static void showToastShort(final String msg) {
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                buildToast(msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** 弹出短时间Toast */
    public static void showToastShort(final String msg, final String bgColor) {
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                buildToast(msg, Toast.LENGTH_SHORT, bgColor).show();
            }
        });
    }

    /** 弹出短时间Toast */
    public static void showToastShort(final String msg, final String bgColor, final int textSp) {
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                buildToast(msg, Toast.LENGTH_SHORT, bgColor, textSp).show();
            }
        });
    }

    private static Toast buildToast(String msg, int duration) {
        return buildToast(msg, duration, "#000000", 16);
    }

    private static Toast buildToast(String msg, int duration, String bgColor) {
        return buildToast(msg, duration, bgColor, 16);
    }

    private static Toast buildToast(String msg, int duration, String bgColor, int textSp) {
        return buildToast(msg, duration, bgColor, textSp, 10);
    }

    /**
     * 构造Toast
     * @param context
     *            上下文
     * @param msg
     *            消息
     * @param duration
     *            显示时间
     * @param bgColor
     *            背景颜色
     * @param textSp
     *            文字大小
     * @param cornerRadius
     *            四边圆角弧度
     * @return
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("ShowToast")
    private static Toast buildToast(String msg, int duration, String bgColor, int textSp, int cornerRadius) {
        Toast mToast = new Toast(getContext());
        mToast.setDuration(duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        // 设置Toast文字
        TextView tv = new TextView(getContext());
        int dpPadding = dip2px(10);
        tv.setPadding(dpPadding, dpPadding, dpPadding, dpPadding);
        tv.setGravity(Gravity.CENTER);
        tv.setText(msg);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSp);
        // Toast文字TextView容器
        LinearLayout mLayout = new LinearLayout(getContext());
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(Color.parseColor(bgColor));
        shape.setCornerRadius(cornerRadius);
        shape.setStroke(1, Color.parseColor(bgColor));
        shape.setAlpha(180);
        mLayout.setBackgroundDrawable(shape);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mLayout.setLayoutParams(params);
        mLayout.setGravity(Gravity.CENTER);
        mLayout.addView(tv);
        // 将自定义View覆盖Toast的View
        mToast.setView(mLayout);
        return mToast;
    }

    /** ----------------------toast封装------end------------------------- */

}
