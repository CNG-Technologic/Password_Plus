package com.george.password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class generatorActivity extends AppCompatActivity {

    public TextView PasswordText;
    public ImageButton refresh, copy;
    private EditText PasswordLength;
    private CheckBox symwals;
    private Animation refresh_anim;

    public int passwordLength = 16;

    private static final String TAG = "syka_blya";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        Toolbar toolbar = findViewById(R.id.toolbarGenerator);
        setSupportActionBar(toolbar);

        refresh = findViewById(R.id.refreshBtn);
        PasswordText = findViewById(R.id.passwordText);
        PasswordLength = findViewById(R.id.lengthPassword);
        symwals = findViewById(R.id.checkBoxSymvals);

        refresh_anim = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);

        //Для генератора паролей
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refresh.startAnimation(refresh_anim);

                if(symwals.isChecked()) {
                    if (PasswordLength.getText().toString().equals("")) {
                        String passwordFinal;
                        passwordFinal = "  " + getRandomPasswordSymwals(passwordLength) + "  ";
                        PasswordText.setText(passwordFinal);
                    } else if (Integer.parseInt(PasswordLength.getText().toString()) <= 32) {
                        passwordLength = Integer.parseInt(PasswordLength.getText().toString());
                        String passwordFi;
                        passwordFi = "  " + getRandomPasswordSymwals(passwordLength) + "  ";
                        PasswordText.setText(passwordFi);
                    } else {
                        Snackbar.make(v, "Invalid value", Snackbar.LENGTH_SHORT).setAction("error", null).show();
                    }
                } else {
                    if (PasswordLength.getText().toString().equals("")) {
                        String passwordF;
                        passwordF = "  " + getRandomPassword(passwordLength) + "  ";
                        PasswordText.setText(passwordF);
                    } else if (Integer.parseInt(PasswordLength.getText().toString()) <= 32) {
                        String passwordFin;
                        passwordLength = Integer.parseInt(PasswordLength.getText().toString());
                        passwordFin = "  " + getRandomPassword(passwordLength) + "  ";
                        PasswordText.setText(passwordFin);
                    } else {
                        Snackbar.make(v, "Invalid value", Snackbar.LENGTH_SHORT).setAction("error", null).show();
                    }
                }
            }
        });
    }


    //Генератор паролей с символами
    public static String getRandomPasswordSymwals(int passwordLength) {
        final String charsPassword ="1234567890%*)?@#$~qwertyuiopasdfghjklzxcvbnm%*)?@#$~QWERTYUIOPASDFGHJKLZXCVBNM%*)?@#$~";
        StringBuilder result = new StringBuilder();
        while(passwordLength > 0) {
            Random rand = new Random();
            result.append(charsPassword.charAt(rand.nextInt(charsPassword.length())));
            passwordLength--;
        }
        return result.toString();
    }

    //Генератор паролей без символов
    public static String getRandomPassword(int passwordLength) {
        final String noSymvalsCharsPassword ="qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder result = new StringBuilder();
        while(passwordLength > 0) {
            Random rand = new Random();
            result.append(noSymvalsCharsPassword.charAt(rand.nextInt(noSymvalsCharsPassword.length())));
            passwordLength--;
        }
        return result.toString();
    }

    public void OnCopyBtnClick(View view) {
        Animation copy_anim;
        PasswordText = findViewById(R.id.passwordText);
        copy = findViewById(R.id.copyBtn);
        copy_anim = AnimationUtils.loadAnimation(this, R.anim.done_or_del_btn);
        copy.startAnimation(copy_anim);

        if(PasswordText.getText().toString().equals("                                           ")) { //Хех
            Log.wtf(TAG, "Ошибка! Пароль не сгенерирован");
            Snackbar.make(view, "Generate password to copy", Snackbar.LENGTH_SHORT).setAction("error", null).show();
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", PasswordText.getText().toString());
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Snackbar.make(view, "Password copied", Snackbar.LENGTH_SHORT).setAction("done", null).show();
            Log.d(TAG, "Пароль скопирован");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_generator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.goHome) {
            Log.d(TAG, "Переход на главную страницу");
            Intent goHome = new Intent(generatorActivity.this, MainActivity.class);
            startActivity(goHome);
        }

        return super.onOptionsItemSelected(item);
    }
}