package io.jachoteam.kaska;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import io.jachoteam.kaska.models.ChatUser;
import io.jachoteam.kaska.models.Message;

public class ChatActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private MessagesList messagesList;
    private MessageInput mMessageInput;
    private MessagesListAdapter<IMessage> adapter;
    //private Message mMessage;
    private ChatUser user;
    private Bitmap imageBitmap;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagesList = findViewById(R.id.messagesList);
        mMessageInput = findViewById(R.id.input);

        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Bitmap bitmap = StringToBitMap(url);
                Glide.with(ChatActivity.this).load(bitmap).into(imageView);
            }
        };

        adapter = new MessagesListAdapter<>("1", imageLoader);
        messagesList.setAdapter(adapter);

        mMessageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                //validate and send message
                adapter.addToStart(addMessage("1", input.toString()), true);
                return true;
            }
        });

        mMessageInput.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                dispatchTakePictureIntent();
                //adapter.addToStart(getImageMessage(), true);
            }
        });

        mMessageInput.setTypingListener(new MessageInput.TypingListener() {
            @Override
            public void onStartTyping() {

            }

            @Override
            public void onStopTyping() {

            }
        });

    }

    private Message addMessage(String userId, String text) {
        user = new ChatUser("1", "Omurbek", "https://avatars2.githubusercontent.com/u/8253376?s=400&u=35f9101ea224652b4e685d632ca67864de8d2aac&v=4", true);
        return new Message(userId, user, text, new Date());
    }

    public Message getImageMessage() {
        user = new ChatUser("1", "Omurbek", "https://avatars2.githubusercontent.com/u/8253376?s=400&u=35f9101ea224652b4e685d632ca67864de8d2aac&v=4", true);
        Message message = new Message("1", user, "Sometimes?");
        message.setImage(new Message.Image(BitMapToString(imageBitmap)));
        return message;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            assert extras != null;
            imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
            adapter.addToStart(getImageMessage(), true);
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
