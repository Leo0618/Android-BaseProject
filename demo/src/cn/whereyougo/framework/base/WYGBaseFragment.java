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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.whereyougo.framework.base.impl.IBaseFragment;

/**
 * Fragment基类(兼容低版本)<br/>
 * <br/>
 * 2015年5月24日 - 下午4:38:18
 * 
 * @author lizhijun
 */
public abstract class WYGBaseFragment extends Fragment implements IBaseFragment {
	/** 日志输出标志 **/
	protected final String TAG = this.getClass().getSimpleName();
	/** 当前Fragment渲染的视图View **/
	private View mContentView = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, TAG + "-->onAttach()");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, TAG + "-->onCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, TAG + "-->onCreateView()");
		// 渲染视图View(防止切换时重绘View)
		if (null != mContentView) {
			ViewGroup parent = (ViewGroup) mContentView.getParent();
			if (null != parent) {
				parent.removeView(mContentView);
			}
		} else {
			mContentView = inflater.inflate(bindLayout(), container);
			findView(mContentView);// 控件初始化
		}
		return mContentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(TAG, TAG + "-->onViewCreated()");
		super.onViewCreated(view, savedInstanceState);
		doBusiness(getActivity());// 业务处理
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, TAG + "-->onActivityCreated()");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, TAG + "-->onSaveInstanceState()");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		Log.d(TAG, TAG + "-->onStart()");
		super.onStart();
	}

	@Override
	public void onResume() {
		Log.d(TAG, TAG + "-->onResume()");
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.d(TAG, TAG + "-->onPause()");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.d(TAG, TAG + "-->onStop()");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, TAG + "-->onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		Log.d(TAG, TAG + "-->onDetach()");
		super.onDetach();
	}

}
