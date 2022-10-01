package com.example.planetb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.planetb.adapters.CategoryAdapter;
import com.example.planetb.adapters.TopicCourseAdapter;
import com.example.planetb.course.CourseActivity;
import com.example.planetb.course.CoursesInterface;
import com.example.planetb.lists.Categories;
import com.example.planetb.lists.Courses;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements CoursesInterface {

    RecyclerView levelR , languageR, typeR , filterCoursesR;

    CategoryAdapter levelAdapter;
    CategoryAdapter typeAdapter;
    CategoryAdapter languageAdapter;
    TopicCourseAdapter filterCourseAdapter;

    SearchView searchView;

    TextView searchTxt;

    Button filterButton;
    ConstraintLayout filterContainer;

    ArrayList<Categories> levelArrayList;
    ArrayList<Categories> typeArrayList;
    ArrayList<Categories> languageArrayList;
    ArrayList<Courses> filterCourseArrayList;

    FirebaseFirestore db;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = SearchActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(SearchActivity.this, R.color.gray_background));

        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_bar2);
        searchTxt = findViewById(R.id.search_txt);

        filterButton = findViewById(R.id.filter_btn);
        filterContainer = findViewById(R.id.filter_container);

        filterInitialization();

        filterCourseArrayList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        levelR = findViewById(R.id.levelRecycleView);
        languageR = findViewById(R.id.languageRecycleView);
        typeR = findViewById(R.id.typeRecycleView);
        filterCoursesR = findViewById(R.id.filter_course_recycle);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(SearchActivity.this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(SearchActivity.this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(SearchActivity.this,LinearLayoutManager.HORIZONTAL,false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this,2,GridLayoutManager.VERTICAL,false);


        levelAdapter = new CategoryAdapter(levelArrayList, SearchActivity.this , this,"courseLevel");
        typeAdapter = new CategoryAdapter(typeArrayList, SearchActivity.this , this,"courseType");
        languageAdapter = new CategoryAdapter(languageArrayList, SearchActivity.this , this,"courseLanguage");
        filterCourseAdapter = new TopicCourseAdapter(SearchActivity.this,filterCourseArrayList,this);



        levelR.setLayoutManager(layoutManager1);
        languageR.setLayoutManager(layoutManager2);
        typeR.setLayoutManager(layoutManager3);
        filterCoursesR.setLayoutManager(gridLayoutManager);

        levelR.setItemAnimator(new DefaultItemAnimator());
        languageR.setItemAnimator(new DefaultItemAnimator());
        typeR.setItemAnimator(new DefaultItemAnimator());
        filterCoursesR.setItemAnimator(new DefaultItemAnimator());

        levelR.setAdapter(levelAdapter);
        languageR.setAdapter(languageAdapter);
        typeR.setAdapter(typeAdapter);
        filterCoursesR.setAdapter(filterCourseAdapter);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filterContainer.getVisibility() == View.VISIBLE){
                    filterContainer.setVisibility(View.GONE);
                }
                else if (filterContainer.getVisibility() == View.GONE){
                    filterContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String text = query;
                filterCourseArrayList.clear();
                filterCourseAdapter.notifyDataSetChanged();
                searchTxt.setText("Showing search result for \"" + text + "\"");
                searchListener("courseName",text);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                return false;
            }
        });


    }

    private void filterInitialization() {

        levelArrayList = new ArrayList<>();
        String[] levelName = {"Introductory","Intermediate","Advanced"};
        Integer[] levelImg = {R.drawable.ic_introductory,R.drawable.ic_intermediate,R.drawable.ic_advanced};

        for (int i=0; i<levelName.length; i++){
            levelArrayList.add(new Categories(levelName[i],levelImg[i]));
        }

        typeArrayList = new ArrayList<>();
        String[] typeName = {"Online Training","In-Person"};
        Integer[] typeImg = {R.drawable.ic_online,R.drawable.ic_offline};

        for (int i=0; i<typeName.length; i++){
            typeArrayList.add(new Categories(typeName[i],typeImg[i]));
        }

        languageArrayList = new ArrayList<>();
        String[] languageName = {"English","German","Spanish"};
        Integer[] languageImg = {R.drawable.ic_english,R.drawable.ic_german,R.drawable.ic_spanish};

        for (int i=0; i<languageName.length; i++){
            languageArrayList.add(new Categories(languageName[i],languageImg[i]));
        }

    }

    private void filterListener(String filterField , String filterValue) {
        db.collection("Courses").whereEqualTo(filterField,filterValue)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!= null){
                            Log.e("firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                filterCourseArrayList.add(dc.getDocument().toObject(Courses.class));
                            }
                            filterCourseAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void searchListener(String filterField , String filterValue) {
        db.collection("Courses").orderBy(filterField).startAt(filterValue)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!= null){
                            Log.e("firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                filterCourseArrayList.add(dc.getDocument().toObject(Courses.class));
                            }
                            filterCourseAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }




    @Override
    public void onPopularCourseClick(int position, String courseName) {
        Intent intent = new Intent(SearchActivity.this, CourseActivity.class);
        intent.putExtra("courseName",courseName);
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(int position, String categoryName , String filterField) {
        String filterValue = categoryName;
        filterCourseArrayList.clear();
        filterCourseAdapter.notifyDataSetChanged();
        filterListener(filterField,filterValue);
    }
}