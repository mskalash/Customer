package mskalash.customer.phone;

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

import mskalash.customer.ActivityMain;
import mskalash.customer.ClientItem;
import mskalash.customer.FragmentNew;
import mskalash.customer.OnPermissionsListener;
import com.customer.R;

import java.util.ArrayList;

public class AdapterPhone extends RecyclerView.Adapter<AdapterPhone.FollowVH> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<ClientItem> arrayList;

    public AdapterPhone(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        getArrayList();
    }
    protected void getArrayList() {
        arrayList = new ArrayList<>();
        Cursor c = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);
        while (c.moveToNext()) {
            String contactName = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phNumber = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            arrayList.add(new ClientItem(contactName, phNumber));
        }
        c.close();
    }

    @Override
    public AdapterPhone.FollowVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.phone_view, parent, false);
        FollowVH holder = new FollowVH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FollowVH holder, int position) {
        holder.username.setText(arrayList.get(position).getProfileName());
        holder.number.setText(arrayList.get(position).getPhone());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    class FollowVH extends RecyclerView.ViewHolder implements View.OnClickListener, OnPermissionsListener {
        TextView username;
        TextView number;
        LinearLayout profile;

        public FollowVH(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.phone_name);
            number = (TextView) itemView.findViewById(R.id.phone_namber);
            profile = (LinearLayout) itemView.findViewById(R.id.phone_set);
            profile.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ((ActivityMain) context).getClientItem().setProfileName(arrayList.get(position).getProfileName());
            ((ActivityMain) context).getClientItem().setPhone(arrayList.get(position).getPhone().replaceAll(" ",""));
            ActivityCompat.requestPermissions((ActivityMain) context, new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        @Override
        public void onPermissionsGranted(String[] permission) {
            ((ActivityMain) context).showScreen(new FragmentNew(), FragmentNew.TAG, true);
        }
    }
}
