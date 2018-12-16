package auth.example.andy.authenticationapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
https://www.safaribooksonline.com/library/view/mastering-firebase-for/9781788624718/91898549-9f8b-4ea5-a04e-2646ef184baa.xhtml
 */
public class MainActivity extends AppCompatActivity {

    // Add this in global scope
    private static final int REQUEST_CODE = 101;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, DashBoardActivity.class));
            finish();
        } else {
            Authenticate();
        }
    }

    private void Authenticate() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(getAuthProviderList())
                        .setIsSmartLockEnabled(false)
                        .build(),
                REQUEST_CODE);
    }

    private List<AuthUI.IdpConfig> getAuthProviderList() {

        return Arrays.asList(
//                new AuthUI.IdpConfig.GoogleBuilder().build(),
//                new AuthUI.IdpConfig.FacebookBuilder().build(),
//                new AuthUI.IdpConfig.TwitterBuilder().build(),
//                new AuthUI.IdpConfig.GitHubBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
//                new AuthUI.IdpConfig.AnonymousBuilder().build()
        );

//        List<AuthUI.IdpConfig> providers = new ArrayList<>();
//        providers.add(
//                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_LINK_PROVIDER).build());
//        return providers;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                startActivity(new Intent(this, DashBoardActivity.class));
                return;
            }
        } else {
            if (response == null) {
                // if user cancelled Sign-in
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                // When device has no network connection
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                // When unknown error occurred
                return;
            }
        }
    }
}
