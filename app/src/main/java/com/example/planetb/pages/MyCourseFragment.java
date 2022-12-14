package com.example.planetb.pages;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.planetb.adapters.TwoColumnCourseAdapter;
import com.example.planetb.course.AddCourseActivity;
import com.example.planetb.R;
import com.example.planetb.course.CourseActivity;
import com.example.planetb.course.CourseWithLinkActivity;
import com.example.planetb.course.CoursesInterface;
import com.example.planetb.lists.Courses;
import com.example.planetb.lists.UserCourses;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MyCourseFragment extends Fragment implements CoursesInterface {

    FloatingActionButton addCourseBtn;
    TwoColumnCourseAdapter myCoursesAdapter;
    RecyclerView myCoursesRecycleView;
    ArrayList<Courses> myCoursesArrayList;
    ArrayList<UserCourses> myCoursesArraylist;


    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;

    FirebaseDatabase rootNode;
    DatabaseReference reference;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addCourseBtn = view.findViewById(R.id.addCourseBtn);
        myCoursesRecycleView = view.findViewById(R.id.myCourses_recycle);
        myCoursesArrayList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance("https://planetb-c524b-default-rtdb.firebaseio.com/");
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false);
        myCoursesRecycleView.setLayoutManager(layoutManager);
        myCoursesRecycleView.setItemAnimator(new DefaultItemAnimator());
        myCoursesAdapter = new TwoColumnCourseAdapter(getContext(),myCoursesArrayList,this);
        myCoursesRecycleView.setAdapter(myCoursesAdapter);

        eventDbChangeListener();

        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddCourseActivity.class));
            }
        });

    }

    private void eventDbChangeListener() {


        rootNode.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Courses")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                    db.collection("Courses").whereEqualTo("courseName", snapshot1.getValue().toString() )
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    if (error!= null){
                                                        Log.e("firestore error", error.getMessage());
                                                        return;
                                                    }

                                                    for (DocumentChange dc : value.getDocumentChanges()){
                                                        if (dc.getType() == DocumentChange.Type.ADDED){
                                                            myCoursesArrayList.add(dc.getDocument().toObject(Courses.class));
                                                        }
                                                        myCoursesAdapter.notifyDataSetChanged();
                                                    }

                                                }
                                            });                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
    }


    @Override
    public void onPopularCourseClick(int position, String courseName) {
        Intent intent = new Intent(getActivity(), CourseWithLinkActivity.class);
        intent.putExtra("courseName",courseName);
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(int position, String categoryName, String filterField) {

    }
}