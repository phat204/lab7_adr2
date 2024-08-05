package fpoly.account.myapplication.screens;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import fpoly.account.myapplication.DAO.ToDoDAO;
import fpoly.account.myapplication.ModelHelper.ToDoDatabaseHelper;
import fpoly.account.myapplication.R;
import fpoly.account.myapplication.adapters.SpacingItemDecoration;
import fpoly.account.myapplication.adapters.ToDoAdapter;
import fpoly.account.myapplication.models.ToDo;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ToDoAdapter adapter;
    private List<ToDo> list;
    private EditText edttitle, edtcontent, edtdate, edttype;
    private Button add;
    private ToDo currentToDo = null;
    private final Context mContext = this;
    AlertDialog.Builder builder;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.demo11list);
        edttitle = findViewById(R.id.tv_title);
        edtcontent = findViewById(R.id.tv_content);
        edtdate = findViewById(R.id.tv_date);
        edttype = findViewById(R.id.tv_type);
        add = findViewById(R.id.btnAdd);
        list = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        ListenFirebaseFirestore();

        adapter = new ToDoAdapter(list);
        SpacingItemDecoration spacingItemDecoration = new SpacingItemDecoration(20);
        recyclerView.addItemDecoration(spacingItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        edttype.setOnClickListener(v -> {
            String[] type = {"De", "Trung binh", "Kho"};
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Chon muc do kho cua cong viec");
            builder.setItems(type, (dialog, which) -> {
                edttype.setText(type[which]);
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        add.setOnClickListener(v -> {
            String id = UUID.randomUUID().toString();
            String title = edttitle.getText().toString();
            String content = edtcontent.getText().toString();
            String date = edtdate.getText().toString();
            String type = edttype.getText().toString();
            if (title.isEmpty() || content.isEmpty() || date.isEmpty() || type.isEmpty()) {
                Toast.makeText(mContext, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                ToDo toDo = new ToDo(id, title, content, date, type, 0);
                HashMap<String, Object> mapTodo = toDo.convertHashmap();
                database.collection("TODO").document(id)
                        .set(mapTodo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Firestore Error", "Add failed ", e);
                            }
                        });
            }
        });
        adapter.setOnItemClickListener(new ToDoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ToDo toDo) {
                edttitle.setText(toDo.getTitle());
                edtcontent.setText(toDo.getContent());
                edtdate.setText(toDo.getDate());
                edttype.setText(toDo.getType());
                currentToDo = toDo;
            }

            @Override
            public void onDeleteClick(int position) {
                builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Xác nhận xoá");
                builder.setIcon(R.drawable.baseline_warning_24);
                builder.setMessage("Bạn có chắc chắn muốn xóa?");
                builder.setPositiveButton("Có", (dialog, which) -> {
                    ToDo toDo = list.get(position);
                    database.collection("TODO").document(toDo.getId())
                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Firestore Error", "Delete failed ", e);
                                }
                            });
//                    toDoDAO.deleteToDo(toDo.getId());
//                    adapter.notifyDataSetChanged();
//                    Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
//                    edttitle.setText("");
//                    edtcontent.setText("");
//                    edtdate.setText("");
//                    edttype.setText("");
//                    list = toDoDAO.getAllData();
                    onResume();
                });
                builder.setNegativeButton("Không", null);
                builder.show();
            }

            @Override
            public void onEditClick(int position) {
                ToDo toDo = list.get(position);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_edit_dialog, null);
                EditText edttitle = view.findViewById(R.id.tv_edit_title);
                EditText edtcontent = view.findViewById(R.id.tv_edit_content);
                EditText edtdate = view.findViewById(R.id.tv_edit_date);
                EditText edttype = view.findViewById(R.id.tv_edit_type);
                edttitle.setText(toDo.getTitle());
                edtcontent.setText(toDo.getContent());
                edtdate.setText(toDo.getDate());
                edttype.setText(toDo.getType());
                edttype.setOnClickListener(v -> {
                    String[] type = {"De", "Trung binh", "Kho"};
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setTitle("Chon muc do kho cua cong viec");
                    builder1.setItems(type, (dialog, which) -> {
                        edttype.setText(type[which]);
                    });
                    AlertDialog alertDialog = builder1.create();
                    alertDialog.show();
                });
                Button btnUpdate = view.findViewById(R.id.btnUpdate);
                Button btnCancel = view.findViewById(R.id.btnCancel);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                btnUpdate.setOnClickListener(v -> {
                    String title = edttitle.getText().toString();
                    String content = edtcontent.getText().toString();
                    String date = edtdate.getText().toString();
                    String type = edttype.getText().toString();
                    if (title.isEmpty() || content.isEmpty() || date.isEmpty() || type.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    } else {
                        toDo.setTitle(title);
                        toDo.setContent(content);
                        toDo.setDate(date);
                        toDo.setType(type);
                        database.collection("TODO").document(String.valueOf(toDo.getId()))
                                .update(toDo.convertHashmap())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Firestore Error", "Edit failed ", e);
                                    }
                                });
                        alertDialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(v -> alertDialog.dismiss());
            }

            @Override
            public void onStatusChange(int position, boolean isDone) {
                ToDo toDo = list.get(position);
                int value = isDone ? 1 : 0;
                database.collection("TODO").document(toDo.getId())
                        .update("status", value).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Firestore Error", "Edit failed ", e);
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new ToDoAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }

    private void ListenFirebaseFirestore() {
        database.collection("TODO").addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore Error", "Listen failed ", error);
                return;
            }

            if (value != null) {
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            ToDo toDo = dc.getDocument().toObject(ToDo.class);
                            list.add(toDo);
                            adapter.notifyItemInserted(list.size() - 1);
                            break;
                        case MODIFIED:
                            ToDo updateTodo = dc.getDocument().toObject(ToDo.class);
                            if (dc.getOldIndex() == dc.getNewIndex()) {
                                list.set(dc.getOldIndex(), updateTodo);
                                adapter.notifyItemChanged(dc.getOldIndex());
                            } else {
                                list.remove(dc.getOldIndex());
                                list.add(updateTodo);
                                adapter.notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());
                            }
                            break;
                        case REMOVED:
                            dc.getDocument().toObject(ToDo.class);
                            list.remove(dc.getOldIndex());
                            adapter.notifyItemRemoved(dc.getOldIndex());
                            break;
                    }
                }
            }
        });
    }
}