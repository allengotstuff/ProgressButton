package com.example.allensun.progressbutton;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.progessbuttonlib.ProgressView;

public class MainActivity extends AppCompatActivity {

    double count;

    ProgressView progressView, progressView_1, progressView_2, progressView_3, progressView_4;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.post(task);
        }
    };

    Runnable task = new Runnable() {
        @Override
        public void run() {
            count += 1;
            progressView.updateProgress(count);
            progressView_1.updateProgress(count);
            progressView_2.updateProgress(count);
            progressView_3.updateProgress(count);
            progressView_4.updateProgress(count);

            handler.sendEmptyMessageDelayed(1, 40);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressView = (ProgressView) findViewById(R.id.ProgressView);
        progressView_1 = (ProgressView) findViewById(R.id.ProgressView_1);
        progressView_2 = (ProgressView) findViewById(R.id.ProgressView_2);
        progressView_3 = (ProgressView) findViewById(R.id.ProgressView_3);
        progressView_4 = (ProgressView) findViewById(R.id.ProgressView_4);

        count = 0;

        handler.sendEmptyMessage(1);
    }
}
