package org.turkotron.snapper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.os.Bundle;
import android.hardware.Camera;
import android.util.Log;
import android.hardware.Camera.PictureCallback;
import android.widget.FrameLayout;
import android.view.SurfaceView;
import android.view.View;
import android.os.Environment;
import android.os.Handler;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";
    private Camera mCamera;
    private CameraPreview mPreview;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "Launching Capture");
        mCamera.takePicture(null, null, mPicture);
    }

    protected void onPause() {
        releaseCamera(); // release the camera immediately on pause event
        super.onPause();
    }

    protected void onStop() {
        releaseCamera(); // release the camera immediately on stop event
        super.onStop();
    }

    protected void onDestroy() {
        releaseCamera(); // release the camera immediately on destroy event
        super.onDestroy();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        Log.d("MyCameraApp",
                "Ext storage state:" + Environment.getExternalStorageState());
        File mediaStorageDir = new File(
                Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "CameraTest");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp",
                        "failed to create directory " + mediaStorageDir.getAbsolutePath());
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
            .format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            Log.d("MyCameraApp", mediaStorageDir.getPath() + File.separator + "IMG_"
                    + timeStamp + ".jpg");
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"
                    + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"
                    + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }
    }
}
