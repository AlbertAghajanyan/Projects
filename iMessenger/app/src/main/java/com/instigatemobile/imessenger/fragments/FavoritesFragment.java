package com.instigatemobile.imessenger.fragments;

/*
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instigatemobile.imessenger.R;

public class FavoritesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }
}
*/


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.instigatemobile.imessenger.R;
import com.instigatemobile.imessenger.activities.ChatActivity;
import com.instigatemobile.imessenger.data.LocalDB;
import com.instigatemobile.imessenger.data.StaticConfig;
import com.instigatemobile.imessenger.models.Friend;
import com.instigatemobile.imessenger.models.ListFriend;
import com.instigatemobile.imessenger.service.ServiceUtils;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String ACTION_DELETE_FRIEND = "com.instigatemobile.imessenger.fragments.DELETE_FRIEND";
    public static int ACTION_START_CHAT = 1;
    public FragFriendClickFloatButton onFABClick;
    private ListFavoritAdapter adapter;
    private ListFriend dataListFriend = null;
    private ArrayList<String> listFriendID = null;
    private ArrayList<String> favoriteFriendsIDs = null;
    private LovelyProgressDialog dialogFindAllFriend;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CountDownTimer detectFriendOnline;
    private BroadcastReceiver deleteFriendReceiver;

    public FavoritesFragment() {
        onFABClick = new FragFriendClickFloatButton();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (favoriteFriendsIDs == null || favoriteFriendsIDs.isEmpty()) {
            favoriteFriendsIDs = new ArrayList<String>();
//            FirebaseDatabase.getInstance().getReference().child("favorite").child(StaticConfig.UID)
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//                            if (dataSnapshot.getValue() == null) {
//                                return;
//                            } else {
//                                HashMap tmp = ((HashMap) dataSnapshot.child(StaticConfig.UID).getValue());
//                                if (tmp == null)
//                                    return;
//                                Iterator it = tmp.keySet().iterator();
//                                while (it.hasNext()) {
//                                    Object key = it.next();
//                                    favoriteFriendsIDs.add(tmp.get(key).toString().trim());
//                                }
//                            }
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                        }
//                    });
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        detectFriendOnline = new CountDownTimer(System.currentTimeMillis(), StaticConfig.TIME_TO_REFRESH) {
            @Override
            public void onTick(long l) {
                ServiceUtils.updateFriendStatus(getContext(), dataListFriend);
                ServiceUtils.updateUserStatus(getContext());
            }

            @Override
            public void onFinish() {
            }
        };

        if (dataListFriend == null) {
            dataListFriend = new ListFriend();
//            ListFriend tmpListFriend = LocalDB.getInstance(getContext()).getListFriend();
//            if (tmpListFriend.getListFriend().size() > 0) {
//                listFriendID = new ArrayList<>();
//                for (Friend friend : tmpListFriend.getListFriend()) {
//                    if (favoriteFriendsIDs.contains(friend.id)) {
//                        listFriendID.add(friend.id);
//                        dataListFriend.add(friend);
//                    }
//                }
//                detectFriendOnline.start();
//            }
        }

        View layout = inflater.inflate(R.layout.fragment_contacts, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerListFrends = (RecyclerView) layout.findViewById(R.id.recycleListFriend);
        recyclerListFrends.setLayoutManager(linearLayoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        for (int i = 0; i < dataListFriend.getListFriend().size(); ++i) {
            System.out.println(dataListFriend.getListFriend().get(i).getId());
            if (!favoriteFriendsIDs.contains(dataListFriend.getListFriend().get(i).getId().toString())) {
                dataListFriend.removeFriend(i);
            }
        }


        adapter = new ListFavoritAdapter(getContext(), dataListFriend, this, favoriteFriendsIDs);
        recyclerListFrends.setAdapter(adapter);
        dialogFindAllFriend = new LovelyProgressDialog(getContext());
        if (listFriendID == null) {
            listFriendID = new ArrayList<>();
//            dialogFindAllFriend.setCancelable(false)
//                    .setIcon(R.drawable.ic_add_friend)
//                    .setTitle(R.string.get_friends)
//                    .setTopColorRes(R.color.colorPrimary)
//                    .show();
//            getListFriendUId();
        }

        deleteFriendReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String idDeleted = intent.getExtras().getString("idFriend");
                for (Friend friend : dataListFriend.getListFriend()) {
                    if (idDeleted.equals(friend.id)) {
                        ArrayList<Friend> friends = dataListFriend.getListFriend();
                        friends.remove(friend);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        };
        IntentFilter intentFilter = new IntentFilter(ACTION_DELETE_FRIEND);
        getContext().registerReceiver(deleteFriendReceiver, intentFilter);

        refresh();
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getContext().unregisterReceiver(deleteFriendReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ACTION_START_CHAT == requestCode && data != null && ListFavoritAdapter.mapMark != null) {
            ListFavoritAdapter.mapMark.put(data.getStringExtra("idFriend"), false);
        }
    }


    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh() {
        listFriendID.clear();
        dataListFriend.getListFriend().clear();
        adapter.notifyDataSetChanged();
        LocalDB.getInstance(getContext()).dropDB();
        detectFriendOnline.cancel();
        getListFriendUId();
        favoriteFriendsIDs.clear();
    }

    private void getListFriendUId() {
        FirebaseDatabase.getInstance().getReference().child("favorite/" + StaticConfig.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    HashMap mapRecord = (HashMap) dataSnapshot.getValue();
                    Iterator listKey = mapRecord.keySet().iterator();
                    while (listKey.hasNext()) {
                        String key = listKey.next().toString();
                        listFriendID.add(mapRecord.get(key).toString());
                    }
                    getAllFriendInfo(0);
                } else {
                    dialogFindAllFriend.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getAllFriendInfo(final int index) {
        if (index == listFriendID.size()) {
            adapter.notifyDataSetChanged();
            dialogFindAllFriend.dismiss();
            mSwipeRefreshLayout.setRefreshing(false);
            detectFriendOnline.start();
        } else {
            final String id = listFriendID.get(index);
            FirebaseDatabase.getInstance().getReference().child("user/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Friend user = new Friend();
                        HashMap mapUserInfo = (HashMap) dataSnapshot.getValue();
                        user.name = (String) mapUserInfo.get("name");
                        user.email = (String) mapUserInfo.get("email");
                        user.avata = (String) mapUserInfo.get("avata");
                        user.id = id;
                        user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();
                        dataListFriend.getListFriend().add(user);
                        LocalDB.getInstance(getContext()).addFriend(user);
                    }
                    getAllFriendInfo(index + 1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public class FragFriendClickFloatButton implements View.OnClickListener {
        Context context;
        LovelyProgressDialog dialogWait;

        public FragFriendClickFloatButton() {
        }

        public FragFriendClickFloatButton getInstance(Context context) {
            this.context = context;
            dialogWait = new LovelyProgressDialog(context);
            return this;
        }

        @Override
        public void onClick(final View view) {
            new LovelyTextInputDialog(view.getContext(), R.style.DialogTheme)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle(R.string.add_friend)
                    .setMessage(R.string.friend_email)
                    .setIcon(R.drawable.ic_add_friend)
                    .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                    .setInputFilter(R.string.email_not_found, new LovelyTextInputDialog.TextFilter() {
                        @Override
                        public boolean check(String text) {
                            Pattern VALID_EMAIL_ADDRESS_REGEX =
                                    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(text);
                            return matcher.find();
                        }
                    })
                    .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                        @Override
                        public void onTextInputConfirmed(String text) {
                            findIDEmail(text);
                        }
                    })
                    .show();
        }

        private void findIDEmail(String email) {
            dialogWait.setCancelable(false)
                    .setIcon(R.drawable.ic_add_friend)
                    .setTitle(R.string.finding_friend)
                    .setTopColorRes(R.color.colorPrimary)
                    .show();
            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dialogWait.dismiss();
                    if (dataSnapshot.getValue() == null) {
                        new LovelyInfoDialog(context)
                                .setTopColorRes(R.color.colorAccent)
                                .setIcon(R.drawable.ic_add_friend)
                                .setTitle(R.string.fail)
                                .setMessage(R.string.email_not_found)
                                .show();
                    } else {
                        String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                        if (id.equals(StaticConfig.UID)) {
                            new LovelyInfoDialog(context)
                                    .setTopColorRes(R.color.colorAccent)
                                    .setIcon(R.drawable.ic_add_friend)
                                    .setTitle(R.string.fail)
                                    .setMessage(R.string.email_not_valid)
                                    .show();
                        } else {
                            HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                            Friend user = new Friend();
                            user.name = (String) userMap.get("name");
                            user.email = (String) userMap.get("email");
                            user.avata = (String) userMap.get("avata");
                            user.id = id;
                            user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();
                            checkBeforAddFriend(id, user);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void checkBeforAddFriend(final String idFriend, Friend userInfo) {
            dialogWait.setCancelable(false)
                    .setIcon(R.drawable.ic_add_friend)
                    .setTitle(R.string.add_friend)
                    .setTopColorRes(R.color.colorPrimary)
                    .show();

            if (listFriendID.contains(idFriend)) {
                dialogWait.dismiss();
                new LovelyInfoDialog(context)
                        .setTopColorRes(R.color.colorPrimary)
                        .setIcon(R.drawable.ic_add_friend)
                        .setTitle(R.string.friend)
                        .setMessage("User " + userInfo.email + " has been added to friends")
                        .show();
            } else {
                addFriend(idFriend, true);
                listFriendID.add(idFriend);
                dataListFriend.getListFriend().add(userInfo);
                LocalDB.getInstance(getContext()).addFriend(userInfo);
                adapter.notifyDataSetChanged();
            }
        }

        private void addFriend(final String idFriend, boolean isIdFriend) {
            if (idFriend != null) {
                if (isIdFriend) {
                    FirebaseDatabase.getInstance().getReference().child("friend/" + StaticConfig.UID).push().setValue(idFriend)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        addFriend(idFriend, false);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialogWait.dismiss();
                                    new LovelyInfoDialog(context)
                                            .setTopColorRes(R.color.colorPrimary)
                                            .setIcon(R.drawable.ic_add_friend)
                                            .setTitle(R.string.fail)
                                            .setMessage(R.string.failed_add_friend)
                                            .show();
                                }
                            });
                } else {
                    FirebaseDatabase.getInstance().getReference().child("friend/" + idFriend).push().setValue(StaticConfig.UID).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                addFriend(null, false);
                            }
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialogWait.dismiss();
                                    new LovelyInfoDialog(context)
                                            .setTopColorRes(R.color.colorPrimary)
                                            .setIcon(R.drawable.ic_add_friend)
                                            .setTitle(R.string.fail)
                                            .setMessage(R.string.failed_add_friend)
                                            .show();
                                }
                            });
                }
            } else {
                dialogWait.dismiss();
                new LovelyInfoDialog(context)
                        .setTopColorRes(R.color.colorPrimary)
                        .setIcon(R.drawable.ic_add_friend)
                        .setTitle(R.string.success)
                        .setMessage(R.string.add_friend_success)
                        .show();
            }
        }
    }
}

class ListFavoritAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static Map<String, Query> mapQuery;
    public static Map<String, DatabaseReference> mapQueryOnline;
    public static Map<String, ChildEventListener> mapChildListener;
    public static Map<String, ChildEventListener> mapChildListenerOnline;
    public static Map<String, Boolean> mapMark;
    LovelyProgressDialog dialogWaitDeleting;
    private List<String> favoriteFriendsIDs = null;
    private ListFriend listFriend;
    private Context context;
    private FavoritesFragment fragment;

    public ListFavoritAdapter(Context context, ListFriend listFriend, FavoritesFragment fragment, List<String> favoritesList) {
        this.listFriend = listFriend;
        this.context = context;
        mapQuery = new HashMap<>();
        mapChildListener = new HashMap<>();
        mapMark = new HashMap<>();
        mapChildListenerOnline = new HashMap<>();
        mapQueryOnline = new HashMap<>();
        this.fragment = fragment;
        favoriteFriendsIDs = favoritesList;
        dialogWaitDeleting = new LovelyProgressDialog(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_friend, parent, false);
        return new ItemFavoritViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final String name = listFriend.getListFriend().get(position).name;
        final String id = listFriend.getListFriend().get(position).id;
        final String idRoom = listFriend.getListFriend().get(position).idRoom;
        final String avata = listFriend.getListFriend().get(position).avata;
        ((ItemFavoritViewHolder) holder).txtName.setText(name);


        boolean isFavorite = favoriteFriendsIDs.contains(id);
        if (isFavorite) {
            ((ItemFavoritViewHolder) holder).imageButton.setImageResource(R.drawable.favorit2);
        } else {
            ((ItemFavoritViewHolder) holder).imageButton.setImageResource(R.drawable.favorit1);
        }

        ((View) ((ItemFavoritViewHolder) holder).imageButton).setOnClickListener(new View.OnClickListener() {
            boolean change = false;
            @Override
            public void onClick(View view) {
                change = favoriteFriendsIDs.contains(id);
                if (change) {
                    ((ItemFavoritViewHolder) holder).imageButton.setImageResource(R.drawable.favorit1);
                    change = false;
                    removeFavorite(id);
                    favoriteFriendsIDs.remove(id);
                    listFriend.removeFriend(position);
                    notifyDataSetChanged();
                } else {
                    ((ItemFavoritViewHolder) holder).imageButton.setImageResource(R.drawable.favorit2);
                    change = true;
                    addFavorite(id);
                    favoriteFriendsIDs.add(id);
                }

            }
        });


        ((View) ((ItemFavoritViewHolder) holder).txtName.getParent().getParent().getParent())
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ItemFavoritViewHolder) holder).txtMessage.setTypeface(Typeface.DEFAULT);
                        ((ItemFavoritViewHolder) holder).txtName.setTypeface(Typeface.DEFAULT);
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, name);
                        ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
                        idFriend.add(id);
                        intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
                        intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, idRoom);
                        ChatActivity.bitmapAvataFriend = new HashMap<>();
                        if (!avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference ref = storage.getReference();
                            ref.child(avata).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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

                        mapMark.put(id, null);
                        fragment.startActivityForResult(intent, FavoritesFragment.ACTION_START_CHAT);
                    }
                });

        ((View) ((ItemFavoritViewHolder) holder).txtName.getParent().getParent().getParent())
                .setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        String friendName = (String) ((ItemFavoritViewHolder) holder).txtName.getText();

                        new AlertDialog.Builder(context)
                                .setTitle(R.string.delete_friend)
                                .setMessage(R.string.delete_confirmation)// + friendName + R.string.question)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        final String idFriendRemoval = listFriend.getListFriend().get(position).id;
                                        dialogWaitDeleting.setTitle(R.string.deleting)
                                                .setCancelable(false)
                                                .setTopColorRes(R.color.colorAccent)
                                                .show();
                                        deleteFriend(idFriendRemoval);
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();

                        return true;
                    }
                });


        if (listFriend.getListFriend().get(position).message.text.length() > 0) {
            ((ItemFavoritViewHolder) holder).txtMessage.setVisibility(View.VISIBLE);
            ((ItemFavoritViewHolder) holder).txtTime.setVisibility(View.VISIBLE);
            if (!listFriend.getListFriend().get(position).message.text.startsWith(id)) {
                ((ItemFavoritViewHolder) holder).txtMessage.setText(listFriend.getListFriend().get(position).message.text);
                ((ItemFavoritViewHolder) holder).txtMessage.setTypeface(Typeface.DEFAULT);
                ((ItemFavoritViewHolder) holder).txtName.setTypeface(Typeface.DEFAULT);
            } else {
                ((ItemFavoritViewHolder) holder).txtMessage.setText(listFriend.getListFriend().get(position).message.text.substring((id + "").length()));
                ((ItemFavoritViewHolder) holder).txtMessage.setTypeface(Typeface.DEFAULT_BOLD);
                ((ItemFavoritViewHolder) holder).txtName.setTypeface(Typeface.DEFAULT_BOLD);
            }
            String time = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(listFriend.getListFriend().get(position).message.timestamp));
            String today = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(System.currentTimeMillis()));
            if (today.equals(time)) {
                ((ItemFavoritViewHolder) holder).txtTime.setText(new SimpleDateFormat("HH:mm").format(new Date(listFriend.getListFriend().get(position).message.timestamp)));
            } else {
                ((ItemFavoritViewHolder) holder).txtTime.setText(new SimpleDateFormat("MMM d").format(new Date(listFriend.getListFriend().get(position).message.timestamp)));
            }
        } else {
            ((ItemFavoritViewHolder) holder).txtMessage.setVisibility(View.GONE);
            ((ItemFavoritViewHolder) holder).txtTime.setVisibility(View.GONE);
            if (mapQuery.get(id) == null && mapChildListener.get(id) == null) {
                mapQuery.put(id, FirebaseDatabase.getInstance().getReference().child("message/" + idRoom).limitToLast(1));
                mapChildListener.put(id, new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                        if (mapMark.get(id) != null) {
                            if (!mapMark.get(id)) {
                                listFriend.getListFriend().get(position).message.text = id + mapMessage.get("text");
                            } else {
                                listFriend.getListFriend().get(position).message.text = (String) mapMessage.get("text");
                            }
                            notifyDataSetChanged();
                            mapMark.put(id, false);
                        } else {
                            listFriend.getListFriend().get(position).message.text = (String) mapMessage.get("text");
                            notifyDataSetChanged();
                        }
                        listFriend.getListFriend().get(position).message.timestamp = (long) mapMessage.get("timestamp");
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mapQuery.get(id).addChildEventListener(mapChildListener.get(id));
                mapMark.put(id, true);
            } else {
                mapQuery.get(id).removeEventListener(mapChildListener.get(id));
                mapQuery.get(id).addChildEventListener(mapChildListener.get(id));
                mapMark.put(id, true);
            }
        }
        if (listFriend.getListFriend().get(position).avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
            ((ItemFavoritViewHolder) holder).avata.setImageResource(R.drawable.default_avata);
        } else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference ref = storage.getReference();
            ref.child(avata).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap src = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ((ItemFavoritViewHolder) holder).avata.setImageBitmap(src);
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }


        if (mapQueryOnline.get(id) == null && mapChildListenerOnline.get(id) == null) {
            mapQueryOnline.put(id, FirebaseDatabase.getInstance().getReference().child("user/" + id + "/status"));
            mapChildListenerOnline.put(id, new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue() != null && dataSnapshot.getKey().equals("isOnline")) {
                        listFriend.getListFriend().get(position).status.isOnline = (boolean) dataSnapshot.getValue();
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue() != null && dataSnapshot.getKey().equals("isOnline")) {
                        if (listFriend.getListFriend().size() <= position) {
                            return;
                        }
                        listFriend.getListFriend().get(position).status.isOnline = (boolean) dataSnapshot.getValue();
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mapQueryOnline.get(id).addChildEventListener(mapChildListenerOnline.get(id));
        }

        if (listFriend.getListFriend().get(position).status.isOnline) {
            ((ItemFavoritViewHolder) holder).avata.setBorderWidth(10);
        } else {
            ((ItemFavoritViewHolder) holder).avata.setBorderWidth(0);
        }
    }

    @Override
    public int getItemCount() {
        return listFriend.getListFriend() != null ? listFriend.getListFriend().size() : 0;
    }

    private void deleteFriend(final String idFriend) {
        if (idFriend != null) {
            FirebaseDatabase.getInstance().getReference().child("friend").child(StaticConfig.UID)
                    .orderByValue().equalTo(idFriend).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() == null) {
                        dialogWaitDeleting.dismiss();
                        new LovelyInfoDialog(context)
                                .setTopColorRes(R.color.colorPrimary)
                                .setTitle(R.string.error)
                                .setMessage(R.string.error_occurred)
                                .show();
                    } else {
                        String idRemoval = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                        FirebaseDatabase.getInstance().getReference().child("friend")
                                .child(StaticConfig.UID).child(idRemoval).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialogWaitDeleting.dismiss();

                                        new LovelyInfoDialog(context)
                                                .setTopColorRes(R.color.colorPrimary)
                                                .setTitle(R.string.success)
                                                .setMessage(R.string.delete_success)
                                                .show();

                                        Intent intentDeleted = new Intent(FavoritesFragment.ACTION_DELETE_FRIEND);
                                        intentDeleted.putExtra("idFriend", idFriend);
                                        context.sendBroadcast(intentDeleted);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogWaitDeleting.dismiss();
                                        new LovelyInfoDialog(context)
                                                .setTopColorRes(R.color.colorPrimary)
                                                .setTitle(R.string.error)
                                                .setMessage(R.string.error_occurred)
                                                .show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            dialogWaitDeleting.dismiss();
            new LovelyInfoDialog(context)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle(R.string.error)
                    .setMessage(R.string.error_occurred)
                    .show();
        }
    }

    private void removeFavorite(final String idFriend) {
        if (idFriend != null) {
            FirebaseDatabase.getInstance().getReference().child("favorite").child(StaticConfig.UID)
                    .orderByValue().equalTo(idFriend).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() == null) {
                        dialogWaitDeleting.dismiss();
                        new LovelyInfoDialog(context)
                                .setTopColorRes(R.color.colorAccent)
                                .setTitle("Error")
                                .setMessage("Error occurred during deleting favorite")
                                .show();

                    } else {
                        String idRemoval = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                        FirebaseDatabase.getInstance().getReference().child("favorite")
                                .child(StaticConfig.UID).child(idRemoval).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialogWaitDeleting.dismiss();
/*
                                        new LovelyInfoDialog(context)
                                                .setTopColorRes(R.color.colorAccent)
                                                .setTitle("Success")
                                                .setMessage("Favorite deleting successfully")
                                                .show();
                                                */
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogWaitDeleting.dismiss();

                                        new LovelyInfoDialog(context)
                                                .setTopColorRes(R.color.colorAccent)
                                                .setTitle("Error")
                                                .setMessage("Error occurred during deleting favorite")
                                                .show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            dialogWaitDeleting.dismiss();
            new LovelyInfoDialog(context)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle("Error")
                    .setMessage("Error occurred during deleting friend")
                    .show();
        }
    }

    public void addFavorite(final String idFriend) {
        if (idFriend != null) {
            FirebaseDatabase.getInstance().getReference().child("favorite/" + StaticConfig.UID).push().setValue(idFriend)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialogWaitDeleting.dismiss();
                                /*
                                new LovelyInfoDialog(context)
                                        .setTopColorRes(R.color.colorAccent)
                                        .setTitle("Success")
                                        .setMessage("Succesful adding favorite")
                                        .show();
                                        */
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//
                        }
                    });
        } else {
            dialogWaitDeleting.dismiss();
            new LovelyInfoDialog(context)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle("Error")
                    .setMessage("Error occurred during adding favorite")
                    .show();
        }
    }

}

class ItemFavoritViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView avata;
    public TextView txtName, txtTime, txtMessage;
    public ImageButton imageButton;
    private Context context;


    ItemFavoritViewHolder(Context context, View itemView) {
        super(itemView);
        avata = (CircleImageView) itemView.findViewById(R.id.icon_avata);
        txtName = (TextView) itemView.findViewById(R.id.txtName);
        txtTime = (TextView) itemView.findViewById(R.id.txtTime);
        txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
        imageButton = (ImageButton) itemView.findViewById(R.id.favorite);
        this.context = context;
    }
}