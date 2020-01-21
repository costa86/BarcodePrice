package com.example.barcodeprice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultsRecycler extends RecyclerView.Adapter<ResultsRecycler.ResultsHolder> {
    private List<ResultsModel> resultsList;

    public ResultsRecycler(List<ResultsModel> resultsList) {
        this.resultsList = resultsList;
    }

    @NonNull
    @Override
    public ResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.row_item, parent, false);
        return new ResultsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsHolder holder, int position) {
        holder.tTitle.setText(resultsList.get(position).title);

    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public class ResultsHolder extends RecyclerView.ViewHolder {
        TextView tTitle;

        public ResultsHolder(@NonNull final View itemView) {
            super(itemView);
            tTitle = itemView.findViewById(R.id.tTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), tTitle.getText(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}
