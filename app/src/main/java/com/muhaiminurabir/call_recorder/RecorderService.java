package com.muhaiminurabir.call_recorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;


public class RecorderService extends Service {

    MediaRecorder recorder;
    static final String TAGS = " Inside Service";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        recorder = new MediaRecorder();
        recorder.reset();

        String phoneNumber = intent.getStringExtra("number");
        Log.d(TAGS, "Phone number in service: " + phoneNumber);

        String time = new CommonMethods().getTIme().toString();

        String path = new CommonMethods().getPath().toString();

        String rec = path + "/" + phoneNumber + "_" + time + ".mp4";

        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
            try {
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            } catch (Exception e2) {
                Log.d("Error Line Number", Log.getStackTraceString(e2));
                recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            }
        }
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        recorder.setOutputFile(rec);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();

        Log.d(TAGS, "onStartCommand: " + "Recording started");

        return START_NOT_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();

        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;

        Log.d(TAGS, "onDestroy: " + "Recording stopped");

    }
}
