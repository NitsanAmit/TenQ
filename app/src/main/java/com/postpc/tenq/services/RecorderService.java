package com.postpc.tenq.services;

import android.media.MediaRecorder;
import android.util.Log;

public class RecorderService {

    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    private MediaRecorder mRecorder;
    private Thread runner;
    private boolean isRecorderOn = true; // TODO: change to false when not debugging

    public RecorderService() {

        if (runner == null && isRecorderOn)
        {
            runner = new Thread(){
                public void run()
                {
                    while (runner != null)
                    {
                        try
                        {
                            Thread.sleep(6000);
                        }
                        catch (InterruptedException e) { };
                        Log.i("amplitude", String.valueOf(getAmplitude()));
                    }
                }
            };
            runner.start();
            Log.d("Noise", "start runner()");
        }
    }

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
                android.util.Log.e("[Monkey]", "IOException: " + android.util.Log.getStackTraceString(ioe));

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }
            try
            {
                mRecorder.start();
            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }

            //mEMA = 0.0;
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

