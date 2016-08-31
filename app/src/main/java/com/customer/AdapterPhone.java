package com.customer;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Максим on 22.08.2016.
 */
public class AdapterPhone extends RecyclerView.Adapter<AdapterPhone.FollowVH> {
    LayoutInflater inflater;
    private Context mContext;
    ArrayList<Client> arrayList;

    public AdapterPhone(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        getArrayList();
    }

    @Override
    public AdapterPhone.FollowVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.phoneview, parent, false);
        FollowVH holder = new FollowVH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FollowVH holder, int position) {
        holder.username.setText(arrayList.get(position).getProfilename());
        holder.message.setText(arrayList.get(position).getPhone());

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    protected void getArrayList() {
        arrayList = new ArrayList<>();
        Cursor c = mContext.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);
        while (c.moveToNext()) {
            String contactName = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phNumber = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            arrayList.add(new Client(contactName, phNumber));
        }
        c.close();
    }

    class FollowVH extends RecyclerView.ViewHolder implements View.OnClickListener, OnPermissionsListener {
        TextView username;
        TextView message;
        LinearLayout profile;

        public FollowVH(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.phonename);
            message = (TextView) itemView.findViewById(R.id.phonenamber);
            profile = (LinearLayout) itemView.findViewById(R.id.phoneset);
            profile.setOnClickListener(this);

        }

        @Override
        public void onPermissionsGranted(String[] permission) {
            ((ActivityMain) mContext).showScreen(new FragmentNew(), FragmentNew.TAG, true);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ((ActivityMain) mContext).getClient().setProfilename(arrayList.get(position).getProfilename());
            ((ActivityMain) mContext).getClient().setPhone(arrayList.get(position).getPhone().replaceAll(" ",""));
            ActivityCompat.requestPermissions((ActivityMain) mContext, new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

        }
    }
}
