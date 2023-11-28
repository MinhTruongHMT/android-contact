package stu.edu.phuthuan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import stu.edu.phuthuan.adapter.UserAdapter;
import stu.edu.phuthuan.model.User;
import stu.edu.phuthuan.util.DBConfigUtil;

public class MainActivity extends AppCompatActivity {

    EditText txtFullName, txtNickName, txtemail;
    Button btnLuu;
    ImageButton btnSua;
    ArrayList<User> dsUser;
    ArrayAdapter<User>adapter;
    ListView lvDSNhanvien;
    ImageView avatarImageView;

    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBConfigUtil.copyDatabaseFromAssets(MainActivity.this);
        addControls();
        addEvents();

    }

    private void addControls() {
        txtFullName = findViewById(R.id.editName);
        txtNickName = findViewById(R.id.editNickName);
        txtemail = findViewById(R.id.editEmail);
        btnSua = findViewById(R.id.btnEdit);
        btnLuu = findViewById(R.id.btnOk);
        avatarImageView = findViewById(R.id.avatarImageView);

        dsUser= new ArrayList<>();

        adapter = new UserAdapter(
                MainActivity.this,
                R.layout.item_user,
                dsUser
        );
        lvDSNhanvien = findViewById(R.id.lvDanhSach);
        lvDSNhanvien.setAdapter(adapter);
    }

    private void addEvents() {
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma = txtFullName.getText().toString();
                String ten = txtNickName.getText().toString();
                String sdt = txtemail.getText().toString();
                User nv = new User(ma, ten, sdt);
                dsUser.add(nv);
                adapter.notifyDataSetChanged();
                txtFullName.setText("");
                txtNickName.setText("");
                txtemail.setText("");
                txtFullName.requestFocus();
            }
        });
        lvDSNhanvien.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc muốn xóa mục này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xác nhận xóa, thực hiện hành động
                        if (position >= 0 && position < dsUser.size()) {
                            dsUser.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Không thực hiện hành động
                    }
                });
                builder.show();
                return true; // Trả về true để ngăn chặn sự kiện `onItemClick` từ được gọi sau đó.
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Lấy Uri của ảnh được chọn
            Uri selectedImageUri = data.getData();
            // Hiển thị ảnh trong ImageView
            avatarImageView.setImageURI(selectedImageUri);
        }
    }

}