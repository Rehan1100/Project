package com.example.project.teacherPanel.Notifications;

import android.app.DownloadManager;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.teacherPanel.ViewPost.view;
import com.example.project.teacherPanel.dashboard;
import com.example.project.userpanel.ModelCLass;
import com.example.project.userpanel.UserUpload.upload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class NotificationAdapter
        extends RecyclerView.Adapter<NotificationAdapter.Viewholder> implements Filterable {
    private List<NotifcationClass> modelCLassList;
    List<NotifcationClass> modelCLassListAll;
    Context context;
    public String Roll;
    private String key;

    public NotificationAdapter(List<NotifcationClass> modelCLassList, Context context) {
        this.modelCLassList = modelCLassList;
        this.modelCLassListAll = new ArrayList<>(modelCLassList);
        this.context = context;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationrows, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        final String id = modelCLassList.get(position).getId();
        final String name = modelCLassList.get(position).getName();
        final String email = modelCLassList.get(position).getEmail();
        final String gender = modelCLassList.get(position).getGender();
        final String item = modelCLassList.get(position).getItem();
        final String url = modelCLassList.get(position).getUrl();
        final String semester = modelCLassList.get(position).getSemester();
        final String answerStatus = modelCLassList.get(position).getAnswerStatus();
        final String cType = modelCLassList.get(position).getCatogeryOfPost();


        holder.setData(item, name, email, semester, answerStatus,cType);
        SharedPreferences getShared2 = context.getApplicationContext().getSharedPreferences("RollName", MODE_PRIVATE);
        Roll = getShared2.getString("Roll", null);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence[] options = {"Download", "Accept", "Reject"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Options");
                dialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {

                            Toast.makeText(context, "Check on your Download it sucessfully download", Toast.LENGTH_SHORT).show();
                            downloadFile(context.getApplicationContext(), modelCLassList.get(position).getName(), ".pdf", DIRECTORY_DOWNLOADS, modelCLassList.get(position).url);
                            FirebaseDatabase node = FirebaseDatabase.getInstance();
                            DatabaseReference reference = node.getReference("answer");
                            Query query = reference.orderByChild("postId").equalTo(modelCLassList.get(position).getId());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            key = dataSnapshot.getKey();
                                            if (snapshot.child(key).child("email").getValue(String.class).equals(modelCLassList.get(position).getEmail())) {
                                                reference.child(key).child("answerstatus").setValue("downloading");

                                                /*      Intent intent = new Intent(context, dashboard.class);
                                                context.startActivity(intent);
                                                ((Notifications) context).finish();
                                          */  }
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        if (which == 1) {

                            Toast.makeText(context, "Accept", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase node = FirebaseDatabase.getInstance();
                            DatabaseReference reference = node.getReference("answer");
                            Query query = reference.orderByChild("postId").equalTo(modelCLassList.get(position).getId());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            key = dataSnapshot.getKey();
                                            if (snapshot.child(key).child("email").getValue(String.class).equals(modelCLassList.get(position).getEmail())) {
                                                reference.child(key).child("status").setValue("Accepted");

                                                removeItem(position);

                                                /*Intent intent=new Intent(context, dashboard.class);
                                                context.startActivity(intent);
                                                ((Notifications)context).finish();
                                                */
                                            }
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                        if (which == 2) {
                            Toast.makeText(context, "Rejected", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase node = FirebaseDatabase.getInstance();
                            DatabaseReference reference = node.getReference("answer");
                            Query query = reference.orderByChild("postId").equalTo(modelCLassList.get(position).getId());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            key = dataSnapshot.getKey();
                                            if (snapshot.child(key).child("email").getValue(String.class).equals(modelCLassList.get(position).getEmail())) {

                                                reference.child(key).child("status").setValue("Rejected");

                                                removeItem(position);
                                         /*       Intent intent=new Intent(context, dashboard.class);
                                                context.startActivity(intent);
                                                ((Notifications)context).finish();
                                         */
                                            }
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
/*
                            removeItem(position);
*/

                        }
                    }
                });
                dialog.show();
            }

        });

    }

    /*

        public void updateData(List<NotifcationClass> viewModels) {
            modelCLassList.clear();
            modelCLassList.addAll(viewModels);
            notifyDataSetChanged();
        }
    */
    public void addItem(int position, NotifcationClass viewModel) {
        modelCLassList.add(position, viewModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {

        modelCLassList.remove(position);
        notifyItemRemoved(position);

    }

    private void downloadFile(Context context, String filename, String fileExtension, String directory, String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, directory, filename + fileExtension);
        downloadManager.enqueue(request);
        Toast.makeText(context, "Download Sucessfully Open Your Download Manager", Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return modelCLassList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }


    Filter filter = new Filter() {
        //Run on BackgroundThread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<NotifcationClass> filteredlist = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredlist.addAll(modelCLassListAll);
            } else {
                for (NotifcationClass data : modelCLassListAll) {
                    if (data.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredlist.add(data);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredlist;
            return filterResults;
        }

        //Run on UIThread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modelCLassList.clear();
            modelCLassList.addAll((Collection<? extends NotifcationClass>) results.values);
            notifyDataSetChanged();
        }
    };

    class Viewholder extends RecyclerView.ViewHolder {

        private TextView Cousrsetitletb,Type;
        private TextView nametb, emailtb, semestertb, label;
        private ImageView imageView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Cousrsetitletb = itemView.findViewById(R.id.Title);
            nametb = itemView.findViewById(R.id.nametv1);
            emailtb = itemView.findViewById(R.id.emailtv1);
            semestertb = itemView.findViewById(R.id.semestertv1);
            imageView = itemView.findViewById(R.id.ProductImage);
            Type = itemView.findViewById(R.id.type);


        }


        private void setData(String item, String name, String email, String semester, String status,String types) {
            Cousrsetitletb.setText(item);
            nametb.setText(name);
            emailtb.setText(email);
            semestertb.setText(semester);
            Type.setText(types);
           /* if (status.equals("downloading"))
            {
               label.setText("This Answer you already download it");
            }
            else
            {
                label.setText("");
            }*/
        }
    }

}
