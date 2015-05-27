/*
 * Copyright (c) 2015. lizhijun (leo0618@126.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package cn.whereyougo.framework;

import java.lang.ref.WeakReference;
import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

/**
 * 应用程序入口<br/>
 * 避免应用启动很久,不要过久在此处理数据,尽量使用异步进行<br/>
 * 2015年5月24日 - 下午6:28:32
 * 
 * @author lizhijun
 */
public class WYGApplication extends Application {
    private static WYGApplication mContext;
    private static Handler mMainThreadHandler;
    private static Looper mMainThreadLooper;
    private static Thread mMainThread;
    private static int mMainThreadId;
    /*** 寄存整个应用Activity **/
    private final Stack<WeakReference<Activity>> activitys = new Stack<WeakReference<Activity>>();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mMainThreadHandler = new Handler();
        mMainThreadLooper = getMainLooper();
        mMainThread = Thread.currentThread();
        mMainThreadId = Process.myTid();
        CrashHandler.init(mContext);// 异常捕获初始化
        new LoadInitDataTask().execute((Void) null);// 异步加载初始数据
    }
    
    /**
     * 获取全局上下文
     * 
     * @return the mContext
     */
    public static WYGApplication getApplication() {
        return mContext;
    }

    /**
     * 获取主线程Handler
     * 
     * @return the mMainThreadHandler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取主线程轮询器
     * 
     * @return the mMainThreadLooper
     */
    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    /**
     * 获取主线程
     * 
     * @return the mMainThread
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程ID
     * 
     * @return the mMainThreadId
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 使用异步加载初始配置数据
     */
    private class LoadInitDataTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO 在这里加载一些初始化数据.比如初始化UIL、各类SDK等等
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
        }
    }

    /**
     * 获取全局上下文
     * 
     * @return the mContext
     */
    public static WYGApplication getAppContext() {
        return mContext;
    }

    /************ Application中存放的Activity操作（压栈/出栈）API（开始） ***********************/
    /**
     * 将Activity压入Application栈
     * 
     * @param task
     *            将要压入栈的Activity对象
     */
    public void pushTask(WeakReference<Activity> task) {
        activitys.push(task);
    }

    /**
     * 将传入的Activity对象从栈中移除
     * 
     * @param task
     */
    public void removeTask(WeakReference<Activity> task) {
        activitys.remove(task);
    }

    /**
     * 根据指定位置从栈中移除Activity
     * 
     * @param taskIndex
     *            Activity栈索引
     */
    public void removeTask(int taskIndex) {
        if (activitys.size() > taskIndex)
            activitys.remove(taskIndex);
    }

    /**
     * 将栈中Activity移除至栈顶
     */
    public void removeToTop() {
        int end = activitys.size();
        int start = 1;
        for (int i = end - 1; i >= start; i--) {
            if (!activitys.get(i).get().isFinishing()) {
                activitys.get(i).get().finish();
            }
        }
    }

    /**
     * 移除全部（用于整个应用退出）
     */
    public void removeAll() {
        for (WeakReference<Activity> task : activitys) {
            if (!task.get().isFinishing()) {
                task.get().finish();
            }
        }
    }
    /*************** Application中存放的Activity操作（压栈/出栈）API（结束） ************************/

}
