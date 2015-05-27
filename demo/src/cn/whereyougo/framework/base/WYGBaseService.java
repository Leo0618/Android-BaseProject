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

import android.app.Service;
import android.content.Intent;
import cn.whereyougo.framework.utils.LogUtil;

/**
 * android 系统中的四大组件之一Service基类<br/>
 * <br/>
 * 2015年5月22日-下午7:32:55
 * 
 * @author lizhijun
 */
public abstract class WYGBaseService extends Service {

	/** 日志输出标志 **/
	protected final String TAG = this.getClass().getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.d(TAG, TAG + "-->onCreate()");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		LogUtil.d(TAG, TAG + "-->onStart()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, TAG + "-->onDestroy()");
	}

}
