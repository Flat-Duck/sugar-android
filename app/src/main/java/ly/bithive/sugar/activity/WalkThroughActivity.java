package ly.bithive.sugar.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import ly.bithive.sugar.R;
import ly.bithive.sugar.SessionManager;

public class WalkThroughActivity extends AppCompatActivity {
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(WalkThroughActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            ViewPager walkThroughPager = findViewById(R.id.walkThroughPager);
            DotsIndicator indicator = findViewById(R.id.indicator);
            WalkThroughAdapter viewPagerAdapter = new WalkThroughAdapter(getSupportFragmentManager());
            walkThroughPager.setAdapter(viewPagerAdapter);
            indicator.setViewPager(walkThroughPager);
        }



        Button getStarted = findViewById(R.id.getStarted);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WalkThroughActivity.this, StartActivity.class));
                finish();
            }
        });
    }

    private static class WalkThroughAdapter extends FragmentPagerAdapter {
        WalkThroughAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new WalkThroughTwo();
                case 2:
                    return new WalkThroughThree();
                case 0:
                default:
                    return new WalkThroughOne();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class WalkThroughOne extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.walk_through_one, container, false);
        }
    }

    public static class WalkThroughTwo extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.walk_through_two, container, false);
        }
    }

    public static class WalkThroughThree extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.walk_through_three, container, false);
        }
    }
}