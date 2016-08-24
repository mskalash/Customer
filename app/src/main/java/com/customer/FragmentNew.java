package com.customer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        File folder1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/." + getResources().getString(R.string.app_name));
        if (!folder1.exists()) {
            folder1.mkdir();
        }
        long imageid = db.insertDummyContact();
        String fileName;
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/." + getResources().getString(R.string.app_name) + "/image/");
        if (!folder.exists()) {
            folder.mkdir();
        }
        if(((MainActivity) getActivity()).getClient().getImagename()!=null)
               {

Uri uri=Uri.parse(((MainActivity) getActivity()).getClient().getImagename());
            return new File(uri.getPath());
        }
         fileName = "image_" + imageid + ".jpg";

        return new File(folder, fileName);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        myDialog.dismiss();
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        selectedImage=null;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:

                    try {

                        copyFile(getPath(imageReturnedIntent.getData()), getTempFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    selectedImage = Uri.fromFile(new File(getTempFile().getPath()));
                    Log.e("DJJFJFFJFHJ",selectedImage.toString());


                    break;
                case ACTIVITY_TAKE_PHOTO:
                    selectedImage = Uri.fromFile(new File(getTempFile().getPath()));
                    break;

            }
            Glide.with(getActivity())
                    .load(selectedImage).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).bitmapTransform(new CropCircleTransformation(getActivity()))
                    .into(newimage);




        }

    }

    public void copyFile(String selectedImagePath, File string) throws IOException {
        InputStream in = new FileInputStream(selectedImagePath);
        OutputStream out = new FileOutputStream(string);
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(1).setVisible(false);
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
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.RECORD_AUDIO}, 3);

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
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
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