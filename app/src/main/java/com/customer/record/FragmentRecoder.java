package com.customer.record;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.customer.ActivityMain;
import com.customer.DatabaseAdapter;
import com.customer.FragmentInfo;
import com.customer.FragmentList;
import com.customer.R;

import java.io.File;
import java.io.IOException;

public class FragmentRecoder extends android.support.v4.app.Fragment implements View.OnClickListener {
    public final static String TAG = "FragmentRecoder";
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new DatabaseAdapter(getActivity());
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_recoder, container, false);
        setView();
        if (((ActivityMain) getActivity()).getClientItem().isCheck())
            id = ((ActivityMain) getActivity()).getClientItem().getFilename();
        else {
            DatabaseAdapter db = new DatabaseAdapter(getActivity());
            id = Environment.getExternalStorageDirectory().getAbsolutePath() + "/." + getResources().getString(R.string.app_name) + "/record/" + "/record_" + db.insertDummyContact() + ".3gp";

        }
        if ((((ActivityMain) getActivity()).getClientItem().getFilename() != null) && (new File(((ActivityMain) getActivity()).getClientItem().getFilename()).exists())) {
            visibly();
            fileName = ((ActivityMain) getActivity()).getClientItem().getFilename();
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

    public void setView() {
        rec = (ImageView) view.findViewById(R.id.rec);
        stop = (ImageView) view.findViewById(R.id.pause);
        play = (ImageView) view.findViewById(R.id.play);
        rec.setOnClickListener(this);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rec:
                Intent intent = new Intent(getActivity(), ActivityRecordingCover.class);
                intent.putExtra("recid", String.valueOf(id));
                startActivityForResult(intent, REQUEST_CODE_FILE);
                if (!recoder) visibly();
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
        } else {
            Toast.makeText(getActivity(), "Wrong result", Toast.LENGTH_SHORT).show();
        }
    }

    public void visibly() {
        play.setVisibility(View.VISIBLE);
        stop.setVisibility(View.VISIBLE);
        recoder = true;

    }

    public void update() {
        DatabaseAdapter db = new DatabaseAdapter(getActivity());
        String name = ((ActivityMain) getActivity()).getClientItem().getProfilename();
        String lastname = ((ActivityMain) getActivity()).getClientItem().getLast();
        String desc = ((ActivityMain) getActivity()).getClientItem().getDesc();
        double lat = ((ActivityMain) getActivity()).getClientItem().getLat();
        double longet = ((ActivityMain) getActivity()).getClientItem().getLonget();
        String filename = ((ActivityMain) getActivity()).getClientItem().getFilename();
        String image = null;
        String phone = ((ActivityMain) getActivity()).getClientItem().getPhone();
        int id = ((ActivityMain) getActivity()).getClientItem().getRecid();
        if (((ActivityMain) getActivity()).getClientItem().getImagename() != null && (new File(Uri.parse(((ActivityMain) getActivity()).getClientItem().getImagename()).getPath()).exists()))
            image = ((ActivityMain) getActivity()).getClientItem().getImagename();
        db.updateContact(name, lastname, desc, lat, longet, filename, image, phone, id);
        ((ActivityMain) getActivity()).getClientItem().clear();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (((ActivityMain) getActivity()).getClientItem().isCheck()) {
            menu.getItem(0).setTitle(R.string.finish);
        }
        menu.getItem(1).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionSettings) {
            if (fileName == null) {
                Toast.makeText(getActivity(), R.string.record, Toast.LENGTH_SHORT).show();
                return true;
            }
            if ((((ActivityMain) getActivity()).getClientItem().isCheck())) {
                update();
                Toast.makeText(getActivity(), R.string.update, Toast.LENGTH_SHORT).show();
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                ((ActivityMain) getActivity()).showScreen(new FragmentList(), FragmentList.TAG, true);
                return true;
            }
            if (mediaPlayer.isPlaying()) mediaPlayer.pause();
            ((ActivityMain) getActivity()).getClientItem().setFilename(fileName);
            ((ActivityMain) getActivity()).getClientItem().setCheck(false);
            ((ActivityMain) getActivity()).showScreen(new FragmentInfo(), FragmentInfo.TAG, true);
        }

        return true;
    }
}