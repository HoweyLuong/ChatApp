package com.example.chatapp.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityChatBinding;
import com.example.chatapp.models.User;
import com.example.chatapp.utilities.Constants;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private User receiverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivityChatBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());
        loadReceiverDetails();
        setListener();


    }


    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.inputFirstName.setText(receiverUser.name);

    }

    private void setListener() {
        binding.imageBack.setOnClickListener(view -> onBackPressed());
    }
}