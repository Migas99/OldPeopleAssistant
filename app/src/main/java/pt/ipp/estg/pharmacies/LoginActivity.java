package pt.ipp.estg.pharmacies;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 100;
    private static final String TAG = "AuthenticationFragment";

    private FirebaseAuth firebaseAuth;

    private LinearLayout emailPasswordButtons;
    private LinearLayout emailPasswordFields;
    private LinearLayout signedInButtons;

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private TextView mEmailField;
    private TextView mPasswordField;

    private Button emailSignInButton;
    private Button emailCreateAccountButton;
    private Button signOutButton;
    private Button verifyEmailButton;
    private Button switchToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.checkAndRequestPermissions();

        //Firebase
        this.firebaseAuth = FirebaseAuth.getInstance();
        //Linear Layouts
        this.emailPasswordButtons = findViewById(R.id.emailPasswordButtons);
        this.emailPasswordFields = findViewById(R.id.emailPasswordFields);
        this.signedInButtons = findViewById(R.id.signedInButtons);
        //Views
        this.mStatusTextView = findViewById(R.id.status);
        this.mDetailTextView = findViewById(R.id.detail);
        this.mEmailField = findViewById(R.id.fieldEmail);
        this.mPasswordField = findViewById(R.id.fieldPassword);
        //Buttons
        this.emailSignInButton = findViewById(R.id.emailSignInButton);
        this.emailCreateAccountButton = findViewById(R.id.emailCreateAccountButton);
        this.signOutButton = findViewById(R.id.signOutButton);
        this.verifyEmailButton = findViewById(R.id.verifyEmailButton);
        this.switchToMenu = findViewById(R.id.switchToMenu);
        //Buttons Listeners
        this.emailSignInButton.setOnClickListener(this);
        this.emailCreateAccountButton.setOnClickListener(this);
        this.signOutButton.setOnClickListener(this);
        this.verifyEmailButton.setOnClickListener(this);
        this.switchToMenu.setOnClickListener(v -> switchToMenu());
    }

    private void checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int permissionMakeCall = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionMakeCall != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        // [START create_user_with_email]
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(this, authResult -> {
            Log.d(TAG, "createUserWithEmail:success");
            FirebaseUser user = firebaseAuth.getCurrentUser();
            updateUI(user);
        }).addOnFailureListener(this, e -> {
            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            updateUI(null);
        });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        // [START sign_in_with_email]
        this.firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(this, authResult -> {
            Log.d(TAG, "signInWithEmail:success");
            FirebaseUser user = firebaseAuth.getCurrentUser();
            Toast.makeText(getApplicationContext(), "Authentication successful.", Toast.LENGTH_SHORT).show();
            updateUI(user);
        }).addOnFailureListener(this, e -> {
            Toast.makeText(getApplicationContext(), "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
            updateUI(null);
        });
        // [END sign_in_with_email]
    }

    private void signOut() {
        firebaseAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        // Disable popUpButton
        this.verifyEmailButton.setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        user.sendEmailVerification().addOnSuccessListener(this, aVoid -> {
            this.verifyEmailButton.setEnabled(true);
            Toast.makeText(getApplicationContext(),
                    "Verification email sent to " + user.getEmail(),
                    Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(this, e -> {
            this.verifyEmailButton.setEnabled(true);
            Toast.makeText(getApplicationContext(),
                    "Failed to send verification email.",
                    Toast.LENGTH_SHORT).show();
        });
        // [END send_email_verification]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            this.emailPasswordButtons.setVisibility(View.GONE);
            this.emailPasswordFields.setVisibility(View.GONE);
            this.signedInButtons.setVisibility(View.VISIBLE);
            this.verifyEmailButton.setEnabled(!user.isEmailVerified());
        } else {
            this.mStatusTextView.setText(R.string.signed_out);
            this.mDetailTextView.setText(null);
            this.emailPasswordButtons.setVisibility(View.VISIBLE);
            this.emailPasswordFields.setVisibility(View.VISIBLE);
            this.signedInButtons.setVisibility(View.GONE);
        }
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.emailCreateAccountButton) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.emailSignInButton) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.signOutButton) {
            signOut();
        } else if (i == R.id.verifyEmailButton) {
            sendEmailVerification();
        }
    }

    private void switchToMenu() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
