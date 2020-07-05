package com.george.password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;

public class getData extends AppCompatActivity {

    EditText webAdress, PasswordText, nameText;

    Button delButton, saveButton;
    ImageButton goInter, copyName, copyPas;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long PasswordId = 0;

    private static final String PISKA = "syka_blya";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        Bundle extras = getIntent().getExtras();

        if (extras != null)
            PasswordId = extras.getLong("id");

        Toolbar toolbar = findViewById(R.id.toolbarGetData);
        setSupportActionBar(toolbar);

        webAdress = findViewById(R.id.WebBox);
        nameText = findViewById(R.id.nameBox);
        PasswordText =findViewById(R.id.passwordBox);

        delButton = findViewById(R.id.delBtn);
        saveButton = findViewById(R.id.saveButton);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        if (PasswordId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                    DatabaseHelper.ID + "=?", new String[] {String.valueOf(PasswordId)} );
            userCursor.moveToFirst();

            nameText.setText(userCursor.getString(1));
            PasswordText.setText(userCursor.getString(2));
            webAdress.setText(userCursor.getString(3));

            userCursor.close();
        } else
            // убрать кнопку "Удалить"
            Log.d(PISKA, "Кнопка УДАЛИТЬ скрыта");
            delButton.setVisibility(View.GONE);
    }

    public void save(View view){
        Log.d(PISKA, "Нажате на кнопку сохранить");
        EditText webBoxCheck = findViewById(R.id.WebBox);
        EditText PasswordBoxCheck = findViewById(R.id.passwordBox);

        Log.d(PISKA, "Анимация кнопки сохранить началась");
        Animation anim_btn;
        saveButton = findViewById(R.id.saveButton);
        anim_btn = AnimationUtils.loadAnimation(this, R.anim.done_or_del_btn);
        saveButton.startAnimation(anim_btn);

        if(webBoxCheck.getText().toString().equals("") || PasswordBoxCheck.getText().toString().equals("") ) {
            Snackbar.make(view, "Nope, it will not work", Snackbar.LENGTH_LONG).setAction("error", null).show();
        } else {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.WEB, nameText.getText().toString());
            cv.put(DatabaseHelper.PASSWORD, PasswordText.getText().toString());
            cv.put(DatabaseHelper.NAME_ACCOUNT, webAdress.getText().toString());

            if (PasswordId > 0)
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.ID + "=" + String.valueOf(PasswordId), null);
            else
                db.insert(DatabaseHelper.TABLE, null, cv);

            goHome();
        }
    }

    public void delete(View view){

        Log.d(PISKA, "Анимация кнопки удалить началась");
        Animation delAnim;
        delButton = findViewById(R.id.delBtn);
        delAnim = AnimationUtils.loadAnimation(this, R.anim.done_or_del_btn);
        delButton.startAnimation(delAnim);

        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(PasswordId)});
        goHome();
    }

    private void goHome(){
        db.close();
        Log.i(PISKA, "Переход на главную ативити");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
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
            Log.d(PISKA, "Переход к генератору паролей");
            Intent goGenerator = new Intent(getData.this, generatorActivity.class);
            startActivity(goGenerator);
        }

        return super.onOptionsItemSelected(item);
    }

    public void OnPasswordBntCopyClick(View view) {

        EditText password = findViewById(R.id.passwordBox);
        Log.d(PISKA, "Анимация кнопки скопировать пароль началась");
        Animation copyPasswordAnim;
        copyPas = findViewById(R.id.copyBtnPasswordBox);
        copyPasswordAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
        copyPas.startAnimation(copyPasswordAnim);

        if(password.getText().toString().equals("")){
            Log.wtf(PISKA, "Ошибка! Поле не может быть пустым"); // wtf - What a Terrible Failure!
            Snackbar.make(view, "Nope, it will not work", Snackbar.LENGTH_LONG).setAction("error", null).show();
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", password.getText().toString());
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Log.i(PISKA, "Пароль скопирован");
            Snackbar.make(view, "Password copied!", Snackbar.LENGTH_LONG).setAction("done", null).show();

        }
    }


    public void OnNameBtnCopyClick(View v) {

        Log.d(PISKA, "Анимация кнопки скопировать имя началась");
        Animation copyNameAnim;
        EditText name = findViewById(R.id.nameBox);
        copyName = findViewById(R.id.copyBtnNameBox);
        copyNameAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
        copyName.startAnimation(copyNameAnim);

        if(name.getText().toString().equals("")) {
            Log.wtf(PISKA, "Ошибка! Поле не может быть пустым"); // wtf - What a Terrible Failure!
            Snackbar.make(v, "Nope, it will not work", Snackbar.LENGTH_SHORT).setAction("error", null).show();
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", name.getText().toString());
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Log.i(PISKA, "Имя пользователя скопировано");
            Snackbar.make(v, "Username copied", Snackbar.LENGTH_SHORT).setAction("done", null).show();

        }
    }

    public void OnGoInternetBtnClick(View view) {

        Log.d(PISKA, "Анимация кнопки переход по ссылке началась");
        Animation goInterAnim;
        goInter = findViewById(R.id.goInternet);
        goInterAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
        goInter.startAnimation(goInterAnim);

        EditText web = findViewById(R.id.WebBox);
        String url = web.getText().toString();

        if(web.getText().toString().equals("")) {
            Log.wtf(PISKA, "Ошибка! Поле не может быть пустым"); // wtf - What a Terrible Failure!
            Snackbar.make(view, "Nope, it will not work", Snackbar.LENGTH_SHORT).setAction("error", null).show();
        } else if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://") ) {
            Log.d(PISKA, "Переход по ссылке с добавленным протоколом пользователем");
            //это нужно, чтобы ссылка открылась не в дефолтном браузере, а в пользовательском
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } else {
            Log.wtf(PISKA, "Протокол отсуствует!");
            String urlCheck = "http://" + url;
            Log.i(PISKA, "К ссылке добавлен протокол");
            //это нужно, чтобы ссылка открылась не в дефолтном браузере, а в пользовательском
            Log.i(PISKA, "Переход по ссылке с добавленным протоколом программой");
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlCheck)));
        }

    }
}