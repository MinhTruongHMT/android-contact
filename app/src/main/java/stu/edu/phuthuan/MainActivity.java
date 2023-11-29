package stu.edu.phuthuan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import stu.edu.phuthuan.adapter.UserAdapter;
import stu.edu.phuthuan.model.User;
import stu.edu.phuthuan.util.DBConfigUtil;
import stu.edu.phuthuan.util.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    EditText txtFullName, txtNickName, txtemail;
    Button btnLuu;
    ImageButton btnSua;
    ArrayList<User> dsUser;
    ArrayAdapter<User> adapter;
    ListView lvDSNhanvien;
    ImageView avatarImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        DBConfigUtil.copyDatabaseFromAssets(MainActivity.this);
        addControls();
        docDuLieu();
        addEvents();

    }
    private void addControls() {
        txtFullName = findViewById(R.id.editName);
        txtNickName = findViewById(R.id.editNickName);
        txtemail = findViewById(R.id.editEmail);
        btnSua = findViewById(R.id.btnEdit);
        btnLuu = findViewById(R.id.btnOk);
        avatarImageView = findViewById(R.id.avatarImageView);
        dsUser = new ArrayList<>();
        adapter = new UserAdapter(MainActivity.this, R.layout.item_user, dsUser);
        lvDSNhanvien = findViewById(R.id.lvDanhSach);
        lvDSNhanvien.setAdapter(adapter);
    }
    private void addEvents() {
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                dispatchTakePictureIntent();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma = txtFullName.getText().toString();
                String ten = txtNickName.getText().toString();
                String sdt = txtemail.getText().toString();
                User nv = new User(ma, ten, sdt);
                addValue(nv);
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
                            int idUser = dsUser.get(position).getId();
                            int result = dbHelper.deleteDataById("User", idUser);

                            if (result > 0) {
                                Toast.makeText(MainActivity.this, "Dữ liệu đã được xóa thành công", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(MainActivity.this, "Lỗi khi xóa dữ liệu hoặc không có dữ liệu để xóa", Toast.LENGTH_SHORT).show();
                            }
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
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            // Lấy Uri của ảnh được chọn
//            Uri selectedImageUri = data.getData();
//            // Hiển thị ảnh trong ImageView
//            avatarImageView.setImageURI(selectedImageUri);
//        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            avatarImageView.setImageBitmap(imageBitmap);
        }
    }
    private void addValue(User user) {
        String imageName = "image_" + UUID.randomUUID().toString() + ".jpg";
        ContentValues values = new ContentValues();
        values.put("name", user.getFullName());
        values.put("nick_name", user.getNickName());
        values.put("email", user.getEmail());

        // Chuyển đổi hình ảnh thành mảng byte trước khi lưu vào cơ sở dữ liệu
        byte[] imageBytes = convertBitmapToByteArray(avatarImageView.getDrawingCache());
        values.put("image", imageBytes);

        long result = dbHelper.insertData("User", values);

        if (result != -1) {
            Toast.makeText(this, "Dữ liệu đã được chèn thành công", Toast.LENGTH_SHORT).show();
            user.setId((int)result);
            dsUser.add(user);
            adapter.notifyDataSetChanged();
        } else {
            deleteImage(imageName);
            Toast.makeText(this, "Lỗi khi chèn dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm chuyển đổi Bitmap thành mảng byte
    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void docDuLieu() {
        Cursor cursor = dbHelper.getAllData();

        if (cursor.moveToFirst()) {
            do {
                // Xử lý dữ liệu từ cursor
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                System.out.println(id);
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String nickName = cursor.getString(cursor.getColumnIndex("nick_name"));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                User user = new User(name, nickName, email);
                user.setId(id);
                dsUser.add(user);
                adapter.notifyDataSetChanged();
                // Thực hiện các thao tác khác
            } while (cursor.moveToNext());
        }

        // Đừng quên đóng cursor khi đã sử dụng xong
        cursor.close();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    private boolean saveImage(Bitmap bitmap, String imageName){
        // Tạo thư mục lưu ảnh trong bộ nhớ nội tại của ứng dụng
        File directory = new File(getFilesDir() + "/images/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, imageName);
        // Lưu ảnh vào bộ nhớ nội tại
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean deleteImage(String imageName) {
        // Tạo đường dẫn đầy đủ đến file ảnh
        File directory = new File(getFilesDir() + "/images/");
        File file = new File(directory, imageName);

        // Kiểm tra xem tệp tồn tại trước khi xóa
        if (file.exists()) {
            // Thực hiện xóa và kiểm tra xem xóa có thành công hay không
            if (file.delete()) {
                return true; // Xóa thành công
            } else {
                return false; // Xóa không thành công
            }
        } else {
            return false; // Tệp không tồn tại
        }
    }

}