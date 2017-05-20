package com.surine.homeme.Adapter.Recycleview;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.surine.homeme.JavaBean.Ctrl;
import com.surine.homeme.R;
import com.surine.homeme.UI.CtrlActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by surine on 2017/5/19.
 */

public class MainRecycleviewAdapter extends RecyclerView.Adapter<MainRecycleviewAdapter.ViewHolder> {
    private List<Ctrl> mCtrlList;
    private Context mContext;
    private Activity activity;

    public MainRecycleviewAdapter(List<Ctrl> ctrlList, Context context, Activity activity) {
        mCtrlList = ctrlList;
        mContext = context;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ctrl,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int position = viewHolder.getAdapterPosition();

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("删除");
                builder.setMessage("确定删除？");
                        builder.setNegativeButton(R.string.dialog_cancel,null);
                builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int id = mCtrlList.get(position).getId();
                        //删除数据
                        DataSupport.delete(Ctrl.class,id);
                        Toast.makeText(mContext, R.string.delete_success, Toast.LENGTH_SHORT).show();
                        mCtrlList.remove(position);
                        notifyItemRemoved(position);
                    }
                });
                builder.show();

                return true;
            }
        });

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent(mContext, CtrlActivity.class);
                intent.putExtra("ctrl_id",mCtrlList.get(position).getId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mContext.startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                }else{
                    mContext.startActivity(intent);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      Ctrl ctrl = mCtrlList.get(position);
        holder.tv_item_ctrl_name.setText(ctrl.getName());
        if(ctrl.getImage_id()==1){
            Glide.with(mContext).load(R.drawable.item_tv).into(holder.img_item_ctrl_head);
        }else{
            Glide.with(mContext).load(R.drawable.item_airconditioning).into(holder.img_item_ctrl_head);
        }
    }

    @Override
    public int getItemCount() {
        return mCtrlList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_ctrl_name;
        ImageView img_item_ctrl_head;
        CardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_item_ctrl_name = (TextView) itemView.findViewById(R.id.tv_item_ctrl_name);
            img_item_ctrl_head = (ImageView) itemView.findViewById(R.id.img_item_ctrl_head);
            card = (CardView) itemView.findViewById(R.id.card);
        }
    }
}
