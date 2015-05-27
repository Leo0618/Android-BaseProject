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
package cn.whereyougo.framework.base.impl;

import android.content.Context;
import android.view.View;

/**
 * Activity接口<br/>
 * <br/>
 * 2015年5月24日 - 下午4:35:03
 * 
 * @author lizhijun
 */
public interface IBaseActivity {

	/**
	 * 绑定渲染视图的布局文件
	 * 
	 * @return 布局文件资源id
	 */
	public int bindLayout();

	/** 初始化控件 */
	public void findViews(final View v);

	/**
	 * 业务处理操作（onCreate方法中被调用）<br/>
	 * 数据逻辑及业务操作在此方法中<br/>
	 * 
	 * @param mContext
	 *            当前Activity对象上下文
	 */
	public void doBusiness(Context context);

}
