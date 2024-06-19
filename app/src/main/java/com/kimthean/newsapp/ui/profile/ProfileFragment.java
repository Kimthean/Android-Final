package com.kimthean.newsapp.ui.profile;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kimthean.newsapp.LoginActivity;
import com.kimthean.newsapp.R;
import com.kimthean.newsapp.ui.bookmark.BookmarkFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView emailTextView = view.findViewById(R.id.emailTextView);
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        CircleImageView profileImageView = view.findViewById(R.id.profileImageView);
        Button signOutBtn = view.findViewById(R.id.signOutButton);
        MaterialCardView bookmark = view.findViewById(R.id.bookmark);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            emailTextView.setText(currentUser.getEmail());
            nameTextView.setText(currentUser.getDisplayName());
            if (currentUser.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(currentUser.getPhotoUrl().toString())
                        .into(profileImageView);
            }
        }

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.bookmarkFragment);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}