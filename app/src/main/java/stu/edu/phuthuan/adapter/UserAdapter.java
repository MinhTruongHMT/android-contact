package stu.edu.phuthuan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import stu.edu.phuthuan.R;
import stu.edu.phuthuan.SuaThongTin;
import stu.edu.phuthuan.model.User;


public class UserAdapter extends ArrayAdapter<User> {
    Activity context;
    int resource;
    List<User> object;

    public UserAdapter(
            Activity context, int resource, List<User> object) {
        super(context, resource, object);
        this.context = context;
        this.resource = resource;
        this.object = object;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        TextView txtMa = convertView.findViewById(R.id.txtMa);
        TextView txtTen = convertView.findViewById(R.id.txtTen);
        TextView txtSdt = convertView.findViewById(R.id.txtEmail);
        ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);
        ImageView ivAvatar = convertView.findViewById(R.id.ivavatar);

        User nv = this.object.get(position);
        txtMa.setText(nv.getFullName());
        txtTen.setText(nv.getNickName());
        txtSdt.setText(nv.getEmail());
        ivAvatar.setImageBitmap(convertByteArrayToBitmap(nv.getAvatar()));
        // Thêm sự kiện click cho btnEdit
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi nút Edit được nhấn
                // Ví dụ: Mở một Activity để chỉnh sửa thông tin người dùng
                Intent  intent = new Intent(context, SuaThongTin.class);
                intent.putExtra("USER",nv);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    public static Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }


}
