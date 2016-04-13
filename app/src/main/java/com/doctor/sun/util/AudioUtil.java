package com.doctor.sun.util;

import android.media.MediaRecorder;

import java.io.IOException;

/**
 * Created by rick on 13/4/2016.
 */
public class AudioUtil {

    private MediaRecorder recorder;

    private static AudioUtil instance;

    public static AudioUtil getInstance() {
        if (instance == null) {
            instance = new AudioUtil();
        }
        return instance;
    }

    public MediaRecorder createRecorder() {
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile("");
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            recorder.prepare();
            return recorder;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
