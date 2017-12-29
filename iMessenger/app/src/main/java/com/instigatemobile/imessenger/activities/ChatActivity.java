package com.instigatemobile.imessenger.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.instigatemobile.imessenger.R;
import com.instigatemobile.imessenger.data.NotificationData;
import com.instigatemobile.imessenger.data.PostRequestData;
import com.instigatemobile.imessenger.data.SharedPreferenceHelper;
import com.instigatemobile.imessenger.data.StaticConfig;
import com.instigatemobile.imessenger.models.Conversation;
import com.instigatemobile.imessenger.models.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerChat;
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    private ListMessageAdapter adapter;
    public String roomId;
    public ArrayList<CharSequence> idFriend;
    private Conversation consersation;
    public ImageButton btnSend;
    public  String nameFriend;
    private EditText editWriteMessage;
    private LinearLayoutManager linearLayoutManager;
    public static HashMap<String, Bitmap> bitmapAvataFriend;
    public Bitmap bitmapAvataUser;


    private static final String SERVER_KEY = "AAAA4RXWeiI:APA91bEiawUeTrWSOB8VJUHAfNpUl2GtXlmwmp9VVf6IJOC0nGU6ihINOerQIiuoGhmpXeyyDcHNDRy6ZFX7X3rPIWWp20YlBkyz185ichGcL8T_bLfpseT5TeJYLdQfwlQ9icR64qcx";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intentData = getIntent();
        idFriend = intentData.getCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID);
        roomId = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
        nameFriend = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND);
        
        consersation = new Conversation();
        initCustomActionBar(nameFriend);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        String base64AvataUser = SharedPreferenceHelper.getInstance(this).getUserInfo().avata;
        if (!base64AvataUser.equals(StaticConfig.STR_DEFAULT_BASE64)) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference ref = storage.getReference();
            ref.child(base64AvataUser).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    bitmapAvataUser = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    editWriteMessage = (EditText) findViewById(R.id.editWriteMessage);
                    if (idFriend != null && nameFriend != null) {
                        linearLayoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerChat = (RecyclerView) findViewById(R.id.recyclerChat);
                        recyclerChat.setLayoutManager(linearLayoutManager);
                        adapter = new ListMessageAdapter(ChatActivity.this, consersation, bitmapAvataFriend, bitmapAvataUser);
                        FirebaseDatabase.getInstance().getReference().child("message/" + roomId).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.getValue() != null) {
                                    HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                                    Message newMessage = new Message();
                                    newMessage.idSender = (String) mapMessage.get("idSender");
                                    newMessage.idReceiver = (String) mapMessage.get("idReceiver");
                                    newMessage.text = (String) mapMessage.get("text");
                                    newMessage.timestamp = (long) mapMessage.get("timestamp");
                                    consersation.getListMessageData().add(newMessage);
                                    adapter.notifyDataSetChanged();
                                    linearLayoutManager.scrollToPosition(consersation.getListMessageData().size() - 1);
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    //consersation.getListMessageData().removeAll(consersation.getListMessageData());
                                    consersation.getListMessageData().clear();
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        recyclerChat.setAdapter(adapter);
                    }
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        } else {
            bitmapAvataUser = null;
        }
    }

    private void initCustomActionBar(String nameFriend) {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);
        Button btnDelMsg = (Button) mCustomView.findViewById(R.id.del_msg);
        btnDelMsg.setOnClickListener(this);
        getSupportActionBar().setTitle(nameFriend);
        TextView friend_name = (TextView) mCustomView.findViewById((R.id.friend_name));
        friend_name.setText(nameFriend);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent result = new Intent();
            result.putExtra("idFriend", idFriend.get(0));
            setResult(RESULT_OK, result);
            this.finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("idFriend", idFriend.get(0));
        setResult(RESULT_OK, result);
        this.finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSend) {
            String content = editWriteMessage.getText().toString().trim();
            if (content.length() > 0) {
                editWriteMessage.setText("");
                Message newMessage = new Message();
                newMessage.idSender = StaticConfig.UID;
                newMessage.idReceiver = roomId;
                newMessage.text = content;
                newMessage.timestamp = System.currentTimeMillis();
                FirebaseDatabase.getInstance().getReference().child("message/" + roomId).push().setValue(newMessage);
                sendHttpRequest();
            }
        } else if (view.getId() == R.id.del_msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.delete_msg_quastion)
                    .setCancelable(true)
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().getRoot().child("message").child(roomId);
                            dbRef.setValue(null);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void sendHttpRequest() {
        Gson gson = new Gson();
        NotificationData data = new NotificationData();
        data.setTitle(getString(R.string.new_message));
        String messageText = editWriteMessage.getText().toString().trim();
        data.setText(messageText);
        PostRequestData postRequestData = new PostRequestData();
        postRequestData.setTo("/topics/" + idFriend.get(0));
        postRequestData.setData(data);
        String json = gson.toJson(postRequestData);
        String url = "https://fcm.googleapis.com/fcm/send";

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "key=" + SERVER_KEY)
                .post(body)
                .build();

        Callback responseCallBack = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("Fail Message", "fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v("response", response.toString());
            }

        };
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(responseCallBack);
    }
}
class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Conversation consersation;
    private HashMap<String, Bitmap> bitmapAvata;
    private HashMap<String, DatabaseReference> bitmapAvataDB;
    private Bitmap bitmapAvataUser;

    public ListMessageAdapter(Context context, Conversation con, HashMap<String, Bitmap> bitmapAvata, Bitmap bitmapAvataUser) {
        this.context = context;
        this.consersation = con;
        this.bitmapAvata = bitmapAvata;
        this.bitmapAvataUser = bitmapAvataUser;
        bitmapAvataDB = new HashMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ChatActivity.VIEW_TYPE_FRIEND_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
            ItemMessageFriendHolder friendHolder =  new ItemMessageFriendHolder(view);
            friendHolder.avata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ListMessageAdapter.this.context, ContactProfileActivity.class);
                    ChatActivity chatActivity = (ChatActivity)(ListMessageAdapter.this.context);
                    intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, chatActivity.idFriend);
                    intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, chatActivity.roomId);
                    intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, chatActivity.nameFriend);
                    ListMessageAdapter.this.context.startActivity(intent);
                    chatActivity.finish();

                }
            });
            return friendHolder;
        } else if (viewType == ChatActivity.VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
            return new ItemMessageUserHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemMessageFriendHolder) {
            ((ItemMessageFriendHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text);
            Bitmap currentAvata = bitmapAvata.get(consersation.getListMessageData().get(position).idSender);
            if (currentAvata != null) {
                ((ItemMessageFriendHolder) holder).avata.setImageBitmap(currentAvata);
            } else {
                final String id = consersation.getListMessageData().get(position).idSender;
                if (bitmapAvataDB.get(id) == null) {
                    bitmapAvataDB.put(id, FirebaseDatabase.getInstance().getReference().child("user/" + id + "/avata"));
                    bitmapAvataDB.get(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                String avataStr = (String) dataSnapshot.getValue();
                                if (!avataStr.equals(StaticConfig.STR_DEFAULT_BASE64)) {
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference ref = storage.getReference();
                                    ref.child(avataStr).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            ChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                        }
                                    });
                                } else {
                                    ChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeResource(context.getResources(), R.drawable.default_avata));
                                }
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        } else if (holder instanceof ItemMessageUserHolder) {
            ((ItemMessageUserHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text);
            if (bitmapAvataUser != null) {
                ((ItemMessageUserHolder) holder).avata.setImageBitmap(bitmapAvataUser);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return consersation.getListMessageData().get(position).idSender.equals(StaticConfig.UID) ? ChatActivity.VIEW_TYPE_USER_MESSAGE : ChatActivity.VIEW_TYPE_FRIEND_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return consersation.getListMessageData().size();
    }
}

class ItemMessageUserHolder extends RecyclerView.ViewHolder {
    public TextView txtContent;
    public CircleImageView avata;

    public ItemMessageUserHolder(View itemView) {
        super(itemView);
        txtContent = (TextView) itemView.findViewById(R.id.textContentUser);
        avata = (CircleImageView) itemView.findViewById(R.id.imageView2);
    }
}

class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
    public TextView txtContent;
    public CircleImageView avata;

    public ItemMessageFriendHolder(View itemView) {
        super(itemView);
        txtContent = (TextView) itemView.findViewById(R.id.textContentFriend);
        avata = (CircleImageView) itemView.findViewById(R.id.imageView3);
    }
}
