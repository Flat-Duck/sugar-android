package ly.bithive.sugar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.MyViewHolder> {

    private List<ReportItem> reportItems;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Date,Cure, Period, Value;


        MyViewHolder(View view) {
            super(view);

            Date = view.findViewById(R.id.date);
            Cure = view.findViewById(R.id.cure);
            Period = view.findViewById(R.id.period);
            Value = view.findViewById(R.id.value);



        }
    }


    public ReportListAdapter(List<ReportItem> reportItems) {
        this.reportItems = reportItems;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ReportItem reportItem = reportItems.get(position);
        holder.Date.setText(reportItem.getDate());
        holder.Cure.setText(reportItem.getCure());
        holder.Value.setText(reportItem.getValue());
        holder.Period.setText(reportItem.getPeriod());

    }

    @Override
    public int getItemCount() {
        return reportItems.size();
    }
}
