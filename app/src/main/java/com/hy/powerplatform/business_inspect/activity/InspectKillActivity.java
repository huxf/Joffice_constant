package com.hy.powerplatform.business_inspect.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hy.powerplatform.Fragment_Adapter;
import com.hy.powerplatform.R;
import com.hy.powerplatform.business_inspect.fragment.KillNoPassFragment;
import com.hy.powerplatform.business_inspect.fragment.KillPassFragment;
import com.hy.powerplatform.my_utils.base.BaseActivity;
import com.hy.powerplatform.my_utils.myViews.Header;
import com.hy.powerplatform.news.myview.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InspectKillActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.rbPass)
    RadioButton rbPass;
    @BindView(R.id.rbNoPass)
    RadioButton rbNoPass;
    @BindView(R.id.radionGroup)
    RadioGroup radionGroup;
    @BindView(R.id.customViewPage)
    CustomViewPager customViewPage;
    @BindView(R.id.activity_inspectstarte)
    LinearLayout activityInspectstarte;

    public Fragment fragmentPass, fragmentNoPass;
    List<Fragment> list = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        fragmentPass = new KillPassFragment();
        fragmentNoPass = new KillNoPassFragment();
        list.add(fragmentPass);
        list.add(fragmentNoPass);
        radionGroup.setOnCheckedChangeListener(this);
        customViewPage.setAdapter(new Fragment_Adapter(getSupportFragmentManager(), list));
        customViewPage.setCurrentItem(0, false);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_inspectskill;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbPass:
                customViewPage.setCurrentItem(0, false);
                break;
            case R.id.rbNoPass:
                customViewPage.setCurrentItem(1, false);
                break;
        }
    }
}

