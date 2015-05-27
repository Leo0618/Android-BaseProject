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
package cn.whereyougo.framework.utils;

import cn.whereyougo.framework.common.WYGConfig;

/**
 * 日志输出<br/>
 * <br/>
 * 2015年5月24日 - 下午4:34:28
 * 
 * @author lizhijun
 */
public final class LogUtil {

	public static void w(String tag, String content) {
		if (WYGConfig.DEBUG) {
			android.util.Log.w(tag, content);
		}
	}

	public static void w(final String tag, Object... objs) {
		if (WYGConfig.DEBUG) {
			android.util.Log.w(tag, getInfo(objs));
		}
	}

	public static void i(String tag, String content) {
		if (WYGConfig.DEBUG) {
			android.util.Log.i(tag, content);
		}
	}

	public static void i(final String tag, Object... objs) {
		if (WYGConfig.DEBUG) {
			android.util.Log.i(tag, getInfo(objs));
		}
	}

	public static void d(String tag, String content) {
		if (WYGConfig.DEBUG) {
			android.util.Log.d(tag, content);
		}
	}

	public static void d(final String tag, Object... objs) {
		if (WYGConfig.DEBUG) {
			android.util.Log.d(tag, getInfo(objs));
		}
	}

	public static void e(String tag, String content) {
		if (WYGConfig.DEBUG) {
			android.util.Log.e(tag, content);
		}
	}

	public static void e(String tag, String content, Throwable e) {
		if (WYGConfig.DEBUG) {
			android.util.Log.e(tag, content, e);
		}
	}

	public static void e(final String tag, Object... objs) {
		if (WYGConfig.DEBUG) {
			android.util.Log.e(tag, getInfo(objs));
		}
	}

	private static String getInfo(Object... objs) {
		StringBuffer sb = new StringBuffer();
		if (null == objs) {
			sb.append("no mesage.");
		} else {
			for (Object object : objs) {
				sb.append(object);
			}
			sb.append("-");
		}
		return sb.toString();
	}

	public static void sysOut(Object msg) {
		if (WYGConfig.DEBUG) {
			System.out.println(msg);
		}
	}

	public static void sysErr(Object msg) {
		if (WYGConfig.DEBUG) {
			System.err.println(msg);
		}
	}
}
