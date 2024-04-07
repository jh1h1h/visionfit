package Team10_VisionFit.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.ViewHolder> {
    private List<String> purchaseHistory;

    public PurchaseHistoryAdapter(List<String> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String purchase = purchaseHistory.get(position);
        holder.purchaseTextView.setText(purchase);
    }

    @Override
    public int getItemCount() {
        return purchaseHistory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView purchaseTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            purchaseTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}

