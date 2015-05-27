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
package cn.whereyougo.framework.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.whereyougo.framework.R;
import cn.whereyougo.framework.base.WYGBaseActivity;
import cn.whereyougo.framework.base.WYGBaseAdapter;
import cn.whereyougo.framework.model.WYGBaseModel;

public class MainActivity extends WYGBaseActivity {
    private Context mContext;
    private TextView mTitle;
    private ProgressBar mProgressbar;
    private ListView mListView;
    private TestListAdapter mAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void findViews(View v) {
        mTitle = (TextView) v.findViewById(R.id.tv_title);
        mProgressbar = (ProgressBar) v.findViewById(R.id.pb_loading);
        mListView = (ListView) v.findViewById(R.id.lv_content);
    }

    @Override
    public void doBusiness(Context context) {
        this.mContext = context;
        mTitle.setText("Listview列表");
        mAdapter = new TestListAdapter(mContext, mUIListener);
        mListView.setAdapter(mAdapter);
        mUIListener.isLoading();
    }

    /** adapter回调的UI刷新监听 */
    private WYGBaseAdapter.UIListener mUIListener = new WYGBaseAdapter.UIListener() {

        @Override
        public void isLoading() {
            // 转菊花等待
            mProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        public void dataCountChanged(int count) {
            // 刷新必要的界面
            mTitle.setText("Listview列表" + "(" + count + ")");
        }

        @Override
        public void loadFinished() {
            // 停止转菊花
            mProgressbar.setVisibility(View.GONE);
        }
    };

    /** 测试适配器对象，实际开发需要单独提出 */
    private class TestListAdapter extends WYGBaseAdapter<TestModel> {
        private UIListener uIListener;

        public TestListAdapter(Context context, cn.whereyougo.framework.base.WYGBaseAdapter.UIListener uiListener) {
            super(context, uiListener);
            uIListener = uiListener;
            // --->模拟异步不断获取数据开始，不断抛到UI线程通知界面刷新
            new Thread() {
                public void run() {
                    Map<String, List<TestModel>> mapData = new HashMap<String, List<TestModel>>();
                    List<TestModel> list = new ArrayList<MainActivity.TestModel>();
                    for (int x = 0; x < 100; x++) {
                        list.add(new TestModel("Test--" + x, x));
                        mapData.put("testModel", list);
                        refreshData(mapData);
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 发送加载完毕的消息到主线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uIListener.loadFinished();
                        }
                    });
                };
            }.start();
            // --->模拟异步不断获取数据结束。 以上数据获取方法存在不合理性仅作此处测试使用。实际开发中由数据中心通过IGetDataListener回调到这里
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_test, null);
                holder.tv = (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TestModel model = getItem(position);
            holder.tv.setText(model.index + "---->" + model.name);
            return convertView;
        }

        class ViewHolder {
            TextView tv;
        }
    }

    /** 测试模式对象，实际开发需要单独提出 */
    @SuppressWarnings("serial")
    private class TestModel extends WYGBaseModel {
        public int index;
        public String name;

        public TestModel(String name, int index) {
            this.index = index;
            this.name = name;
        }

    }
}
