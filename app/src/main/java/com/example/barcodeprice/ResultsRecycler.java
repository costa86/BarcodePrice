package com.example.barcodeprice;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        holder.tTitle.setText(offers.get(position).title);
        holder.tMerchant.setText(offers.get(position).merchant);
        holder.tDomain.setText(offers.get(position).domain);
        holder.tCurrency.setText(currency);
        holder.tCondition.setText(offers.get(position).condition);
        holder.tLink.setText(offers.get(position).link);
        holder.tPrice.setText(String.valueOf(offers.get(position).price));
        holder.tUpdated_t.setText(Helpers.secondsToDate(dateL, "EEE, dd/MMM/yy"));
        holder.tXOfY.setText(pos + "/" + getItemCount());

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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String title = "Partilhar " + tTitle.getText();

                    String text = String.format("%s \nPor %s %s\n%s", tTitle.getText()
                            , tPrice.getText(), tCurrency.getText(), tLink.getText());

                    Helpers.shareThis(v.getContext(), title, text);

                    return false;
                }
            });

        }

    }
}
