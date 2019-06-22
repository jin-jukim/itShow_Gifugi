package kr.hs.e_mirim.Japan_Gifugi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class festival_basic extends AppCompatActivity {
    Context context;
    Intent intent;

    ImageButton search;

    TextView menu_text;
    RelativeLayout back;

    ImageButton spring;
    ImageButton summer;
    ImageButton fall;
    ImageButton winter;

    LinearLayout list_view;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<>();

    String name, email, pw, activity, season;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_basic);
        context = this;

        intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        pw = intent.getStringExtra("pw");
        activity = intent.getStringExtra("activity");
        season = intent.getStringExtra("season");

        menu_text = (TextView) findViewById(R.id.top_view_text);
        menu_text.setText("FESTIVAL");

        search = (ImageButton)findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, search.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });//search

        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.equals("category")) {
                    intent = new Intent(context, category.class);
                } else if(activity.equals("main")){
                    intent = new Intent(context, MainActivity.class);
                }
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("pw", pw);
                startActivity(intent);
                overridePendingTransition(0, 0);

                finish();
            }
        });//back

        spring = findViewById(R.id.f_spring);
        summer = findViewById(R.id.f_summer);
        fall = findViewById(R.id.f_fall);
        winter = findViewById(R.id.f_winter);

        switch (season) {
            case "spring":
                spring.setImageResource(R.drawable.on_spring);
                summer.setImageResource(R.drawable.un_summer);
                fall.setImageResource(R.drawable.un_fall);
                winter.setImageResource(R.drawable.un_winter);
                break;

            case "summer":
                spring.setImageResource(R.drawable.un_spring);
                summer.setImageResource(R.drawable.on_summer);
                fall.setImageResource(R.drawable.un_fall);
                winter.setImageResource(R.drawable.un_winter);
                break;

            case "fall":
                spring.setImageResource(R.drawable.un_spring);
                summer.setImageResource(R.drawable.un_summer);
                fall.setImageResource(R.drawable.on_fall);
                winter.setImageResource(R.drawable.un_winter);
                break;

            case "winter":
                spring.setImageResource(R.drawable.un_spring);
                summer.setImageResource(R.drawable.un_summer);
                fall.setImageResource(R.drawable.un_fall);
                winter.setImageResource(R.drawable.on_winter);
                break;
        }

        list_view = findViewById(R.id.list_view);

        listView = (ListView) findViewById(R.id.list_item);

        initDatabase();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listView.setAdapter(adapter);

        mReference = mDatabase.getReference("content/festival"); // 변경값을 확인할 child 이름
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String list2 = messageData.getValue().toString();
                    Array.add(list2);
                    adapter.add(list2);
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }//oncreate

    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("log");
        mReference.child("log").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
        };
        mReference.addChildEventListener(mChild);
    }//initdatabase

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }
}
