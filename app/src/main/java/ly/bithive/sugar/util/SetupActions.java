package ly.bithive.sugar.util;
import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import ly.bithive.sugar.R;

public class SetupActions {
    Context mContext;

     public SetupActions(Context context) {
        this.mContext = context;
    }

    public ArrayAdapter<String> loadCureArray(){
       List<String> spinnerCureArray =  new ArrayList<>();
        spinnerCureArray.add(mContext.getResources().getString(R.string.cure_milk));
        spinnerCureArray.add(mContext.getResources().getString(R.string.cure_insulin));
        spinnerCureArray.add(mContext.getResources().getString(R.string.cure_sugar));
        spinnerCureArray.add(mContext.getResources().getString(R.string.cure_pill));
        spinnerCureArray.add(mContext.getResources().getString(R.string.cure_nothing));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, spinnerCureArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public ArrayAdapter<String>  loadPeriodsArray(){
       List<String> spinnerPeriodsArray =  new ArrayList<>();
        spinnerPeriodsArray.add(mContext.getResources().getString(R.string.before_eat));
        spinnerPeriodsArray.add(mContext.getResources().getString(R.string.after_eat));
        spinnerPeriodsArray.add(mContext.getResources().getString(R.string.before_sleep));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, spinnerPeriodsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
