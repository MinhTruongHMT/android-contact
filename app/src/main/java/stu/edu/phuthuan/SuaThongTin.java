package stu.edu.phuthuan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import stu.edu.phuthuan.model.User;

public class SuaThongTin extends AppCompatActivity {
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_thong_tin);
        getDataFromIntent();
    }
    private void getDataFromIntent(){
        Intent intent = getIntent();
        if(intent.hasExtra("USER")){
            user = (User) intent.getSerializableExtra("USER");
            Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}