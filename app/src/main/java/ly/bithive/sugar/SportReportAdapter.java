package ly.bithive.sugar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SportReportAdapter extends RecyclerView.Adapter<SportReportAdapter.MyViewHolder> {

    private List<SportReportItem> SportReportItems;
    Context context;


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sDate, sType, sPeriod;


        MyViewHolder(View view) {
            super(view);

            sDate = view.findViewById(R.id.sport_report_date);
            sType = view.findViewById(R.id.sport_report_type);
            sPeriod = view.findViewById(R.id.sport_report_time);

        }
    }


    public SportReportAdapter(Context mContext,List<SportReportItem> SportReportItems) {
        this.SportReportItems = SportReportItems;
        this.context = mContext;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sport_report, parent, false);

        return new MyViewHolder(itemView);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SportReportItem SportReportItem = SportReportItems.get(position);
        holder.sDate.setText(context.getResources().getString(R.string.in) +" "+ SportReportItem.getDate() +" "+ context.getResources().getString(R.string.at) +SportReportItem.getTime() +" "+ context.getResources().getString(R.string.u_done));
        holder.sType.setText(SportReportItem.getType()+" "+ context.getResources().getString(R.string.exrsice_for));
        holder.sPeriod.setText(SportReportItem.getPeriod() +" "+ context.getResources().getString(R.string.minutes));
    }

    @Override
    public int getItemCount() {
        return SportReportItems.size();
    }
}
