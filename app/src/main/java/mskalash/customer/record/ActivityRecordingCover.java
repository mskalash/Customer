package mskalash.customer.record;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.customer.R;

import java.io.File;
import java.io.IOException;

public class ActivityRecordingCover extends AppCompatActivity {

    private ImageView recordIcon;
    protected MediaRecorder recorder = null;
    protected String fileName = null;
    protected Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_cover_activity_layout);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        Intent intent = getIntent();
        fileName = intent.getStringExtra("recid");
        File folder1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/." + getResources().getString(R.string.app_name));
        if (!folder1.exists()) {
            folder1.mkdir();
        }
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/." + getResources().getString(R.string.app_name) + "/record/");
        if (!folder.exists()) {
            folder.mkdir();
        }
        startRecording();
        recordIcon = (ImageView) findViewById(R.id.mic);
        recordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopUpdateRecordInDatabaseAndReturn();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            stopUpdateRecordInDatabaseAndReturn();
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void stopUpdateRecordInDatabaseAndReturn() {
        stopRecording();
        int count = 1;// databaseAdapter.updateRecord(null, null, fileName, Integer.parseInt(recordID));
        if (count != 1) {
            Toast.makeText(this, "The record was NOT properly saved to database", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Nevertheless the record was saved in External Storage in AppRec folder", Toast.LENGTH_LONG).show();
            Log.e("LOG", "The record was NOT properly saved to database. The count of affected rows does not equal to 1");
            Log.w("LOG", "Nevertheless the record was saved in External Storage in AppRec folder");
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("filename", fileName);
        setResult(Activity.RESULT_OK, returnIntent);

        finish();
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setAudioEncodingBitRate(128000);
        recorder.setAudioSamplingRate(44100);
        recorder.setOutputFile(fileName);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("AudioRecord", "prepare() failed");
            Log.e("AudioRecord", "" + e);
        }

        recorder.start();

    }

    private void stopRecording() {
        chronometer.stop();
        recorder.stop();
        recorder.release();
        recorder = null;
    }


}


