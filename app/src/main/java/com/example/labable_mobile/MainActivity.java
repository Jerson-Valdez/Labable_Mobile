package com.example.labable_mobile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        accountManager = new AccountManager();

        Account account = new Account(
                "ACC-1",
                new HashMap<>(),
                "A",
                "09934128934",
                "a123456",
                "j@gmail.com",
                "Janver Flores"
        );

        accountManager.addAccount(account);

        VideoView videoView = findViewById(R.id.advertisement);

        MediaController controller = new MediaController(this);
        controller.setAnchorView(videoView);
        videoView.setMediaController(controller);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.advertisement);
        videoView.setVideoURI(video);

        videoView.setOnPreparedListener(player -> {
            player.setLooping(true);
            videoView.start();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        intent.putExtra("accountManager", accountManager);
        startActivity(intent);
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("accountManager", accountManager);
        startActivity(intent);
    }

    public static int dpToPx(Context ctx, int px) {
        return (int) ctx.getResources().getDisplayMetrics().density * px;
    }
}