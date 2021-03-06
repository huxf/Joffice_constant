package com.hy.powerplatform.duban.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.hy.powerplatform.R;
import com.hy.powerplatform.duban.bean.DBCheckPerson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/5.
 */

public class DBCheckPersonAdapter extends RecyclerView.Adapter<DBCheckPersonAdapter.ViewHolder> {
    Context context;
  private CallBackPosition callBackPosition;
    List<DBCheckPerson.ResultBean> beanList = new ArrayList<>();

    public DBCheckPersonAdapter(Context context, List<DBCheckPerson.ResultBean> checkList) {
        this.context = context;
        this.beanList = checkList;
    }

    public interface CallBackPosition {
        void onItemClick(int position, String tag);
    }

    public void setOnItemLitener(CallBackPosition callBackPosition) {
        this.callBackPosition = callBackPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_checkperson, parent, false);
        ViewHolder viewHolser = new ViewHolder(view);
        return viewHolser;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvName.setText(beanList.get(position).getFullname());
        holder.position1 = position;
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("XXXXX",position+"--");
                if (holder.tvName.isChecked()){
                    callBackPosition.onItemClick(position,"1");
                }else {
                    callBackPosition.onItemClick(position,"2");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox tvName;
        int position1;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvName = (CheckBox) itemView.findViewById(R.id.tvName);
        }
    }
}
