package com.customer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Максим on 08.08.2016.
 */
public class RecoderFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    View view;
    ImageView rec;
    ImageView play;
    ImageView stop;
    MediaPlayer mediaPlayer;
    String fileName;
    boolean recoder = false;
    final int REQUEST_CODE_FILE = 1;

    DatabaseAdapter db;
    public final static String TAG = "RecoderFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new DatabaseAdapter(getActivity());
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_recoder, container, false);
        rec = (ImageView) view.findViewById(R.id.rec);
        stop = (ImageView) view.findViewById(R.id.pause);
        play = (ImageView) view.findViewById(R.id.play);
        if (recoder) visibl();
        rec.setOnClickListener(this);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rec:
                Intent intent = new Intent(getActivity(), RecordingCoverActivity.class);
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
            if (fileName == null) {
                Toast.makeText(getActivity(), "Please Recodering info", Toast.LENGTH_SHORT).show();
            } else {
                ((MainActivity) getActivity()).getClient().setFilename(fileName);
                DatabaseAdapter db = new DatabaseAdapter(getActivity());
                ((MainActivity) getActivity()).showScreen(new InfoFragment(), InfoFragment.TAG, true);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}