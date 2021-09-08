package com.example.project.userpanel.UserUpload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.teacherPanel.dashboard;
import com.example.project.teacherPanel.post.pdfload;
import com.example.project.userpanel.userdashboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class upload extends AppCompatActivity {
    String item, saveCurrentDate, key;
    TextView DateTV;
    private Uri imageURI;
    ImageView imageView;
    CardView pdf;
    Button upload;
    StorageReference storageReference;
    DatabaseReference databaseReference, databaseReference2;
    Spinner subject;
    List<String> list;
    private String LoginUserEmail;
    private ProgressDialog loadingbar;
    String email, gender, name, semester;
    public String existemail, existid;
    public String PostID = "";
    private ArrayList<String> list2=new ArrayList<>();
    private String status="Pending";
    private String answerstatus="unloading";
    private String catogeryOfPost="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        SharedPreferences getShared = getApplicationContext().getSharedPreferences("loginName", MODE_PRIVATE);
        LoginUserEmail = getShared.getString("Email", null);
        PostID = getIntent().getStringExtra("id");
        catogeryOfPost = getIntent().getStringExtra("catogry");

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");
        Query query = database.orderByChild("email").equalTo(LoginUserEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        key = dataSnapshot.getKey();

                        email = dataSnapshot.child("email").getValue(String.class);
                        gender = dataSnapshot.child("gender").getValue(String.class);
                        name = dataSnapshot.child("name").getValue(String.class);
                        semester = dataSnapshot.child("semester").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference database2 = FirebaseDatabase.getInstance().getReference("answer");
        Query query2 = database2.orderByChild("email").equalTo(LoginUserEmail);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        key = dataSnapshot.getKey();

                        existemail = dataSnapshot.child("email").getValue(String.class);
                        existid = dataSnapshot.child("postId").getValue(String.class);

                        list2.add(existemail);
                        list2.add(existid);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pdf = findViewById(R.id.product);
        upload = findViewById(R.id.Post);
        imageView = findViewById(R.id.image);
        DateTV = findViewById(R.id.textView3);
        loadingbar = new ProgressDialog(this);

        //Spinner code
        subject = findViewById(R.id.course2);
        list = new ArrayList<>();
        list.add(0, "Select Course");
        list.add("Computer");
        list.add("Biology");
        list.add("Physics");
        list.add("History");
        list.add("GeoGraphy");
        final ArrayAdapter<String> arr = new ArrayAdapter<String>(this, R.layout.spinner, list);

        arr.setDropDownViewResource(R.layout.spinner);

        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getItemAtPosition(position).equals("Select Course")) {

                    item = parent.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getApplicationContext(), "please select any one", Toast.LENGTH_SHORT).show();
            }
        });
        subject.setAdapter(arr);

        //DateCode

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = dateFormat.format(calendar.getTime());
        DateTV.setText("Date :" + saveCurrentDate);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("answer");
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDFFile();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post();
            }
        });
    }

    public void post() {
        vaildateProduct();
    }

    public void vaildateProduct() {

        if (imageURI == null) {
            Toast.makeText(this, "PDF File is Menidatry...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(item)) {
            Toast.makeText(this, "Please Select Your Course Name", Toast.LENGTH_SHORT).show();
        } else {

            uploadPDFile();

        }
    }


    private void selectPDFFile() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF file"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {


            imageURI = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void uploadPDFile() {
        Log.d("Tag", existemail + "\n" +existid + "\n" +email + "\n" +PostID + "\n" );
        if (!list2.contains(PostID)) {

            final ProgressDialog progressDialog = new ProgressDialog(upload.this);
            progressDialog.setTitle("Loading");
            progressDialog.show();
            StorageReference reference = storageReference.child("Answer/" + System.currentTimeMillis() + ".pdf");
            reference.putFile(imageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            Answer answer = new Answer(name, email, semester, gender, url.toString(), item, PostID,status,answerstatus,catogeryOfPost);
                            databaseReference2.push().setValue(answer);
                            Toast.makeText(upload.this, "uploading", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            //name.setText("");
                            Intent intent = new Intent(getApplicationContext(), userdashboard.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded: " + (int) progress + "%");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(upload.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "you already post it", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {

        Intent intent2=new Intent(getApplicationContext(),userdashboard.class);
        startActivity(intent2);
        finish();
    }
}
