package ly.bithive.sugar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ly.bithive.sugar.R;
import io.feeeei.circleseekbar.CircleSeekBar;


public class SportActivity extends AppCompatActivity implements CircleSeekBar.OnSeekBarChangeListener{
    CircleSeekBar seekBar,seekBar2;
    Context context;
    TextView seekBarVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        context = this;
        seekBarVal = findViewById(R.id.seekBarVal);
        seekBar = findViewById(R.id.seekBar);
       // seekBar2 = findViewById(R.id.seekBar2);

        seekBar.setMaxProcess(600);
      //  seekBar2.setMaxProcess(60);
        seekBar.setCurProcess(0);
       // seekBar2.setCurProcess(0);

        seekBar.setOnSeekBarChangeListener(this);
       // seekBar2.setOnSeekBarChangeListener(this);


        String[] data = {"جري" ,"سباحة","مشي" ,  "دراجة" ,"حصة لياقة","نشاط خفيف","نشاط شديد"};


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_selected, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SportActivity.this,adapterView.getItemAtPosition(i).toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onChanged(CircleSeekBar seekbar, int curValue) {
//        switch (seekbar.getId()){
//            case (R.id.seekBar) :
                seekBarVal.setText(curValue +  " Minutes");// +seekBar2.getCurProcess());
//                break;
//            case (R.id.seekBar2) :
//                seekBarVal.setText(seekBar.getCurProcess() + " :" + curValue);
//                break;
//            default:
//                break;

       // }
    }
}