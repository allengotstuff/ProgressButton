package com.example.allensun.progressbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.progessbuttonlib.ProgressView;

public class MainActivity extends AppCompatActivity {

    double count;

    ProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressView = (ProgressView) findViewById(R.id.ProgressView);

        count = 0;

        progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count += 2;
                progressView.updateProgress(count);
            }
        });
    }
}
