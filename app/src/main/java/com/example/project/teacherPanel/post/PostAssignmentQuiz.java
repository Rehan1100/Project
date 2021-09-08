package com.example.project.teacherPanel.post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.example.project.teacherPanel.dashboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PostAssignmentQuiz extends AppCompatActivity {
    String item, id, SaveCurrentTime, saveCurrentDate,postdate;
    TextView DateTV;
    private Uri imageURI;
    ImageView imageView;
    CardView pdf;
    EditText title, desc, Date;
    Button upload;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private ProgressDialog loadingbar;
    Spinner semester,category;
    int Year, month, day;
    List<String> list;
    //Date
    String DeadlineDate;
    public int Startday, Startmonth, StartYear;
    private String LoginUserEmail;
    private List<String> list2;
    private String categoryOfPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_assignment_quiz);
   //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences getShared = getApplicationContext().getSharedPreferences("loginName", MODE_PRIVATE);
        LoginUserEmail = getShared.getString("Email", null);

        pdf = findViewById(R.id.product);
        title = findViewById(R.id.edittitle);
        desc = findViewById(R.id.editDesc);
        upload = findViewById(R.id.Post);
        imageView = findViewById(R.id.image);
        Date = findViewById(R.id.editdate);
        DateTV = findViewById(R.id.textView3);
        loadingbar = new ProgressDialog(this);

        //Spinner code
        semester = findViewById(R.id.course2);

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

        category = findViewById(R.id.category);

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

        //DateCode

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = dateFormat.format(calendar.getTime());
        SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm:ss a");
        SaveCurrentTime = TimeFormat.format(calendar.getTime());
        id = saveCurrentDate + SaveCurrentTime;
        DateTV.setText("Post Date :\t " + saveCurrentDate.toString());
        postdate = DateTV.getText().toString();
        String[] dateParts = saveCurrentDate.split("/");
        int getday = Integer.parseInt(dateParts[0]);
        int getmonth = Integer.parseInt(dateParts[1]);
        int getyear = Integer.parseInt(dateParts[2]);

        final Calendar lastCalender = Calendar.getInstance();
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Year = lastCalender.get(Calendar.YEAR);
                month = lastCalender.get(Calendar.MONTH);
                day = lastCalender.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(PostAssignmentQuiz.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String finalmonth = null;
                        int month1 = month + 1;
                        Startday = (dayOfMonth);
                        Startmonth = (month1);
                        StartYear = year;
                        if (month1<=9)
                        {
                            finalmonth="0"+(month1);
                        }
                        else {
                                finalmonth= String.valueOf(month1);
                        }
                        DeadlineDate = dayOfMonth + "/" + (finalmonth) + "/" + year;

                        if (getday > Startday && getmonth> Startmonth && getyear > StartYear) {

                            Date.setText("");
                            Toast.makeText(PostAssignmentQuiz.this, "You're Selecting the Previous date", Toast.LENGTH_SHORT).show();
                        } else if (getday == Startday && getmonth== Startmonth && getyear == StartYear) {
                            Date.setText("");
                            Toast.makeText(PostAssignmentQuiz.this, "You're Selecting the current date", Toast.LENGTH_SHORT).show();
                        } else if (getday < Startday || (getday>Startday && (getmonth<Startmonth || getyear<StartYear)) || ( getday==Startday && (getmonth<Startmonth || getyear <Startmonth)) ) {

                            if (getyear<=StartYear) {
                                Date.setText(DeadlineDate);
                            }}
                    }
                }, Year, month, day);
                datePickerDialog.show();
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
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
        String name = title.getText().toString();
        String description = desc.getText().toString();
        String finaldate = Date.getText().toString();
        if (imageURI == null) {
            Toast.makeText(this, "PDF File is Menidatry...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write PDF name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please write PDF description", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(finaldate)) {
            Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(item)) {
            Toast.makeText(this, "Please select semester", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(categoryOfPost)) {
            Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show();
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


//                uploadPDFile(data.getData());

        }
    }


    private void uploadPDFile() {


        final ProgressDialog progressDialog = new ProgressDialog(PostAssignmentQuiz.this);
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
                        pdfload uploadpdf = new pdfload(title.getText().toString(), url.toString(), desc.getText().toString(), item, id,DeadlineDate,saveCurrentDate,LoginUserEmail,categoryOfPost);
                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadpdf);
                        Toast.makeText(PostAssignmentQuiz.this, "uploading", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        title.setText("");
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

                Toast.makeText(PostAssignmentQuiz.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent= new Intent(PostAssignmentQuiz.this,dashboard.class);
        startActivity(intent);
        finish();
    }
}