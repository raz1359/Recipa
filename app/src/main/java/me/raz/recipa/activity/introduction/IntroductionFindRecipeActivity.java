package me.raz.recipa.activity.introduction;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import me.raz.recipa.R;
import me.raz.recipa.activity.introduction.IntroductionSharingActivity;


public class IntroductionFindRecipeActivity extends AppCompatActivity {
    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intredaction1);

        button = findViewById(R.id.btn1);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(IntroductionFindRecipeActivity.this, IntroductionSharingActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        });
    }
}