package com.hy.powerplatform.oa_flow.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.refreshview.CustomRefreshView;
import com.google.gson.Gson;
import com.hy.powerplatform.R;
import com.hy.powerplatform.business_inspect.utils.DBHandler;
import com.hy.powerplatform.my_utils.base.Constant;
import com.hy.powerplatform.my_utils.utils.ProgressDialogUtil;
import com.hy.powerplatform.oa_flow.activity.FlowLeaveDetailActivity;
import com.hy.powerplatform.oa_flow.adapter.FlowLeaveListAdapter;
import com.hy.powerplatform.oa_flow.bean.MyLeave;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hy.powerplatform.my_utils.base.Constant.TAG_ONE;
import static com.hy.powerplatform.my_utils.base.Constant.TAG_TWO;


/**
 * Created by dell on.
 */

public class FragmentLeaveList extends Fragment implements FlowLeaveListAdapter.OnGetAdapterPositionListener {
    @BindView(R.id.recyclerView)
    CustomRefreshView recyclerView;
    @BindView(R.id.liContent1)
    LinearLayout llNoContent;

    int limit = 20;
    int start = 0;
    String res = "";
    View view;
    Unbinder unbinder;
    FlowLeaveListAdapter adapter;
    List<MyLeave.ResultBean> beanList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.getRecyclerView().setLayoutManager(manager);
        adapter = new FlowLeaveListAdapter(getActivity(), beanList);
        adapter.sendOnGetAdapterPositionListener(this);
        recyclerView.setAdapter(adapter);
//        //禁止下拉刷新
//        recyclerView.setRefreshEnable(false);
//        //禁止加载更多
//        recyclerView.setLoadMoreEnable(false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProgressDialogUtil.startLoad(getActivity(),"获取数据中");
        getData(start, limit);
        setClient();
    }

    /**
     * 获取数据
     *
     * @param start
     * @param limit
     */
    private void getData(final int start, final int limit) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Constant.BASE_URL2 + Constant.MYLIST+start+"&limit="+limit+"&Q_subject_S_LK="+Constant.LEAVERNAME;
                DBHandler dbA = new DBHandler();
                res = dbA.OAQingJiaMy(url);
                if (res.equals("获取数据失败") || res.equals("")) {
                    handler.sendEmptyMessage(TAG_TWO);
                } else {
                    handler.sendEmptyMessage(TAG_ONE);
                }
            }
        }).start();
    }

    /**
     * 滑动监听
     */
    private void setClient() {
        recyclerView.setOnLoadListener(new CustomRefreshView.OnLoadListener() {
            @Override
            public void onRefresh() {
                beanList.clear();
                start = 0;
                limit = 20;
                getData(start, limit);
            }

            @Override
            public void onLoadMore() {
                start = limit;
                limit += 20;
                getData(start, limit);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void getFlowList(FlowList flowList) {
//        if (flowList.getData().getContent() != null && flowList.getData().getContent().size() > 0) {
//            for (int i = 0; i < flowList.getData().getContent().size(); i++) {
////                beanList.add(flowList.getData().getContent().get(i));
//            }
//            if (beanList.size() < 20) {
//                adapter.notifyDataSetChanged();
//                recyclerView.complete();
//                recyclerView.onNoMore();
//            } else if (beanList.size() < page * size + 20) {
//                adapter.notifyDataSetChanged();
//                recyclerView.onNoMore();
//            } else {
//                adapter.notifyDataSetChanged();
//                recyclerView.complete();
//            }
//        } else {
//            llNoContent.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getAdapterPosition(int position) {
        MyLeave.ResultBean bean = beanList.get(position);
        Intent intent = new Intent(getActivity(), FlowLeaveDetailActivity.class);
        intent.putExtra("bean", bean.getRunId());
        startActivity(intent);
    }

    /** @author suncat
        * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
        * @return
    */
    public static final boolean ping() {

        String result = null;
        try {
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
        }
        return false;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TAG_ONE:
                    Gson gson = new Gson();
                    MyLeave bean = gson.fromJson(res,MyLeave.class);
                    for (int i = 0;i<bean.getResult().size();i++){
                        beanList.add(bean.getResult().get(i));
                    }

                    if (bean.getResult().size() == 0&&beanList.size()==0){
                        if (recyclerView != null) {
                            recyclerView.setVisibility(View.GONE);
                            llNoContent.setVisibility(View.VISIBLE);
                        }
                        ProgressDialogUtil.stopLoad();
                        break;
                    }else if (bean.getResult().size() == 0&&beanList.size()!=0){
                        if (recyclerView != null) {
                            recyclerView.complete();
                            recyclerView.onNoMore();
                            adapter.notifyDataSetChanged();
                        }
                    }else if (bean.getResult().size() != 0&&beanList.size()!=0&&bean.getResult().size()<20){
                        if (recyclerView != null) {
                            recyclerView.complete();
                            recyclerView.onNoMore();
                            adapter.notifyDataSetChanged();
                        }
                    }else {
                        if (recyclerView != null) {
                            recyclerView.complete();
                            adapter.notifyDataSetChanged();
                        }
                    }
                    ProgressDialogUtil.stopLoad();
                    break;
                case TAG_TWO:
                    ProgressDialogUtil.stopLoad();
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}