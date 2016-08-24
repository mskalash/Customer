package com.customer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Максим on 08.08.2016.
 */
public class FragmentInfo extends Fragment implements View.OnClickListener {
    View view;
    ImageView play;
    TextView lastname;
    TextView name;
    TextView description;
    ImageView avatar;
    MediaPlayer mediaPlayer;
    String phone;
    FancyButton call;
    FancyButton send;
    public final static String TAG = "FragmentInfo";
    boolean playrec = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_info, container, false);
        play = (ImageView) view.findViewById(R.id.plaer);
        lastname = (TextView) view.findViewById(R.id.lastname);
        name = (TextView) view.findViewById(R.id.name);
        description = (TextView) view.findViewById(R.id.description);
        avatar = (ImageView) view.findViewById(R.id.profile_avatar);
        call = (FancyButton) view.findViewById(R.id.callphone);
        send = (FancyButton) view.findViewById(R.id.messege);
        if (((MainActivity) getActivity()).getClient().isCheck()) {
            DatabaseAdapter db = new DatabaseAdapter(getActivity());
            db.selectprofile(((MainActivity) getActivity()).getClient().getRecid());
        }
        if (((MainActivity) getActivity()).getClient().getImagename() != null) {
            Uri image = Uri.parse(((MainActivity) getActivity()).getClient().getImagename());
            Glide.with(getActivity())
                    .load(image).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .into(avatar);
            Log.e("image",((MainActivity) getActivity()).getClient().getImagename());
        }
        setMedia();
        phone = ((MainActivity) getActivity()).getClient().getPhone();
        lastname.setText(((MainActivity) getActivity()).getClient().getLast());
        name.setText(((MainActivity) getActivity()).getClient().getProfilename());
        description.setText(((MainActivity) getActivity()).getClient().getDesc());
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
            if (!((MainActivity) getActivity()).getClient().isCheck()) {
                if(mediaPlayer.isPlaying()) mediaPlayer.pause();
                addcon();
                Toast.makeText(getActivity(),"Customer added",Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).showScreen(new FragmentList(), FragmentList.TAG, true);
            } else {
                ((MainActivity) getActivity()).showScreen(new FragmentMap(), FragmentMap.TAG, true);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addcon() {
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        String name = ((MainActivity) getActivity()).getClient().getProfilename();
        String lastname = ((MainActivity) getActivity()).getClient().getLast();
        String desc = ((MainActivity) getActivity()).getClient().getDesc();
        double lat = ((MainActivity) getActivity()).getClient().getLat();
        double longet = ((MainActivity) getActivity()).getClient().getLonget();
        String filename = ((MainActivity) getActivity()).getClient().getFilename();
        String image = null;
        String phone = ((MainActivity) getActivity()).getClient().getPhone();
        if (((MainActivity) getActivity()).getClient().getImagename() != null)
            image = ((MainActivity) getActivity()).getClient().getImagename();
        db.addcontact(name, lastname, desc, lat, longet, filename, image, phone);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle("DONE");
        if (((MainActivity) getActivity()).getClient().isCheck())
            menu.getItem(0).setTitle("EDIT");
        menu.getItem(1).setVisible(false);

    }

    public void setMedia() {
        mediaPlayer = new MediaPlayer();
        try {

            mediaPlayer.setDataSource(((MainActivity) getActivity()).getClient().getFilename());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plaer:
                if (playrec == false) {
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    playrec = true;
                    return;
                } else {
                    play.setImageResource(R.drawable.play);
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
