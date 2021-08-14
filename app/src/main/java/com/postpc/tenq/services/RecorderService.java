package com.postpc.tenq.services;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.util.Log;
import android.os.Handler;

import static java.lang.Math.abs;
import static java.lang.Math.exp;

public class RecorderService {

    public static final int MILLIS = 6000;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    private MediaRecorder mRecorder;
    private static Thread runner;
    private boolean isRecorderOn = false; // need to be true for recording
    private double prevAmplitude;
    private final AudioManager audioManager;

    public RecorderService(Context applicationContext) {

        this.audioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
        this.prevAmplitude = getAmplitude();

        if (runner == null && isRecorderOn)
        {
            runner = new Thread(() -> {
                while (runner != null)
                {
                    try
                    {
                        Thread.sleep(MILLIS);
                    }
                    catch (InterruptedException e) {
                        android.util.Log.e("[Recorder]", "InterruptedException: " + android.util.Log.getStackTraceString(e));
                    }

                    double currentAmplitude = getAmplitude();
                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                    // TODO: maybe change it to decreasing / increasing by 1 each time the amplitude is lower / higher
                    //  respectively instead of changing the volume using the amplitude value
                    if (abs(currentAmplitude - prevAmplitude) > (double) (32767 / maxVolume)) {
                        int newVolume = (int) (( currentAmplitude / 32767 ) * maxVolume);

                        // add lower bound so music won't shut down when there are no noises
                        if (newVolume > 4) {
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
                        }
                        android.util.Log.e("[volume]", String.valueOf(newVolume));
                    }
                    prevAmplitude = currentAmplitude;
                    //mHandler.post(runner);
                }
            });
            runner.start();
        }
    }

    private final Runnable updater = new Runnable(){
        public void run(){
            double db = soundDb(1);
        }
    };
    private final Handler mHandler = new Handler();

    public void startRecorder(){
        if (mRecorder == null)
        {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try
            {
                mRecorder.prepare();
            }catch (java.io.IOException ioe) {
                android.util.Log.e("[Recorder]", "IOException: " + android.util.Log.getStackTraceString(ioe));

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Recorder]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }
            try
            {
                mRecorder.start();
            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Recorder]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }

        }

    }

    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double soundDb(double ampl){
        return 20 * Math.log10(getAmplitudeEMA() / ampl);
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude());
        else
            return 0;

    }
    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    public void setRecorderOn(boolean isRecorderOn) {
        this.isRecorderOn = isRecorderOn;
    }

    public boolean getRecorderOn() {
        return this.isRecorderOn;
    }

    public void turnRecorderOn() {
        this.isRecorderOn = true;
    }
}

