package com.customer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Максим on 16.08.2016.
 */
public class ListAdapter extends RecyclerView.Adapter<FollowVH> {
    private LayoutInflater inflater;
    private Context mContext;
    ArrayList<Client> arrayList;

    public ListAdapter(Context context, ArrayList<Client> arrayList) {
        inflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
        mContext = context;
    }

    @Override
    public FollowVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewcuston, parent, false);
        FollowVH holder = new FollowVH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FollowVH holder, int position) {
        String image = arrayList.get(position).getImagename();
        Glide.with(mContext).load(image).into(holder.avatar);
        holder.username.setText(arrayList.get(position).getProfilename()+" "+arrayList.get(position).getLast());
        holder.message.setText(arrayList.get(position).getDesc());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

class FollowVH extends RecyclerView.ViewHolder {
    ImageView delete;
    ImageView avatar;
    TextView username;
    TextView message;

    public FollowVH(View itemView) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        username = (TextView) itemView.findViewById(R.id.firsname);
        message = (TextView) itemView.findViewById(R.id.desc);
        delete = (ImageView) itemView.findViewById(R.id.delete);
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }


}


