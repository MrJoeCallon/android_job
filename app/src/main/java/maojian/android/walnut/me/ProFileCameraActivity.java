package maojian.android.walnut.me;

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import maojian.android.walnut.*;
import maojian.android.walnut.ImagePicker.ImagePickerActivity;
import maojian.android.walnut.utils.eventbus.PostSuccessEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import maojian.android.walnut.R;

/**
 * Created by android on 5/8/16.
 */

public class ProFileCameraActivity extends FragmentActivity implements OnClickListener, PictureCallback {
    private CameraSurfacePreview mCameraSurPreview = null;
    private ImageButton mCaptureButton = null;
    //private ProgressBar mVideoCapture;

    private RoundProgressBar mVideoCapture;

    private String TAG = "Dennis";

    private int mOrientation = 0;

    private OrientationEventListener mOrientationListener; // screen orientation listener
    private boolean mScreenProtrait = true;
    private boolean mCurrentOrient = false;

    private ImageView camera_shading;
    private ImageView camera_switchorientation;


    private Camera mCamera;

    // 0表示后置，1表示前置
    private int cameraPosition = 1;

    private ImageButton camera_switchcamera;
    private ImageButton camera_return;
    private ImageButton camera_library;

    private boolean isVideo = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_procamera);

        isVideo = false;



        // Create our Preview view and set it as the content of our activity.
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
//
//        mCamera = Camera.open();;
//
       mCameraSurPreview = new CameraSurfacePreview(this);
        preview.addView(mCameraSurPreview);
        EventBus.getDefault().register(this);
        // Add a listener to the Capture button
        mCaptureButton = (ImageButton) findViewById(R.id.button_capture);

//        mCaptureButton.setEnabled(false);

        mCaptureButton.setOnClickListener(this);


        mVideoCapture = (RoundProgressBar) findViewById(R.id.loading_process_dialog_progressBar);

        mVideoCapture.setProgress(0);

        //preview.addView(mCaptureButton);

//        camera_shading = (ImageView) findViewById(R.id.camera_shading);
//        camera_shading.getBackground().setAlpha(180);

//        camera_switchorientation = (ImageView) findViewById(R.id.camera_switchorientation);
//
//        startOrientationChangeListener();

        camera_switchcamera = (ImageButton) findViewById(R.id.camera_switchcamera_button);
        camera_switchcamera.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 切换前后摄像头

                        mCameraSurPreview.cameraSwitch();

                    }
                }
        );



        camera_return = (ImageButton) findViewById(R.id.camera_return_button);
        camera_return.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //mCamera.release();
                        ProFileCameraActivity.this.finish();
                    }
                }
        );




    }


    @Override
    protected void onResume() {

        super.onResume();


        camera_shading = (ImageView) findViewById(R.id.camera_shading);

        isVideo = false;


        Log.e("abc", "cameraactivity resume");

    }

    private final void startOrientationChangeListener() {
        mOrientationListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
                if (((rotation >= 0) && (rotation <= 45)) || (rotation >= 315)||((rotation>=135)&&(rotation<=225))) {//portrait
                    mCurrentOrient = true;
                    if(mCurrentOrient!=mScreenProtrait)
                    {
                        mScreenProtrait = mCurrentOrient;
                        //OrientationChanged(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        camera_shading.getBackground().setAlpha(180);
                        mCaptureButton.setEnabled(false);
                        camera_switchorientation.setVisibility(View.VISIBLE);
                        Log.d(TAG, "Screen orientation changed from Landscape to Portrait!");
                    }
                }
                else if (((rotation > 45) && (rotation < 135))||((rotation>225)&&(rotation<315))) {//landscape
                    mCurrentOrient = false;
                    if(mCurrentOrient!=mScreenProtrait)
                    {
                        mScreenProtrait = mCurrentOrient;
                        //OrientationChanged(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        camera_shading.getBackground().setAlpha(0);
                        mCaptureButton.setEnabled(true);
                        camera_switchorientation.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "Screen orientation changed from Portrait to Landscape!");
                    }
                }
            }
        };
        mOrientationListener.enable();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        //save the picture to sdcard
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null){
            Log.d(TAG, "Error creating media file, check storage permissions: ");
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

        // Restart the preview and re-enable the shutter button so that we can take another picture
        camera.startPreview();

        //See if need to enable or not
        mCaptureButton.setEnabled(true);

        EditprofileActivity.profileImage = pictureFile.toString();
        finish();

//        Intent intent = new Intent(ProFileCameraActivity.this, UploadActivity.class);
//        Log.e("abc","picturefile path: "+pictureFile.toString());
//        intent.putExtra("filepath", pictureFile.toString());
//        intent.putExtra("isVideo",false);
//        startActivity(intent);


    }

    @Override
    public void onClick(View v) {
        mCaptureButton.setEnabled(false);

        // get an image from the camera
        if (!isVideo) {
            mCameraSurPreview.takePicture(this);
        }
    }

    private File getOutputMediaFile(){
        //get the mobile Pictures directory

        //get the current time
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return new File(BaseConstant.getAppPicFile() + "/IMAGE_"+ timeStamp + ".jpg");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(PostSuccessEvent mFinishActivityEvent) {
        finish();
    }

}