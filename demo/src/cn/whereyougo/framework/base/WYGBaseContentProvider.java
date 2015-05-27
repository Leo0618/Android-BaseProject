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

import android.content.ContentProvider;

/**
 * android 系统中的四大组件之一ContentProvider基类<br/>
 * <br/>
 * 2015年5月22日-下午7:38:46
 * 
 * @author lizhijun
 */
public abstract class WYGBaseContentProvider extends ContentProvider {

	/** 日志输出标志 **/
	protected final String TAG = this.getClass().getSimpleName();
}
