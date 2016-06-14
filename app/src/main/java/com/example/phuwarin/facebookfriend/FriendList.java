package com.example.phuwarin.facebookfriend;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

/**
 * Created by Phuwarin on 6/7/2016.
 */
public class FriendList extends AppCompatActivity {

    private AlertDialog alertDialog;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_friend_list);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");
        //Log.d(MainActivity.TAG, "jsondata = " + jsondata);

        JSONArray friendslist;
        String[] friends = null, pic_friend = null;
        try {
            friendslist = new JSONArray(jsondata);
            //Log.d(MainActivity.TAG, "size = " + friendslist.length());
            //Log.d(MainActivity.TAG, "friendslist = " + friendslist);
            friends = new String[friendslist.length()];
            pic_friend = new String[friendslist.length()];

            for (int index = 0; index < friendslist.length(); ++index) {
                friends[index] = friendslist.getJSONObject(index).getString("name");
                pic_friend[index] = friendslist.getJSONObject(index).getJSONObject("picture").getJSONObject("data").getString("url");
                Log.d(MainActivity.TAG, "Friend #" + index + " = " + friends[index]);
                Log.d(MainActivity.TAG, "Picture #" + index + " = " + pic_friend[index]);
                Log.d(MainActivity.TAG, "=================================");
            }
            Arrays.sort(friends);
            Arrays.sort(pic_friend);
            //Log.d(MainActivity.TAG, "outer for loop");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (String name : friends) {
            Log.d(MainActivity.TAG, "FriendName = " + name);
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
        //Log.d(MainActivity.TAG, "onDestroy");
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
        Toast.makeText(FriendList.this, "Jumpto", Toast.LENGTH_LONG).show();

        CharSequence[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "Y", "Z"};

        AlertDialog.Builder builder = new AlertDialog.Builder(FriendList.this);
        builder.setTitle("Jump to Title");
        builder.setItems(alphabet, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(MainActivity.TAG, "which = " + which);
                if (which == 13) {
                    listView.setSelectionFromTop(30, 0);
                }
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
}
