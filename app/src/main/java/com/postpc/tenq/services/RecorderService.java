package com.postpc.tenq.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaRecorder;

import java.util.Set;

import static java.lang.Math.abs;

public class RecorderService {

    private static double mEMA = 0.0;
    private static final int MILLIS = 6000;
    private static final double EMA_FILTER = 0.6;
    private static final int MAX_AMPLITUDE = 32767;

    private MediaRecorder mRecorder;
    private static Thread runner;
    // true if user set the recording option on, needs bluetooth connection in order to be active
    private boolean userSetRecorderOn;
    private boolean isRecorderOn;
    private boolean deviceConnected;
    private double prevAmplitude;
    private final AudioManager audioManager;

    public RecorderService(Context applicationContext) {

        this.audioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
        this.prevAmplitude = getAmplitude();
        this.userSetRecorderOn = true;
        this.isRecorderOn = true;
        this.deviceConnected = false;
        runner = null;

        if (BluetoothAdapter.getDefaultAdapter() != null) {
            Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            this.deviceConnected = pairedDevices.size() > 0;
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                    deviceConnected = true;
                    if (userSetRecorderOn) {
                        startRecorder();
                    }
                } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                    deviceConnected = false;
                    if (userSetRecorderOn) { // if the user didn't want the recorder to be on there is nothing to stop
                        stopRecorder();
                    }
                }
            }
        };
        applicationContext.registerReceiver(mReceiver, filter);
    }

    public void startRecorder(){

        isRecorderOn = true;

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

        if (runner == null)
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

                    if (abs(currentAmplitude - prevAmplitude) > (double) (MAX_AMPLITUDE / maxVolume)) {
                        int newVolume = (int) (( currentAmplitude / MAX_AMPLITUDE ) * maxVolume);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (Math.max(newVolume, 4)), 0);
                    }
                    android.util.Log.e("[Recorder]", "amplitude: " + currentAmplitude);
                    prevAmplitude = currentAmplitude;
                }
            });
            runner.start();
        }

    }

    public void stopRecorder() {

        isRecorderOn = false;

        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        runner = null;
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

    public void setUserSetRecorderOn(boolean userSetRecorderOn) {
        this.userSetRecorderOn = userSetRecorderOn;
    }

    public boolean isUserSetRecorderOn() {
        return this.userSetRecorderOn;
    }

    public boolean isDeviceConnected() {
        return deviceConnected;
    }

    public boolean isRecorderOn() {
        return isRecorderOn;
    }
}

