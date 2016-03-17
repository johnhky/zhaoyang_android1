package io.ganguo.library.util;

import android.media.MediaRecorder;

import java.io.File;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Audio 工具
 * <p/>
 * Created by Tony on 10/6/15.
 */
public class Audios {

    private static Logger LOG = LoggerFactory.getLogger(Audios.class);

    private static MediaRecorder mRecorder;

    /**
     * 播放音频文件
     *
     * @param file
     */
    public static void startPlay(File file) {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // audio file
        mRecorder.setOutputFile(file.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            LOG.e("play error.", e);
        }
    }

    /**
     * 停止播放音频文件
     */
    public static void stopPlay() {
        try {
            mRecorder.stop();
            mRecorder.reset(); // set state to idle
            mRecorder.release();
            mRecorder = null;
        } catch (Exception e) {
            LOG.e("stop error.", e);
        }
    }

}
