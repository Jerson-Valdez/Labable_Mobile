package com.example.labable_mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileHeader {

    private final Activity activity;
    private final AccountManager accountManager;

    public ProfileHeader(Activity activity, View headerRoot, AccountManager accountManager) {
        this.activity = activity;
        this.accountManager = accountManager;

        ImageButton profileButton = headerRoot.findViewById(R.id.profileButton);
        if (profileButton != null) {
            profileButton.setOnClickListener(v -> showProfileMenu());
        }
    }

    private void showProfileMenu() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_profile, null);

        TextView deleteAccount = dialogView.findViewById(R.id.profileDeleteAccount);
        TextView logout = dialogView.findViewById(R.id.profileLogout);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        deleteAccount.setOnClickListener(v -> {
            dialog.dismiss();
            confirmDeleteAccount();
        });

        logout.setOnClickListener(v -> {
            dialog.dismiss();
            performLogout();
        });

        dialog.show();
    }

    private void confirmDeleteAccount() {
        final Account current = accountManager.getLoggedInAccount();
        if (current == null) {
            return;
        }

        new AlertDialog.Builder(activity)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    accountManager.deleteAccount(current);
                    accountManager.logout();
                    goToLogin(null);
                })
                .show();
    }

    private void performLogout() {
        Account account = accountManager.getLoggedInAccount();
        accountManager.logout();
        goToLogin(account);
    }

    private void goToLogin(Account account) {
        Intent intent = new Intent(activity, Login.class);
        intent.putExtra("accountManager", accountManager);

        if (account != null && account.isRemember()) {
            intent.putExtra("email", account.getEmail());
            intent.putExtra("password", account.getPassword());
        }

        activity.startActivity(intent);
        activity.finish();
    }
}
