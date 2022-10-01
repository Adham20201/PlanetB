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
import com.example.planetb.lists.Categories;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private final CoursesInterface coursesInterface;

    ArrayList<Categories> categories;
    Context context;
    String filterField;

    public CategoryAdapter(ArrayList<Categories> categories, Context context , CoursesInterface coursesInterface , String filterField) {
        this.categories = categories;
        this.context = context;
        this.coursesInterface = coursesInterface;
        this.filterField = filterField;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_category,parent,false);
        return new CategoryAdapter.ViewHolder(view,coursesInterface,categories,filterField);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(categories.get(position).getCourseIcon());
        holder.mainText.setText(categories.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView mainText;
        public ViewHolder(@NonNull View itemView , CoursesInterface coursesInterface , ArrayList<Categories> categories , String filterField) {
            super(itemView);

            imageView =itemView.findViewById(R.id.category_img);
            mainText =itemView.findViewById(R.id.category_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (coursesInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            coursesInterface.onCategoryClick(pos,categories.get(pos).getCategoryName(), filterField);
                        }
                    }
                }
            });

        }
    }
}
