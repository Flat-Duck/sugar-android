package ly.bithive.sugar.util;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.*;


@RequiresApi(api = Build.VERSION_CODES.N)
public class Helper {

   public static Double calculateIntake(int weight,int workTime) {
        return ((weight * 100 / 3.0) + (workTime / 6 * 7));
    }


    public static String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c);
    }

    public static String getCurrentTime() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        return df.format(c);
    }
}
