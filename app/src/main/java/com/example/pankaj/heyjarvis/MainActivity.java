package com.example.pankaj.heyjarvis;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;

import ai.kitt.snowboy.AppResCopy;
import ai.kitt.snowboy.MsgEnum;
import ai.kitt.snowboy.audio.AudioDataSaver;
import ai.kitt.snowboy.audio.RecordingThread;

public class MainActivity extends AppCompatActivity {
    RecordingThread recordingThread;
    Boolean isDetectionOn = false;

    private TextToSpeech tts;
    TextView tv ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.hw);
       // tts = new TextToSpeech(this, this);
        initHotword();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordingThread.startRecording();
                Toast.makeText(MainActivity.this, "Recording started", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(recordingThread !=null && !isDetectionOn) {


            isDetectionOn = true;
        }
    }

    private void initHotword() {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                AppResCopy.copyResFromAssetsToSD(this);

                recordingThread = new RecordingThread(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        MsgEnum message = MsgEnum.getMsgEnum(msg.what);
                        switch(message) {
                            case MSG_ACTIVE:
                                //HOTWORD DETECTED. NOW DO WHATEVER YOU WANT TO DO HERE
                                Toast.makeText(MainActivity.this, "YES BOSS", Toast.LENGTH_SHORT).show();
                                //
                               // recordingThread.stopRecording();
                                break;
                            case MSG_INFO:
                                break;
                            case MSG_VAD_SPEECH:
                                break;
                            case MSG_VAD_NOSPEECH:
                                break;
                            case MSG_ERROR:
                                break;
                            default:
                                super.handleMessage(msg);
                                break;
                        }
                    }
                }, new AudioDataSaver());
            }
        }

    @Override
    protected void onStop() {
        super.onStop();
        if(recordingThread !=null && isDetectionOn){
            recordingThread.stopRecording();
            Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
            isDetectionOn = false;
        }
    }
}

