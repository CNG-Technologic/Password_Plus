package com.george.password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    ListView ListPasswords;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    private static final String NETAG = "syka_blya";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        //Нажатие на элемент листа
        ListPasswords = findViewById(R.id.passwordList);
        ListPasswords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(NETAG, "Нажат элемент листа");
                Intent intent = new Intent(getApplicationContext(), getData.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();

        db = databaseHelper.getReadableDatabase();
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);

        String[] headers = new String[] {DatabaseHelper.NAME_ACCOUNT, DatabaseHelper.WEB};

        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, userCursor,
                headers, new int[] {android.R.id.text1, android.R.id.text2}, 0);
        ListPasswords.setAdapter(userAdapter);
    }

    public void add(View view) {
        Log.d(NETAG, "Анимация кнопки add началась");
        Animation anim_fab;
        anim_fab = AnimationUtils.loadAnimation(this, R.anim.rotate);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.startAnimation(anim_fab);
        Log.d(NETAG, "Переход к GetData активити");
        Intent intent = new Intent(this, getData.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        userCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.goGenerator) {
            Log.d(NETAG, "Переход к генератору паролей");
            Intent goGenerator = new Intent(MainActivity.this, generatorActivity.class);
            startActivity(goGenerator);
        }

        return super.onOptionsItemSelected(item);
    }

}