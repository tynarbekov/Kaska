package io.jachoteam.kaska;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.jachoteam.kaska.models.User;
import io.jachoteam.kaska.screens.common.CameraHelper;

public class CreatePostActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef;
    DatabaseReference postRef;
    ImageView backImage;
    ImageView postImage;
    ImageView postImage1;
    ImageView postImage2;
    ImageView postImage3;
    TextView sharePost;
    User user;
    String userUid;
    Intent intent;
    File postFile;
    private StorageReference mStorage;
    private ProgressDialog progressDialog;
    private CameraHelper cameraHelper;
    private int currentPhotoIndex = 0;
    private int currentProgress = 0;
    private Uri[] postImagesUri = new Uri[4];
    private Uri[] downloadUri = new Uri[4];
    private int currentDownloadUriIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        cameraHelper = new CameraHelper(this);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorage = FirebaseStorage.getInstance().getReference();
        backImage = (ImageView) findViewById(R.id.back_image);
        postImage = (ImageView) findViewById(R.id.post_image);
        postImage1 = (ImageView) findViewById(R.id.post_image1);
        postImage2 = (ImageView) findViewById(R.id.post_image2);
        postImage3 = (ImageView) findViewById(R.id.post_image3);
        sharePost = (TextView) findViewById(R.id.share_text);

        progressDialog = new ProgressDialog(this);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Share post", "here");
                progressDialog.setMessage("Uploading post...");
                progressDialog.show();
                for (int i = 0; i < postImagesUri.length; i++) {
                    if (null != postImagesUri[i]) {
                        uploadFile(postImagesUri[i]);
                    } else {
                        currentProgress += 25;
                    }
                }
            }
        });


        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoIndex = 0;
                cameraHelper.takeCameraPicture();
            }
        });

        postImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoIndex = 1;
                cameraHelper.takeCameraPicture();
            }
        });
        postImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoIndex = 2;
                cameraHelper.takeCameraPicture();
            }
        });
        postImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoIndex = 3;
                cameraHelper.takeCameraPicture();
            }
        });

    }

    private void uploadFile(Uri uri) {
        StorageReference filePath = mStorage.child("users/" + userUid + "/test").child(uri.getLastPathSegment());
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CreatePostActivity.this, "Uploading finished!", Toast.LENGTH_LONG).show();
                currentProgress += 25;

                downloadUri[currentDownloadUriIndex] = taskSnapshot.getDownloadUrl();
                currentDownloadUriIndex++;

                if (currentProgress == 100) {
                    progressDialog.dismiss();

                    // TODO SAVE TO POST MODEL
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && null != cameraHelper.getImageUri()) {

            Uri uri = cameraHelper.getImageUri();
            if (currentPhotoIndex == 0) {
                Picasso.get().load(uri).into(postImage);
            } else if (currentPhotoIndex == 1) {
                Picasso.get().load(uri).into(postImage1);
            } else if (currentPhotoIndex == 2) {
                Picasso.get().load(uri).into(postImage2);
            } else if (currentPhotoIndex == 3) {
                Picasso.get().load(uri).into(postImage3);
            }

            postImagesUri[currentPhotoIndex] = uri;
        }
    }

}
