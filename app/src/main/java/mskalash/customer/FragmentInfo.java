package mskalash.customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.customer.R;

import mskalash.customer.map.FragmentMapInfo;

import java.io.File;
import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class FragmentInfo extends Fragment implements View.OnClickListener {
    public final static String TAG = "FragmentInfo";
    private View view;
    private FloatingActionButton play;
    private TextView lastName;
    private TextView name;
    private TextView description;
    private ImageView avatar;
    private MediaPlayer mediaPlayer;
    private String phone;
    private Button call;
    private Button send;
    private ImageView imageBackground;
    private boolean playRec = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_info, container, false);
        setView();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                play.setImageResource(R.drawable.playprofile);
                playRec = false;
            }

        });
        phone = ((ActivityMain) getActivity()).getClientItem().getPhone();
        lastName.setText(phone);
        ((ActivityMain) getActivity()).toolbar.setTitle(((ActivityMain) getActivity()).getClientItem().getProfileName() + " " + ((ActivityMain) getActivity()).getClientItem().getLast());
        name.setText(((ActivityMain) getActivity()).getClientItem().getProfileName() + " " + ((ActivityMain) getActivity()).getClientItem().getLast());
        description.setText(((ActivityMain) getActivity()).getClientItem().getDesc());
        call.setOnClickListener(this);
        play.setOnClickListener(this);
        send.setOnClickListener(this);
        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        childFragTrans.add(R.id.profilemap, new FragmentMapInfo()).commit();
        return view;
    }

    public void setView() {
        play = (FloatingActionButton) view.findViewById(R.id.plaer);
        lastName = (TextView) view.findViewById(R.id.lastname);
        name = (TextView) view.findViewById(R.id.name);
        description = (TextView) view.findViewById(R.id.description);
        avatar = (ImageView) view.findViewById(R.id.profile_avatar);
        call = (Button) view.findViewById(R.id.callphone);
        send = (Button) view.findViewById(R.id.messege);
        imageBackground = (ImageView) view.findViewById(R.id.imagebackgraund);
        if (((ActivityMain) getActivity()).getClientItem().isCheck()) {
            DatabaseAdapter db = new DatabaseAdapter(getActivity());
            db.selectProfile(((ActivityMain) getActivity()).getClientItem().getId());
        }
        if ((((ActivityMain) getActivity()).getClientItem().getImageName() != null) && (new File(Uri.parse(((ActivityMain) getActivity()).getClientItem().getImageName()).getPath()).exists())) {
            Uri image = Uri.parse(((ActivityMain) getActivity()).getClientItem().getImageName());
            Glide.with(getActivity())
                    .load(image).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .into(avatar);
            Glide.with(getActivity())
                    .load(image).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .bitmapTransform(new BlurTransformation(getActivity()))
                    .into(imageBackground);
            Log.e("image", ((ActivityMain) getActivity()).getClientItem().getImageName());
        }
        setMedia();
    }
    public void setMedia() {
        mediaPlayer = new MediaPlayer();
        try {

            mediaPlayer.setDataSource(((ActivityMain) getActivity()).getClientItem().getFileName());
            mediaPlayer.prepare();
        } catch (IOException e) {
            play.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle(R.string.finish);

        if (((ActivityMain) getActivity()).getClientItem().isCheck()) {
            menu.getItem(0).setTitle(R.string.edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            menu.getItem(1).setVisible(true).setTitle(R.string.delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        } else {
            menu.getItem(1).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionSettings) {

            if (!((ActivityMain) getActivity()).getClientItem().isCheck()) {
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                addContact();

                Toast.makeText(getActivity(), R.string.add, Toast.LENGTH_SHORT).show();
                ((ActivityMain) getActivity()).showScreen(new FragmentList(), FragmentList.TAG, true);
            } else {
                ((ActivityMain) getActivity()).showScreen(new FragmentNew(), FragmentNew.TAG, true);
            }
            return true;
        }
        if (id == R.id.deleteAll) {
            dialogDeleteAll();
        }
        return super.onOptionsItemSelected(item);
    }
    public void  dialogDeleteAll(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getResources().getString(R.string.deletecont) + " ?")
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String recoder = ((ActivityMain) getActivity()).getClientItem().getFileName();
                        String image = ((ActivityMain) getActivity()).getClientItem().getImageName();
                        int idinfo = ((ActivityMain) getActivity()).getClientItem().getId();
                        ((ActivityMain) getActivity()).deleteProfile(idinfo, recoder, image);
                        getActivity().onBackPressed();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void addContact() {
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        String name = ((ActivityMain) getActivity()).getClientItem().getProfileName();
        String lastName = ((ActivityMain) getActivity()).getClientItem().getLast();
        String desc = ((ActivityMain) getActivity()).getClientItem().getDesc();
        double lat = ((ActivityMain) getActivity()).getClientItem().getLat();
        double longet = ((ActivityMain) getActivity()).getClientItem().getLonget();
        String filename = null;
        if (new File(((ActivityMain) getActivity()).getClientItem().getFileName()).exists())
            filename = ((ActivityMain) getActivity()).getClientItem().getFileName();
        String image = null;
        String phone = ((ActivityMain) getActivity()).getClientItem().getPhone();
        if ((((ActivityMain) getActivity()).getClientItem().getImageName() != null) && (new File(Uri.parse(((ActivityMain) getActivity()).getClientItem().getImageName()).getPath()).exists()))
            image = ((ActivityMain) getActivity()).getClientItem().getImageName();
        db.addContact(name, lastName, desc, lat, longet, filename, image, phone);

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plaer:
                if (playRec == false) {
                    play.setImageResource(R.drawable.pauseprofile);
                    mediaPlayer.start();
                    playRec = true;
                    return;
                } else {
                    play.setImageResource(R.drawable.playprofile);
                    mediaPlayer.pause();
                    playRec = false;
                }
                break;
            case R.id.callphone:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                break;
            case R.id.messege:
                String uri = "smsto:" + phone;
                Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
                startActivity(intent2);
                break;
        }
    }
}
