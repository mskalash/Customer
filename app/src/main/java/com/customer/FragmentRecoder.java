package com.customer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Максим on 08.08.2016.
 */
public class FragmentRecoder extends android.support.v4.app.Fragment implements View.OnClickListener {
    View view;
    ImageView rec;
    ImageView play;
    ImageView stop;
    MediaPlayer mediaPlayer;
    String fileName;
    boolean recoder = false;
    final int REQUEST_CODE_FILE = 1;
    String id;
    DatabaseAdapter db;
    public final static String TAG = "FragmentRecoder";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new DatabaseAdapter(getActivity());
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_recoder, container, false);
        rec = (ImageView) view.findViewById(R.id.rec);
        stop = (ImageView) view.findViewById(R.id.pause);
        play = (ImageView) view.findViewById(R.id.play);
        if (((MainActivity) getActivity()).getClient().getFilename()!=null) visibl();
        rec.setOnClickListener(this);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        if (((MainActivity) getActivity()).getClient().isCheck())
            id = ((MainActivity) getActivity()).getClient().getFilename();
        else {
            DatabaseAdapter db = new DatabaseAdapter(getActivity());
            id = Environment.getExternalStorageDirectory().getAbsolutePath() + "/."+getResources().getString(R.string.app_name)+"/record/"+"/record_"+db.insertDummyContact()+".3gp";

        }
        if (((MainActivity) getActivity()).getClient().getFilename() != null) {
            visibl();
            fileName = ((MainActivity) getActivity()).getClient().getFilename();
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(fileName);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (((MainActivity) getActivity()).getClient().isCheck()) {
            menu.getItem(0).setTitle("FINISH");
        }
        menu.getItem(1).setVisible(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rec:
                Intent intent = new Intent(getActivity(), RecordingCoverActivity.class);
                Log.e("recoder", String.valueOf(id));
                intent.putExtra("recid", String.valueOf(id));
                startActivityForResult(intent, REQUEST_CODE_FILE);
                if (!recoder) visibl();
                break;
            case R.id.play:
                if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                break;
            case R.id.pause:
                mediaPlayer.pause();

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FILE:
                    fileName = data.getStringExtra("filename");
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(fileName);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            // если вернулось не ОК
        } else {
            Toast.makeText(getActivity(), "Wrong result", Toast.LENGTH_SHORT).show();
        }
    }

    public void visibl() {
        play.setVisibility(View.VISIBLE);
        stop.setVisibility(View.VISIBLE);
        recoder = true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if (((MainActivity) getActivity()).getClient().isCheck()) {
                update();
                Toast.makeText(getActivity(), "Customer updated", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).showScreen(new FragmentList(), FragmentList.TAG, true);
                return true;
            }
            if (fileName == null) {
                Toast.makeText(getActivity(), "Please Recodering info", Toast.LENGTH_SHORT).show();
            } else {
                ((MainActivity) getActivity()).getClient().setFilename(fileName);
                ((MainActivity) getActivity()).getClient().setCheck(false);
                ((MainActivity) getActivity()).showScreen(new FragmentInfo(), FragmentInfo.TAG, true);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void update() {
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        String name = ((MainActivity) getActivity()).getClient().getProfilename();
        String lastname = ((MainActivity) getActivity()).getClient().getLast();
        String desc = ((MainActivity) getActivity()).getClient().getDesc();
        double lat = ((MainActivity) getActivity()).getClient().getLat();
        double longet = ((MainActivity) getActivity()).getClient().getLonget();
        String filename = ((MainActivity) getActivity()).getClient().getFilename();
        String image = null;
        String phone = ((MainActivity) getActivity()).getClient().getPhone();
        int id = ((MainActivity) getActivity()).getClient().getRecid();
        if (((MainActivity) getActivity()).getClient().getImagename() != null)
            image = ((MainActivity) getActivity()).getClient().getImagename();
        db.updateContact(name, lastname, desc, lat, longet, filename, image, phone, id);
        ((MainActivity) getActivity()).getClient().clear();
    }
}