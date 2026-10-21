package com.example.quanlysach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlysach.model.Book;

public class AddBookActivity extends AppCompatActivity {
    private EditText etAddTitle, etAddAuthor, etAddNote;
    private Spinner spCategory;
    private RadioGroup rgStatus;
    private RatingBar rbAddRating;
    private Button btnSaveBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thêm sách");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút Quay lại (<-)
        }

        etAddTitle = findViewById(R.id.etAddTitle);
        etAddAuthor = findViewById(R.id.etAddAuthor);
        etAddNote = findViewById(R.id.etAddNote);
        spCategory = findViewById(R.id.spCategory);
        rgStatus = findViewById(R.id.rgStatus);
        rbAddRating = findViewById(R.id.rbAddRating);
        btnSaveBook = findViewById(R.id.btnSaveBook);

        // Nạp danh sách thể loại vào Spinner[cite: 1]
        String[] categories = {"Văn học", "Khoa học", "Công nghệ", "Kỹ năng sống", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        btnSaveBook.setOnClickListener(v -> {
            String title = etAddTitle.getText().toString().trim();
            String author = etAddAuthor.getText().toString().trim();

            if (title.isEmpty()) {
                etAddTitle.setError("Tên sách không được để trống!");
                return;
            }
            if (author.isEmpty()) {
                etAddAuthor.setError("Tác giả không được để trống!");
                return;
            }

            String category = spCategory.getSelectedItem().toString();

            String status = "Chưa đọc";
            int selectedId = rgStatus.getCheckedRadioButtonId();
            if (selectedId == R.id.rbReading) {
                status = "Đang đọc";
            } else if (selectedId == R.id.rbRead) {
                status = "Đã đọc";
            }

            float rating = rbAddRating.getRating();
            String note = etAddNote.getText().toString().trim();

            Book book = new Book(title, author, category, status, rating, note);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_BOOK", book);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}