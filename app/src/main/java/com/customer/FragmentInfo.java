package com.customer;

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

import java.io.File;
import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Максим on 08.08.2016.
 */
public class FragmentInfo extends Fragment implements View.OnClickListener {
    View view;
    FloatingActionButton play;
    TextView lastname;
    TextView name;
    TextView description;
    ImageView avatar;
    MediaPlayer mediaPlayer;
    String phone;
    Button call;
    Button send;
    ImageView imagebackgraund;
    public final static String TAG = "FragmentInfo";
    boolean playrec = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_info, container, false);
        play = (FloatingActionButton) view.findViewById(R.id.plaer);
        lastname = (TextView) view.findViewById(R.id.lastname);
        name = (TextView) view.findViewById(R.id.name);
        description = (TextView) view.findViewById(R.id.description);
        avatar = (ImageView) view.findViewById(R.id.profile_avatar);
        call = (Button) view.findViewById(R.id.callphone);
        send = (Button) view.findViewById(R.id.messege);
        imagebackgraund = (ImageView) view.findViewById(R.id.imagebackgraund);
        if (((ActivityMain) getActivity()).getClient().isCheck()) {
            DatabaseAdapter db = new DatabaseAdapter(getActivity());
            db.selectprofile(((ActivityMain) getActivity()).getClient().getRecid());
        }
        if ((((ActivityMain) getActivity()).getClient().getImagename() != null) && (new File(Uri.parse(((ActivityMain) getActivity()).getClient().getImagename()).getPath()).exists())) {
            Uri image = Uri.parse(((ActivityMain) getActivity()).getClient().getImagename());
            Glide.with(getActivity())
                    .load(image).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .into(avatar);
            Glide.with(getActivity())
                    .load(image).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .bitmapTransform(new BlurTransformation(getActivity()))
                    .into(imagebackgraund);
            Log.e("image", ((ActivityMain) getActivity()).getClient().getImagename());
        }
        setMedia();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                play.setImageResource(R.drawable.playprofile);
                playrec = false;
            }

        });
        phone = ((ActivityMain) getActivity()).getClient().getPhone();
        lastname.setText(phone);
        ((ActivityMain) getActivity()).mToolbar.setTitle(((ActivityMain) getActivity()).getClient().getProfilename() + " " + ((ActivityMain) getActivity()).getClient().getLast());
        name.setText(((ActivityMain) getActivity()).getClient().getProfilename() + " " + ((ActivityMain) getActivity()).getClient().getLast());
        description.setText(((ActivityMain) getActivity()).getClient().getDesc());
        call.setOnClickListener(this);
        play.setOnClickListener(this);
        send.setOnClickListener(this);
        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        childFragTrans.add(R.id.profilemap, new FaragmentMapInfo()).commit();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            if (!((ActivityMain) getActivity()).getClient().isCheck()) {
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                addcon();
                Toast.makeText(getActivity(), R.string.add, Toast.LENGTH_SHORT).show();
                ((ActivityMain) getActivity()).showScreen(new FragmentList(), FragmentList.TAG, true);
            } else {
                ((ActivityMain) getActivity()).showScreen(new FragmentMap(), FragmentMap.TAG, true);
            }
            return true;
        }
        if (id == R.id.delete_all) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(getActivity().getResources().getString(R.string.deletecont) + " ?")
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String recoder = ((ActivityMain) getActivity()).getClient().getFilename();
                            String image = ((ActivityMain) getActivity()).getClient().getImagename();
                            int idinfo = ((ActivityMain) getActivity()).getClient().getRecid();
                            ((ActivityMain) getActivity()).deleteprofile(idinfo, recoder, image);
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
        return super.onOptionsItemSelected(item);
    }

    public void addcon() {
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        String name = ((ActivityMain) getActivity()).getClient().getProfilename();
        String lastname = ((ActivityMain) getActivity()).getClient().getLast();
        String desc = ((ActivityMain) getActivity()).getClient().getDesc();
        double lat = ((ActivityMain) getActivity()).getClient().getLat();
        double longet = ((ActivityMain) getActivity()).getClient().getLonget();
        String filename = null;
        if (new File(((ActivityMain) getActivity()).getClient().getFilename()).exists())
            filename = ((ActivityMain) getActivity()).getClient().getFilename();
        String image = null;
        String phone = ((ActivityMain) getActivity()).getClient().getPhone();
        if ((((ActivityMain) getActivity()).getClient().getImagename() != null) && (new File(((ActivityMain) getActivity()).getClient().getImagename()).exists()))
            image = ((ActivityMain) getActivity()).getClient().getImagename();
        db.addcontact(name, lastname, desc, lat, longet, filename, image, phone);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle(R.string.finish);

        if (((ActivityMain) getActivity()).getClient().isCheck()) {
            menu.getItem(0).setTitle(R.string.edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            menu.getItem(1).setVisible(true).setTitle(R.string.delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        } else {
            menu.getItem(1).setVisible(false);
        }
    }

    public void setMedia() {
        mediaPlayer = new MediaPlayer();
        try {

            mediaPlayer.setDataSource(((ActivityMain) getActivity()).getClient().getFilename());
            mediaPlayer.prepare();
        } catch (IOException e) {
            play.setVisibility(View.GONE);
//            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plaer:
                if (playrec == false) {
                    play.setImageResource(R.drawable.pauseprofile);
                    mediaPlayer.start();
                    playrec = true;
                    return;
                } else {
                    play.setImageResource(R.drawable.playprofile);
                    mediaPlayer.pause();
                    playrec = false;
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
