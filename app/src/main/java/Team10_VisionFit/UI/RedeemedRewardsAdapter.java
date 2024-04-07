package Team10_VisionFit.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamten.visionfit.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedeemedRewardsAdapter extends RecyclerView.Adapter<RedeemedRewardsAdapter.ViewHolder> {
    private Map<String, Integer> redeemedRewardsList;

    public RedeemedRewardsAdapter(Map<String, Integer> redeemedRewardsList) {
        this.redeemedRewardsList = redeemedRewardsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_redeemed_reward, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> keys = new ArrayList<>(redeemedRewardsList.keySet());
        String rewardName = keys.get(position);
        int count = redeemedRewardsList.get(rewardName);

        holder.rewardNameTextView.setText(rewardName);
        holder.rewardCountTextView.setText(String.valueOf(count));
    }

    @Override
    public int getItemCount() {
        return redeemedRewardsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rewardNameTextView;
        TextView rewardCountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rewardNameTextView = itemView.findViewById(R.id.reward_name_text_view);
            rewardCountTextView = itemView.findViewById(R.id.reward_count_text_view);
        }
    }
}

