package com.example.barcodeprice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CapturaAdapter extends RecyclerView.Adapter<CapturaAdapter.CapturaHolder> {
    private List<Captura> capturas = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public CapturaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.captura_item, parent, false);
        return new CapturaHolder(v);
    }

    public interface OnItemClickListener {
        void onItemClick(Captura captura);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull CapturaHolder holder, int position) {
        Captura captura = capturas.get(position);
        holder.tBarcode.setText(captura.getBarcode());
        holder.tNote.setText(captura.getNote());
        holder.tLat.setText(String.valueOf(captura.getLat()));
        holder.tLon.setText(String.valueOf(captura.getLon()));
    }

    public void setCapturas(List<Captura> capturas) {
        this.capturas = capturas;
        notifyDataSetChanged();
    }

    public Captura getCapturaAt(int pos) {
        return capturas.get(pos);
    }

    @Override
    public int getItemCount() {
        return capturas.size();
    }

    public class CapturaHolder extends RecyclerView.ViewHolder {
        private TextView tLat, tLon, tBarcode, tNote;

        public CapturaHolder(@NonNull View itemView) {
            super(itemView);
            tLat = itemView.findViewById(R.id.tLat);
            tLon = itemView.findViewById(R.id.tLon);
            tBarcode = itemView.findViewById(R.id.tBarcode);
            tNote = itemView.findViewById(R.id.tNote);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(capturas.get(position));
                    }
                }
            });
        }
    }
}
