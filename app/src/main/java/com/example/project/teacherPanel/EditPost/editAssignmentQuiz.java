
package com.example.project.teacherPanel.EditPost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.teacherPanel.ViewPost.view;
import com.example.project.teacherPanel.dashboard;
import com.example.project.teacherPanel.post.pdfload;
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

import static android.content.Context.MODE_PRIVATE;

public class editAssignmentQuiz extends AppCompatActivity {
    EditText Title, Description, DeadDate;
    TextView DateTV;
    Spinner semester,category;
    DatabaseReference databaseReference;
    ImageView pdf;
    String postid = "", key;
    private DatabaseReference reference;
    List<String> list,list2;
    private String item,categoryOfPost;
    private String takeitem;
    private Button post;
    private Uri imageURI;
    int Year, month, day;
    public int Startday, Startmonth, StartYear;
    String DeadlineDate;
    StorageReference storageReference;
    private String saveCurrentDate;
    private String postDate;
    private String pdfURL;
    private String LoginUserEmail;
    private int resId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assignment_quiz);
        postid = getIntent().getStringExtra("pid");
        postDate = getIntent().getStringExtra("postDate");
        SharedPreferences getShared = getApplicationContext().getSharedPreferences("loginName", MODE_PRIVATE);
        LoginUserEmail = getShared.getString("Email", null);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
        saveCurrentDate = dateFormat.format(calendar.getTime());
        Title = findViewById(R.id.edittitle);
        Description = findViewById(R.id.editDesc);
        DeadDate = findViewById(R.id.editdate);
        semester = findViewById(R.id.course2);
        pdf = findViewById(R.id.pdf);
        post = findViewById(R.id.Post);
        DateTV = findViewById(R.id.textView3);
        Bundle bundle = getIntent().getExtras();
        resId = bundle.getInt("pdfURL");

        String[] dateParts = postDate.split("/");
        int getday = Integer.parseInt(dateParts[0]);
        int getmonth = Integer.parseInt(dateParts[1]);
        int getyear = Integer.parseInt(dateParts[2]);

        list = new ArrayList<>();
        list.add(0, "Select Semester");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        final ArrayAdapter<String> arr = new ArrayAdapter<String>(this, R.layout.spinner, list);
        arr.setDropDownViewResource(R.layout.spinner);
        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).equals("Select Semester")) {
                    item = parent.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getApplicationContext(), "please select any one", Toast.LENGTH_SHORT).show();
            }
        });
        semester.setAdapter(arr);


        //spineer2

        category = findViewById(R.id.catrory2);

        list2 = new ArrayList<>();
        list2.add(0, "Select Category");
        list2.add("Quiz");
        list2.add("Assignment");

        final ArrayAdapter<String> arr2 = new ArrayAdapter<String>(this, R.layout.spinner, list2);

        arr2.setDropDownViewResource(R.layout.spinner);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getItemAtPosition(position).equals("Select Category")) {

                    categoryOfPost = parent.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getApplicationContext(), "please select any one", Toast.LENGTH_SHORT).show();
            }
        });
        category.setAdapter(arr2);


        final Calendar lastCalender = Calendar.getInstance();
        DeadDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Year = lastCalender.get(Calendar.YEAR);
                month = lastCalender.get(Calendar.MONTH);
                day = lastCalender.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(editAssignmentQuiz.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String finalmonth = null;
                        int month1 = month + 1;
                        Startday = (dayOfMonth);
                        Startmonth = (month1);
                        StartYear = year;
                        if (month1 <= 9) {
                            finalmonth = "0" + (month1);
                        } else {
                            finalmonth = String.valueOf(month1);
                        }
                        DeadlineDate = dayOfMonth + "/" + (finalmonth) + "/" + year;

                        if (getday > Startday && getmonth > Startmonth && getyear > StartYear) {

                            DeadDate.setText("");
                            Toast.makeText(editAssignmentQuiz.this, "You're Selecting the Previous date", Toast.LENGTH_SHORT).show();
                        } else if (getday == Startday && getmonth == Startmonth && getyear == StartYear) {
                            DeadDate.setText("");
                            Toast.makeText(editAssignmentQuiz.this, "You're Selecting the current date", Toast.LENGTH_SHORT).show();
                        } else if (getday < Startday || (getday > Startday && (getmonth < Startmonth || getyear < StartYear)) || (getday == Startday && (getmonth < Startmonth || getyear < Startmonth))) {
                            if (getyear <= StartYear) {
                                DeadDate.setText(DeadlineDate);
                            }
                        }
                    }
                }, Year, month, day);
                datePickerDialog.show();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateproduct();
            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPDFFile();
            }
        });
        getproductDetail(postid);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

    }

    private void validateproduct() {

        String name = Title.getText().toString();
        String description = Description.getText().toString();
        String finaldate = DeadDate.getText().toString();
        if (imageURI == null) {
            Toast.makeText(this, "PDF File is Menidatry...", Toast.LENGTH_SHORT).show();
        } else
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write PDF name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please write PDF description", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(finaldate)) {
            Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(item)) {
            Toast.makeText(this, "Please select semester", Toast.LENGTH_SHORT).show();
        }  else if (TextUtils.isEmpty(categoryOfPost)) {
            Toast.makeText(this, "Please select Category", Toast.LENGTH_SHORT).show();
        } else {
            uploadPDFile();
        }
    }

    private void getproductDetail(String postid) {

        reference = FirebaseDatabase.getInstance().getReference().child("uploads");
        Query query = reference.orderByChild(postid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        key = dataSnapshot.getKey();
                    }
                    Title.setText(snapshot.child(key).child("title").getValue(String.class));
                    Description.setText(snapshot.child(key).child("desc").getValue(String.class));
                    DeadDate.setText(snapshot.child(key).child("deadlineDate").getValue(String.class));
                    DateTV.setText(snapshot.child(key).child("postDate").getValue(String.class));
                    //  Picasso.get().load(snapshot.child(key).child("pdf").getValue(String.class)).into(pdf);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                pdf.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadPDFile() {


        final ProgressDialog progressDialog = new ProgressDialog(editAssignmentQuiz.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + ".pdf");
        reference.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        Uri url = uri.getResult();
                        pdfload uploadpdf = new pdfload(Title.getText().toString(), url.toString(), Description.getText().toString(), item, postid, DeadDate.getText().toString(), saveCurrentDate, LoginUserEmail,categoryOfPost);
                        DatabaseReference node = FirebaseDatabase.getInstance().getReference("uploads");
                        node.orderByChild(postid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String key;
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        key = dataSnapshot.getKey();

                                        databaseReference.child(key).setValue(uploadpdf);


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(editAssignmentQuiz.this, "uploading", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), dashboard.class);
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

                Toast.makeText(editAssignmentQuiz.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext(), view.class);
        startActivity(intent);
    }
}