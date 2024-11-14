package com.example.chatapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivitySignUpBinding;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * In this class it represents the Sign Up part which help to provide the user to interface
 * for creating a new user account with the profile image, name and email.
 * and it will push intot the Firebase
 */
public class SignUpActivity extends AppCompatActivity {
private ActivitySignUpBinding binding;
private PreferenceManager preferenceManager;
private String encodeImage;
    /**
     * Called when the activity is first created. Initializes the UI, sets up
     * event listeners, and inflates the activity's layout.
     *
     * @param savedInstanceState The saved state of the activity, if available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }
    /**
     * Sets up listeners for UI elements in the sign-up screen. Handles button
     * clicks for signing up and selecting a profile image.
     */
    private void setListeners() {

        binding.textSignIn.setOnClickListener(v ->onBackPressed());
        binding.buttonSignUp.setOnClickListener(v->{
            if(isValidateSignUpDetails()) {
                SignUp();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    /**
     * Displays a toast message on the screen.
     *
     * @param message The message to display in the toast.
     */
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    /**
     * Initiates the sign-up process by posting user data to Firebase Firestore.
     * If successful, saves user session data and navigates to MainActivity.
     */
    private void SignUp() {
        //check loading
        loading(true);
        //Post to Firebase
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String,String> user = new HashMap<>();
        user.put(Constants.KEY_NAME,binding.inputName.getText().toString());
        user.put(Constants.KEY_EMAIL,binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD,binding.inputPassowrd.getText().toString());
        user.put(Constants.KEY_IMAGE, encodeImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {

                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodeImage);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }).addOnFailureListener( exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });

    }

    /**
            * Encodes a bitmap image into a Base64 string.
            *
            * @param bitmap The bitmap to encode.
     * @return A Base64-encoded string representation of the image.
            */
    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight()*previewWidth / bitmap.getWidth();

        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);


    }
    /**
     * Activity result launcher for picking an image from external storage.
     * Handles the result, setting the profile image and encoding it.
     */
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if(result.getResultCode() == RESULT_OK) {
                    Uri imageUri = result.getData().getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        binding.imageProfile.setImageBitmap(bitmap);
                        binding.textAddImage.setVisibility(View.GONE);
                        encodeImage = encodeImage(bitmap);




                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
    );
    /**
     * Validates the user input for sign-up. Ensures all fields are filled in
     * correctly, and checks if the password and confirm password match.
     *
     * @return True if the sign-up details are valid, false otherwise.
     */
    private Boolean isValidateSignUpDetails() {
        if (encodeImage == null) {
            showToast("Please select your image");
            return  false;
        }
        if(binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Please enter your Name");
            return false;
        }else if(binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast(("Please enter your Email"));
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast(("Please enter Valid Email"));
            return false;
        }else if(binding.inputPassowrd.getText().toString().trim().isEmpty()) {
            showToast("Please enter your Password");
            return false;
        }else if(binding.inputConfirmPassowrd.getText().toString().trim().isEmpty()){
            showToast("Please confirm your Password");
            return false;
        }else if(!binding.inputPassowrd.getText().toString().equals(binding.inputConfirmPassowrd.getText().toString())) {
            showToast("Password not the same");
            return false;
        }else {
            return true;
        }
    }
    /**
     * Controls the visibility of the loading indicator during the sign-up process.
     *
     * @param isLoading True if the loading indicator should be shown, false otherwise.
     */
    private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);

        }
    }

}