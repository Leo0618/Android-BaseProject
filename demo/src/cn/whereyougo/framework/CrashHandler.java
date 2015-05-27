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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Process;
import android.widget.Toast;
import cn.whereyougo.framework.common.WYGConfig;
import cn.whereyougo.framework.utils.PackageManagerUtil;
import cn.whereyougo.framework.utils.LogUtil;

/**
 * 截获（记录）崩溃.<br/>
 * 当程序产生未捕获异常则有此类接管并将异常记录在SD根目录或应用缓存目录的.logex文件夹下面.<br/>
 * 2015年5月22日-下午5:56:43
 * 
 * @author lizhijun
 */
public class CrashHandler implements UncaughtExceptionHandler {
    /** 记录标志. */
    private static final String TAG = "CrashHandler";
    /** 错误日志文件夹名称 . */
    private static final String DEBUE_DIRNAME = ".logex";
    /** CrashHandler实例. */
    private volatile static CrashHandler instanec;

    /** 初始化. */
    public static void init(Context context) {
        if (null == instanec) {
            synchronized (CrashHandler.class) {
                if (null == instanec) {
                    instanec = new CrashHandler(context);
                }
            }
        }
    }

    /** 程序的Context对象. */
    private Context mContext;
    /** 用于格式化日期,作为日志文件名的一部分. */
    private DateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault());

    /** 系统默认的UncaughtException处理类. */
    @SuppressWarnings("unused")
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /** 进程名字. */
    private String mProcessName;

    /** 保证只有一个CrashHandler实例. */
    private CrashHandler(Context context) {
        mContext = context.getApplicationContext();
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);

        mProcessName = PackageManagerUtil.getProcessNameByPid(mContext, Process.myPid());
        LogUtil.d(TAG, TAG + "-->CrashHandler is init! ProcessName:", mProcessName);
    }

    /** 当UncaughtException发生时会转入该函数来处理. */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        android.util.Log.d(TAG, "---------------uncaughtException start---------------\r\n");
        android.util.Log.d(TAG, "process [" + mProcessName + "],is abnormal!\r\n");
        try {
            handleException(thread, throwable);
        } catch (Exception ex) {
            android.util.Log.d(TAG, "uncaughtException,ex:", ex);
        }
        if (WYGConfig.DEBUG) {
            android.util.Log.e(TAG, "uncaughtException,throwable:", throwable);
        }
        if (PackageManagerUtil.isMainProcess(mContext, mProcessName)) {
            // 提示
            Toast.makeText(mContext, "~~ bug is coming ~~", Toast.LENGTH_SHORT).show();

            ComponentName componentName = PackageManagerUtil.getTheProcessBaseActivity(mContext);
            if (componentName != null) {
                Intent intent = new Intent();
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(componentName);

                // mContext.startActivity(intent);
                AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, restartIntent); // 100毫秒钟后重启应用
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LogUtil.e(TAG, "error : ", e);
            }
        }
        android.util.Log.d(TAG, "---------------uncaughtException end---------------\r\n");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     * 
     * @param rhrowable
     * @return true:如果处理了该异常信息;否则返回false.
     * @throws IOException
     */
    private boolean handleException(Thread thread, Throwable rhrowable) throws IOException {
        String logdirPath = null;
        // 记录文件文件夹
        if (Environment.getExternalStorageDirectory() != null) {
            logdirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + DEBUE_DIRNAME + File.separator;
        } else {
            logdirPath = mContext.getCacheDir().getAbsolutePath() + File.separator + DEBUE_DIRNAME + File.separator;
        }
        File logdir = new File(logdirPath);
        if (logdir.exists()) {
            if (!logdir.isDirectory()) {
                boolean b = logdir.delete();
                android.util.Log.d(TAG, "handleException,delete:" + b);
                b = logdir.mkdirs();
                android.util.Log.d(TAG, "handleException,mkdirs:" + b);
            } else {
                if (!WYGConfig.DEBUG) {
                    try {
                        clearLogexMax(logdir, 10);
                    } catch (Exception ex) {
                        android.util.Log.e(TAG, "handleException,ex:" + ex);
                    }
                }
            }
        } else {
            logdir.mkdirs();
        }
        // 本次记录文件名
        Date date = new Date(); // 当前时间
        String logFileName = formatter.format(date) + String.format("[%s-%d]", thread.getName(), thread.getId()) + ".txt";
        File logex = new File(logdir, logFileName);
        // 写入异常到文件中
        FileWriter fw = new FileWriter(logex, true);
        fw.write("\r\nProcess[" + mProcessName + "," + Process.myPid() + "]"); // 进程信息，线程信息
        fw.write("\r\n" + thread + "(" + thread.getId() + ")"); // 进程信息，线程信息
        fw.write("\r\nTime stamp：" + date); // 日期
        // 打印调用栈
        PrintWriter printWriter = new PrintWriter(fw);
        rhrowable.printStackTrace(printWriter);
        Throwable cause = rhrowable.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        fw.write("\r\n");
        fw.flush();
        printWriter.close();
        fw.close();
        return true;
    }

    /**
     * 清理日志,限制日志数量.
     * 
     * @param logdir
     * @param max
     *            自多保存的日志数量
     */
    private void clearLogexMax(File logdir, int max) {
        File[] logList = logdir.listFiles();
        final int length = logList.length;
        android.util.Log.d(TAG, "clearLogexMax,length:" + length + ",max:" + max);
        if (length > max) {
            // 按照时间排序
            Comparator<? super File> comparator = new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    // long result = lhs.lastModified() - rhs.lastModified();
                    // return (int) (result);
                    return lhs.getName().compareToIgnoreCase(rhs.getName());
                }
            };
            Arrays.sort(logList, comparator);
            // 保留
            for (int i = max; i < length; i++) {
                try {
                    File logF = logList[i];
                    boolean b = logF.delete();
                    android.util.Log.d(TAG, "clearLogexMax,log:" + logF.getName() + ",b:" + b);
                } catch (Exception ex) {
                    android.util.Log.e(TAG, "clearLogexMax,ex:" + ex);
                }
            }
        }
    }

}
