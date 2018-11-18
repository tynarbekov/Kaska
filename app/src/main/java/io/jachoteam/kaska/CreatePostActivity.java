package io.jachoteam.kaska;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mvc.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import id.zelory.compressor.Compressor;
import io.jachoteam.kaska.data.firebase.FirebaseFeedPostsRepository;
import io.jachoteam.kaska.data.firebase.FirebaseUsersRepository;
import io.jachoteam.kaska.models.Comment;
import io.jachoteam.kaska.models.FeedPost;
import io.jachoteam.kaska.models.Image;
import io.jachoteam.kaska.models.User;
import io.jachoteam.kaska.screens.common.BaseActivity;
import io.jachoteam.kaska.screens.common.CameraHelper;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CreatePostActivity extends BaseActivity {
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public String audioFilePath = "";
    public Uri audioUri = null;
    public Uri audioDownloadUri = null;
    public List<Task<Uri>> taskArrayList = new ArrayList<>();
    FirebaseFeedPostsRepository firebaseFeedPostsRepository = new FirebaseFeedPostsRepository();
    FirebaseUsersRepository firebaseUsersRepository = new FirebaseUsersRepository();
    ImageView backImage;
    ImageView postImage;
    ImageView postImage1;
    ImageView postImage2;
    ImageView postImage3;
    Button recordAudioButton;
    TextView sharePost;
    User user;
    String userUid;
    FeedPost feedPost;
    private String AUDIO = "audio";
    private String VIDEO = "video";
    private String PHOTO = "PHOTO";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
    private StorageReference mStorage;
    private ProgressDialog progressDialog;
    private CameraHelper cameraHelper;
    private int currentPhotoIndex = 0;
    private int currentProgress = 0;
    private Uri[] postImagesUri = new Uri[4];
    private Uri[] downloadUri = new Uri[4];
    private int currentDownloadUriIndex = 0;
    private EditText captionText;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImagePicker.setMinQuality(600, 600);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseUsersRepository.getUser(userUid).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User newUser) {
                user = newUser;
            }
        });
        setContentView(R.layout.activity_create_post);
        cameraHelper = new CameraHelper(this);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorage = FirebaseStorage.getInstance().getReference();
        backImage = findViewById(R.id.back_image);
        postImage = findViewById(R.id.post_image);
        postImage1 = findViewById(R.id.post_image1);
        postImage2 = findViewById(R.id.post_image2);
        postImage3 = findViewById(R.id.post_image3);
        sharePost = findViewById(R.id.share_text);
        captionText = findViewById(R.id.caption_input);
        recordAudioButton = findViewById(R.id.record_audio_button);

        progressDialog = new ProgressDialog(this);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recordAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAudioFromMic();
            }
        });

        sharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedPost = createFeedPost();
                Log.i("Share post", "here");
                progressDialog.setMessage("Uploading post...");
                progressDialog.show();

                if (audioUri != null) {
                    taskArrayList.add(uploadFile(audioUri, AUDIO, 0));
                }

                for (int i = 0; i < postImagesUri.length; i++) {
                    if (null != postImagesUri[i]) {
                        taskArrayList.add(uploadFile(postImagesUri[i], PHOTO, currentDownloadUriIndex));
                        currentDownloadUriIndex++;
                    }
                }

                Tasks.whenAllSuccess(taskArrayList).addOnCompleteListener(new OnCompleteListener<List<Object>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Object>> task) {
                        Log.i("tasksuccess", "YEAA");
                        List<Object> downloadUriList = task.getResult();
                        firebaseFeedPostsRepository.createFeedPost(userUid, feedPost);
                        progressDialog.dismiss();
                    }
                });

            }
        });


        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoIndex = 0;
//                cameraHelper.takeCameraPicture();
                onPickImage();
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

    public void onPickImage() {
        // Click on image button
        ImagePicker.pickImage(this, "Select your image:");
    }

    private Task<Uri> uploadAudioFile() {
        final StorageReference ref = mStorage.child("users/" + userUid + "/audios").child(audioUri.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(audioUri);

        return uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.i("tasksuccess-audio", downloadUri.getPathSegments().toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void recordAudioFromMic() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    /**
     * @param uri
     * @param TYPE
     * @param index
     * @return
     */
    private Task<Uri> uploadFile(Uri uri, final String TYPE, final int index) {

        String link = "users/" + userUid;
        if (TYPE.equals(PHOTO)) {
            link += "/pictures";
        } else if (TYPE.equals(AUDIO)) {
            link += "/audios";
        }

        final StorageReference ref = mStorage.child(link).child(uri.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(uri);

        return uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri uri = task.getResult();

                    if (TYPE.equals(PHOTO)) {
                        Image image = new Image("image" + index,
                                uri.toString(),
                                index);
                        Log.i("image", image.toString());
                        feedPost.getImages().put(image.getUid(), image);
                    } else if (TYPE.equals(AUDIO)) {
                        feedPost.setAudioUrl(uri.toString());
                    }

                } else {
                    Log.i("ERROR UPLOADING", TYPE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && null != cameraHelper.getImageUri()) {

            Uri uri = cameraHelper.getImageUri();
            File imageFile = new File(uri.getPath());
            File abc = new File(String.valueOf(uri));
            File compressedImageFile = null;
            try {
                compressedImageFile = new Compressor(this).setQuality(50).compressToFile(imageFile);
                uri = Uri.fromFile(compressedImageFile);
                Log.i("Image compressed", uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

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
        } else if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION && resultCode == RESULT_OK) {
            Log.i("audio", "recorded successfully");
            audioUri = Uri.fromFile(new File(audioFilePath));
        } else {
            Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            Log.i("BITMAP", "image picked");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION: {
                audioFilePath = Environment.getExternalStorageDirectory() + "/" + simpleDateFormat.format(new Date()) + ".wav";
                int color = getResources().getColor(R.color.colorPrimaryDark);
                AndroidAudioRecorder.with(this)
                        // Required
                        .setFilePath(audioFilePath)
                        .setColor(color)
                        .setRequestCode(REQUEST_RECORD_AUDIO_PERMISSION)
                        // Optional
                        .setSource(AudioSource.MIC)
                        .setChannel(AudioChannel.STEREO)
                        .setSampleRate(AudioSampleRate.HZ_16000)
                        .setAutoStart(false)
                        .setKeepDisplayOn(true)
                        .record();
                return;
            }
        }
    }

    private FeedPost createFeedPost() {
        return new FeedPost(
                userUid,
                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                "",
                captionText.getText().toString(),
                new HashMap<String, Image>(),
                new ArrayList<Comment>(),
                Calendar.getInstance().getTimeInMillis(),
                user.getPhoto(),
                "",
                0,
                "");
    }
}
