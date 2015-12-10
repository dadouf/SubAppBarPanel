package com.davidferrand.subappbarpanelsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.davidferrand.subappbarpanel.SubAppBarPanel;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SubAppBarPanel panel;

    private ImageView expandIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        panel = (SubAppBarPanel) findViewById(R.id.main_panel);
        expandIndicator = (ImageView) findViewById(R.id.main_toolbar_indicator);

        setSupportActionBar(toolbar);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panel.toggle();
            }
        });

        panel.setOnPanelMovementListener(new SubAppBarPanel.OnPanelMovementListener() {
            @Override
            public void onPanelMovementStarted(boolean expanding) {
                expandIndicator.setImageResource(expanding ?
                        R.drawable.ic_expand_less_white_24dp :
                        R.drawable.ic_expand_more_white_24dp);
            }

            @Override
            public void onPanelMovementEnded(boolean expanded) {

            }
        });
    }
}
