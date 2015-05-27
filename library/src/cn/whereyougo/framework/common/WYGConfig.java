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
package cn.whereyougo.framework.common;

import cn.whereyougo.framework.BuildConfig;

public final class WYGConfig {
	/** 调试开关,引用构建配置自动设置 */
	public final static boolean DEBUG = BuildConfig.DEBUG;

	static {
		cn.whereyougo.framework.utils.LogUtil.i("WYGConfig", "------------");
		cn.whereyougo.framework.utils.LogUtil.i("WYGConfig", "DEBUG=", DEBUG);
		cn.whereyougo.framework.utils.LogUtil.i("WYGConfig", "------------");
	}

}
