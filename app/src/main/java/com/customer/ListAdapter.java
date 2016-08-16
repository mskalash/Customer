package com.customer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    public void onBindViewHolder(FollowVH holder, final int position) {
        String image = arrayList.get(position).getImagename();
        if (image!=null)
        Glide.with(mContext).load(image).into(holder.avatar);
        holder.username.setText(arrayList.get(position).getProfilename()+" "+arrayList.get(position).getLast());
        holder.message.setText(arrayList.get(position).getDesc());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mContext).deleteprofile(arrayList.get(position).getRecid(),arrayList.get(position).getFilename());

              arrayList.remove(position);
                notifyItemRemoved(position);
             }
        });
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mContext).getClient().setCheck(true);
                ((MainActivity) mContext).getClient().setRecid(arrayList.get(position).getRecid());
                ((MainActivity) mContext).showScreen(new FragmentInfo(), FragmentInfo.TAG, true);
            }
        });

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
    LinearLayout profile;
    public FollowVH(View itemView) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        username = (TextView) itemView.findViewById(R.id.firsname);
        message = (TextView) itemView.findViewById(R.id.desc);
        delete = (ImageView) itemView.findViewById(R.id.delete);
profile=(LinearLayout)itemView.findViewById(R.id.profileid);
    }


}


