package ly.bithive.sugar.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.github.anastr.speedviewlib.SpeedView;

import ly.bithive.sugar.R;

public class BmiActivity extends AppCompatActivity {
    EditText weightNum, heightNum;
    TextView resultLabel;
    HalfGauge halfGauge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        halfGauge = findViewById(R.id.speedView);
        weightNum = (EditText) findViewById(R.id.weightNum);
        heightNum = (EditText) findViewById(R.id.heightNum);
        resultLabel = (TextView) findViewById(R.id.resultLabel);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }

setUpGauge();
       // speedView.setUnit("BMI");
//        speedView.setStartDegree(0);
//        speedView.setEndDegree(180);
//        speedView.setWithTremble(false);
//        speedView.setMinSpeed(9);
//        speedView.setMaxSpeed(65);

        //speedView.setcolor


    }

    private void setUpGauge() {
        Range range = new  Range();
        range.setColor(Color.parseColor("#ff0099cc"));
        //range.setColor(android.R.color.holo_blue_dark);
        range.setFrom (16.0);
        range.setTo(18.4);

        Range range2 =new  Range();
        range2.setColor(Color.parseColor("#ff669900"));
        range2.setFrom(18.4);
        range2.setTo(24.9);

        Range range3 =new  Range();
        range3.setColor(Color.parseColor("#ffff8800"));
        //range3.setColor(android.R.color.holo_orange_dark);
        range3.setFrom(24.9);
        range3.setTo(29.9);

        Range range4 = new  Range();
        range4.setColor(Color.parseColor("#ffff4444"));
        //range4.setColor(android.R.color.holo_red_light);
        range4.setFrom(29.9);
        range4.setTo(40.0);

        //add color ranges to gauge
        halfGauge.addRange(range);
        halfGauge.addRange(range2);
        halfGauge.addRange(range3);
        halfGauge.addRange(range4);
        //set min max and current value
        halfGauge.setMinValue(16);
        halfGauge.setMaxValue(40.0);
        halfGauge.setValue(0.0);
    }

    public void calculateClickHandler(View view) {
        if (view.getId() == R.id.calcBMI) {

            double weight = 0;
            double height = 0;

            if (!(weightNum.getText().toString().equals(""))) {
                weight = Double.parseDouble(weightNum.getText().toString());
            }

            if (!(heightNum.getText().toString().equals(""))) {
                height = Double.parseDouble(heightNum.getText().toString());
            }

            double bmi;

            //weight = weight ;
            //height = height * height;
            bmi = calculateBMI(weight, height);
            //  }

            // round to 1 digit
            double newBMI = Math.round(bmi * 10.0) / 10.0;
            DecimalFormat f = new DecimalFormat("##.0");

            // interpret the meaning of the bmi value
            String bmiInterpretation = interpretBMI(newBMI);

            // now set the value in the results text
            resultLabel.setText("BMI Score = " + newBMI + "\n" + bmiInterpretation);
           // speedView.speedTo((float) newBMI,1500);
            //speedView.setUnitUnderSpeedText(true);
            halfGauge.setValue(newBMI);
        }
    }

    // the formula to calculate the BMI index
    private double calculateBMI(double weight, double height) {
        return (double) (((weight ) / (height * height)) );
    }

    // interpret what BMI means
    private String interpretBMI(double bmi) {

        if (bmi == 0) {
            return "Enter your details";
        } else if (bmi < 18.5) {
            return "You are underweight";
        } else if (bmi < 25) {
            return "You are normal weight";
        } else if (bmi < 30) {
            return "You are overweight";
        } else if (bmi < 40) {
            return "You are obese";
        } else {
            return "You are severely obese";
        }
    }

}
