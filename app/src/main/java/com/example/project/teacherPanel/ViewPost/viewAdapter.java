package com.example.project.teacherPanel.ViewPost;

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
import com.example.project.teacherPanel.EditPost.editAssignmentQuiz;
import com.example.project.teacherPanel.dashboard;
import com.example.project.userpanel.ModelCLass;
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

public class viewAdapter extends RecyclerView.Adapter<viewAdapter.Viewholder> implements Filterable {
    private List<viewCLass> modelCLassList;
    List<viewCLass> modelCLassListAll;
    Context context;
    public String Roll;

    public viewAdapter(List<viewCLass> modelCLassList, Context context) {
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
        final String pid = modelCLassList.get(position).getId();
        final String title = modelCLassList.get(position).getTitle();
        final String disc = modelCLassList.get(position).getDisc();
        final String pdf = modelCLassList.get(position).getPdf();
        final String postdate = modelCLassList.get(position).getPostDate();
        final String lastdate = modelCLassList.get(position).getLastdate();
        final String type = modelCLassList.get(position).getCatogeryofPost();
        final String semester = modelCLassList.get(position).getSemester();


        holder.setData(title, disc, pdf, postdate, lastdate,type);
        SharedPreferences getShared2 = context.getApplicationContext().getSharedPreferences("RollName", MODE_PRIVATE);
        Roll = getShared2.getString("Roll", null);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Roll.equals("Teacher")) {

                    CharSequence option[] = new CharSequence[]
                            {
                                    "Edit", "Delete"
                            };

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Modification");
                    dialog.setItems(option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent intent = new Intent(context, editAssignmentQuiz.class);
                                intent.putExtra("pid", pid);
                                intent.putExtra("postDate", postdate);
                                intent.putExtra("pdfURL", pdf);
                                context.startActivity(intent);
                                ((view)context).finish();
//                                Toast.makeText(context, pid, Toast.LENGTH_SHORT).show();


                            }
                            if (which == 1) {
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference node = db.getReference("uploads");
                                Query query = node.orderByChild("id").equalTo(modelCLassList.get(position).getId());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.exists()) {
                                            for (DataSnapshot datas : snapshot.getChildren()) {
                                                String key = datas.getKey();
                                                node.child(key).removeValue();
                                                removeItem(position);

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                               /* Intent intent = new Intent(context, dashboard.class);
                                context.startActivity(intent);
                                ((dashboard)context).finish();
*/
                            }
                        }
                    });
                    dialog.show();

                }
            }
        });

    }

    public void removeItem(int position) {

        modelCLassList.remove(position);
        notifyItemRemoved(position);

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
            List<viewCLass> filteredlist = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredlist.addAll(modelCLassListAll);
            } else {
                for (viewCLass data : modelCLassListAll) {
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
            modelCLassList.addAll((Collection<? extends viewCLass>) results.values);
            notifyDataSetChanged();
        }
    };

    class Viewholder extends RecyclerView.ViewHolder {

        private TextView title, PDate, LDate,Category;
        private TextView discription;
        private ImageView imageView;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.Title);
            discription = itemView.findViewById(R.id.Discreption);
            imageView = itemView.findViewById(R.id.ProductImage);
            PDate = itemView.findViewById(R.id.postdate);
            LDate = itemView.findViewById(R.id.FinalDate);
            Category = itemView.findViewById(R.id.category);


        }


        private void setData(String title1, String disc1, String image1,String pdate,String ldate,String category) {
            title.setText(title1);
            discription.setText(disc1);
            PDate.setText("PostedDate:"+pdate);
            LDate.setText("LastDate:"+ldate);
            Category.setText(category);
            // Picasso.get().load(image1).into(imageView);


        }
    }


}
