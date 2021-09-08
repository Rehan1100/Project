package com.example.project.userpanel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.loginsignin.Login;
import com.example.project.teacherPanel.Notifications.Notifications;
import com.example.project.teacherPanel.dashboard;
import com.example.project.userpanel.UserUpload.upload;
import com.example.project.userpanel.userNotifications.UserNotification;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
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

public class userdashboard extends AppCompatActivity {
    List<ModelCLass> modelCLassList = new ArrayList<>();
    String title, disc, image;
    RecyclerView recyclerView;
    public  Adapter adapter;
    TextView msg;
    DatabaseReference reference;
    private String LoginUserEmail;
    private String semester;
    private String semesterrole;
    private String saveCurrentDate;
    TextView tv;
    //Drawer Work
    Toolbar Studenttoolbar;
    NavigationView nav;
    ActionBarDrawerToggle   studenttoggle;
    DrawerLayout StudentdrawerLayout;
    List datanode = new ArrayList();
    BackgroundThread bg;
    private String categoryOfPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdashboard);
        bg= new BackgroundThread(getApplicationContext());
        bg.execute("answer");

        Studenttoolbar = findViewById(R.id.studenttoolbar);
        setSupportActionBar(Studenttoolbar);
        nav = findViewById(R.id.studentnavMenu);
        StudentdrawerLayout = findViewById(R.id.studentDrawer);
        studenttoggle = new ActionBarDrawerToggle(this, StudentdrawerLayout, Studenttoolbar, R.string.StudentOpenDrawer, R.string.StudentCloseDrawer);
        StudentdrawerLayout.addDrawerListener(studenttoggle);
        studenttoggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.studenthome:

                        StudentdrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.studentlogout:
                        StudentdrawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent2);
                        finish();
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences settings = getSharedPreferences("name", Context.MODE_PRIVATE);
                        settings.edit().remove("Name").commit();
                        SharedPreferences settings1 = getSharedPreferences("loginName", Context.MODE_PRIVATE);
                        settings1.edit().remove("Email").commit();

                        return true;

                }
                return true;
            }
        });

        recyclerView = findViewById(R.id.recylerView);
        msg = findViewById(R.id.textView6);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        SharedPreferences getShared4 = getApplicationContext().getSharedPreferences("Studentsemes", MODE_PRIVATE);
        semesterrole = getShared4.getString("semester", null);

        SharedPreferences getShared = getApplicationContext().getSharedPreferences("loginName", MODE_PRIVATE);
        LoginUserEmail = getShared.getString("Email", null);
        reference = FirebaseDatabase.getInstance().getReference().child("uploads");
        Query query = reference.orderByChild("semester").equalTo(semesterrole);
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
                        String Date = snapshot.child(key).child("deadlineDate").getValue(String.class);
                        String lDate = snapshot.child(key).child("postDate").getValue(String.class);
                        String id = snapshot.child(key).child("id").getValue(String.class);
                        categoryOfPost = snapshot.child(key).child("catogeryOfPost").getValue(String.class);

                        ModelCLass modelCLass = new ModelCLass(title, disc, image, semester, lDate, Date, id,categoryOfPost);
                        modelCLassList.add(modelCLass);
                    }
                        adapter = new Adapter(modelCLassList, userdashboard.this);

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
        menuInflater.inflate(R.menu.studentoptionalmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchst);
        SearchView searchView;

        MenuItem item = menu.findItem(R.id.badgest);
        MenuItemCompat.setActionView(item, R.layout.bademenulayout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);

        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), UserNotification.class);
                startActivity(intent);
                finish();
            }
        });

        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter==null)
                {

                }else {
                    adapter.getFilter().filter(newText);
                }    return true;
            }
        });

        return super.onCreateOptionsMenu(menu);



    }

    class BackgroundThread extends AsyncTask<String, Void, String> {

        private Context context1;
        private String LoginUserEmail;
        private String key;

        public BackgroundThread(Context context) {
            this.context1 = context;
        }

        @Override
        protected String doInBackground(String... data) {
            SharedPreferences getShared = context1.getSharedPreferences("loginName", MODE_PRIVATE);
            LoginUserEmail = getShared.getString("Email", null);
            DatabaseReference db;
            db = FirebaseDatabase.getInstance().getReference(data[0]);
            Query query = db.orderByChild("email").equalTo(LoginUserEmail);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            key = data.getKey();
                           String statusOfAssignment=snapshot.child(key).child("status").getValue(String.class);

                           if (statusOfAssignment.equals("Rejected")||statusOfAssignment.equals("Accepted"))
                           {

                            datanode.add(key);
                           }


                        }
                        Log.d("Tag", datanode.size() + "");
                        int size = datanode.size();
                        if (size<=0)
                        {
                            tv.setVisibility(View.GONE);

                        }
                        else if (size >= 99) {
                            tv.setVisibility(View.VISIBLE);
                            tv.setText("99");
                        } else if (size <= 99) {
                            tv.setVisibility(View.VISIBLE);
                            tv.setText(datanode.size() + "");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;

        }


    }

}