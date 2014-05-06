package com.tackable.foobar.criminalintent.app;

import android.annotation.TargetApi;
import android.graphics.Camera;
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

import java.io.IOException;
import java.util.List;

/**
 * Created by stevenwoo on 5/5/14.
 */
public class CrimeCameraFragment extends Fragment {
    private static final String TAG = "CrimeCameraFragment";

    private android.hardware.Camera mCamera;
    private SurfaceView mSurfaceView;

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
                getActivity().finish();
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
        return v;
    }
}
