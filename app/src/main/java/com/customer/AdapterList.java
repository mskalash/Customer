package com.customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Максим on 16.08.2016.
 */
public class AdapterList extends RecyclerView.Adapter<AdapterList.FollowVH> {
    private LayoutInflater inflater;
    private Context mContext;
    ArrayList<Client> arrayList;
public AdapterList(){}
    public AdapterList(Context context, ArrayList<Client> arrayList) {
        inflater = LayoutInflater.from(context);
//        this.arrayList = arrayList;
        this.arrayList = new ArrayList<>(arrayList);
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
        if (image != null) {
            Glide.with(mContext).load(image).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.avatar);
            Log.e("image",image);}
        holder.username.setText(arrayList.get(position).getProfilename());
        holder.message.setText(arrayList.get(position).getDesc());
        holder.namelist.setText(arrayList.get(position).getLast());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class FollowVH extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        ImageView avatar;
        TextView username;
        TextView message;
        LinearLayout profile;
        TextView namelist;

        public FollowVH(View itemView) {
            super(itemView);
            namelist = (TextView) itemView.findViewById(R.id.namelist);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            username = (TextView) itemView.findViewById(R.id.firsname);
            message = (TextView) itemView.findViewById(R.id.desc);
            profile = (LinearLayout) itemView.findViewById(R.id.profileid);
profile.setOnLongClickListener(this);
            profile.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

                ((MainActivity) mContext).getClient().setCheck(true);
                ((MainActivity) mContext).getClient().setRecid(arrayList.get(position).getRecid());
                ((MainActivity) mContext).showScreen(new FragmentInfo(), FragmentInfo.TAG, true);

        }

        @Override
        public boolean onLongClick(View view) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {int position = getAdapterPosition();
                            ((MainActivity) mContext).deleteprofile(arrayList.get(position).getRecid(), arrayList.get(position).getFilename());
                            arrayList.remove(position);
                            notifyItemRemoved(position);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return false;
        }
    }

    public void delete(){
        arrayList.clear();
        notifyDataSetChanged();
    }
}

