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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import cn.whereyougo.framework.model.WYGBaseModel;
import cn.whereyougo.framework.utils.LogUtil;

/**
 * 数据中心基类，由继承的子类实现获取数据的方法{@code asyncAquireData()}，通过三个send...接口实现与UI动态交互<br/>
 * 数据持久化操作由子类各自维护<br/>
 * 2015年5月25日-下午2:44:21
 * @author lizhijun
 */
public abstract class WYGBaseDataManager<T extends WYGBaseModel> {

    protected final String TAG = this.getClass().getSimpleName();
    protected Context mContext;

    private ReferenceQueue<IGetDataListener<T>> mListenerReferenceQueue = null;
    private ConcurrentLinkedQueue<WeakReference<IGetDataListener<T>>> mWeakListenerArrayList = null;
    private final Handler mUiHandler = new Handler(Looper.getMainLooper()); // ui线程

    public WYGBaseDataManager(Context context) {
        this.mContext = context;
        mListenerReferenceQueue = new ReferenceQueue<IGetDataListener<T>>();
        mWeakListenerArrayList = new ConcurrentLinkedQueue<WeakReference<IGetDataListener<T>>>();
    }

    /** 是否在加载数据中 */
    private final AtomicBoolean isAquireData = new AtomicBoolean(false);

    /** 异步获取本地缓存数据 */
    protected void asyncAquireData() {
        LogUtil.d(TAG, "asyncAquireData invoke.");
        if (!isAquireData.get()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        isAquireData.set(true);
                        requestGetData();
                    } finally {
                        isAquireData.set(false);
                    }
                }
            }).start();
        }
    }

    /**
     * 获取数据，该方法由子类实现，不建议在UI线程直接调用.用于网络请求数据或是扫描本地数据
     */
    protected abstract void requestGetData();

    /** 发消息给UI线程,获取数据开始 */
    protected void sendStartLoadDataMsgToUI() {
        LogUtil.d(TAG, "sendStartLoadDataMsgToUI");
        synchronized (mWeakListenerArrayList) {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (WeakReference<IGetDataListener<T>> weakListener : mWeakListenerArrayList) {
                        IGetDataListener<T> listenerItem = weakListener.get();
                        if (listenerItem != null) {
                            listenerItem.startLoadData();
                        }
                    }
                }
            });
        }
    }

    /** 发消息给UI线程,有数据获取到 */
    protected void sendLoadedDataMsgToUI(final Map<String, List<T>> datas) {
        if (datas == null || datas.isEmpty()) {
            return;
        }
        LogUtil.d(TAG, "sendloadedDataMsgToUI");
        synchronized (mWeakListenerArrayList) {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (WeakReference<IGetDataListener<T>> weakListener : mWeakListenerArrayList) {
                        IGetDataListener<T> listenerItem = weakListener.get();
                        if (listenerItem != null) {
                            listenerItem.loadedData(datas);
                        }
                    }
                }
            });
        }
    }

    /** 发消息给UI线程,数据加载完成 */
    protected void sendLoadFinishMsgToUI() {
        LogUtil.d(TAG, "sendloadFinishMsgToUI");
        synchronized (mWeakListenerArrayList) {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (WeakReference<IGetDataListener<T>> weakListener : mWeakListenerArrayList) {
                        IGetDataListener<T> listenerItem = weakListener.get();
                        if (listenerItem != null) {
                            listenerItem.loadDataFinish();
                        }
                    }
                }
            });
        }
    }

    /** 注册监听 */
    public void registerListener(IGetDataListener<T> listener) {
        if (listener == null) {
            return;
        }
        synchronized (mWeakListenerArrayList) {
            // 每次注册的时候清理已经被系统回收的对象
            Reference<? extends IGetDataListener<T>> releaseListener = null;
            while ((releaseListener = mListenerReferenceQueue.poll()) != null) {
                mWeakListenerArrayList.remove(releaseListener);
            }
            // 弱引用处理
            for (WeakReference<IGetDataListener<T>> weakListener : mWeakListenerArrayList) {
                IGetDataListener<T> listenerItem = weakListener.get();
                if (listenerItem == listener) {
                    return;
                }
            }
            WeakReference<IGetDataListener<T>> weakListener = new WeakReference<IGetDataListener<T>>(listener, mListenerReferenceQueue);
            this.mWeakListenerArrayList.add(weakListener);
        }
    }

    /** 取消注册监听 */
    public void unregisterListener(IGetDataListener<T> listener) {
        if (listener == null) {
            return;
        }
        synchronized (mWeakListenerArrayList) {
            // 弱引用处理
            for (WeakReference<IGetDataListener<T>> weakListener : mWeakListenerArrayList) {
                IGetDataListener<T> listenerItem = weakListener.get();
                if (listenerItem == listener) {
                    mWeakListenerArrayList.remove(weakListener);
                    return;
                }
            }
        }
    }

    /**
     * 数据中心和适配器直接的回调监听<br/>
     * <br/>
     * 2015年5月25日-下午2:39:57
     * @author lizhijun
     */
    public interface IGetDataListener<T> {
        /** 开始获取数据 */
        public void startLoadData();

        /** 获取到数据 从异步线程抛到这里 */
        public void loadedData(Map<String, List<T>> datas);

        /** 结束获取数据 */
        public void loadDataFinish();
    }

}
