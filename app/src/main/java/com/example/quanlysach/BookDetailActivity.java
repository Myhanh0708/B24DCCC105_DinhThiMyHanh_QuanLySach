package com.example.quanlysach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlysach.model.Book;

public class BookDetailActivity extends AppCompatActivity {
    private TextView tvDetailTitle, tvDetailAuthorCategory, tvDetailStatus, tvDetailNote;
    private RatingBar rbDetailRating;
    private Button btnMarkRead, btnBack, btnDelete;
    private Book currentBook;
    private boolean isModified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Thiết lập tiêu đề và nút quay lại trên ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chi tiết sách");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailAuthorCategory = findViewById(R.id.tvDetailAuthorCategory);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);
        tvDetailNote = findViewById(R.id.tvDetailNote);
        rbDetailRating = findViewById(R.id.rbDetailRating);
        btnMarkRead = findViewById(R.id.btnMarkRead);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        // Nhận dữ liệu đối tượng Book được truyền qua Intent
        currentBook = (Book) getIntent().getSerializableExtra("BOOK_DATA");

        if (currentBook != null) {
            tvDetailTitle.setText(currentBook.getTitle());
            tvDetailAuthorCategory.setText(currentBook.getAuthor() + " · " + currentBook.getCategory());
            tvDetailStatus.setText(currentBook.getStatus());
            rbDetailRating.setRating(currentBook.getRating());
            tvDetailNote.setText(currentBook.getNote().isEmpty() ? "Không có ghi chú" : currentBook.getNote());

            // Nếu sách đã ở trạng thái Đã đọc thì vô hiệu hóa nút đánh dấu[cite: 1]
            if ("Đã đọc".equals(currentBook.getStatus())) {
                btnMarkRead.setEnabled(false);
            }
        }

        // Cập nhật rating trực tiếp khi người dùng bấm thay đổi số sao
        rbDetailRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser && currentBook != null) {
                currentBook.setRating(rating);
                isModified = true;
            }
        });

        // Nút Đánh dấu đã đọc[cite: 1]
        btnMarkRead.setOnClickListener(v -> {
            if (currentBook != null) {
                currentBook.setStatus("Đã đọc");
                tvDetailStatus.setText("Đã đọc");
                btnMarkRead.setEnabled(false);
                isModified = true;
            }
        });

        // Nút Quay lại bên trong giao diện
        btnBack.setOnClickListener(v -> {
            if (isModified) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ACTION", "UPDATE");
                resultIntent.putExtra("UPDATED_BOOK", currentBook);
                setResult(RESULT_OK, resultIntent);
            }
            finish();
        });

        // Nút Xóa sách khỏi danh sách[cite: 1]
        btnDelete.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("ACTION", "DELETE");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    // Xử lý sự kiện khi bấm nút mũi tên quay lại (<-) trên thanh ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        if (isModified) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("ACTION", "UPDATE");
            resultIntent.putExtra("UPDATED_BOOK", currentBook);
            setResult(RESULT_OK, resultIntent);
        }
        finish();
        return true;
    }
}