package com.example.barcodeprice;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodeprice.Offers;

import java.util.List;

public class ResultsRecycler extends RecyclerView.Adapter<ResultsRecycler.ResultsHolder> {
    private List<Offers> offers;

    public ResultsRecycler(List<Offers> offers) {
        this.offers = offers;

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
        holder.tTitle.setText(offers.get(position).title);
    }

    @Override
    public int getItemCount() {
        return offers.size();
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
