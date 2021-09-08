package com.example.project.teacherPanel.Notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.loginsignin.Login;
import com.example.project.teacherPanel.dashboard;
import com.example.project.teacherPanel.post.PostAssignmentQuiz;
import com.example.project.userpanel.Adapter;
import com.example.project.userpanel.ModelCLass;
import com.example.project.userpanel.userdashboard;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {

    List<NotifcationClass> modelCLassList = new ArrayList<>();
    String email, gender, item, name, semester, url;
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    DatabaseReference reference;
    private String status;
    private String id;
    private String answerstatus;
    private TextView msg;
    private String categoryOfPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        SharedPreferences getShared4 = getApplicationContext().getSharedPreferences("Studentsemes", MODE_PRIVATE);
        semester = getShared4.getString("semester", null);
        recyclerView = findViewById(R.id.notificationRecycler);
        msg=findViewById(R.id.textView7);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        reference = FirebaseDatabase.getInstance().getReference().child("answer");
        Query query = reference.orderByChild("item").equalTo(semester);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        String key = d.getKey();

                        id = snapshot.child(key).child("postId").getValue(String.class);
                        email = snapshot.child(key).child("email").getValue(String.class);
                        gender = snapshot.child(key).child("gender").getValue(String.class);
                        semester = snapshot.child(key).child("semester").getValue(String.class);
                        item = snapshot.child(key).child("item").getValue(String.class);
                        url = snapshot.child(key).child("url").getValue(String.class);
                        name = snapshot.child(key).child("name").getValue(String.class);
                        status = snapshot.child(key).child("status").getValue(String.class);
                        answerstatus = snapshot.child(key).child("answerstatus").getValue(String.class);
                        categoryOfPost = snapshot.child(key).child("catogeryOfPost").getValue(String.class);

                        if (status.equals("Pending")) {
                            NotifcationClass modelCLass = new NotifcationClass(name, email, gender, semester, url, item, id, answerstatus,categoryOfPost);
                            modelCLassList.add(modelCLass);
                        }


                    }
                    adapter = new NotificationAdapter(modelCLassList, Notifications.this);

                    if(modelCLassList.isEmpty())
                    {
                        msg.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.GONE);
                    }
                    else
                    {
                        msg.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.optionalmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView;
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                adapter.getFilter().filter(newText);
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Notifications.this, dashboard.class);
        startActivity(intent);
        finish();
    }
}