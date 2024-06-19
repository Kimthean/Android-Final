package com.kimthean.newsapp.ui.bookmark;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kimthean.newsapp.R;

public class BookmarkFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("news").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Handle the document
                        // For example, you can print the document ID
                        System.out.println(document);
                        Log.println(Log.INFO, "BookmarkFragment", document.getId());
                    }
                }
            } else {
                // Handle the error
                // For example, you can print the exception
                System.out.println(task.getException());
            }
        });

        return view;
    }
}