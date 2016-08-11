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

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * Created by Максим on 08.08.2016.
 */
public class NewFragment extends Fragment {
    CircularImageView newimage;
    View v;
    Uri selectedImage;
    EditText profilename;
    EditText profilelast;
    EditText profiledesc;
    static final int GALLERY_REQUEST = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        v= inflater.inflate(R.layout.fragment_new, container, false);
        profilename=(EditText)v.findViewById(R.id.editname);
        profilelast=(EditText)v.findViewById(R.id.editlast);
        profiledesc=(EditText) v.findViewById(R.id.editdesc);
        newimage=(CircularImageView)v.findViewById(R.id.editavatar);
        newimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);            }
        });

        return v;}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

      //  Bitmap bitmap = null;


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
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new RecoderFragment()).commit();
            ((MainActivity) getActivity()).getClient().setProfilename(profilename.getText().toString());
            ((MainActivity) getActivity()).getClient().setLast(profilelast.getText().toString());
            ((MainActivity) getActivity()).getClient().setDesc(profiledesc.getText().toString());
            if (selectedImage!=null) ((MainActivity) getActivity()).getClient().setImagename(selectedImage.toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}