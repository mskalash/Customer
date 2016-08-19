package com.customer;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Максим on 08.08.2016.
 */
public class FragmentNew extends Fragment implements OnPermissionsListener {
    ImageView newimage;
    View v;
    Uri selectedImage;
    EditText profilename;
    EditText profilelast;
    EditText profiledesc;
    EditText profilephone;
    public final static String TAG = "FragmentNew";
    static final int GALLERY_REQUEST = 1;
    private final static int ACTIVITY_TAKE_PHOTO = 0;
    Dialog myDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        v = inflater.inflate(R.layout.fragment_new, container, false);
        profilename = (EditText) v.findViewById(R.id.editname);
        profilelast = (EditText) v.findViewById(R.id.editlast);
        profiledesc = (EditText) v.findViewById(R.id.editdesc);
        profilephone = (EditText) v.findViewById(R.id.editphone);
        newimage = (ImageView) v.findViewById(R.id.editavatar);
        if (((MainActivity) getActivity()).getClient().getProfilename() != null) settext();

        newimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageDialog();


            }
        });

        return v;
    }

    public void messageDialog() {
        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.dialogscreen);
        myDialog.setCancelable(true);
        myDialog.setTitle("Select Image");
        Button image = (Button) myDialog.findViewById(R.id.images);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

            }
        });
        Button photo = (Button) myDialog.findViewById(R.id.photos);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri imageUri = Uri.fromFile(getTempFile());
                Intent intent = createIntentForCamera(imageUri);
                startActivityForResult(intent, ACTIVITY_TAKE_PHOTO);
            }
        });
        myDialog.show();

    }

    private Intent createIntentForCamera(Uri imageUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        return intent;
    }

    private File getTempFile() {
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        long imageid = db.insertDummyContact();
        String fileName = "image_" + imageid + ".jpg";
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/image/");
        if (!folder.exists()) {
            folder.mkdir();
        }
        return new File(folder, fileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        myDialog.dismiss();
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:

                    selectedImage = imageReturnedIntent.getData();


                    break;
                case ACTIVITY_TAKE_PHOTO:
                    selectedImage = Uri.fromFile(getTempFile());

            }
            Glide.with(getActivity())
                    .load(selectedImage).bitmapTransform(new CropCircleTransformation(getActivity()))
                    .into(newimage);
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
            else if (profilephone.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Write person phone number", Toast.LENGTH_SHORT).show();
            } else {
                ((MainActivity) getActivity()).getClient().setProfilename(profilename.getText().toString());
                ((MainActivity) getActivity()).getClient().setLast(profilelast.getText().toString());
                ((MainActivity) getActivity()).getClient().setDesc(profiledesc.getText().toString());
                ((MainActivity) getActivity()).getClient().setPhone(profilephone.getText().toString());
                ActivityCompat.requestPermissions(getActivity(),new String[]{
                        Manifest.permission.RECORD_AUDIO},3);

                if (selectedImage != null)
                    ((MainActivity) getActivity()).getClient().setImagename(selectedImage.toString());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void settext() {
        if (((MainActivity) getActivity()).getClient().getImagename() != null) {
            selectedImage = Uri.parse(((MainActivity) getActivity()).getClient().getImagename());
            Glide.with(getActivity())
                    .load(selectedImage)
                    .into(newimage);
        }
        profilelast.setText(((MainActivity) getActivity()).getClient().getLast());
        profilename.setText(((MainActivity) getActivity()).getClient().getProfilename());
        profiledesc.setText(((MainActivity) getActivity()).getClient().getDesc());
        profilephone.setText(((MainActivity) getActivity()).getClient().getPhone());

    }

    @Override
    public void onPermissionsGranted(String[] permission) {
        ((MainActivity) getActivity()).showScreen(new FragmentRecoder(), FragmentRecoder.TAG, true);
    }
}