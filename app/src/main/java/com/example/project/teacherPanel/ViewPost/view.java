package com.example.project.teacherPanel.ViewPost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.teacherPanel.dashboard;
import com.example.project.teacherPanel.post.PostAssignmentQuiz;
import com.example.project.userpanel.Adapter;
import com.example.project.userpanel.ModelCLass;
import com.example.project.userpanel.userdashboard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class view extends AppCompatActivity {

    List<viewCLass> modelCLassList = new ArrayList<>();
    String  title, disc, image;
    RecyclerView recyclerView;
    viewAdapter adapter;
    DatabaseReference reference;
    private String LoginUserEmail;
    private String semester;
    private String pid;
    private String postdate;
    private String lastdate;
    private TextView msg;
    private String categoryOfPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        recyclerView = findViewById(R.id.recyclerview);
        msg = findViewById(R.id.textView5);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        SharedPreferences getShared = getApplicationContext().getSharedPreferences("loginName", MODE_PRIVATE);
        LoginUserEmail = getShared.getString("Email", null);
        reference = FirebaseDatabase.getInstance().getReference().child("uploads");
        Query query = reference.orderByChild("email").equalTo(LoginUserEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        String key = d.getKey();
                        title = snapshot.child(key).child("title").getValue(String.class);
                        disc = snapshot.child(key).child("desc").getValue(String.class);
                        semester = snapshot.child(key).child("semester").getValue(String.class);
                        image = snapshot.child(key).child("pdfFile").getValue(String.class);
                        pid = snapshot.child(key).child("id").getValue(String.class);
                        postdate = snapshot.child(key).child("postDate").getValue(String.class);
                        lastdate = snapshot.child(key).child("deadlineDate").getValue(String.class);
                        categoryOfPost = snapshot.child(key).child("catogeryOfPost").getValue(String.class);
                        viewCLass modelCLass = new viewCLass(title, disc, image, semester,pid,postdate,lastdate,categoryOfPost);
                        modelCLassList.add(modelCLass);
                    }
                    adapter = new viewAdapter(modelCLassList, view.this);

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

    @Override
    public void onBackPressed() {

        Intent intent= new Intent(view.this, dashboard.class);
        startActivity(intent);
        finish();
    }
}