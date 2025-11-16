package com.example.labable_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    private AccountManager accountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Bundle extras = getIntent().getExtras();

        accountManager = (AccountManager) extras.getSerializable("accountManager");

        LinearLayout emailContainer = findViewById(R.id.loginEmailContainer);
        EditText email = findViewById(R.id.loginEmail);
        LinearLayout passwordContainer = findViewById(R.id.loginPasswordContainer);
        EditText password = findViewById(R.id.loginPassword);
        CheckBox rememberMe = findViewById(R.id.loginRemember);
        Button loginBtn = findViewById(R.id.loginButton);
        LinearLayout googleBtn = findViewById(R.id.loginGoogleButton);

        email.addTextChangedListener(Form.emailListener(true, accountManager, emailContainer));
        password.addTextChangedListener(Form.passwordListener(true, passwordContainer));
        rememberMe.setOnClickListener(v -> {
            if (rememberMe.isChecked()) {
                rememberMe.setButtonDrawable(R.drawable.check_vector);
            } else {
                rememberMe.setButtonDrawable(R.drawable.form_circle_field);
            }
        });

        emailContainer.setOnClickListener(v -> {
            email.requestFocus();
        });
        passwordContainer.setOnClickListener(v -> {
            password.requestFocus();
        });

        if (extras.get("email") != null) {
            String rememberedEmail = extras.getString("email");
            String rememberedPassword = extras.getString("password");

            email.setText(rememberedEmail);
            password.setText(rememberedPassword);
            rememberMe.performClick();
        }

        loginBtn.setOnClickListener(v -> {
            if (!Form.loginFormValid) {
                new AlertDialog.Builder(this)
                        .setTitle("Login Failed")
                        .setNegativeButton("Close", null)
                        .setMessage("Enter your credentials first!")
                        .show();
                return;
            }
            try {
                Account account = accountManager.getAccountByEmailAndPassword(email.getText().toString(), password.getText().toString());

                Log.i("Login", String.valueOf(rememberMe.isChecked()));
                account.setRemember(rememberMe.isChecked());
                accountManager.login(account);
                Intent intent = new Intent(this, CustomerDashboard.class);
                intent.putExtra("accountManager", accountManager);
                startActivity(intent);
                finish();
            } catch (Error e) {
                new AlertDialog.Builder(this)
                    .setTitle("Login Failed")
                    .setNegativeButton("Close", null)
                    .setMessage("Credentials entered are invalid! Please try again.")
                    .show();
            }
        });

        googleBtn.setOnClickListener(Form.alternativeAccessListener());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        intent.putExtra("accountManager", accountManager);
        startActivity(intent);
    }
}
