package com.example.labable_mobile;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Terms extends AppCompatActivity  {
    private boolean accepted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terms);

        accepted = getIntent().getBooleanExtra("accepted", false);

        Button termsConfirmation = findViewById(R.id.termsConfirmation);

        if (accepted) {
            termsConfirmation.setBackgroundTintList(
                    ColorStateList.valueOf(getResources().getColor(R.color.green_light))
            );
        }

        termsConfirmation.setOnClickListener(v -> {
            accepted = !accepted;   

            if (accepted) {
                termsConfirmation.setBackgroundTintList(
                        ColorStateList.valueOf(getResources().getColor(R.color.green_light))
                );
            } else {
                termsConfirmation.setBackgroundTintList(
                        ColorStateList.valueOf(getResources().getColor(R.color.black))
                );
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("accepted", accepted);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
