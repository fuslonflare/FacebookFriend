package com.example.phuwarin.facebookfriend;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phuwarin on 6/7/2016.
 */
public class FriendList extends AppCompatActivity {

    private AlertDialog alertDialog;
    private ListView listView;
    private String[] friends, pic_friend;
    private CharSequence[] Alphabet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_friend_list);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");
//        Log.d(MainActivity.TAG, "jsondata = " + jsondata);

        JSONArray friendslist;
        friends = null;
        pic_friend = null;
        try {
            friendslist = new JSONArray(jsondata);
//            Log.d(MainActivity.TAG, "size = " + friendslist.length());
//            Log.d(MainActivity.TAG, "friendslist = " + friendslist);
            friends = new String[friendslist.length()];
            pic_friend = new String[friendslist.length()];

            for (int index = 0; index < friendslist.length(); ++index) {
                friends[index] = friendslist.getJSONObject(index).getString("name");
                pic_friend[index] = friendslist.getJSONObject(index).getJSONObject("picture").getJSONObject("data").getString("url");
//                Log.d(MainActivity.TAG, "Friend #" + index + " = " + friends[index]);
//                Log.d(MainActivity.TAG, "Picture #" + index + " = " + pic_friend[index]);
//                Log.d(MainActivity.TAG, "=================================");
            }
            List<String> listFriend = Arrays.asList(friends);
            List<String> listFriendPic = Arrays.asList(pic_friend);

            concurrentSort(listFriend, listFriend, listFriendPic);
//            Log.d(MainActivity.TAG, "outer for loop");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BufferList bufferList = new BufferList(getApplicationContext(), friends, pic_friend);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(bufferList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        try {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d(MainActivity.TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        else if (id == R.id.action_jump) {
            jumpto();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void jumpto() {

        char[] alphabet = new char[26];
        for (int i = 0; i < 26; ++i) {
            alphabet[i] = (char) (i + 65);
        }
        Alphabet = new CharSequence[27];

        for (int i = 0; i < 26; ++i) {
            Alphabet[i] = String.valueOf(alphabet[i]);
        }
        Alphabet[26] = "อักขระภาษาไทย";

        AlertDialog.Builder builder = new AlertDialog.Builder(FriendList.this);
        builder.setTitle("Jump to Title");
        builder.setItems(Alphabet, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listView.setSelectionFromTop(whereRU(which), 0);
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        Intent login = new Intent(FriendList.this, MainActivity.class);
        startActivity(login);
        finish();
    }

    private int whereRU(int selectItem) {
        int position = -1;
        if (selectItem == 26) {
            for (int i = 0; i < friends.length; ++i) {
                if ((int)(friends[i].charAt(0)) > 90 || (int)(friends[i].charAt(0)) < 65) {
                    position = i;
                    break;
                }
            }
        }
        else {
            for (int i = 0; i < friends.length; ++i) {
                if (String.valueOf(Alphabet[selectItem]).equals(String.valueOf(friends[i].charAt(0)))) {
                    position = i;
                    break;
                }
            }
        }
        return position;
    }

    public static <T extends Comparable<T>> void concurrentSort(final List<T> key, List<?>... lists) {
        // Create a List of indices
        List<Integer> indices = new ArrayList<Integer>();
        for(int i = 0; i < key.size(); i++)
            indices.add(i);

        // Sort the indices list based on the key
        Collections.sort(indices, new Comparator<Integer>(){
            @Override public int compare(Integer i, Integer j) {
                return key.get(i).compareTo(key.get(j));
            }
        });

        // Create a mapping that allows sorting of the List by N swaps.
        // Only swaps can be used since we do not know the type of the lists
        Map<Integer,Integer> swapMap = new HashMap<Integer, Integer>(indices.size());
        List<Integer> swapFrom = new ArrayList<Integer>(indices.size()),
                swapTo   = new ArrayList<Integer>(indices.size());
        for(int i = 0; i < key.size(); i++){
            int k = indices.get(i);
            while(i != k && swapMap.containsKey(k))
                k = swapMap.get(k);

            swapFrom.add(i);
            swapTo.add(k);
            swapMap.put(i, k);
        }

        // use the swap order to sort each list by swapping elements
        for(List<?> list : lists)
            for(int i = 0; i < list.size(); i++)
                Collections.swap(list, swapFrom.get(i), swapTo.get(i));
    }
}
