package com.code2concept.blurbehindalertdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.code2concept.blurbehindalertdialog.utils.AppUtils;
import com.code2concept.blurbehindalertdialog.views.BlurView;

public class MainActivity extends Activity {

    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.blurButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Code2Concept - Blur");
                builder.setMessage("This is Blur Demo.Click Ok to exit");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                alert = builder.create();

               new BlurAsyncTask().execute();

            }
        });
    }

    class BlurAsyncTask extends AsyncTask<Void, Integer, Bitmap> {

        private  final String TAG = BlurAsyncTask.class.getName();

        protected Bitmap doInBackground(Void...arg0) {

            Bitmap map  = AppUtils.takeScreenShot(MainActivity.this);
            Bitmap fast = new BlurView().fastBlur(map, 10);

            return fast;
        }


        protected void onPostExecute(Bitmap result) {
            if (result != null){
                final Drawable draw=new BitmapDrawable(getResources(),result);
                Window window = alert.getWindow();
                window.setBackgroundDrawable(draw);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.setGravity(Gravity.CENTER);
                alert.show();

                TextView textView = (TextView) alert.findViewById(android.R.id.title);
                if (textView != null)
                    textView.setGravity(Gravity.CENTER);
            }

        }
    }
}
