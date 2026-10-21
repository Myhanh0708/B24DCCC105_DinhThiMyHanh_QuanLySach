package com.example.quanlysach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlysach.adapter.BookAdapter;
import com.example.quanlysach.model.Book;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView tvTotal, tvRead, tvUnread;
    private ListView lvBooks;
    private Button btnAddBook;

    private ArrayList<Book> bookList;
    private BookAdapter adapter;
    private int selectedPosition = -1;

    private final ActivityResultLauncher<Intent> addBookLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Book newBook = (Book) result.getData().getSerializableExtra("NEW_BOOK");
                    if (newBook != null) {
                        bookList.add(newBook);
                        updateUI();
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> detailBookLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String action = result.getData().getStringExtra("ACTION");
                    if ("DELETE".equals(action)) {
                        if (selectedPosition != -1 && selectedPosition < bookList.size()) {
                            bookList.remove(selectedPosition);
                        }
                    } else if ("UPDATE".equals(action)) {
                        Book updatedBook = (Book) result.getData().getSerializableExtra("UPDATED_BOOK");
                        if (updatedBook != null && selectedPosition != -1) {
                            bookList.set(selectedPosition, updatedBook);
                        }
                    }
                    updateUI();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thư viện của tôi");
        }
        tvTotal = findViewById(R.id.tvTotal);
        tvRead = findViewById(R.id.tvRead);
        tvUnread = findViewById(R.id.tvUnread);
        lvBooks = findViewById(R.id.lvBooks);
        btnAddBook = findViewById(R.id.btnAddBook);

        initData();

        adapter = new BookAdapter(this, bookList);
        lvBooks.setAdapter(adapter);

        updateUI();

        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            addBookLauncher.launch(intent);
        });

        lvBooks.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
            intent.putExtra("BOOK_DATA", bookList.get(position));
            detailBookLauncher.launch(intent);
        });
    }

    private void initData() {
        bookList = new ArrayList<>();
        // Dữ liệu mẫu chuẩn theo đề bài PTIT[cite: 1]
        bookList.add(new Book("Lập trình Android cơ bản", "Nguyễn Văn A", "Công nghệ", "Đang đọc", 4.0f, "Sách dành cho người mới bắt đầu"));
        bookList.add(new Book("Đắc nhân tâm", "Dale Carnegie", "Kỹ năng sống", "Đã đọc", 5.0f, ""));
        bookList.add(new Book("Dế Mèn phiêu lưu ký", "Tô Hoài", "Văn học", "Chưa đọc", 0.0f, ""));
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
        int total = bookList.size();
        int read = 0;
        for (Book b : bookList) {
            if ("Đã đọc".equals(b.getStatus())) {
                read++;
            }
        }
        int unread = total - read;

        tvTotal.setText(String.valueOf(total));
        tvRead.setText(String.valueOf(read));
        tvUnread.setText(String.valueOf(unread));
    }
}