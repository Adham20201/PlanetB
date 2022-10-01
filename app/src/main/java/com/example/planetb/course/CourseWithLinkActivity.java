package com.example.planetb.course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.planetb.R;
import com.example.planetb.lists.Courses;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class CourseWithLinkActivity extends AppCompatActivity {

    Courses course;

    FirebaseFirestore db;

    String courseName;

    ImageView imgLogo;
    TextView txtTitle, txtOrganization, txtCategory, txtLevel, txtLanguage, txtType, txtBrief;

    Boolean imageUploaded, textUploaded , work;

    ProgressBar progressBar;
    ConstraintLayout progressBarCont;

    UploadingDone uploadingDone;

    Button addCourse;

    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_with_link);

        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance("https://planetb-c524b-default-rtdb.firebaseio.com/");

        uploadingDone = new UploadingDone();
        uploadingDone.execute();

        imageUploaded = false;
        textUploaded = false;
        work = true;

        addCourse = findViewById(R.id.select_Course_btn);

        progressBarCont = findViewById(R.id.progressBarContainer);
        progressBar = findViewById(R.id.progressBar);

        imgLogo = findViewById(R.id.img_course_logo);
        txtTitle = findViewById(R.id.txt_course_name);
        txtOrganization = findViewById(R.id.txt_organization);
        txtCategory = findViewById(R.id.txt_category);
        txtLevel = findViewById(R.id.text_level);
        txtLanguage = findViewById(R.id.txt_language);
        txtType = findViewById(R.id.txt_type);
        txtBrief = findViewById(R.id.txt_brief);

        courseName = getIntent().getStringExtra("courseName");

        db = FirebaseFirestore.getInstance();
        db.collection("Courses").whereEqualTo("courseName",courseName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                course = document.toObject(Courses.class);

                                Picasso.get()
                                        .load(course.getCourseImageUrl())
                                        .fit()
                                        .centerCrop()
                                        .into(imgLogo, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {
                                                imageUploaded = true;
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                imageUploaded = true;
                                            }
                                        });

                                txtTitle.setText(course.getCourseName());
                                txtOrganization.setText(course.getCourseOrganizer());
                                txtCategory.setText(course.getCourseCategory());
                                txtLevel.setText(course.getCourseLevel());
                                txtLanguage.setText(course.getCourseLanguage());
                                txtType.setText(course.getCourseType());
                                txtBrief.setText(course.getCourseBrief());

                            }
                        }
                        textUploaded = true;

                    }
                });

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(course.getCourseUrl()));
                startActivity(browserIntent);
            }
        });

    }

    private class UploadingDone extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            while (work){
                if (textUploaded && imageUploaded){
                    work = false;
                    progressBar.setVisibility(View.GONE);
                    progressBarCont.setVisibility(View.GONE);
                    uploadingDone.cancel(true);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}