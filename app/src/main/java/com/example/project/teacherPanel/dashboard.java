package com.example.project.teacherPanel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.loginsignin.Login;
import com.example.project.teacherPanel.Notifications.Notifications;
import com.example.project.teacherPanel.ViewPost.view;
import com.example.project.teacherPanel.post.PostAssignmentQuiz;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {

    CardView post,Viewpost;
    TextView textView;

    //Drawer Work
    Toolbar toolbar;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    BackgroundThread bg;
    DrawerLayout drawerLayout;
    private String Name;
    private String LoginUserEmail;
    TextView tv;
    List datanode = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bg= new BackgroundThread(getApplicationContext());
        bg.execute("answer");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.navMenu);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home1:

                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
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

        // now over code is Start from here!
        post = findViewById(R.id.addTeacher);
        Viewpost = findViewById(R.id.ViewCard);
        textView = findViewById(R.id.textView4);
        SharedPreferences getShared3 = getApplicationContext().getSharedPreferences("name", MODE_PRIVATE);
        Name = getShared3.getString("Name", null);
        SharedPreferences getShared = getApplicationContext().getSharedPreferences("loginName", MODE_PRIVATE);
        LoginUserEmail = getShared.getString("Email", null);
        textView.setText("Welcome" + " " + Name.toString());
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PostAssignmentQuiz.class);
                startActivity(intent);
                finish();
            }
        });



        Viewpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getApplicationContext(), view.class);
                startActivity(intent);
                finish();
            }
        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.techermenu, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.bademenulayout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);

        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Notifications.class);
                startActivity(intent);
                finish();
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
                Query query = db.orderByChild("status").equalTo("Pending");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                key = data.getKey();
                                datanode.add(key);
                            }
                            Log.d("Tag", datanode.size()+"");
                            int size=datanode.size();
                            if (size>=99){
                                tv.setVisibility(View.VISIBLE);
                                tv.setText("99");
                            }else if (size<=99)
                            {   tv.setVisibility(View.VISIBLE);
                                tv.setText(datanode.size()+"");
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
