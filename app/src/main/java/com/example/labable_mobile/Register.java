package com.example.labable_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.HashMap;

public class Register extends AppCompatActivity {
    private AccountManager accountManager;
    private CheckBox agreement;
    private boolean accepted;
    private ActivityResultLauncher<Intent> termsLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        accountManager = (AccountManager) getIntent().getExtras().getSerializable("accountManager");
        accepted = false;

        termsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        accepted = result.getData().getBooleanExtra("accepted", false);
                        if (accepted) {
                            agreement.setButtonDrawable(R.drawable.check_vector);
                            agreement.setChecked(true);
                        } else {
                            agreement.setButtonDrawable(R.drawable.form_circle_field);
                            agreement.setChecked(false);
                        }
                    }
                }
        );

        EditText firstName = findViewById(R.id.registerFirstName);
        EditText lastName = findViewById(R.id.registerLastName);
        EditText email = findViewById(R.id.registerEmail);
        EditText phone = findViewById(R.id.registerPhone);
        EditText address = findViewById(R.id.registerAddress);
        EditText password = findViewById(R.id.registerPassword);
        EditText confirmPassword = findViewById(R.id.registerConfirmPassword);
        agreement = findViewById(R.id.registerTerms);
        Button registerBtn = findViewById(R.id.registerButton);
        LinearLayout googleBtn = findViewById(R.id.registerGoogleButton);

        firstName.addTextChangedListener(Form.nameListener(firstName));
        lastName.addTextChangedListener(Form.nameListener(lastName));
        email.addTextChangedListener(Form.emailListener(false, accountManager, email));
        phone.addTextChangedListener(Form.phoneListener(phone));
        address.addTextChangedListener(Form.addressListener(address));
        password.addTextChangedListener(Form.passwordListener(false, password));
        confirmPassword.addTextChangedListener(Form.confirmPasswordListener(confirmPassword, password));
        agreement.setOnClickListener(v -> {
            agreement.setChecked(accepted);
            Intent intent = new Intent(this, Terms.class);
            intent.putExtra("accepted", accepted);
            termsLauncher.launch(intent);
        });
        registerBtn.setOnClickListener(v -> {
            if (!Form.registerFormValid || !agreement.isChecked()) {
                if (!agreement.isChecked()) {
                    new AlertDialog.Builder(this)
                            .setTitle("Registration Failed")
                            .setNegativeButton("Close", null)
                            .setMessage("You must agree to the terms of service first!")
                            .show();
                    return;
                }
                new AlertDialog.Builder(this)
                        .setTitle("Registration Failed")
                        .setNegativeButton("Close", null)
                        .setMessage("Fill out every field first!")
                        .show();
                return;
            }
            Account account = new Account(
                    accountManager.generateId(),
                    new HashMap<>(),
                    address.getText().toString(),
                    phone.getText().toString(),
                    password.getText().toString(),
                    email.getText().toString(),
                    firstName.getText().toString() + ' ' + lastName.getText().toString());
            account.setRemember(false);

            accountManager.addAccount(account);

            new AlertDialog.Builder(this)
                    .setTitle("Account Created")
                    .setPositiveButton("Okay", (dialog, which) -> {
                        accountManager.login(account);
                        accountManager.addAccount(account);
                        Intent intent = new Intent(this, CustomerDashboard.class);
                        intent.putExtra("accountManager", accountManager);
                        startActivity(intent);
                        finish();
                    })
                    .setMessage("You have successfully created an account! You will be redirected to the dashboard.")
                    .show();
        });
        googleBtn.setOnClickListener(Form.alternativeAccessListener());

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
}
