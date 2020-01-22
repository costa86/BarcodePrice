package com.example.barcodeprice;

import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        String pos = String.valueOf(position + 1);
        String currency = (String.valueOf(offers.get(position).currency).equals("")) ? "USD" : String.valueOf(offers.get(position).currency);
        long dateL = offers.get(position).updated_t;

        holder.tTitle.setText("Titulo: " + offers.get(position).title);
        holder.tMerchant.setText("Loja: " + offers.get(position).merchant);
        holder.tDomain.setText("Site: " + offers.get(position).domain);
        holder.tCurrency.setText("Moeda: " + currency);
        holder.tCondition.setText("Estado: " + offers.get(position).condition);
        holder.tLink.setText(offers.get(position).link);
        holder.tPrice.setText("Preço: " + offers.get(position).price);
        holder.tUpdated_t.setText("Atualizado a: " + secondsToDate(dateL, "EEE, dd/MMM/yy"));
        holder.tXOfY.setText("Resultado " + pos + "/" + getItemCount());

    }

    public String secondsToDate(long seconds, String pattern) {
        Date date = new Date(seconds * 1000L);
        SimpleDateFormat jdf = new SimpleDateFormat(pattern);
        return jdf.format(date);
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }


    public class ResultsHolder extends RecyclerView.ViewHolder {
        TextView tTitle, tMerchant, tDomain, tCurrency, tCondition, tLink, tPrice, tUpdated_t, tXOfY;

        public ResultsHolder(@NonNull final View itemView) {
            super(itemView);
            tTitle = itemView.findViewById(R.id.tTitle);
            tMerchant = itemView.findViewById(R.id.tMerchant);
            tDomain = itemView.findViewById(R.id.tDomain);
            tCurrency = itemView.findViewById(R.id.tCurrency);
            tCondition = itemView.findViewById(R.id.tCondition);
            tLink = itemView.findViewById(R.id.tLink);
            tPrice = itemView.findViewById(R.id.tPrice);
            tUpdated_t = itemView.findViewById(R.id.tUpdated_t);
            tXOfY = itemView.findViewById(R.id.tXofY);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String res = "A ir para o site...";
                    Uri page = Uri.parse(tLink.getText().toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW, page);
                    if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                        v.getContext().startActivity(intent);
                    } else {
                        res = "Impossibilitado de abrir o site";
                    }
                    Toast.makeText(itemView.getContext(), res, Toast.LENGTH_LONG).show();
                }

            });

        }


    }
}
