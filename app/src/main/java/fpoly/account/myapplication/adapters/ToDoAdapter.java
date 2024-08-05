package fpoly.account.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpoly.account.myapplication.DAO.ToDoDAO;
import fpoly.account.myapplication.R;
import fpoly.account.myapplication.models.ToDo;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {
    private List<ToDo> toDoList;
    private static OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ToDo toDo);

        void onDeleteClick(int position);

        void onEditClick(int position);

        void onStatusChange(int position, boolean isDone);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ToDoAdapter(List<ToDo> toDoList) {
        this.toDoList = toDoList;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recyclerview_item, parent, false);
        return new ToDoViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        final ToDo currentToDo = toDoList.get(position);
        holder.tvDate.setText(currentToDo.getDate());
        holder.tvTitle.setText(currentToDo.getTitle());
        holder.chbStatus.setChecked(currentToDo.getStatus() == 1);
        holder.chbStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onStatusChange(position, isChecked);
            }
        });
        holder.layoutItem.setOnClickListener(v -> {
            onItemClickListener.onItemClick(currentToDo);
        });
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvDate, tvType;
        ImageView ivDelete, ivEdit;
        CheckBox chbStatus;
        LinearLayout layoutItem;

        public ToDoViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvtitle);
            tvDate = itemView.findViewById(R.id.tvdate);
            ivDelete = itemView.findViewById(R.id.ivdelete);
            ivEdit = itemView.findViewById(R.id.ivedit);
            chbStatus = itemView.findViewById(R.id.chbstatus);
            layoutItem = itemView.findViewById(R.id.layout_item);
            ivDelete.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onDeleteClick(position);
                    }
                }
            });
            ivEdit.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onEditClick(position);
                    }
                }
            });
        }
    }
}
