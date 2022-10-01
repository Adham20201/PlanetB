package com.example.planetb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planetb.R;
import com.example.planetb.course.CoursesInterface;
import com.example.planetb.lists.Courses;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopicCourseAdapter extends RecyclerView.Adapter<TopicCourseAdapter.ViewHolder>{

    private final CoursesInterface coursesInterface;
    ArrayList<Courses> topicCourse;
    Context context;

    public TopicCourseAdapter(Context context, ArrayList<Courses> topicCourse, CoursesInterface coursesInterface){
        this.context =context;
        this.topicCourse = topicCourse;
        this.coursesInterface = coursesInterface;
    }

    @NonNull
    @Override
    public TopicCourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_courses,parent,false);
        return new TopicCourseAdapter.ViewHolder(view,coursesInterface, topicCourse);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicCourseAdapter.ViewHolder holder, int position) {

        Picasso.get()
                .load(topicCourse.get(position).getCourseImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);

        holder.mainText.setText(topicCourse.get(position).getCourseName());
        holder.subText.setText(topicCourse.get(position).getCourseOrganizer());
    }

    @Override
    public int getItemCount() {
        return topicCourse.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView mainText, subText;
        public ViewHolder(@NonNull View itemView, CoursesInterface coursesInterface,ArrayList<Courses> popularCourse ) {
            super(itemView);

            imageView =itemView.findViewById(R.id.img_popular_course);
            mainText =itemView.findViewById(R.id.title_popular_course);
            subText =itemView.findViewById(R.id.txt_organization);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (coursesInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            coursesInterface.onPopularCourseClick(pos,popularCourse.get(pos).getCourseName());
                        }
                    }
                }
            });

        }
    }

}
