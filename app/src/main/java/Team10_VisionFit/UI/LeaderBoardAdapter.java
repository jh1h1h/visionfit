package Team10_VisionFit.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamten.visionfit.R;

import java.util.ArrayList;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardHolder>{
    LayoutInflater mInflater;
    Context context;
    // TODO: store all 3 in one array for standardisation, though not very important
    ArrayList<String> names;
    ArrayList<String> ranks;
    ArrayList<String> reps;

    LeaderBoardAdapter(Context context, ArrayList<String> names, ArrayList<String> ranks, ArrayList<String> reps){
        mInflater = LayoutInflater.from( context);
        this.context = context;
        this.names = names;
        this.ranks = ranks;
        this.reps = reps;
    }

    @NonNull
    @Override
    public LeaderBoardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.frag_leaderboard_row, parent, false);
        return new LeaderBoardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardHolder holder, int position) {
        holder.getNameView().setText(names.get(position));
        holder.getRankView().setText(ranks.get(position));
        holder.getRepsView().setText(reps.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class LeaderBoardHolder extends RecyclerView.ViewHolder{

        private TextView nameView;
        private TextView repsView;
        private TextView rankView;
        public LeaderBoardHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.Name);
            repsView = itemView.findViewById(R.id.Reps);
            rankView = itemView.findViewById(R.id.Rank);
            // TODO: Write code to detect a long click to for more user options
            /*** detecting a longClick and deleting the list item
             * 1 call setOnLongClickListener on itemView
             * 2 get the position that is being long-clicked
             * by calling getAbsoluteAdapterPosition
             * notify the recyclerview by calling notifyDataSetChanged() when data has changed */
        }

        public TextView getNameView() {
            return nameView;
        }

        public TextView getRankView() {
            return rankView;
        }

        public TextView getRepsView() {
            return repsView;
        }
    }
}
