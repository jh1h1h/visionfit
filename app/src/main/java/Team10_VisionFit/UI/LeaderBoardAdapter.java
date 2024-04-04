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
    ArrayList<String> data;

    LeaderBoardAdapter(Context context, ArrayList<String> dataSource){
        mInflater = LayoutInflater.from( context);
        this.context = context;
        data = dataSource;
    }

    @NonNull
    @Override
    public LeaderBoardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /** this is pretty much standard code so we will leave it here
         *  inflates the xml layout for each list item and is ready for the data to be added */
        View itemView = mInflater.inflate(R.layout.frag_leaderboard_row, parent, false);
        return new LeaderBoardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardHolder holder, int position) {
        holder.getNameView().setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class LeaderBoardHolder extends RecyclerView.ViewHolder{

        private TextView nameView;
        public LeaderBoardHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.Name);

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
    }
}
