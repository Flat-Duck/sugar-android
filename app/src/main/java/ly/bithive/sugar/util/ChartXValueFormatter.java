
package ly.bithive.sugar.util;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class ChartXValueFormatter extends ValueFormatter {
    ArrayList<String> dateArray;
    public ChartXValueFormatter(ArrayList<String> dateArray) {
        this.dateArray = dateArray;

    }
    public String getAxisLabel(Float value, AxisBase axis)  {
        int x = Integer.parseInt(String.valueOf(value));
        return dateArray.get(x);
    }
}
