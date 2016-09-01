package com.customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.File;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Максим on 16.08.2016.
 */
public class AdapterList extends RecyclerView.Adapter<AdapterList.FollowVH> {
    private LayoutInflater inflater;
    private Context mContext;
    ArrayList<Client> arrayList;

    public AdapterList(Context context, ArrayList<Client> arrayList) {
        inflater = LayoutInflater.from(context);
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

        if ((image != null)&&(new File(Uri.parse(image).getPath()).exists())) {
            Log.e("Image", arrayList.get(position).getImagename());
            Glide.with(mContext).load(image).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.avatar);
        } else {
            Glide.with(mContext).load(R.drawable.newprofile).into(holder.avatar);
        }
        holder.username.setText(arrayList.get(position).getProfilename() + " " + arrayList.get(position).getLast());
        holder.message.setText(arrayList.get(position).getDesc());
        if (arrayList.get(position).isFavorite())
            holder.fav.setLiked(true);
        else {
            holder.fav.setLiked(false);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class FollowVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView avatar;
        TextView username;
        TextView message;
        LinearLayout profile;
        LikeButton fav;
        DatabaseAdapter db = new DatabaseAdapter(mContext);
        TextView more;

        public FollowVH(View itemView) {
            super(itemView);
            more=(TextView)itemView.findViewById(R.id.more);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            username = (TextView) itemView.findViewById(R.id.firsname);
            message = (TextView) itemView.findViewById(R.id.desc);
            profile = (LinearLayout) itemView.findViewById(R.id.profileid);
            fav = (LikeButton) itemView.findViewById(R.id.star_button);
            fav.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    int position = getAdapterPosition();

                    arrayList.get(position).setFavorite(true);
                    db.updatefavorites(arrayList.get(position).getRecid(), true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    int position = getAdapterPosition();
                    arrayList.get(position).setFavorite(false);
                    db.updatefavorites(arrayList.get(position).getRecid(), false);
                }
            });
            profile.setOnLongClickListener(this);
            more.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            ((ActivityMain) mContext).getClient().setCheck(true);
            ((ActivityMain) mContext).getClient().setRecid(arrayList.get(position).getRecid());
            ((ActivityMain) mContext).showScreen(new FragmentInfo(), FragmentInfo.TAG, true);

        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setMessage(mContext.getResources().getString(R.string.deletecont) + " " + arrayList.get(position).getProfilename() + " ?")
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int position = getAdapterPosition();
                            ((ActivityMain) mContext).deleteprofile(arrayList.get(position).getRecid(), arrayList.get(position).getFilename(), arrayList.get(position).getImagename());
                            arrayList.remove(position);
                            notifyItemRemoved(position);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return false;
        }
    }

    public void delete() {
        arrayList.clear();
        notifyDataSetChanged();
    }

    ArrayList<Client> first;
    public void unfavorite() {
        arrayList = new ArrayList<>(first);
        Log.e("----------", "----------------------------");
        notifyDataSetChanged();
    }

    public void favorite() {
        first = new ArrayList<>(arrayList);

        for (int i = 0; i < arrayList.size(); i++) {

            if (!arrayList.get(i).isFavorite()) {
                notifyItemRemoved(i);
                arrayList.remove(i);
                i--;
            }
            Log.e("Delete", String.valueOf(arrayList.size()));
        }
    }

}


