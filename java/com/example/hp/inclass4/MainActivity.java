/*
In-Class Assignment #4
MainActivity.java
Ryan Harris
 */
package com.example.hp.inclass4;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    Handler handler;
    ProgressBar progressBar;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Display Image");

        progressBar = findViewById(R.id.progress_bar);
        imageView = findViewById(R.id.imageView);
        Button loadThread = findViewById(R.id.load_using_thread);
        Button loadAsync = findViewById(R.id.load_using_async);

        loadThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new LoadThread(), "Image 1");
                thread.start();
            }
        });


        loadAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadAsync().execute("https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg","abc","def");
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.d("demo", "Message Received...." + msg.what);
                if(msg.what == 1){
                    progressBar.setProgress((Integer) msg.obj);
                } else if (msg.what == 2){
                    imageView.setImageBitmap((Bitmap) msg.obj);
                    progressBar.setProgress((Integer) 0);
                }
                return false;
            }
        });
    }

    class LoadThread implements Runnable {

        @Override
        public void run() {
            Log.d("demo", "Started Work!");
            Bitmap myBitmap =
                    getImageBitmap("https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg");
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100000; j++) {
                }
                Message message = new Message();
                message.obj = i;
                message.what = 1;
                handler.sendMessage(message);
            }
            Message message2 = new Message();
            message2.what = 2;
            message2.obj = myBitmap;
            handler.sendMessage(message2);
            Log.d("demo", "Ended Work");
        }

        public Bitmap getImageBitmap(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //Params, Progress, Results
    class LoadAsync extends AsyncTask<String, Integer, Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            imageView.setImageBitmap((Bitmap) s);
            progressBar.setProgress((Integer) 0);
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress((Integer) values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            if(urls[0] != null && !urls[0].equals("")){
                Bitmap myBitmap =
                        getImageBitmap(urls[0]);
                for (int i = 0; i < 100; i++) {
                    for (int j = 0; j < 100000; j++) {
                    }
                    publishProgress(i);
                }
                return myBitmap;
            }

            return null;
        }
        Bitmap getImageBitmap(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}




