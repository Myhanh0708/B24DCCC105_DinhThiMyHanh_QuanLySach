package com.example.quanlysach.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.quanlysach.R;
import com.example.quanlysach.model.Book;
import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_book, parent, false);
        }

        Book book = getItem(position);

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvAuthor = convertView.findViewById(R.id.tvAuthor);
        TextView tvCategory = convertView.findViewById(R.id.tvCategory);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);
        RatingBar rbItem = convertView.findViewById(R.id.rbItem);

        if (book != null) {
            tvTitle.setText(book.getTitle());
            tvAuthor.setText("Tác giả: " + book.getAuthor());
            tvCategory.setText(book.getCategory());
            tvStatus.setText(book.getStatus());
            rbItem.setRating(book.getRating());

            // Tùy chỉnh màu sắc theo đúng yêu cầu: Xanh lá, Vàng, Xám
            String status = book.getStatus();
            if ("Đã đọc".equals(status)) {
                tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9")); // Nền xanh lá nhạt
                tvStatus.setTextColor(Color.parseColor("#2E7D32"));         // Chữ xanh lá đậm
            } else if ("Đang đọc".equals(status)) {
                tvStatus.setBackgroundColor(Color.parseColor("#FFFDE7")); // Nền vàng nhạt
                tvStatus.setTextColor(Color.parseColor("#F57F17"));         // Chữ vàng đậm/cam
            } else {
                tvStatus.setBackgroundColor(Color.parseColor("#F5F5F5")); // Nền xám nhạt
                tvStatus.setTextColor(Color.parseColor("#616161"));         // Chữ xám đậm
            }
        }

        return convertView;
    }
}