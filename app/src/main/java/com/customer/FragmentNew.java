package com.customer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * Created by Максим on 08.08.2016.
 */
public class FragmentNew extends Fragment {
    ImageView newimage;
    View v;
    Uri selectedImage;
    EditText profilename;
    EditText profilelast;
    EditText profiledesc;
    public final static String TAG = "FtagmentNew";
    static final int GALLERY_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        v = inflater.inflate(R.layout.fragment_new, container, false);
        profilename = (EditText) v.findViewById(R.id.editname);
        profilelast = (EditText) v.findViewById(R.id.editlast);
        profiledesc = (EditText) v.findViewById(R.id.editdesc);
        newimage = (ImageView) v.findViewById(R.id.editavatar);
        if (((MainActivity) getActivity()).getClient().getProfilename() != null) settext();

        newimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    Glide.with(getActivity())
                            .load(selectedImage)
                            .into(newimage);
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if (profilename.getText().toString().isEmpty())
                Toast.makeText(getActivity(), "Write person name", Toast.LENGTH_SHORT).show();
            else if (profilelast.getText().toString().isEmpty())
                Toast.makeText(getActivity(), "Write person last name", Toast.LENGTH_SHORT).show();
            else if (profiledesc.getText().toString().isEmpty())
                Toast.makeText(getActivity(), "Write person description", Toast.LENGTH_SHORT).show();
            else {
                ((MainActivity) getActivity()).showScreen(new FragmentRecoder(), FragmentRecoder.TAG, true);
                ((MainActivity) getActivity()).getClient().setProfilename(profilename.getText().toString());
                ((MainActivity) getActivity()).getClient().setLast(profilelast.getText().toString());
                ((MainActivity) getActivity()).getClient().setDesc(profiledesc.getText().toString());
                if (selectedImage != null)
                    ((MainActivity) getActivity()).getClient().setImagename(selectedImage.toString());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void settext() {
if(((MainActivity) getActivity()).getClient().getImagename()!=null){
    selectedImage=Uri.parse(((MainActivity) getActivity()).getClient().getImagename());
    Glide.with(getActivity())
            .load(selectedImage)
            .into(newimage);
}
        profilelast.setText(((MainActivity) getActivity()).getClient().getLast());
        profilename.setText(((MainActivity) getActivity()).getClient().getProfilename());
        profiledesc.setText(((MainActivity) getActivity()).getClient().getDesc());

    }
}