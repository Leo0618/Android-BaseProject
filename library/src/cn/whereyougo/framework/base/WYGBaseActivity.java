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
package cn.whereyougo.framework.base;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import cn.whereyougo.framework.WYGApplication;
import cn.whereyougo.framework.base.impl.IBaseActivity;

/**
 * android 系统中的四大组件之一Activity基类<br/>
 * <br/>
 * 2015年5月22日-下午7:07:36
 * 
 * @author lizhijun
 */
public abstract class WYGBaseActivity extends Activity implements IBaseActivity {
	/*** 整个应用Applicaiton **/
	private WYGApplication mApplication = null;
	/** 当前Activity的弱引用，防止内存泄露 **/
	private WeakReference<Activity> activity = null;
	/** 当前Activity渲染的视图View **/
	private View mContentView = null;
	/** 日志输出标志,当前类的类名 **/
	protected final String TAG = this.getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, TAG + "-->onCreate()");
		mContentView = LayoutInflater.from(this).inflate(bindLayout(), null); // 填充渲染视图View
		setContentView(mContentView);
		mApplication = (WYGApplication) getApplicationContext(); // 获取应用Application
		activity = new WeakReference<Activity>(this); // 将当前Activity压入栈
		mApplication.pushTask(activity);
		findViews(mContentView);// 初始化控件
		doBusiness(this); // 业务操作
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, TAG + "-->onRestart()");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, TAG + "-->onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, TAG + "-->onResume()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, TAG + "-->onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, TAG + "-->onStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, TAG + "-->onDestroy()");
		mApplication.removeTask(activity);
	}
}
