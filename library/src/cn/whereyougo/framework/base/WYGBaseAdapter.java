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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import cn.whereyougo.framework.model.WYGBaseModel;
import cn.whereyougo.framework.utils.LogUtil;

/**
 * list类型的Adapter基类<br/>
 * 严格按照MVC模式设计，适配器进行数据处理，将进度和结果通过UI监听回调传给Activity，Activity仅仅作为界面显示<br/>
 * <br/>
 * 2015年5月24日 - 下午4:48:14
 * 
 * @author lizhijun
 */
public abstract class WYGBaseAdapter<T extends WYGBaseModel> extends android.widget.BaseAdapter {
    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();

    /** 数据存储集合 **/
    private List<T> mDataList = Collections.synchronizedList(new ArrayList<T>());
    /** Context上下文 **/
    @SuppressWarnings("unused")
    private Context mContext;
    /** Activity回调adapter监听 **/
    private UIListener mUIListener;
    /** 每一页显示条数 **/
    private int mPerPageSize = 10;

    /**
     * 默认每一页10条数据
     * 
     * @param context
     *            上下文
     * @param uiListener
     *            UI界面刷新监听
     */
    public WYGBaseAdapter(Context context, UIListener uiListener) {
        this(context, 10, uiListener);
    }

    public WYGBaseAdapter(Context context, int mPerPageSize, UIListener uiListener) {
        this.mContext = context;
        this.mPerPageSize = mPerPageSize;
        this.mUIListener = uiListener;
    }

    /**
     * 将数据传入，刷新数据
     * 
     * @param mapData
     */
    protected void refreshData(Map<String, List<T>> mapData) {
        if (mapData == null) {
            return;
        }
        ArrayList<T> addList = new ArrayList<T>();
        for (List<T> listData : mapData.values()) {
            for (T model : listData) {
                boolean exist = false;
                if (mDataList.contains(model)) {
                    exist = true;
                }
                if (!exist) {
                    addList.add(model);
                }
            }
        }
        if (!addList.isEmpty()) {
            mDataList.addAll(addList);
            // notifyDataSetChanged();
            // mUIListener.dataCountChanged(mDataList.size());
            // TODO 当adapter获取数据改为使用IGetDataListener之后,这里不使用handler直接notifyData...
            sendNotifyDataChange2AdapterOrUI(MSG_WHAT_NOTIFY_DATA_CHANGE);// 测试使用
        }
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public T getItem(int position) {
        if (position < mDataList.size())
            return mDataList.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /** 获取当前页索引位置 */
    public int getCurrentPageIndex() {
        return (getCount() / mPerPageSize) + 1;
    }

    /**
     * 添加数据，并刷新适配器
     * 
     * @param model
     *            数据项
     */
    public boolean addItem(T model) {
        boolean result = false;
        if (model != null) {
            try {
                mDataList.add(model);
                result = true;
            } catch (Exception e) {
                LogUtil.e(TAG, TAG + "-->Exception:" + e.getMessage());
            }
        }
        return result;
    }

    /**
     * 在指定索引位置添加数据，并刷新适配器
     * 
     * @param position
     *            索引
     * @param model
     *            数据
     */
    public boolean addItem(int position, T model) {
        boolean result = false;
        if (position <= mDataList.size() && model != null) {
            try {
                mDataList.add(position, model);
                result = true;
            } catch (Exception e) {
                LogUtil.e(TAG, TAG + "-->Exception:" + e.getMessage());
            }
        }
        return result;
    }

    /**
     * 集合方式添加数据，并刷新适配器
     * 
     * @param models
     *            集合
     */
    public boolean addItem(List<T> models) {
        boolean result = false;
        if (models != null && models.size() > 0) {
            try {
                mDataList.addAll(models);
                result = true;
            } catch (Exception e) {
                LogUtil.e(TAG, TAG + "-->Exception:" + e.getMessage());
            }
        }
        return result;
    }

    /**
     * 在指定索引位置添加数据集合，并刷新适配器
     * 
     * @param position
     *            索引
     * @param models
     *            数据集合
     */
    public boolean addItem(int position, List<T> models) {
        boolean result = false;
        if (position <= mDataList.size() && models != null && models.size() > 0) {
            try {
                mDataList.addAll(position, models);
                result = true;
            } catch (Exception e) {
                LogUtil.e(TAG, TAG + "-->Exception:" + e.getMessage());
            }
        }
        return result;
    }

    /**
     * 移除指定对象数据，并刷新适配器
     * 
     * @param model
     *            移除对象
     * @return 是否移除成功
     */
    public boolean removeItem(T model) {
        boolean result = false;
        if (model != null) {
            try {
                mDataList.remove(model);
                result = true;
            } catch (Exception e) {
                LogUtil.e(TAG, TAG + "-->Exception:" + e.getMessage());
            }
        }
        return result;
    }

    /**
     * 移除指定索引位置对象，并刷新适配器
     * 
     * @param position
     *            删除对象索引位置
     * @return 被删除的对象
     */
    public T removeItem(int position) {
        T mode = null;
        if (position < mDataList.size()) {
            try {
                mode = mDataList.remove(position);
            } catch (Exception e) {
                LogUtil.e(TAG, TAG + "-->Exception:" + e.getMessage());
            }
        }
        return mode;
    }

    /**
     * 移除指定集合对象，并刷新适配器
     * 
     * @param models
     *            待移除的集合
     * @return 是否移除成功
     */
    public boolean removeAll(List<T> models) {
        boolean result = false;
        if (models != null && models.size() > 0 && mDataList.containsAll(models)) {
            try {
                result = mDataList.removeAll(models);
            } catch (Exception e) {
                LogUtil.e(TAG, TAG + "-->Exception:" + e.getMessage());
            }
        }
        return result;
    }

    /** 清空数据 */
    public void clear() {
        mDataList.clear();
    }

    /**
     * UI显示监听，由Activity实现此接口，数据适配器数据处理情况通过该接口回调给Activity<br/>
     * <br/>
     * 2015年5月24日 - 下午5:01:51
     * 
     * @author lizhijun
     */
    public static interface UIListener {
        /** 正在加载数据 */
        void isLoading();

        /** 总数据个数变化 */
        void dataCountChanged(int count);

        /** 加载数据完毕 */
        void loadFinished();
    }

    /**
     * 发送数据变化消息到适配器或者UI<br/>
     * 通知适配器 : {@link #MSG_WHAT_NOTIFY_ADAPTER_DATA_CHANGE} <br/>
     * 通知UI : {@link #MSG_WHAT_NOTIFY__UI_DATA_CHANGE} <br/>
     * 通知两者 : {@link #MSG_WHAT_NOTIFY_DATA_CHANGE} <br/>
     * */
    protected void sendNotifyDataChange2AdapterOrUI(int msgWhat) {
        UIHandler.obtainMessage(msgWhat).sendToTarget();
    }

    public static final int MSG_WHAT_NOTIFY_ADAPTER_DATA_CHANGE = 0x001;
    public static final int MSG_WHAT_NOTIFY__UI_DATA_CHANGE = 0x002;
    public static final int MSG_WHAT_NOTIFY_DATA_CHANGE = 0x003;
    private Handler UIHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            if (MSG_WHAT_NOTIFY_ADAPTER_DATA_CHANGE == msg.what) {
                notifyDataSetChanged();
            } else if (MSG_WHAT_NOTIFY__UI_DATA_CHANGE == msg.what) {
                mUIListener.dataCountChanged(mDataList.size());
            } else if (MSG_WHAT_NOTIFY_DATA_CHANGE == msg.what) {
                mUIListener.dataCountChanged(mDataList.size());
                notifyDataSetChanged();
            }
        };
    };
}
