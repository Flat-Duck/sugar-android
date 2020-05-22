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
        TextView Date,MG, NG, EG, SG,MI,NI,EI,SI;


        MyViewHolder(View view) {
            super(view);

            Date = view.findViewById(R.id.date);
            MG = view.findViewById(R.id.morningGlycemia);
            NG = view.findViewById(R.id.noonGlycemia);
            EG = view.findViewById(R.id.eveningGlycemia);
            SG = view.findViewById(R.id.sleepGlycemia);
            MI = view.findViewById(R.id.morningInsulin);
            NI = view.findViewById(R.id.noonInsulin);
            EI = view.findViewById(R.id.eveningInsulin);
            SI = view.findViewById(R.id.sleepInsulin);


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
        holder.MG.setText(reportItem.getMorningGlycemia());
        holder.NG.setText(reportItem.getNoonGlycemia());
        holder.EG.setText(reportItem.getEveningGlycemia());
        holder.SG.setText(reportItem.getSleepGlycemia());
        holder.MI.setText(reportItem.getMorningInsulin());
        holder.NI.setText(reportItem.getNoonInsulin());
        holder.EI.setText(reportItem.getEveningInsulin());
        holder.SI.setText(reportItem.getSleepInsulin());
    }

    @Override
    public int getItemCount() {
        return reportItems.size();
    }
}
