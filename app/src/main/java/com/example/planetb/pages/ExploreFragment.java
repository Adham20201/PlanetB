package com.example.planetb.pages;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.planetb.SearchActivity;
import com.example.planetb.adapters.TopicCourseAdapter;
import com.example.planetb.course.CourseActivity;
import com.example.planetb.course.CoursesInterface;
import com.example.planetb.adapters.CategoryAdapter;
import com.example.planetb.adapters.PopularCourseAdapter;
import com.example.planetb.lists.Categories;
import com.example.planetb.lists.Courses;
import com.example.planetb.R;
import com.example.planetb.lists.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ExploreFragment extends Fragment implements CoursesInterface {

    String topicCategory;

    TextView txtHello, txtTopicCategory;
    SearchView searchView;
    ProgressBar progressBar;
    ConstraintLayout progressBarCont;

    ArrayList<Courses> popularCourseArrayList;
    ArrayList<Courses> topicCourseArrayList;
    ArrayList<Categories> categoriesArrayList;

    RecyclerView popularCourseRecyclerView, categoryRecycleView, topicRecyclerView;
    PopularCourseAdapter popularCourseAdapter;
    TopicCourseAdapter topicCourseAdapter;

    CategoryAdapter categoryAdapter;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtHello = view.findViewById(R.id.text_hello);
        txtTopicCategory = view.findViewById(R.id.topic_category);
        progressBarCont = view.findViewById(R.id.progressBarContainer);
        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.search_bar);


        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance("https://planetb-c524b-default-rtdb.firebaseio.com/");
        rootNode.getReference("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                if (user != null){
                    String name = user.getFirstName();
                    String hi = "Hi, " + name;
                    txtHello.setText(hi);
                }
                progressBarCont.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBarCont.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        popularCourseArrayList = new ArrayList<>();
        topicCourseArrayList = new ArrayList<>();

        dataInitialization();
        //coursesData();

        popularCourseRecyclerView = view.findViewById(R.id.popular_course_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        popularCourseRecyclerView.setLayoutManager(layoutManager);
        popularCourseRecyclerView.setItemAnimator(new DefaultItemAnimator());
        popularCourseAdapter = new PopularCourseAdapter(getContext(),popularCourseArrayList,this);
        popularCourseRecyclerView.setAdapter(popularCourseAdapter);

        categoryRecycleView = view.findViewById(R.id.category_recycle);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL);
        categoryRecycleView.setLayoutManager(gridLayoutManager);
        categoryAdapter = new CategoryAdapter(categoriesArrayList, getContext() , this,"courseCategory");
        categoryRecycleView.setAdapter(categoryAdapter);

        topicRecyclerView = view.findViewById(R.id.topic_recycle);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        topicRecyclerView.setLayoutManager(layoutManager2);
        topicRecyclerView.setItemAnimator(new DefaultItemAnimator());
        topicCourseAdapter = new TopicCourseAdapter(getContext(),topicCourseArrayList,this);
        topicRecyclerView.setAdapter(topicCourseAdapter);

        eventDbChangeListener();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0){
                    startActivity(new Intent(getActivity(), SearchActivity.class));
                }
                return false;
            }
        });

    }

    private void eventDbChangeListener() {
        db.collection("Courses").orderBy("courseName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!= null){
                            Log.e("firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                popularCourseArrayList.add(dc.getDocument().toObject(Courses.class));
                            }
                            popularCourseAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }

    private void eventCategoryChosenListener() {
        db.collection("Courses").whereEqualTo("courseCategory",topicCategory)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!= null){
                            Log.e("firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                topicCourseArrayList.add(dc.getDocument().toObject(Courses.class));
                            }
                            topicCourseAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void dataInitialization() {

        categoriesArrayList = new ArrayList<>();
        String[] categoryName = {"AI","Agriculture","Capacity Building","Climate","Disasters","Earthquake","Forestation","Population","Water Resources"};
        Integer[] categoryImg = {R.drawable.ic_ai,R.drawable.ic_argiculture,R.drawable.ic_capacity_building,R.drawable.ic_climate,R.drawable.ic_disasters,R.drawable.ic_earthquake,R.drawable.ic_forestation,R.drawable.ic_population,R.drawable.ic_water_resources};

        for (int i=0; i<categoryName.length; i++){
            categoriesArrayList.add(new Categories(categoryName[i],categoryImg[i]));
        }

    }

    @Override
    public void onPopularCourseClick(int position, String courseName) {
        Intent intent = new Intent(getActivity(), CourseActivity.class);
        intent.putExtra("courseName",courseName);
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(int position, String categoryName, String filterField) {
        topicCategory = categoryName;
        txtTopicCategory.setText(categoryName);
        topicCourseArrayList.clear();
        topicCourseAdapter.notifyDataSetChanged();
        eventCategoryChosenListener();
    }
}