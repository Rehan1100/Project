package com.example.project.userpanel;

import android.app.DownloadManager;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.teacherPanel.Notifications.Notifications;
import com.example.project.teacherPanel.dashboard;
import com.example.project.userpanel.UserUpload.upload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> implements Filterable {
    private List<ModelCLass> modelCLassList;
    List<ModelCLass> modelCLassListAll;
    Context context;
    public String Roll;

    public Adapter(List<ModelCLass> modelCLassList, Context context) {
        this.modelCLassList = modelCLassList;
        this.modelCLassListAll = new ArrayList<>(modelCLassList);
        this.context = context;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rows, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {

        final String title = modelCLassList.get(position).getTitle();
        final String disc = modelCLassList.get(position).getDisc();
        final String pdf = modelCLassList.get(position).getPdf();
        final String semester = modelCLassList.get(position).getSemester();
        final String postD = modelCLassList.get(position).getpDate();
        final String lastD = modelCLassList.get(position).getLdate();
        final String id = modelCLassList.get(position).getId();
        final String type = modelCLassList.get(position).catogeryOfPost;


        holder.setData(title, disc, pdf, postD, lastD,type);
        SharedPreferences getShared2 = context.getApplicationContext().getSharedPreferences("RollName", MODE_PRIVATE);
        Roll = getShared2.getString("Roll", null);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence[] options = {"Download", "Reply"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("What you want");
                dialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1) {
                            Intent intent = new Intent(context, upload.class);
                            intent.putExtra("id",id.toString());
                            intent.putExtra("catogry",type.toString());
                            context.startActivity(intent);
                            ((userdashboard)context).finish();

                        }
                     //that is the Point
                        if (which == 0) {

                            downloadFile(context.getApplicationContext(), modelCLassList.get(position).getTitle(), ".pdf", DIRECTORY_DOWNLOADS, modelCLassList.get(position).pdf);
                        }
                    }
                });dialog.show();



            }

        });

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
            List<ModelCLass> filteredlist = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredlist.addAll(modelCLassListAll);
            } else {
                for (ModelCLass data : modelCLassListAll) {
                    if (data.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            modelCLassList.addAll((Collection<? extends ModelCLass>) results.values);
            notifyDataSetChanged();
        }
    };

    class Viewholder extends RecyclerView.ViewHolder {

        private TextView Category,title, postdate, LastDate;
        private TextView discription;
        private ImageView imageView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.Title);
            discription = itemView.findViewById(R.id.Discreption);
            postdate = itemView.findViewById(R.id.postdate);
            LastDate = itemView.findViewById(R.id.FinalDate);
            imageView = itemView.findViewById(R.id.ProductImage);
            Category = itemView.findViewById(R.id.category);


        }


        private void setData(String title1, String disc1, String image1, String pdate, String lDate,String category) {
            title.setText(title1);
            discription.setText(disc1);
            LastDate.setText("LastDate:" + lDate);
            postdate.setText("PostDate:" + pdate);
            Category.setText(category);

            // Picasso.get().load(image1).into(imageView);


        }
    }


}
