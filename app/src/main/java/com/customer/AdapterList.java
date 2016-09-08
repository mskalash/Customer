package com.customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
import java.util.Collections;
import java.util.Comparator;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class AdapterList extends RecyclerView.Adapter<AdapterList.FollowVH> {
    private LayoutInflater inflater;
    private Context context;
    ArrayList<ClientItem> arrayList;
    public boolean nameSort = false;
    public boolean dateSort = true;

    public AdapterList(Context context, ArrayList<ClientItem> arrayList) {
        inflater = LayoutInflater.from(context);
        this.arrayList = new ArrayList<>(arrayList);
        this.context = context;
    }

    @Override
    public FollowVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewcuston, parent, false);
        FollowVH holder = new FollowVH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FollowVH holder, final int position) {
        String image = arrayList.get(position).getImageName();

        if ((image != null) && (new File(Uri.parse(image).getPath()).exists())) {
            Glide.with(context).load(image).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).bitmapTransform(new CropCircleTransformation(context)).into(holder.avatar);
        } else {
            Glide.with(context).load(R.drawable.ava).into(holder.avatar);
        }
        holder.username.setText(arrayList.get(position).getProfileName() + " " + arrayList.get(position).getLast());
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
        DatabaseAdapter db = new DatabaseAdapter(context);
        TextView more;

        public FollowVH(View itemView) {
            super(itemView);
            more = (TextView) itemView.findViewById(R.id.more);
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
                    db.updatefavorites(arrayList.get(position).getId(), true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    int position = getAdapterPosition();
                    arrayList.get(position).setFavorite(false);
                    db.updatefavorites(arrayList.get(position).getId(), false);
                }
            });
            profile.setOnLongClickListener(this);
            more.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            ((ActivityMain) context).getClientItem().setCheck(true);
            ((ActivityMain) context).getClientItem().setRecid(arrayList.get(position).getId());
            ((ActivityMain) context).showScreen(new FragmentInfo(), FragmentInfo.TAG, true);

        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setMessage(context.getResources().getString(R.string.deletecont) + " " + arrayList.get(position).getProfileName() + " ?")
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int position = getAdapterPosition();
                            ((ActivityMain) context).deleteProfile(arrayList.get(position).getId(), arrayList.get(position).getFileName(), arrayList.get(position).getImageName());
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


    public void deleteAll() {
        arrayList.clear();
        notifyDataSetChanged();
    }

    public void setArrayList() {
        DatabaseAdapter db = new DatabaseAdapter(context);
        arrayList = db.getContactsData();
        notifyDataSetChanged();
    }

    public boolean sortName(boolean star) {
        dateSort = false;
        if (star) {
            nameSort=!nameSort;
        }
        Collections.sort(arrayList, new Comparator<ClientItem>() {
            @Override
            public int compare(ClientItem clientItem, ClientItem clientItem1) {
                return clientItem.getProfileName().compareTo(clientItem1.getProfileName());
            }
        });
        if (!nameSort) {
            nameSort = true;
            notifyDataSetChanged();
            return nameSort;
        } else {
            Collections.reverse(arrayList);
            nameSort = false;
        }
        notifyDataSetChanged();
        return nameSort;
    }

    public boolean sortDate(boolean checkStar,boolean star) {
        nameSort = false;
        setArrayList();
        if (checkStar) {
            dateSort=!dateSort;
        }
        if (star){
            favorite();
        }
        if (!dateSort) {
            dateSort = true;
            notifyDataSetChanged();
            return dateSort;
        } else {
            Collections.reverse(arrayList);
            dateSort = false;
        }
        notifyDataSetChanged();
        return dateSort;
    }


    public void favorite() {
        for (int i = 0; i < arrayList.size(); i++) {
            if (!arrayList.get(i).isFavorite()) {
                notifyItemRemoved(i);
                arrayList.remove(i);
                i--;
            }
        }
    }


}


