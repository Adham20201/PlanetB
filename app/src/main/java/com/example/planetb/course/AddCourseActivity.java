package com.example.planetb.course;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;

import com.example.planetb.MainActivity;
import com.example.planetb.R;
import com.example.planetb.lists.Courses;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AddCourseActivity extends AppCompatActivity implements View.OnClickListener {

    Button submitBtn;
    ImageView imgLogo;
    TextInputLayout editTxtTitle, editTxtOrganization, editTxtCategory, editTxtLevel, editTxtLanguage, editTxtType, ediTxtBrief;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;

    Uri chosenCourseImageUri;

    String randomID;

    Courses course;

    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        course = new Courses();
        randomID = UUID.randomUUID().toString();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Courses Backgrounds");

        submitBtn = findViewById(R.id.upload_btn);
        imgLogo = findViewById(R.id.add_img_course_logo);
        editTxtTitle = findViewById(R.id.addCourseTitle);
        editTxtOrganization = findViewById(R.id.addCourseProducer);
        editTxtCategory = findViewById(R.id.addCourseCategory);
        editTxtLevel = findViewById(R.id.addCourseLevel);
        editTxtLanguage = findViewById(R.id.addCourseLanguage);
        editTxtType = findViewById(R.id.addCourseTrainingType);
        ediTxtBrief = findViewById(R.id.addCourseBrief);

        imgLogo.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

    }

    private void coursesData(){
        String courseName = editTxtTitle.getEditText().getText().toString();
        String courseOrganizer = editTxtOrganization.getEditText().getText().toString();
        String courseCategory = editTxtCategory.getEditText().getText().toString();
        String courseLanguage = editTxtLanguage.getEditText().getText().toString();
        String courseType = editTxtType.getEditText().getText().toString();
        String courseLevel = editTxtLevel.getEditText().getText().toString();
        String courseBrief = ediTxtBrief.getEditText().getText().toString();

        String courseUrl = null;

        course.setCourseName(courseName);
        course.setCourseOrganizer(courseOrganizer);
        course.setCourseCategory(courseCategory);
        course.setCourseLanguage(courseLanguage);
        course.setCourseType(courseType);
        course.setCourseLevel(courseLevel);
        course.setCourseBrief(courseBrief);
        course.setCourseUrl(courseUrl);


    }



    private void openFileChooser (){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage (){
        coursesData();
        StorageReference fileReference = storageReference.child(randomID+"."+getFileExtension(chosenCourseImageUri));
        UploadTask uploadTask = fileReference.putFile(chosenCourseImageUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    course.setCourseImageUrl(downloadUri.toString());
                    uploadFile();
                }
            }
            });

    }

    private void uploadFile (){
        db.collection("Courses").document(randomID)
                .set(course)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(AddCourseActivity.this, MainActivity.class));
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_img_course_logo:
                openFileChooser();
                break;
            case R.id.upload_btn:
                uploadImage();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            chosenCourseImageUri = data.getData();
            Picasso.get()
                    .load(chosenCourseImageUri)
                    .fit()
                    .into(imgLogo);
        }
    }
}