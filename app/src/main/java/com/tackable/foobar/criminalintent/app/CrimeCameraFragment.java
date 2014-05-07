package com.tackable.foobar.criminalintent.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by stevenwoo on 5/5/14.
 */
public class CrimeCameraFragment extends Fragment {
    private static final String TAG = "CrimeCameraFragment";
    public static final String EXTRA_PHOTO_FILENAME = "com.tackable.foobar.criminalintent.photo_filename";

    private android.hardware.Camera mCamera;
    private SurfaceView mSurfaceView;
    private View mProgressContainer;

    private android.hardware.Camera.ShutterCallback mShutterCallback = new android.hardware.Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };
    private android.hardware.Camera.PictureCallback mJpegCallback = new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
            // create a filename
            String filename = UUID.randomUUID().toString() + ".jpg";
            // save the jpeg data to disk
            FileOutputStream os = null;
            boolean success = true;
            try {
                os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                os.write(data);
            } catch (Exception e) {
                Log.e(TAG, "Error writing to photo file " + filename, e);
                success = false;
            } finally {
                try {
                    if (os != null)
                        os.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error closing output file " + filename, e);
                    success = false;
                }
            }

                // set the photo filename on the result intent
                if (success) {
                    Intent i = new Intent();
                    i.putExtra(EXTRA_PHOTO_FILENAME, filename);
                    Log.v(TAG, "intent saving photo name");
                    getActivity().setResult(Activity.RESULT_OK, i);
                } else {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                }
            getActivity().finish();
        }
    };

    @TargetApi(11)
    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            mCamera = android.hardware.Camera.open(0);
        }
        else{
            mCamera = android.hardware.Camera.open();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if( mCamera != null ){
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_camera, container, false);
        Button takePictureButton = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if( mCamera != null ){
                    mCamera.takePicture(mShutterCallback, null, mJpegCallback);
                }
            }
        });
        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if( mCamera != null){
                        mCamera.setPreviewDisplay(holder);
                    }
                }
                catch (IOException exception){
                    Log.e(TAG, "Error setting up camera preview display", exception);
                }
            }



            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if( mCamera == null ){
                    return;
                }
                android.hardware.Camera.Parameters parameters = mCamera.getParameters();
                android.hardware.Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
                parameters.setPreviewSize(s.width, s.height);
                mCamera.setParameters(parameters);
                try{
                    mCamera.startPreview();
                }
                catch (Exception e ){
                    Log.e(TAG, "Could not start preview", e);
                    mCamera.release();
                    mCamera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if( mCamera != null ){
                    mCamera.stopPreview();
                }
            }
        });
        mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);
        return v;
    }

    private android.hardware.Camera.Size getBestSupportedSize(List<android.hardware.Camera.Size> sizes, int width, int height){
        android.hardware.Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for(android.hardware.Camera.Size s: sizes){
            int area = s.width * s.height;
            if( area > largestArea ){
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }
}
