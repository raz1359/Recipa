package me.raz.recipa.activity.introduction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

import me.raz.recipa.R;
import me.raz.recipa.activity.introduction.IntroductionGroceriesCreationActivity;

public class IntroductionSharingActivity extends AppCompatActivity {

    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intredaction2);

        button = findViewById(R.id.btn2);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(IntroductionSharingActivity.this, IntroductionGroceriesCreationActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        });


    }
}