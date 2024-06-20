package com.kimthean.newsapp.ui.bookmark;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kimthean.newsapp.R;
import com.kimthean.newsapp.data.Source;
import com.kimthean.newsapp.ui.news.News;
import com.kimthean.newsapp.ui.news.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookmarkFragment extends Fragment {

    private RecyclerView rvBookmarks;
    private NewsAdapter adapter;
    private List<News> bookmarkedNews;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noBookmarks;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        rvBookmarks = view.findViewById(R.id.rvBookmarks);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        noBookmarks = view.findViewById(R.id.noBookmarks);
        rvBookmarks.setLayoutManager(new LinearLayoutManager(getContext()));
        bookmarkedNews = new ArrayList<>();
        adapter = new NewsAdapter(bookmarkedNews);
        rvBookmarks.setAdapter(adapter);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        swipeRefreshLayout.setOnRefreshListener(this::fetchBookmarks);

        if (currentUser != null) {
            fetchBookmarks();
        } else {
            Log.e("BookmarkFragment", "User not authenticated");
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void fetchBookmarks() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            swipeRefreshLayout.setRefreshing(true);
            db.collection("users")
                    .document(userId)
                    .collection("bookmarks")
                    .get()
                    .addOnSuccessListener(this::onBookmarksLoaded)
                    .addOnFailureListener(e -> {
                        Log.e("BookmarkFragment", "Error fetching bookmarks", e);
                        Toast.makeText(getContext(), "Error fetching bookmarks", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    });
        } else {
            Log.e("BookmarkFragment", "User not authenticated");
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void onBookmarksLoaded(QuerySnapshot querySnapshot) {
        if (querySnapshot.isEmpty()) {
            noBookmarks.setVisibility(View.VISIBLE);
        } else {
            noBookmarks.setVisibility(View.GONE);
        }
        bookmarkedNews.clear();
        for (QueryDocumentSnapshot document : querySnapshot) {
            String title = document.getString("title");
            String url = document.getString("url");
            String sourceName = document.getString("source");
            String imageUrl = document.getString("imageUrl");
            String newsTimeUpdated = document.getString("timeUpdated");

            Source source = new Source();
            source.setId(null);
            source.setName(sourceName);

            News news = new News(title, imageUrl, source,newsTimeUpdated , url);
            bookmarkedNews.add(news);
        }
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }


}