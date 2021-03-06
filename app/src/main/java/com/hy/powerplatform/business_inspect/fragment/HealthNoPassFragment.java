package com.hy.powerplatform.business_inspect.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hy.powerplatform.R;
import com.hy.powerplatform.SharedPreferencesHelper;
import com.hy.powerplatform.business_inspect.activity.InspectDriveSelectTypeActivity;
import com.hy.powerplatform.business_inspect.activity.InspectLineSelectTypeActivity;
import com.hy.powerplatform.business_inspect.activity.SelectOneCarActivity;
import com.hy.powerplatform.business_inspect.adapter.ExpandableStartNoPassAdapter;
import com.hy.powerplatform.business_inspect.adapter.InspectStarteAdapter;
import com.hy.powerplatform.business_inspect.adapter.SpinnerAdapter;
import com.hy.powerplatform.business_inspect.bean.CarCode;
import com.hy.powerplatform.business_inspect.bean.InspectDrive;
import com.hy.powerplatform.business_inspect.bean.InspectHealth;
import com.hy.powerplatform.business_inspect.bean.InspectLine;
import com.hy.powerplatform.business_inspect.bean.ShowLine;
import com.hy.powerplatform.business_inspect.presenter.CarCodePresenter;
import com.hy.powerplatform.business_inspect.presenter.InspectDrivePresenter;
import com.hy.powerplatform.business_inspect.presenter.InspectHealthPresenter;
import com.hy.powerplatform.business_inspect.presenter.InspectLinePresenter;
import com.hy.powerplatform.business_inspect.presenter.carcodepresenterimpl.CarCodePresenterimpl;
import com.hy.powerplatform.business_inspect.presenter.carcodepresenterimpl.InspectDrivePresenterimpl;
import com.hy.powerplatform.business_inspect.presenter.carcodepresenterimpl.InspectHealthPresenterimpl;
import com.hy.powerplatform.business_inspect.presenter.carcodepresenterimpl.InspectLinePresenterimpl;
import com.hy.powerplatform.business_inspect.utils.DBHandler;
import com.hy.powerplatform.business_inspect.utils.GetDataThread;
import com.hy.powerplatform.business_inspect.view.CarCodeView;
import com.hy.powerplatform.business_inspect.view.InspectDriveView;
import com.hy.powerplatform.business_inspect.view.InspectHealthView;
import com.hy.powerplatform.business_inspect.view.InspectLineView;
import com.hy.powerplatform.database.DbManager;
import com.hy.powerplatform.database.MyDatabaseHelper;
import com.hy.powerplatform.my_utils.base.Constant;
import com.hy.powerplatform.my_utils.base.MyApplication;
import com.hy.powerplatform.my_utils.myViews.EditTextChange;
import com.hy.powerplatform.my_utils.utils.ProgressDialogUtil;
import com.hy.powerplatform.my_utils.utils.time_select.CustomDatePickerDay;
import com.hy.powerplatform.select_photo.ImagesSelectorActivity;
import com.hy.powerplatform.select_photo.SelectorSettings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.hy.powerplatform.my_utils.base.Constant.TAG_ONE;

/**
 * Created by dell on 2017/8/1.
 */

public class HealthNoPassFragment extends Fragment implements CarCodeView, InspectLineView
        , InspectDriveView, InspectHealthView, InspectStarteAdapter.MyClickListener {
    View view;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.imTimeSelect)
    ImageView imTimeSelect;
    @BindView(R.id.etSelectLine)
    EditText etSelectLine;
    @BindView(R.id.imLineSelect)
    ImageView imLineSelect;
    @BindView(R.id.etSelectCar)
    EditText etSelectCar;
    @BindView(R.id.imCarSelect)
    ImageView imCarSelect;
    @BindView(R.id.etSelectDriver)
    EditText etSelectDriver;
    @BindView(R.id.imDriverSelect)
    ImageView imDriverSelect;
    @BindView(R.id.etSelectAddress)
    EditText etSelectAddress;
    @BindView(R.id.etSelectWeather)
    EditText etSelectWeather;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;
    Unbinder unbinder;

    SQLiteDatabase db;
    MyDatabaseHelper helper;
    Cursor cursor;
    String carName, selectTag;
    SpinnerAdapter spinnerAdapter;
    InspectStarteAdapter adapter;
    CarCodePresenter carCodePresenter;
    InspectLinePresenter inspectLinePresenter;
    InspectDrivePresenter inspectDrivePresenter;
    InspectHealthPresenter inspectHealthPresenter;
    int tag;
    File tmpDir;
    String carNo;
    String groupPosition1;
    String childPosition1;
    String groupPosition;
    String childPosition;
    String dirPath = "temp";
    String busCode, carLine, carDrive, carAddress, userCode;
    SharedPreferencesHelper sharecPreferencesHelper;

    private ArrayList<String> mResults = new ArrayList<>();//图片list
    List<CarCode.DataBean> listCarCode = new ArrayList<>();
    List<CarCode.DataBean> listCarCodeTest = new ArrayList<>();//判断数据库是否有车牌号信息
    List<InspectLine.DataBean> listInspectLine = new ArrayList<>();//获取线路网络数据
    List<ShowLine> listInspectLineTest = new ArrayList<>();//获取线路数据库数据
    List<InspectDrive.DataBean> listInspectDrive = new ArrayList<>();//获取驾驶员网络数据
    List<InspectDrive.DataBean> listInspectDriveTest = new ArrayList<>();//获取驾驶员数据库数据
    List<String> smallTypeList01 = new ArrayList<String>();//子项list
    List<String> smallTypeList02 = new ArrayList<String>();//子项list
    //检查小项
    List<List<String>> smallTypeList = new ArrayList<>();
    List<String> bigTypeList = new ArrayList<>();
    List<String> typeList = new ArrayList<>();  //检查结果
    List<InspectHealth.ResultBean> inspectHealthList = new ArrayList<>();
    private CustomDatePickerDay customDatePicker1, customDatePicker2;
    //图片list
    List<Map<String, String>> imageList = new ArrayList<>();
    List<Map<String, String>> imageList1 = new ArrayList<>();
    private Map<String, String> map_project = new HashMap<String, String>();
    private Map<String, String> map_project_ = new HashMap<String, String>();
    private Map<String, String> map_photoName = new HashMap<String, String>();
    private Map<String, String> map_photoName_ = new HashMap<String, String>();
    private Map<String, String> map_beizhu = new HashMap<String, String>();
    private Map<String, String> map_beizhu_ = new HashMap<String, String>();
    //储存不通过i;
    List<String> typeNoList = new ArrayList<String>();
    ExpandableStartNoPassAdapter expandableListViewAdapter;
    Intent intent;
    int size = 0;
    SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(MyApplication.getContextObject(),"login");
    String found = sharedPreferencesHelper.getData(MyApplication.getContextObject(),"Found","");
    String BASE_URL="http://"+sharedPreferencesHelper.getData(MyApplication.getContextObject(),"Ip","")
            +":"+sharedPreferencesHelper.getData(MyApplication.getContextObject(),"Socket","")+"/"+found+"/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_health_pass, container, false);
        unbinder = ButterKnife.bind(this, view);
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        expandableListView.setIndicatorBounds(width - 100, width);
        sharecPreferencesHelper = new SharedPreferencesHelper(getActivity(), "login");
        helper = DbManager.getInstance(MyApplication.getContextObject());
        db = helper.getReadableDatabase();
        carCodePresenter = new CarCodePresenterimpl(this, getActivity());
        inspectLinePresenter = new InspectLinePresenterimpl(this, getActivity());
        inspectDrivePresenter = new InspectDrivePresenterimpl(this, getActivity());
        inspectHealthPresenter = new InspectHealthPresenterimpl(this, getActivity());
        inspectHealthPresenter.getInspectHealthPresenterData();
        //设置Edittext
        new EditTextChange(etSelectLine).changeText();
        new EditTextChange(etSelectCar).changeText();
        new EditTextChange(etSelectDriver).changeText();
        new EditTextChange(etSelectAddress).changeText();
        //选择时间
        initDatePicker();
        //判断数据库是否有车牌号信息
        decideDbForCarCodeData();
        //判断数据库是否有线路信息
        decideDbForInspectLineData();
        //判断数据库是否有驾驶员信息
        decideDbForInspectDriveData();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tvTime, R.id.imLineSelect, R.id.imCarSelect, R.id.imDriverSelect, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvTime:
                customDatePicker1.show(tvTime.getText().toString());
                break;
            case R.id.imLineSelect:
                String etTextLine = etSelectLine.getText().toString().trim();
                intent = new Intent(getActivity(), InspectLineSelectTypeActivity.class);
                intent.putExtra("condition", etTextLine);
                startActivityForResult(intent, Constant.TAG_THERE);
                break;
            case R.id.imCarSelect:
                String etText = etSelectCar.getText().toString().trim();
                intent = new Intent(getActivity(), SelectOneCarActivity.class);
                intent.putExtra("condition", etText);
                startActivityForResult(intent, Constant.TAG_TWO);
                break;
            case R.id.imDriverSelect:
                String etTextDrive = etSelectDriver.getText().toString().trim();
                intent = new Intent(getActivity(), InspectDriveSelectTypeActivity.class);
                intent.putExtra("condition", etTextDrive);
                startActivityForResult(intent, Constant.TAG_FOUR);
                break;
            case R.id.btn:
                carNo = etSelectCar.getText().toString();
                carLine = etSelectLine.getText().toString();
                carDrive = etSelectDriver.getText().toString();
                carAddress = etSelectAddress.getText().toString();
                if (carNo.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入车牌号", Toast.LENGTH_SHORT).show();
                } else if (carLine.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入线路", Toast.LENGTH_SHORT).show();
                } else if (carDrive.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入驾驶员", Toast.LENGTH_SHORT).show();
                } else if (carDrive.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入区段", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialogUtil.startLoad(getActivity(), Constant.UPRESULT);
                    String sqlR = "select * from carcode where name = '" + etSelectCar.getText().toString() + "'";// order by random() limit 100
                    cursor = DbManager.queryBySQL(db, sqlR, null);
                    listCarCodeTest = DbManager.cursorToClassCar(cursor);
                    //busCode = listCarCodeTest.get(0).getValue();
                    String sqlH = "select * from inspectDrive where driverName = '" + etSelectDriver.getText().toString() + "'";// order by random() limit 100
                    Cursor cursor1;
                    cursor1 = DbManager.queryBySQL(db, sqlH, null);
                    //listInspectDriveTest = DbManager.cursorToInspectDrive(cursor1);
                    //userCode = listInspectDriveTest.get(0).getDriverId();
                    sendHealthData();
                }
                break;
        }
    }

    /**
     * 图片选择权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constant.TAG_ONE:
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getActivity(), ImagesSelectorActivity.class);
                    intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
                    intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                    intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                    startActivityForResult(intent, Constant.TAG_ONE);
                } else {
                    Toast.makeText(getActivity(), "权限被拒绝，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.TAG_ONE:
                if (resultCode == RESULT_OK) {
                    mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                    assert mResults != null;

                    // show results in textview
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format("Totally %d images selected:", mResults.size())).append("\n");
                    for (String result : mResults) {
                        sb.append(result).append("\n");
                    }
                }
                BitmapFactory.Options opts = new BitmapFactory.Options();//获取缩略图显示到屏幕上
                opts.inSampleSize = 2;
                if (mResults.size() != 0) {
                    Bitmap cbitmap01 = BitmapFactory.decodeFile(mResults.get(0), opts);
                    //图片存储到本地
                    saveImageToSD(cbitmap01, "temp");
                }
                break;
            case Constant.TAG_TWO:
                if (resultCode == Constant.TAG_THERE) {
                    carName = data.getStringExtra("bian");
                    busCode = data.getStringExtra("bianId");
                    etSelectCar.setText(carName);
                }
                break;
            case Constant.TAG_THERE:
                if (resultCode == Constant.TAG_FOUR) {
                    String line = data.getStringExtra("bianCode");
                    etSelectLine.setText(line);
                }
                break;
            case Constant.TAG_FOUR:
                if (resultCode == Constant.TAG_FIVE) {
                    String line = data.getStringExtra("bian");
                    userCode = data.getStringExtra("bianDriver");
                    etSelectDriver.setText(line);
                }
                break;
        }
    }

    /**
     * 图片保存到本地
     *
     * @param cbitmap01
     * @param dirPath
     */
    private void saveImageToSD(Bitmap cbitmap01, String dirPath) {
        //新建文件夹用于存放裁剪后的图片
        tmpDir = new File(Environment.getExternalStorageDirectory() + "/" + dirPath);
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }

        //新建文件存储裁剪后的图片
        File img = new File(tmpDir.getAbsolutePath() + "/" + String.valueOf(size) + ".png");
        try {
            //打开文件输出流
            FileOutputStream fos = new FileOutputStream(img);
            //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
            cbitmap01.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            //刷新输出流
            fos.flush();
            //关闭输出流
            fos.close();
            //返回File类型的Uri
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //getImageFromSD(dirPath);
        Map<String, String> map = new HashMap<>();
        map.put(String.valueOf(size), String.valueOf(size) + ".png");
        imageList.add(map);
        imageList1.add(map);
    }

    /**
     * 选择时间
     */
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        tvTime.setText(now.split(" ")[0]);
        //currentTime.setText(now);

        customDatePicker1 = new CustomDatePickerDay(getActivity(), new CustomDatePickerDay.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvTime.setText(time.split(" ")[0]);
            }
        }, "2000-01-01 00:00", "2030-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动

        customDatePicker2 = new CustomDatePickerDay(getActivity(), new CustomDatePickerDay.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvTime.setText(time);
            }
        }, "2000-01-01 00:00", "2030-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }

    /**
     * 判断数据库是否有车牌号信息
     */
    private void decideDbForCarCodeData() {
        //查询数据
        String sqlR = "select * from carcode order by random() limit '" + 10 + "'";// order by random() limit 100
        cursor = DbManager.queryBySQL(db, sqlR, null);
        listCarCodeTest = DbManager.cursorToClassCar(cursor);
        if (listCarCodeTest.size() != 0) {
        } else {
            carCodePresenter.getCarCodePresenterData();
        }
    }

    /**
     * 判断数据库是否有线路信息
     */
    private void decideDbForInspectLineData() {
        //查询数据
        Cursor cursor;
        String sqlR = "select * from inspectLine order by random() limit '" + 1 + "'";// order by random() limit 100
        cursor = DbManager.queryBySQL(db, sqlR, null);
        listInspectLineTest = DbManager.cursorToInspectLine(cursor);
        if (listInspectLineTest.size() != 0) {
        } else {
            inspectLinePresenter.getInspectLinePresenterData();
        }
    }

    /**
     * 判断数据库是否有驾驶员信息
     */
    private void decideDbForInspectDriveData() {
        //查询数据
        Cursor cursor;
        String sqlR = "select * from inspectDrive order by random() limit '" + 1 + "'";// order by random() limit 100
        cursor = DbManager.queryBySQL(db, sqlR, null);
        //listInspectDriveTest = DbManager.cursorToInspectDrive(cursor);
        if (listInspectDriveTest.size() != 0) {
        } else {
            inspectDrivePresenter.getInspectDrivePresenterData();
        }
    }


    /**
     * 车牌号回调
     *
     * @param carCode
     */
    @Override
    public void getCarCodeViewData(CarCode carCode) {
        GetDataThread.saveCarCoodeData(carCode, db, helper, listCarCode);
    }

    /**
     * 驾驶人员回掉
     *
     * @param inspectDrive
     */
    @Override
    public void getInspectDriveViewData(InspectDrive inspectDrive) {
        GetDataThread.saveInspectDriveData(inspectDrive, db, helper, listInspectDrive);
    }

    @Override
    public void getInspectHealthViewData(InspectHealth inspectHealth) {
        for (int i = 0; i < inspectHealth.getResult().size(); i++) {
            inspectHealthList.add(inspectHealth.getResult().get(i));
            if (!bigTypeList.contains(inspectHealth.getResult().get(i).getType())) {
                bigTypeList.add(inspectHealth.getResult().get(i).getType());
            }
        }
        for (int j = 0; j < bigTypeList.size(); j++) {
            smallTypeList01 = new ArrayList<>();
            for (int k = 0; k < inspectHealth.getResult().size(); k++) {
                if (bigTypeList.get(j).equals(inspectHealth.getResult().get(k).getType())) {
                    smallTypeList01.add(inspectHealth.getResult().get(k).getProjectName());
                    smallTypeList02.add(inspectHealth.getResult().get(k).getProjectName());
                }
            }
            smallTypeList.add(smallTypeList01);
        }
        expandableListViewAdapter = new ExpandableStartNoPassAdapter(MyApplication.getContextObject(), bigTypeList, smallTypeList, handler);
        expandableListView.setAdapter(expandableListViewAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                setChildViewHeight(expandableListView, groupPosition, true);
            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                setChildViewHeight(expandableListView, groupPosition, false);
            }
        });
    }

    /**
     * 设置组的子View的高度
     *
     * @param listView
     * @param groupPosition
     * @param isExpanded
     */
    public static void setChildViewHeight(ExpandableListView listView, int groupPosition, Boolean isExpanded) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int childTotalHeight = 0;
        View child = null;
        for (int i = 0; i < listAdapter.getChildrenCount(groupPosition); i++) {
            child = listAdapter.getChildView(groupPosition, i, false, null, listView);
            child.measure(0, 0);
            childTotalHeight += child.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        if (isExpanded) {//展开状态，增加高度
            layoutParams.height += childTotalHeight;
        } else {//收起状态，减掉二阶子项高度
            layoutParams.height -= childTotalHeight;
        }
        listView.setLayoutParams(layoutParams);
    }

    /**
     * 线路回调
     *
     * @param inspectLine
     */
    @Override
    public void getInspectLineViewData(InspectLine inspectLine) {
        GetDataThread.saveInspectLineData(inspectLine, db, helper, listInspectLine);
    }

    @Override
    public void clickListener(View v) {
        tag = (int) v.getTag();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    TAG_ONE);
        } else {
            Intent intent = new Intent(getActivity(), ImagesSelectorActivity.class);
            intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
            intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
            intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
            startActivityForResult(intent, TAG_ONE);
        }
    }

    /**
     * 向服务器发送数据
     */
    private void sendHealthData() {
        for (int j = 0; j < smallTypeList02.size(); j++) {
            typeList.add("0");
        }
        for (int j = 0; j < smallTypeList02.size(); j++) {
            for (int i = 0; i < typeNoList.size(); i++) {
                if (j == Integer.valueOf(typeNoList.get(i))) {
                    typeList.set(j, "1");
                }
            }
        }
        final AsyncHttpClient client = new AsyncHttpClient();
        final String url = BASE_URL + "starkh/upLoadImageVehicleHealthInspection.do";
        //http://221.195.69.109:7080/joffice/starkh/upLoadImageYoufushifancheKaohe.do
        for (int i = 0; i < smallTypeList02.size(); i++) {
            map_beizhu.put(smallTypeList02.get(i), "");
            map_project.put(smallTypeList02.get(i), typeList.get(i));
            Random rand = new Random();
            if (imageList.size() != 0) {
                for (int j = 0; j < imageList.size(); j++) {
                    for (String key : imageList.get(j).keySet()) {
                        if (!Integer.valueOf(key).equals(i)) {
                            String tag = smallTypeList02.get(i);
                            boolean contains = map_photoName.containsKey(tag);
                            if (contains) {         //如果条件为真
                            } else {
                                map_photoName.put(smallTypeList02.get(i), "");
                            }
                        } else {
//                            String filepath = imageList.get(j).get(key);
//                            String name = filepath.substring(0, filepath.lastIndexOf("."))
//                                    + System.currentTimeMillis() + rand.nextInt(10) + filepath.substring(filepath.lastIndexOf("."));
//                            imageList1.get(j).put(key,name);
//                            map_photoName.put(smallTypeList02.get(i), name);
//                            final RequestParams params = new RequestParams();
//                            Filed file = new Filed(Environment.getExternalStorageDirectory() + "/" + dirPath + "/" + imageList.get(j).get(key));
//                            String sdpath = Environment.getExternalStorageDirectory()
//                                    + "/" + dirPath;// 获取sdcard的根路径
                            final RequestParams params = new RequestParams();
                            File file = new File(Environment.getExternalStorageDirectory() + "/" + dirPath + "/" + imageList.get(j).get(key));
                            String sdpath = Environment.getExternalStorageDirectory()
                                    + "/" + dirPath;// 获取sdcard的根路径
                            String filepath = imageList.get(j).get(key);
                            String name = filepath.substring(0, filepath.lastIndexOf("."))
                                    +System.currentTimeMillis()+rand.nextInt(10)+filepath.substring(filepath.lastIndexOf("."));
                            imageList1.get(j).put(key,name);
                            try {
                                params.put("upload", file);
                                params.put("fullname", name);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // 执行post请求
                                    client.post(url, params, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int arg0, String arg1) {
                                            super.onSuccess(arg0, arg1);
                                            Log.i("XXX", "XXX");
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                            super.onFailure(statusCode, headers, responseBody, error);
                                            Log.i("XXX", "XXXXX");
                                        }
                                    });
                                }
                            },2500);
                        }
                    }
                }
            } else {
                map_photoName.put(smallTypeList02.get(i), "");
            }
            map_photoName_.putAll(map_photoName);
            map_project_.putAll(map_project);
            map_beizhu_.putAll(map_beizhu);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBHandler dbH = new DBHandler();
                String turl = BASE_URL + "starkh/mobileSaveVehicleHealthInspection.do";
                String userName = sharecPreferencesHelper.getData(getActivity(), "userName", "");
                //获取当前时间
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                Date date = new Date(System.currentTimeMillis());
                String InspectionDate = tvTime.getText().toString();
                String kaoheDate = String.valueOf(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm:ss");
                String InspectionTime = simpleDateFormatTime.format(date);
                String currency = carDrive;
                String userCode_ = userCode;
                String busCode_ = busCode;
                String rode = "1";
                String note = "";
                String mfbjf = "fa";
                String section_ = carAddress;
                String weather_ = etSelectWeather.getText().toString();
                String res = dbH.fuwuweishengCommitValue(turl, userName, InspectionDate, InspectionTime,
                        currency, userCode_, busCode_, rode, section_, weather_, mfbjf, note, map_project_,
                        map_photoName_, map_beizhu_);
                ProgressDialogUtil.stopLoad();
                if (res.equals("")) {
                    handler.sendEmptyMessage(Constant.TAG_ONE);
                } else {
                    handler.sendEmptyMessage(Constant.TAG_TWO);
                }

            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.TAG_ONE:
                    Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.TAG_TWO:
                    Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.TAG_THERE:
                    Bundle bundle = msg.getData();
                    groupPosition = bundle.getString("groupPosition");
                    childPosition = bundle.getString("childPosition");
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                TAG_ONE);
                    } else {
                        Intent intent = new Intent(getActivity(), ImagesSelectorActivity.class);
                        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
                        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                        startActivityForResult(intent, TAG_ONE);
                    }
                    break;
                case Constant.TAG_FOUR:
                    size = 0;
                    Bundle bundle1 = msg.getData();
                    String groupPosition1 = bundle1.getString("groupPosition");
                    String childPosition1 = bundle1.getString("childPosition");
                    if (!groupPosition1.equals("0")) {
                        for (int i = 0; i < Integer.valueOf(groupPosition1); i++) {
                            size = size + (smallTypeList.get(i).size());
                        }
                        size = size + Integer.valueOf(childPosition1);
                    } else {
                        size = Integer.parseInt(childPosition1);
                    }
                    typeNoList.add(String.valueOf(size));
                    Log.i("size", String.valueOf(typeNoList.size()));
                    Log.i("sizet", typeNoList.toString());
                    break;
            }
        }
    };

}