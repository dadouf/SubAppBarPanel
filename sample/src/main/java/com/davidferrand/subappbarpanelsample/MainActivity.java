package com.davidferrand.subappbarpanelsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.davidferrand.subappbarpanel.SubAppBarPanel;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SubAppBarPanel panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        panel = (SubAppBarPanel) findViewById(R.id.main_panel);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panel.toggle();
            }
        });
    }
}
