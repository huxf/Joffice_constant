package com.hy.powerplatform.oa_flow.notice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.refreshview.CustomRefreshView;
import com.hy.powerplatform.R;
import com.hy.powerplatform.my_utils.base.BaseActivity;
import com.hy.powerplatform.my_utils.base.Constant;
import com.hy.powerplatform.my_utils.base.OkHttpUtil;
import com.hy.powerplatform.my_utils.myViews.Header;
import com.hy.powerplatform.my_utils.utils.BaseRecyclerAdapter;
import com.hy.powerplatform.my_utils.utils.BaseViewHolder;
import com.hy.powerplatform.my_utils.utils.ProgressDialogUtil;
import com.hy.powerplatform.oa_flow.notice.bean.NoticeList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

import static com.hy.powerplatform.my_utils.base.Constant.TAG_ONE;
import static com.hy.powerplatform.my_utils.base.Constant.TAG_TWO;

public class NoticeListActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.recyclerView)
    CustomRefreshView recyclerView;

    int limit = 20;
    int start = 0;
    private OkHttpUtil httpUtil;
    BaseRecyclerAdapter baseAdapter;
    final HashMap<String, String> map = new HashMap();
    List<NoticeList> beanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        httpUtil = OkHttpUtil.getInstance(this);

        baseAdapter = new BaseRecyclerAdapter<NoticeList>(this, R.layout.adapter_notice_item, beanList) {
            @Override
            public void convert(BaseViewHolder holder, final NoticeList noticeBean) {
                holder.setText(R.id.tv_title, noticeBean.getTitle());
                holder.setText(R.id.tv_content, noticeBean.getCreatetime());
//                List<String> imgSrc = new ArrayList<String>();
//                imgSrc = GetHtmlImageSrcList(noticeBean.getContent());
                String text = GetHtmlText(noticeBean.getContent());
//                String s = noticeBean.getDetails().replaceAll("<p>", "");
//                s = s.replaceAll("</p>", "");
                holder.setText(R.id.tvDetail, text);
                holder.setOnClickListener(R.id.noticeItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NoticeListActivity.this, NoticeDetailActivity.class);
                        intent.putExtra("bean", noticeBean);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(baseAdapter);

        getData(start, limit);
        setClient();
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

    private void getData(final int start, final int limit) {
        ProgressDialogUtil.startLoad(this, getResources().getString(R.string.get_data));
        final String path_url = Constant.BASE_URL2 + Constant.NOTICE + "?start=" + start + "&limit=" + limit;
        map.clear();
        map.put("Q_isNotice_SN_EQ", "1  ");
        map.put("sort", "newsId");
        map.put("dir", "DESC");
        map.put("Q_status_SN_EQ", "1");
        httpUtil.postForm(path_url, map, new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
//                Log.i("main", "response:" + e.toString());
                Message message = new Message();
                Bundle b = new Bundle();
                b.putString("error", e.toString());
                message.setData(b);
                message.what = TAG_ONE;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Response response) throws IOException {
//                Log.i("main", "response:" + response.body().string());
                String data = response.body().string();
                Message message = new Message();
                Bundle b = new Bundle();
                b.putString("data", data);
                message.setData(b);
                message.what = TAG_TWO;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_notice_list;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    /**
     * 获取HTML文件里面的IMG标签的SRC地址
     *
     * @param htmlText 带html格式的文本
     */
    public static List<String> GetHtmlImageSrcList(String htmlText) {
        List<String> imgSrc = new ArrayList<String>();
        Matcher m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(htmlText);
        while (m.find()) {
            imgSrc.add(m.group(1));
        }
        return imgSrc;
    }

    /**
     * 去掉所有的HTML,获取其中的文本信息
     *
     * @param htmlText
     * @return
     */
    public static String GetHtmlText(String htmlText) {
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlText);
        htmlText = m_html.replaceAll(""); // 过滤HTML标签
        return htmlText;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TAG_ONE:
                    String message = msg.getData().getString("error");
                    Toast.makeText(NoticeListActivity.this, message, Toast.LENGTH_SHORT).show();
                    ProgressDialogUtil.stopLoad();
                    break;
                case TAG_TWO:
                    Bundle b1 = msg.getData();
                    String data = b1.getString("data");
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            NoticeList bean = new NoticeList();
                            bean.setTitle(jsonObject1.getString("subject"));
                            bean.setContent(jsonObject1.getString("content"));
                            bean.setAuthor(jsonObject1.getString("author"));
                            bean.setCreatetime(jsonObject1.getString("createtime"));
                            beanList.add(bean);
                        }
                        if (jsonArray.length() != 0) {
                            if (jsonArray.length() == 0 && beanList.size() != 0) {
                                if (recyclerView != null) {
                                    recyclerView.complete();
                                    recyclerView.onNoMore();
                                    baseAdapter.notifyDataSetChanged();
                                }
                            } else if (jsonArray.length() != 0 && beanList.size() != 0 && jsonArray.length() < 20) {
                                if (recyclerView != null) {
                                    recyclerView.complete();
                                    recyclerView.onNoMore();
                                    baseAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (recyclerView != null) {
                                    recyclerView.complete();
                                    baseAdapter.notifyDataSetChanged();
                                }
                            }
                            baseAdapter.notifyDataSetChanged();
                        } else {
                            recyclerView.complete();
                            recyclerView.onNoMore();
                            baseAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ProgressDialogUtil.stopLoad();
                    break;
            }
        }
    };
}