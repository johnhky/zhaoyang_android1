package com.doctor.sun.media;

import android.media.MediaPlayer;
import android.net.Uri;

import com.doctor.sun.AppContext;

/**
 * Created by rick on 18/4/2016.
 */
public class AudioController {
    private static AudioController instance;

    private MediaPlayer mediaPlayer;
    private State state = new State();

    public static AudioController getInstance() {
        if (instance == null) {
            instance = new AudioController();
        }
        return instance;
    }

    public void play(String data, MediaPlayer.OnCompletionListener listener) {
        if (stopIfCurrentPlaying(data)) return;
        //第一次尝试播放audio,或者点击的是别的audio,停止当前播放的audio
        if (mediaPlayer != null) {
            state.onCompletion(mediaPlayer);
        }
        //记录这一次的state
        state.setData(data).setListener(listener);
        //开始播放新的短语音
        startPlay(data, state);
    }

    public void replayCurrent() {
        State clone = state.clone();
        stopIfCurrentPlaying(state.data);
        state = clone;
        startPlay(clone.data, clone);
    }

    private boolean stopIfCurrentPlaying(String data) {
        if (state.currentPlaying(data)) {
            //点击的是当前正在播放的audio, 停止当前audio
            state.onCompletion(mediaPlayer);
            return true;
        }
        return false;
    }

    private void startPlay(String data, MediaPlayer.OnCompletionListener listener) {
        mediaPlayer = MediaPlayer.create(AppContext.me(), Uri.parse(data));
        mediaPlayer.setOnCompletionListener(listener);
        mediaPlayer.start();
    }


    private static class State implements MediaPlayer.OnCompletionListener, Cloneable {
        private String data = "";
        private MediaPlayer.OnCompletionListener listener;

        private State setData(String data) {
            this.data = data;
            return this;
        }

        private State setListener(MediaPlayer.OnCompletionListener listener) {
            this.listener = listener;
            return this;
        }

        private State reset() {
            return setData("").setListener(null);
        }

        private boolean currentPlaying(String data) {
            return this.data.equals(data);
        }

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (listener != null) {
                listener.onCompletion(mediaPlayer);
                reset();
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        }

        @Override
        public State clone() {
            return new State().setData(data).setListener(listener);
        }
    }
}
