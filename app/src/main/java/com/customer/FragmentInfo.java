package com.customer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;

/**
 * Created by Максим on 08.08.2016.
 */
public class FragmentInfo extends Fragment {
    View view;
    ImageView play;
    TextView lastname;
    TextView name;
    TextView description;
    ImageView avatar;
    MediaPlayer mediaPlayer;
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
        if (((MainActivity) getActivity()).getClient().isCheck()) {
            DatabaseAdapter db = new DatabaseAdapter(getActivity());
            db.selectprofile(((MainActivity) getActivity()).getClient().getRecid());
        }
        if (((MainActivity) getActivity()).getClient().getImagename() != null) {
            Uri image = Uri.parse(((MainActivity) getActivity()).getClient().getImagename());
            Glide.with(getActivity())
                    .load(image)
                    .into(avatar);
        }

        setMedia();

        lastname.setText(((MainActivity) getActivity()).getClient().getLast());
        name.setText(((MainActivity) getActivity()).getClient().getProfilename());
        description.setText(((MainActivity) getActivity()).getClient().getDesc());

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        childFragTrans.add(R.id.profilemap, new FaragmentMapInfo()).commit();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            DatabaseAdapter db = new DatabaseAdapter(getActivity());
            String name = ((MainActivity) getActivity()).getClient().getProfilename();
            String lastname = ((MainActivity) getActivity()).getClient().getLast();
            String desc = ((MainActivity) getActivity()).getClient().getDesc();
            double lat = ((MainActivity) getActivity()).getClient().getLat();
            double longet = ((MainActivity) getActivity()).getClient().getLonget();
            String filename = ((MainActivity) getActivity()).getClient().getFilename();
            String image = null;
            if (((MainActivity) getActivity()).getClient().getImagename() != null)
                image = ((MainActivity) getActivity()).getClient().getImagename();
            db.addcontact(name, lastname, desc, lat, longet, filename, image);

            ((MainActivity) getActivity()).showScreen(new FragmentFirst(), FragmentFirst.TAG, false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (((MainActivity) getActivity()).getClient().isCheck())
            menu.getItem(0).setVisible(false);
            menu.getItem(0).setTitle("DONE");

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
}
