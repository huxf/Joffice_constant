package com.hy.powerplatform.oa_flow.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hy.powerplatform.R;
import com.hy.powerplatform.SharedPreferencesHelper;
import com.hy.powerplatform.business_inspect.utils.DBHandler;
import com.hy.powerplatform.my_utils.base.AlertDialogCallBackP;
import com.hy.powerplatform.my_utils.base.BaseActivity;
import com.hy.powerplatform.my_utils.base.Constant;
import com.hy.powerplatform.my_utils.myViews.Header;
import com.hy.powerplatform.my_utils.myViews.MyAlertDialog;
import com.hy.powerplatform.my_utils.utils.ProgressDialogUtil;
import com.hy.powerplatform.my_utils.utils.time_select.CustomDatePickerDay;
import com.hy.powerplatform.oa_flow.adapter.FlowMessageAdapter;
import com.hy.powerplatform.oa_flow.bean.File;
import com.hy.powerplatform.oa_flow.bean.FlowBillWillDetail;
import com.hy.powerplatform.oa_flow.bean.FlowContractPerson;
import com.hy.powerplatform.oa_flow.bean.FlowMessage1;
import com.hy.powerplatform.oa_flow.util.MyStringSpilt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hy.powerplatform.my_utils.base.Constant.TAG_EIGHT;
import static com.hy.powerplatform.my_utils.base.Constant.TAG_FIVE;
import static com.hy.powerplatform.my_utils.base.Constant.TAG_FOUR;
import static com.hy.powerplatform.my_utils.base.Constant.TAG_NINE;
import static com.hy.powerplatform.my_utils.base.Constant.TAG_ONE;
import static com.hy.powerplatform.my_utils.base.Constant.TAG_SEVEN;
import static com.hy.powerplatform.my_utils.base.Constant.TAG_SIX;
import static com.hy.powerplatform.my_utils.base.Constant.TAG_THERE;
import static com.hy.powerplatform.my_utils.base.Constant.TAG_TWO;

public class FlowBillWillDetailActivity extends BaseActivity {
    @BindView(R.id.header)
    Header header;
    @BindView(R.id.tvZDPerson)
    TextView tvZDPerson;
    @BindView(R.id.tvZDTime)
    TextView tvZDTime;
    @BindView(R.id.tvDepartment)
    TextView tvDepartment;
    @BindView(R.id.tvJKPerson)
    TextView tvJKPerson;
    @BindView(R.id.tvDriver)
    TextView tvDriver;
    @BindView(R.id.tvCarNo)
    TextView tvCarNo;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvUse)
    TextView tvUse;
    @BindView(R.id.tvOtherPerson)
    TextView tvOtherPerson;
    @BindView(R.id.tvOtherPhone)
    TextView tvOtherPhone;
    @BindView(R.id.tvReason)
    TextView tvReason;
    @BindView(R.id.tvBigMoney)
    TextView tvBigMoney;
    @BindView(R.id.tvSmallMoney)
    TextView tvSmallMoney;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvBlame)
    TextView tvBlame;
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvAllMoneyM)
    TextView tvAllMoneyM;
    @BindView(R.id.tvAllMoneyY)
    TextView tvAllMoneyY;
    @BindView(R.id.cb1)
    CheckBox cb1;
    @BindView(R.id.cb2)
    CheckBox cb2;
    @BindView(R.id.cb3)
    CheckBox cb3;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.cb4)
    CheckBox cb4;
    @BindView(R.id.cb5)
    CheckBox cb5;
    @BindView(R.id.cb6)
    CheckBox cb6;
    @BindView(R.id.ll2)
    LinearLayout ll2;
    @BindView(R.id.cb7)
    CheckBox cb7;
    @BindView(R.id.cb8)
    CheckBox cb8;
    @BindView(R.id.cb9)
    CheckBox cb9;
    @BindView(R.id.ll5)
    LinearLayout ll5;
    @BindView(R.id.rb1)
    CheckBox rb1;
    @BindView(R.id.rb2)
    CheckBox rb2;
    @BindView(R.id.rb3)
    CheckBox rb3;
    @BindView(R.id.ll3)
    LinearLayout ll3;
    @BindView(R.id.rb4)
    CheckBox rb4;
    @BindView(R.id.rb5)
    CheckBox rb5;
    @BindView(R.id.rb6)
    CheckBox rb6;
    @BindView(R.id.ll4)
    LinearLayout ll4;
    @BindView(R.id.tvLeader)
    TextView tvLeader;
    @BindView(R.id.etLeader)
    EditText etLeader;
    @BindView(R.id.tvLeader1)
    TextView tvLeader1;
    @BindView(R.id.etLeader1)
    EditText etLeader1;
    @BindView(R.id.tvLeader2)
    TextView tvLeader2;
    @BindView(R.id.etLeader2)
    EditText etLeader2;
    @BindView(R.id.btnUp)
    Button btnUp;
    @BindView(R.id.tvData)
    TextView tvData;
    @BindView(R.id.textView16)
    TextView textView16;
    @BindView(R.id.btnT)
    FloatingActionButton btnT;
    @BindView(R.id.tvText)
    TextView tvText;
    @BindView(R.id.btnHistory)
    Button btnHistory;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private String name, taskId, res, aqfwyj, fgldyj, ldspyj, liushuihao;
    private String mainId, signaName, destName, destType, checkTask, qianzhiData = "";
    String leader = "";
    String leaderCode = "";
    String leaderName = "";
    boolean assigned;
    String tag = "noEnd";
    String comment = "";
    String aqreout, fgreout, ldspreout, flowAssignld, serialNumber = "";

    String[] bigNametemp = null;
    String[] bigCodetemp = null;
    List<String> resultList = new ArrayList<>();
    List<String> bigResultList = new ArrayList<>();

    String role = "";
    String url;
    String userCode = "";
    String userName = "";
    String[] nametemp = null;
    String[] codetemp = null;
    List<String> namelist = new ArrayList<>();
    List<FlowBillWillDetail.TransBean> beanList = new ArrayList<>();
    List<String> selectList = new ArrayList<>();
    private CustomDatePickerDay customDatePicker1;

    String shiJian = "";
    String xiangguanfujian = "";
    String btnTTag = "N";
    String flowMessage = "";
    String runID = "";
    FlowMessageAdapter adapter;
    List<FlowMessage1.DataBean> flowList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        LinearLayoutManager manager  = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        Intent intent = getIntent();
        name = intent.getStringExtra("activityName");
        taskId = intent.getStringExtra("taskId");
        getData(name, taskId);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_bill_data1;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }


    /**
     * 获取数据
     *
     * @param Name
     * @param taskId
     */
    private void getData(final String Name, final String taskId) {
        ProgressDialogUtil.startLoad(this, getResources().getString(R.string.get_data));
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Constant.BASE_URL2 + Constant.DETAILWILL + Name + "&taskId=" + taskId;
                DBHandler dbA = new DBHandler();
                res = dbA.OAQingJiaWillDoDex(url);
                if (res.equals("获取数据失败") || res.equals("") || res.equals("false")) {
                    handler.sendEmptyMessage(TAG_TWO);
                } else {
                    handler.sendEmptyMessage(TAG_ONE);
                }
            }
        }).start();
    }

    /**
     * 请求第一个处理人  之后不再让别人处理
     */
    private void getLastPerson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Constant.BASE_URL2 + Constant.LASTPERSON;
                DBHandler dbA = new DBHandler();
                checkTask = dbA.OAQingJiaLast(url, taskId);
                if (checkTask.equals("保存失败") || checkTask.equals("")) {
                    handler.sendEmptyMessage(TAG_TWO);
                } else {
                    handler.sendEmptyMessage(TAG_EIGHT);
                }
            }
        }).start();
    }

    /**
     * 获取destType  最后不是end的下一级审核人
     */
    private void getNoEndPerson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Constant.BASE_URL2 + Constant.NOENDPERSON;
                DBHandler dbA = new DBHandler();
                qianzhiData = dbA.OAQingJiaFornt1(url, taskId, destName);
                if (qianzhiData.equals("保存失败") || qianzhiData.equals("")) {
                    handler.sendEmptyMessage(TAG_TWO);
                } else {
                    handler.sendEmptyMessage(TAG_SEVEN);
                }
            }
        }).start();
    }

    //
    private void getNextPerson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Constant.BASE_URL2 + Constant.NESTPERSON;
                DBHandler dbA = new DBHandler();
                qianzhiData = dbA.OAQingJiaFornt1(url, taskId, destName);
                if (qianzhiData.equals("保存失败") || qianzhiData.equals("")) {
                    handler.sendEmptyMessage(TAG_TWO);
                } else {
                    handler.sendEmptyMessage(TAG_SEVEN);
                }
            }
        }).start();
    }

    /**
     * 设置多选框显示
     */
    private void setCbRbVer() {
        resultList.clear();
        bigResultList.clear();
        if (codetemp != null) {
            for (String s : codetemp) {
                resultList.add(s);
            }
        }
        if (bigCodetemp != null) {
            for (String s : bigCodetemp) {
                bigResultList.add(s);
            }
        }
        if (nametemp != null) {
            if (nametemp.length == 1) {
                rb1.setText(nametemp[0]);
                ll3.setVisibility(View.VISIBLE);
                rb1.setVisibility(View.VISIBLE);
                rb2.setVisibility(View.INVISIBLE);
                rb3.setVisibility(View.INVISIBLE);
            }
            if (nametemp.length == 2) {
                rb1.setText(nametemp[0]);
                rb2.setText(nametemp[1]);
                ll3.setVisibility(View.VISIBLE);
                rb1.setVisibility(View.VISIBLE);
                rb2.setVisibility(View.VISIBLE);
                rb3.setVisibility(View.INVISIBLE);
            }
            if (nametemp.length == 3) {
                rb1.setText(nametemp[0]);
                rb2.setText(nametemp[1]);
                rb3.setText(nametemp[2]);
                ll3.setVisibility(View.VISIBLE);
                rb1.setVisibility(View.VISIBLE);
                rb2.setVisibility(View.VISIBLE);
                rb3.setVisibility(View.VISIBLE);
            }
            if (nametemp.length == 4) {
                rb1.setText(nametemp[0]);
                rb2.setText(nametemp[1]);
                rb3.setText(nametemp[2]);
                rb4.setText(nametemp[3]);
                ll3.setVisibility(View.VISIBLE);
                ll4.setVisibility(View.VISIBLE);
                rb1.setVisibility(View.VISIBLE);
                rb2.setVisibility(View.VISIBLE);
                rb3.setVisibility(View.VISIBLE);
                rb4.setVisibility(View.VISIBLE);
                rb5.setVisibility(View.INVISIBLE);
                rb6.setVisibility(View.INVISIBLE);
            }
            if (nametemp.length == 5) {
                rb1.setText(nametemp[0]);
                rb2.setText(nametemp[1]);
                rb3.setText(nametemp[2]);
                rb4.setText(nametemp[3]);
                rb5.setText(nametemp[4]);
                ll3.setVisibility(View.VISIBLE);
                ll4.setVisibility(View.VISIBLE);
                rb1.setVisibility(View.VISIBLE);
                rb2.setVisibility(View.VISIBLE);
                rb3.setVisibility(View.VISIBLE);
                rb4.setVisibility(View.VISIBLE);
                rb5.setVisibility(View.VISIBLE);
                rb6.setVisibility(View.INVISIBLE);
            }
            if (nametemp.length >= 6) {
                rb1.setText(nametemp[0]);
                rb2.setText(nametemp[1]);
                rb3.setText(nametemp[2]);
                rb4.setText(nametemp[3]);
                rb5.setText(nametemp[4]);
                rb6.setText(nametemp[5]);
                ll3.setVisibility(View.VISIBLE);
                ll4.setVisibility(View.VISIBLE);
                rb1.setVisibility(View.VISIBLE);
                rb2.setVisibility(View.VISIBLE);
                rb3.setVisibility(View.VISIBLE);
                rb4.setVisibility(View.VISIBLE);
                rb5.setVisibility(View.VISIBLE);
                rb6.setVisibility(View.VISIBLE);
            }
        }


        if (bigNametemp != null) {
            if (bigNametemp.length == 1) {
                cb1.setText(bigNametemp[0]);
                ll1.setVisibility(View.VISIBLE);
                cb1.setVisibility(View.VISIBLE);
                cb2.setVisibility(View.INVISIBLE);
                cb3.setVisibility(View.INVISIBLE);
            }
            if (bigNametemp.length == 2) {
                cb1.setText(bigNametemp[0]);
                cb2.setText(bigNametemp[1]);
                ll1.setVisibility(View.VISIBLE);
                cb1.setVisibility(View.VISIBLE);
                cb2.setVisibility(View.VISIBLE);
                cb3.setVisibility(View.INVISIBLE);
            }
            if (bigNametemp.length == 3) {
                cb1.setText(bigNametemp[0]);
                cb2.setText(bigNametemp[1]);
                cb3.setText(bigNametemp[2]);
                ll1.setVisibility(View.VISIBLE);
                cb1.setVisibility(View.VISIBLE);
                cb2.setVisibility(View.VISIBLE);
                cb3.setVisibility(View.VISIBLE);
            }
            if (bigNametemp.length == 4) {
                cb1.setText(bigNametemp[0]);
                cb2.setText(bigNametemp[1]);
                cb3.setText(bigNametemp[2]);
                cb4.setText(bigNametemp[3]);
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.VISIBLE);
                cb1.setVisibility(View.VISIBLE);
                cb2.setVisibility(View.VISIBLE);
                cb3.setVisibility(View.VISIBLE);
                cb4.setVisibility(View.VISIBLE);
                cb5.setVisibility(View.INVISIBLE);
                cb6.setVisibility(View.INVISIBLE);
            }
            if (bigNametemp.length == 5) {
                cb1.setText(bigNametemp[0]);
                cb2.setText(bigNametemp[1]);
                cb3.setText(bigNametemp[2]);
                cb4.setText(bigNametemp[3]);
                cb5.setText(bigNametemp[4]);
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.VISIBLE);
                cb1.setVisibility(View.VISIBLE);
                cb2.setVisibility(View.VISIBLE);
                cb3.setVisibility(View.VISIBLE);
                cb4.setVisibility(View.VISIBLE);
                cb5.setVisibility(View.VISIBLE);
                cb6.setVisibility(View.INVISIBLE);
            }
            if (bigNametemp.length == 6) {
                cb1.setText(bigNametemp[0]);
                cb2.setText(bigNametemp[1]);
                cb3.setText(bigNametemp[2]);
                cb4.setText(bigNametemp[3]);
                cb5.setText(bigNametemp[4]);
                cb6.setText(bigNametemp[5]);
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.VISIBLE);
                cb1.setVisibility(View.VISIBLE);
                cb2.setVisibility(View.VISIBLE);
                cb3.setVisibility(View.VISIBLE);
                cb4.setVisibility(View.VISIBLE);
                cb5.setVisibility(View.VISIBLE);
                cb6.setVisibility(View.VISIBLE);
            }
            if (bigNametemp.length == 7) {
                cb1.setText(bigNametemp[0]);
                cb2.setText(bigNametemp[1]);
                cb3.setText(bigNametemp[2]);
                cb4.setText(bigNametemp[3]);
                cb5.setText(bigNametemp[4]);
                cb6.setText(bigNametemp[5]);
                cb7.setText(bigNametemp[6]);
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.VISIBLE);
                ll5.setVisibility(View.VISIBLE);
                cb1.setVisibility(View.VISIBLE);
                cb2.setVisibility(View.VISIBLE);
                cb3.setVisibility(View.VISIBLE);
                cb4.setVisibility(View.VISIBLE);
                cb5.setVisibility(View.VISIBLE);
                cb6.setVisibility(View.VISIBLE);
                cb7.setVisibility(View.VISIBLE);
            }
            if (bigNametemp.length == 8) {
                cb1.setText(bigNametemp[0]);
                cb2.setText(bigNametemp[1]);
                cb3.setText(bigNametemp[2]);
                cb4.setText(bigNametemp[3]);
                cb5.setText(bigNametemp[4]);
                cb6.setText(bigNametemp[5]);
                cb7.setText(bigNametemp[6]);
                cb8.setText(bigNametemp[7]);
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.VISIBLE);
                ll5.setVisibility(View.VISIBLE);
                cb1.setVisibility(View.VISIBLE);
                cb2.setVisibility(View.VISIBLE);
                cb3.setVisibility(View.VISIBLE);
                cb4.setVisibility(View.VISIBLE);
                cb5.setVisibility(View.VISIBLE);
                cb6.setVisibility(View.VISIBLE);
                cb7.setVisibility(View.VISIBLE);
                cb8.setVisibility(View.VISIBLE);
            }
            if (bigNametemp.length >= 9) {
                cb1.setText(bigNametemp[0]);
                cb2.setText(bigNametemp[1]);
                cb3.setText(bigNametemp[2]);
                cb4.setText(bigNametemp[3]);
                cb5.setText(bigNametemp[4]);
                cb6.setText(bigNametemp[5]);
                cb7.setText(bigNametemp[6]);
                cb8.setText(bigNametemp[7]);
                cb9.setText(bigNametemp[8]);
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.VISIBLE);
                ll5.setVisibility(View.VISIBLE);
                cb1.setVisibility(View.VISIBLE);
                cb2.setVisibility(View.VISIBLE);
                cb3.setVisibility(View.VISIBLE);
                cb4.setVisibility(View.VISIBLE);
                cb5.setVisibility(View.VISIBLE);
                cb6.setVisibility(View.VISIBLE);
                cb7.setVisibility(View.VISIBLE);
                cb8.setVisibility(View.VISIBLE);
                cb9.setVisibility(View.VISIBLE);
            }
        }
        getLastPerson();
    }

    @OnClick({R.id.btnUp, R.id.tvData, R.id.btnT,R.id.btnHistory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnHistory:
                recyclerView.setVisibility(View.VISIBLE);
                ProgressDialogUtil.startLoad(FlowBillWillDetailActivity.this,"获取数据中");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //String name =URLDecoder.decode(待转值,"utf-8");
                        String url = Constant.BASE_URL2 + Constant.FLOWMESSAGE;
                        DBHandler dbA = new DBHandler();
                        flowMessage = dbA.OAFlowMessage(url,runID);
                        if (flowMessage.equals("获取数据失败") || flowMessage.equals("")) {
                            handler.sendEmptyMessage(TAG_TWO);
                        } else {
                            handler.sendEmptyMessage(111);
                        }
                    }
                }).start();
                btnHistory.setEnabled(false);
                break;
            case R.id.btnT:
                btnTTag = "Y";
                if (beanList.size() != 0) {
                    if (beanList.size() == 1) {
                        ProgressDialogUtil.startLoad(FlowBillWillDetailActivity.this, "获取数据中");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DBHandler dbA = new DBHandler();
                                destType = beanList.get(0).getDestType();
                                if (destType.equals("decision") || destType.equals("fork") || destType.equals("join")) {
                                    handler.sendEmptyMessage(TAG_SIX);
                                } else if (destType.indexOf("end") == -1) {
                                    handler.sendEmptyMessage(TAG_FIVE);
                                } else {
                                    getLastPerson();
                                }
                                signaName = beanList.get(0).getName();
                                destName = beanList.get(0).getDestination();
                            }
                        }).start();
                    } else {
                        namelist.clear();
                        for (int i = 0; i < beanList.size(); i++) {
                            namelist.add(beanList.get(i).getDestination());
                        }
                        MyAlertDialog.MyListAlertDialog(FlowBillWillDetailActivity.this, namelist, new AlertDialogCallBackP() {
                            @Override
                            public void oneselect(final String data) {
                                ProgressDialogUtil.startLoad(FlowBillWillDetailActivity.this, "获取数据中");
                                destName = data;
                                for (int i = 0; i < beanList.size(); i++) {
                                    if (destName.equals(beanList.get(i).getDestination())) {
                                        signaName = beanList.get(i).getName();
                                        destType = beanList.get(i).getDestType();
                                    }
                                }
                                if (destType.equals("decision") || destType.equals("fork") || destType.equals("join")) {
                                    handler.sendEmptyMessage(TAG_SIX);
                                } else if (destType.indexOf("end") == -1) {
                                    handler.sendEmptyMessage(TAG_FIVE);
                                } else {
                                    getLastPerson();
                                }

                            }

                            @Override
                            public void select(List<String> list) {

                            }

                            @Override
                            public void confirm() {

                            }

                            @Override
                            public void cancel() {

                            }
                        });
                    }
                } else {
                    Toast.makeText(FlowBillWillDetailActivity.this, "审批人为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvData:
                List<String> dataList = new ArrayList<>();
                if (!xiangguanfujian.equals("")) {
                    dataList = new MyStringSpilt().stringSpiltList(xiangguanfujian);
                    if (dataList.size() == 1) {
                        String id = new MyStringSpilt().stringSpilt(dataList.get(0));
                        final String url = Constant.BASE_URL2 + Constant.FILEDATA + "?fileId=" + id;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DBHandler dbA = new DBHandler();
                                res = dbA.OAQingJiaMyDetail(url);
                                if (res.equals("获取数据失败") || res.equals("")) {
                                    handler.sendEmptyMessage(TAG_TWO);
                                } else {
                                    handler.sendEmptyMessage(TAG_NINE);
                                }
                            }
                        }).start();
                    } else if (dataList.size() > 1) {
                        MyAlertDialog.MyListAlertDialog(this, dataList, new AlertDialogCallBackP() {
                            @Override
                            public void oneselect(final String data1) {
                                String id = new MyStringSpilt().stringSpilt(data1);
                                final String url = Constant.BASE_URL2 + Constant.FILEDATA + "?fileId=" + id;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DBHandler dbA = new DBHandler();
                                        res = dbA.OAQingJiaMyDetail(url);
                                        if (res.equals("获取数据失败") || res.equals("")) {
                                            handler.sendEmptyMessage(TAG_TWO);
                                        } else {
                                            handler.sendEmptyMessage(TAG_NINE);
                                        }
                                    }
                                }).start();
                            }

                            @Override
                            public void select(List<String> list) {

                            }

                            @Override
                            public void confirm() {

                            }

                            @Override
                            public void cancel() {

                            }
                        });
                    }
                }
                break;
            case R.id.btnUp:
                Gson gson = new Gson();
                FlowBillWillDetail bean = gson.fromJson(res, FlowBillWillDetail.class);
                aqfwyj = bean.getMainform().get(0).getAqfwbyj();
                fgldyj = bean.getMainform().get(0).getFgldyj();
                ldspyj = bean.getMainform().get(0).getCwzjyj();
                userName = new SharedPreferencesHelper(FlowBillWillDetailActivity.this,
                        "login").getData(FlowBillWillDetailActivity.this, "userStatus", "");
                userCode = new SharedPreferencesHelper(FlowBillWillDetailActivity.this,
                        "login").getData(FlowBillWillDetailActivity.this, "userCode", "");
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                if (etLeader.getVisibility() == View.VISIBLE) {
                    comment = "";
                    comment = etLeader.getText().toString();
                    try {
                        jsonObject.put("ui", userCode);
                        jsonObject.put("un", userName);
                        jsonObject.put("c", str);
                        jsonObject.put("v", etLeader.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                    if (aqfwyj.equals("")) {
                        aqfwyj = jsonArray.toString();
                    } else {
                        aqfwyj = aqfwyj + "," + jsonArray.toString();
                        aqfwyj = aqfwyj.toString().replace("],[", ",");
                    }
                }
                if (etLeader1.getVisibility() == View.VISIBLE) {
                    comment = "";
                    comment = etLeader1.getText().toString();
                    try {
                        jsonObject.put("ui", userCode);
                        jsonObject.put("un", userName);
                        jsonObject.put("c", str);
                        jsonObject.put("v", etLeader1.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                    if (fgldyj.equals("")) {
                        fgldyj = jsonArray.toString();
                    } else {
                        fgldyj = fgldyj + "," + jsonArray.toString();
                        fgldyj = fgldyj.toString().replace("],[", ",");
                    }
                }
                if (etLeader2.getVisibility() == View.VISIBLE) {
                    comment = "";
                    comment = etLeader2.getText().toString();
                    try {
                        jsonObject.put("ui", userCode);
                        jsonObject.put("un", userName);
                        jsonObject.put("c", str);
                        jsonObject.put("v", etLeader2.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                    if (ldspyj.equals("")) {
                        ldspyj = jsonArray.toString();
                    } else {
                        ldspyj = ldspyj + "," + jsonArray.toString();
                        ldspyj = ldspyj.toString().replace("],[", ",");
                    }
                }
                if (comment.equals("")) {
                    if (!aqreout.equals("2") && !fgreout.equals("2") && !ldspreout.equals("2")) {
                        comment = "";
                        personSession();
                    } else if (!aqfwyj.equals("") && !fgldyj.equals("") && !ldspyj.equals("")) {
                        comment = "";
                        personSession();
                    } else {
                        Toast.makeText(this, "请签字", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    personSession();
                }
                break;
        }
    }

    private void personSession() {
        if (bigResultList.size() != 0) {
            sendData();
        } else {
            if (btnTTag.equals("N")) {
                Gson gson = new Gson();
                FlowBillWillDetail bean = gson.fromJson(res, FlowBillWillDetail.class);
                aqfwyj = bean.getMainform().get(0).getAqfwbyj();
                fgldyj = bean.getMainform().get(0).getFgldyj();
                ldspyj = bean.getMainform().get(0).getCwzjyj();
                Toast.makeText(this, "请点击加号选择路径", Toast.LENGTH_SHORT).show();
            } else {
                sendData();
            }
        }
    }

    private void sendData() {
        ProgressDialogUtil.startLoad(FlowBillWillDetailActivity.this, "提交数据中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String zdPerson = tvZDPerson.getText().toString();
                final String zdTime = tvZDTime.getText().toString();
                final String department = tvDepartment.getText().toString();
                final String JKPerson = tvJKPerson.getText().toString();
                final String driver = tvDriver.getText().toString();
                final String carNo = tvCarNo.getText().toString();
                final String address = tvAddress.getText().toString();
                final String use = tvUse.getText().toString();
                final String otherPerson = tvOtherPerson.getText().toString();
                final String otherPhone = tvOtherPhone.getText().toString();
                final String reason = tvReason.getText().toString();
                final String smallMoney = tvSmallMoney.getText().toString();
                final String bigMoney = tvBigMoney.getText().toString();
                final String type = tvType.getText().toString();
                final String blame = tvBlame.getText().toString();

                final String num = tvNum.getText().toString();
                final String money = tvMoney.getText().toString();
                final String allMoneyM = tvAllMoneyM.getText().toString();
                final String allmoneyY = tvAllMoneyY.getText().toString();
                final String userCode = new SharedPreferencesHelper(FlowBillWillDetailActivity.this,
                        "login").getData(FlowBillWillDetailActivity.this, "userCode", "");
                if (!rb6.isChecked()) {
                    resultList.remove(5);
                }
                if (!rb5.isChecked()) {
                    resultList.remove(4);
                }
                if (!rb4.isChecked()) {
                    resultList.remove(3);
                }
                if (!rb3.isChecked()) {
                    resultList.remove(2);
                }
                if (!rb2.isChecked()) {
                    resultList.remove(1);
                }
                if (!rb1.isChecked()) {
                    resultList.remove(0);
                }

                if (!cb9.isChecked()) {
                    bigResultList.remove(8);
                }
                if (!cb8.isChecked()) {
                    bigResultList.remove(7);
                }
                if (!cb7.isChecked()) {
                    bigResultList.remove(6);
                }
                if (!cb6.isChecked()) {
                    bigResultList.remove(5);
                }
                if (!cb5.isChecked()) {
                    bigResultList.remove(4);
                }
                if (!cb4.isChecked()) {
                    bigResultList.remove(3);
                }
                if (!cb3.isChecked()) {
                    bigResultList.remove(2);
                }
                if (!cb2.isChecked()) {
                    bigResultList.remove(1);
                }
                if (!cb1.isChecked()) {
                    bigResultList.remove(0);
                }

                String userCodes = resultList.toString();
                userCodes = userCodes.toString().replace("[", "");
                userCodes = userCodes.toString().replace("]", "");

                String bigUserCodes = bigResultList.toString();
                bigUserCodes = bigUserCodes.toString().replace("[", "");
                bigUserCodes = bigUserCodes.toString().replace("]", "");

                if (!bigUserCodes.equals("") && !userCodes.equals("")) {
                    flowAssignld = leader + ":" + role + "|" + bigUserCodes + ":" + userCodes;
                    flowAssignld = flowAssignld.replace(" ", "");
                    flowAssignld = flowAssignld.replace(":|", "|");
                } else if (!bigUserCodes.equals("") && userCodes.equals("")) {
                    flowAssignld = leader + ":" + role + "|" + bigUserCodes;
                    flowAssignld = flowAssignld.replace(" ", "");
                    flowAssignld = flowAssignld.replace(":|", "|");
                } else {
                    flowAssignld = destName + "|" + userCodes;
                    flowAssignld = flowAssignld.replace(" ", "");
                    flowAssignld = flowAssignld.replace(":|", "|");
                    flowAssignld = flowAssignld.replace(":", "");
                }
                String url = Constant.BASE_URL2 + Constant.EXAMINEDATA;
                DBHandler dbA = new DBHandler();

                String upData = dbA.OABillder1(url, zdPerson, zdTime, department, JKPerson, driver, carNo,
                        address, use, otherPerson, otherPhone, reason, smallMoney, bigMoney, type, blame,
                        num, money, allMoneyM, allmoneyY, userCode, destName, taskId, flowAssignld,
                        mainId, aqfwyj, fgldyj, ldspyj, serialNumber, comment, liushuihao, shiJian);
                if (upData.equals("")) {
                    handler.sendEmptyMessage(TAG_THERE);
                } else {
                    handler.sendEmptyMessage(TAG_FOUR);
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 111:
                    Gson gsonF = new Gson();
                    FlowMessage1 beanF = gsonF.fromJson(flowMessage, FlowMessage1.class);
                    for (int i = 0;i<beanF.getData().size();i++){
                        flowList.add(beanF.getData().get(i));
                    }
                    adapter = new FlowMessageAdapter(FlowBillWillDetailActivity.this,flowList);
                    recyclerView.setAdapter(adapter);
                    ProgressDialogUtil.stopLoad();
                    break;
                case TAG_ONE:
                    Gson gson = new Gson();
                    FlowBillWillDetail bean = gson.fromJson(res, FlowBillWillDetail.class);
                    taskId = bean.getTaskId();
                    final String zdPerson = bean.getMainform().get(0).getZhiDan();
                    final String zdTime = bean.getMainform().get(0).getZhidanshijian();
                    final String department = bean.getMainform().get(0).getBuMen();
                    final String jkPerson = bean.getMainform().get(0).getJieKuanRen();
                    final String driver = bean.getMainform().get(0).getJiaShiYuan();
                    final String carNo = bean.getMainform().get(0).getCheHao();
                    final String address = bean.getMainform().get(0).getDiDian();
                    final String use = bean.getMainform().get(0).getJieKuanYT();
                    final String otherPerson = bean.getMainform().get(0).getShouShangRenShu();
                    final String otherPhone = bean.getMainform().get(0).getSanZheDH();
                    final String reason = bean.getMainform().get(0).getShiGuYuanYin();
                    final String bigMoney = bean.getMainform().get(0).getJine();
                    final String smallMoney = bean.getMainform().get(0).getXiaoxie();
                    final String type = bean.getMainform().get(0).getXianZhong();
                    final String blam = bean.getMainform().get(0).getShiGuZeRen();
                    final String num = bean.getMainform().get(0).getJiCi();
                    final String money = bean.getMainform().get(0).getYiJie();
                    final String allmoneyM = bean.getMainform().get(0).getDanyueleiji();
                    final String allmoneyY = bean.getMainform().get(0).getBennianleiji();
                    liushuihao = bean.getMainform().get(0).getLiuShuiHao();
                    shiJian = bean.getMainform().get(0).getShiJian();
                    xiangguanfujian = bean.getMainform().get(0).getXiangguanfujian();
                    runID = bean.getMainform().get(0).getRunId();
                    tvData.setText(xiangguanfujian);

                    aqfwyj = bean.getMainform().get(0).getAqfwbyj();
                    fgldyj = bean.getMainform().get(0).getFgldyj();
                    ldspyj = bean.getMainform().get(0).getCwzjyj();
                    tvZDPerson.setText(zdPerson);
                    tvZDTime.setText(zdTime);
                    tvDepartment.setText(department);
                    tvJKPerson.setText(jkPerson);
                    tvDriver.setText(driver);
                    tvCarNo.setText(carNo);
                    tvAddress.setText(address);
                    tvUse.setText(use);
                    tvOtherPerson.setText(otherPerson);
                    tvOtherPhone.setText(otherPhone);
                    tvReason.setText(reason);
                    tvSmallMoney.setText(smallMoney);
                    tvBigMoney.setText(bigMoney);
                    tvType.setText(type);
                    tvBlame.setText(blam);
                    tvNum.setText(num);
                    tvMoney.setText(money);
                    tvAllMoneyM.setText(allmoneyM);
                    tvAllMoneyY.setText(allmoneyY);

                    mainId = String.valueOf(bean.getMainform().get(0).getMainId());
                    for (int i = 0; i < bean.getTrans().size(); i++) {
                        beanList.add(bean.getTrans().get(i));
                    }
                    ProgressDialogUtil.stopLoad();

                    String formRights = bean.getFormRights();
                    try {
                        JSONObject jsonObject = new JSONObject(formRights);
                        aqreout = jsonObject.getString("aqfwbyj");
                        fgreout = jsonObject.getString("fgldyj");
                        ldspreout = jsonObject.getString("cwzjyj");
                        if (aqreout.equals("2")) {
                            tvLeader.setVisibility(View.GONE);
                            etLeader.setVisibility(View.VISIBLE);
                        } else {
                            tvLeader.setVisibility(View.VISIBLE);
                            etLeader.setVisibility(View.GONE);
                        }
                        if (fgreout.equals("2")) {
                            tvLeader1.setVisibility(View.GONE);
                            etLeader1.setVisibility(View.VISIBLE);
                        } else {
                            tvLeader1.setVisibility(View.VISIBLE);
                            etLeader1.setVisibility(View.GONE);
                        }
                        if (ldspreout.equals("2")) {
                            tvLeader2.setVisibility(View.GONE);
                            etLeader2.setVisibility(View.VISIBLE);
                        } else {
                            tvLeader2.setVisibility(View.VISIBLE);
                            etLeader2.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (ldspyj != null && !ldspyj.equals("")) {
                        if (tvLeader2.getVisibility() == View.VISIBLE) {
                            String word3 = "";
                            try {
                                JSONArray jsonArray = new JSONArray(ldspyj);
                                JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);
                                word3 = jsonObject.getString("v") + "\u3000" + jsonObject.getString("un") + ":" + jsonObject.getString("c");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            tvLeader2.setText(word3);
                        }
                    }

                    if (fgldyj != null && !fgldyj.equals("")) {
                        if (tvLeader1.getVisibility() == View.VISIBLE) {
                            String word2 = "";
                            try {
                                JSONArray jsonArray = new JSONArray(fgldyj);
                                JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);
                                word2 = jsonObject.getString("v") + "\u3000" + jsonObject.getString("un") + ":" + jsonObject.getString("c");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            tvLeader1.setText(word2);
                        }
                    }

                    if (aqfwyj != null && !aqfwyj.equals("")) {
                        if (tvLeader.getVisibility() == View.VISIBLE) {
                            String word1 = "";
                            try {
                                JSONArray jsonArray = new JSONArray(aqfwyj);
                                JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);
                                word1 = jsonObject.getString("v") + "\u3000" + jsonObject.getString("un") + ":" + jsonObject.getString("c");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            tvLeader.setText(word1);
                        }
                    }
                    break;
                case TAG_TWO:
                    Toast.makeText(FlowBillWillDetailActivity.this, "操作数据失败", Toast.LENGTH_SHORT).show();
                    ProgressDialogUtil.stopLoad();
                    break;
                case TAG_THERE:
                    Toast.makeText(FlowBillWillDetailActivity.this, "提交数据成功", Toast.LENGTH_SHORT).show();
                    ProgressDialogUtil.stopLoad();
                    finish();
                    break;
                case TAG_FOUR:
                    Toast.makeText(FlowBillWillDetailActivity.this, "提交数据失败", Toast.LENGTH_SHORT).show();
                    ProgressDialogUtil.stopLoad();
                    break;
                case TAG_FIVE:
                    getNoEndPerson();
                    break;
                case TAG_SIX:
                    getNextPerson();
                    break;
                case TAG_SEVEN:
                    bigNametemp = null;
                    bigCodetemp = null;
                    Gson gson1 = new Gson();
                    Log.e("XXXXH", res);
                    FlowContractPerson bean1 = gson1.fromJson(qianzhiData, FlowContractPerson.class);
                    if (bean1.getData() != null) {
                        if (bean1.getData().size() > 1) {
                            role = bean1.getData().get(1).getRole();
                            userCode = bean1.getData().get(1).getUserNames();
                            userName = bean1.getData().get(1).getUserCodes();
                            nametemp = userName.split(",");
                            codetemp = userCode.split(",");
                        }
                        leader = bean1.getData().get(0).getRole();
                        leaderCode = bean1.getData().get(0).getUserNames();
                        leaderName = bean1.getData().get(0).getUserCodes();
                        bigNametemp = leaderName.split(",");
                        bigCodetemp = leaderCode.split(",");
                    }
                    setCbRbVer();
                    break;
                case TAG_EIGHT:
                    try {
                        JSONObject jsonObject = new JSONObject(checkTask);
                        if (!jsonObject.isNull("assigned")) {
                            assigned = true;
                        } else {
                            assigned = jsonObject.getBoolean("assigned");
                        }
                        tag = "end";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ProgressDialogUtil.stopLoad();
                    break;
                case TAG_NINE:
                    Gson gson2 = new Gson();
                    File file = gson2.fromJson(res, File.class);
                    String filePath = file.getData().getFilePath();
                    String url = Constant.FIELDETAIL + filePath;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    break;
            }
        }
    };

}
