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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.customer.record.FragmentRecoder;

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
    ImageView new_image;
    View view;
    Uri selectedImage;
    EditText profileName;
    EditText profileLast;
    EditText profileDesc;
    EditText profilePhone;
    public final static String TAG = "FragmentNew";
    static final int GALLERY_REQUEST = 1;
    private final static int ACTIVITY_TAKE_PHOTO = 0;
    Dialog myDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_new, container, false);
        setView();
        if (((ActivityMain) getActivity()).getClientItem().getProfilename() != null) settext();
        new_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageDialog();
            }
        });

        return view;
    }

    public void setView() {
        profileName = (EditText) view.findViewById(R.id.edit_name);
        profileLast = (EditText) view.findViewById(R.id.edit_last);
        profileDesc = (EditText) view.findViewById(R.id.edit_desc);
        profilePhone = (EditText) view.findViewById(R.id.edit_phone);
        new_image = (ImageView) view.findViewById(R.id.edit_avatar);
    }

    public void messageDialog() {
        myDialog = new Dialog(getContext());
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_screen);
        myDialog.setCancelable(true);

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
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(myDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        myDialog.getWindow().setAttributes(lp);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
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
        if (((ActivityMain) getActivity()).getClientItem().getImagename() != null) {

            Uri uri = Uri.parse(((ActivityMain) getActivity()).getClientItem().getImagename());
            return new File(uri.getPath());
        }
        if (((ActivityMain) getActivity()).getClientItem().getRecid() != -1) {
            fileName = "image_" + ((ActivityMain) getActivity()).getClientItem().getRecid() + ".jpg";
        } else {
            fileName = "image_" + imageid + ".jpg";
        }

        return new File(folder, fileName);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        myDialog.dismiss();
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        selectedImage = null;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:

                    try {

                        copyFile(getPath(imageReturnedIntent.getData()), getTempFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    selectedImage = Uri.fromFile(new File(getTempFile().getPath()));
                    break;
                case ACTIVITY_TAKE_PHOTO:
                    selectedImage = Uri.fromFile(new File(getTempFile().getPath()));
                    break;
            }
            Glide.with(getActivity())
                    .load(selectedImage).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).bitmapTransform(new CropCircleTransformation(getActivity()))
                    .into(new_image);
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
        if (id == R.id.actionSettings) {
            if (profileName.getText().toString().isEmpty())
                Toast.makeText(getActivity(), R.string.persenname, Toast.LENGTH_SHORT).show();
            else if (profileLast.getText().toString().isEmpty())
                Toast.makeText(getActivity(), R.string.persenlast, Toast.LENGTH_SHORT).show();
            else if (profileDesc.getText().toString().isEmpty())
                Toast.makeText(getActivity(), R.string.persendesc, Toast.LENGTH_SHORT).show();
            else if (profilePhone.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), R.string.persentel, Toast.LENGTH_SHORT).show();
            } else {
                ((ActivityMain) getActivity()).getClientItem().setProfilename(profileName.getText().toString());
                ((ActivityMain) getActivity()).getClientItem().setLast(profileLast.getText().toString());
                ((ActivityMain) getActivity()).getClientItem().setDesc(profileDesc.getText().toString());
                ((ActivityMain) getActivity()).getClientItem().setPhone(profilePhone.getText().toString());
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.RECORD_AUDIO}, 3);

                if (selectedImage != null)
                    ((ActivityMain) getActivity()).getClientItem().setImagename(selectedImage.toString());

            }


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void settext() {
        if (((ActivityMain) getActivity()).getClientItem().getImagename() != null && (new File(Uri.parse(((ActivityMain) getActivity()).getClientItem().getImagename()).getPath()).exists())) {
            selectedImage = Uri.parse(((ActivityMain) getActivity()).getClientItem().getImagename());
            Glide.with(getActivity())
                    .load(selectedImage)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .into(new_image);
        }
        profileLast.setText(((ActivityMain) getActivity()).getClientItem().getLast());
        profileName.setText(((ActivityMain) getActivity()).getClientItem().getProfilename());
        profileDesc.setText(((ActivityMain) getActivity()).getClientItem().getDesc());
        profilePhone.setText(((ActivityMain) getActivity()).getClientItem().getPhone());

    }

    @Override
    public void onPermissionsGranted(String[] permission) {
        ((ActivityMain) getActivity()).showScreen(new FragmentRecoder(), FragmentRecoder.TAG, true);
    }
}