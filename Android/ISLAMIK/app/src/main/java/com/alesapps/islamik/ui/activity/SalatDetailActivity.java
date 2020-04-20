package com.alesapps.islamik.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.R;
import com.alesapps.islamik.model.SurahModel;
import com.alesapps.islamik.utils.CommonUtil;
import java.util.ArrayList;
import java.util.List;

public class SalatDetailActivity extends BaseActionBarActivity implements View.OnClickListener {
    LinearLayout layout_first;
    LinearLayout layout_second;
    LinearLayout layout_third;
    LinearLayout layout_fourth;
    ScrollView scr_fajr;
    LinearLayout layout_fajr;
    TextView txt_main_chapter;
    TextView txt_main_verse;
    TextView txt_chapter;
    TextView txt_verse;
    LinearLayout layout_fifth;
    LinearLayout layout_sixth;

    public static String language = "en";
    public static List<SurahModel> mDataList = new ArrayList<>();
    int speed = 50;
    int count = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle("", -1);
        ShowActionBarIcons(true, R.id.action_back);
        CommonUtil.SetLocale(this, language);
        setContentView(R.layout.activity_salat_detail);
        layout_first = findViewById(R.id.layout_first);
        layout_second = findViewById(R.id.layout_second);
        layout_third = findViewById(R.id.layout_third);
        layout_fourth = findViewById(R.id.layout_fourth);
        scr_fajr = findViewById(R.id.scr_fajr);
        layout_fajr = findViewById(R.id.layout_fajr);
        txt_main_chapter = findViewById(R.id.txt_main_chapter);
        txt_main_verse = findViewById(R.id.txt_main_verse);
        txt_chapter = findViewById(R.id.txt_chapter);
        txt_verse = findViewById(R.id.txt_verse);
        layout_fifth = findViewById(R.id.layout_fifth);
        layout_sixth = findViewById(R.id.layout_sixth);
        initialize();
    }

    private void initialize() {
        txt_main_chapter.setText(AppConstant.MAIN_CHAPTER);
        txt_main_verse.setText(CommonUtil.GetStrMainVerse(this));

        SurahModel model = mDataList.get(0);
        txt_chapter.setText(model.surah);
        txt_verse.setText(CommonUtil.GetStrVerse(this, model.surahId, model.verseStart, model.verseEnd));

        speed = AppConstant.SPEED_VALUE_ARRAY[model.speed];
        showData();
    }

    private void showData() {
        count ++;
        layout_first.setVisibility(View.GONE);
        layout_second.setVisibility(View.GONE);
        layout_third.setVisibility(View.GONE);
        layout_fourth.setVisibility(View.GONE);
        scr_fajr.setVisibility(View.GONE);
        layout_fifth.setVisibility(View.GONE);
        layout_sixth.setVisibility(View.GONE);
        txt_chapter.setVisibility(View.GONE);
        txt_verse.setVisibility(View.GONE);
        if (count == 0) {
            scr_fajr.setVisibility(View.VISIBLE);
            txt_chapter.setVisibility(View.VISIBLE);
            txt_verse.setVisibility(View.VISIBLE);
            layout_fajr.scrollTo(0,0);
            layout_fajr.post(moveTextView);
        } else if (count == 1) {
            layout_first.setVisibility(View.VISIBLE);
        } else if (count == 2) {
            layout_second.setVisibility(View.VISIBLE);
        } else if (count == 3) {
            layout_third.setVisibility(View.VISIBLE);
        } else if (count == 4) {
            layout_fourth.setVisibility(View.VISIBLE);
        } else if (count == 5) {
            layout_third.setVisibility(View.VISIBLE);
        } else if (count == 6) {
            layout_fifth.setVisibility(View.VISIBLE);
        } else if (count == 7) {
            layout_sixth.setVisibility(View.VISIBLE);
        } else if (count == 8) {
            myBack();
        }
        gotoNext();
    }

    private void gotoNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count != 0 && count < 8)
                    showData();
            }
        }, AppConstant.TIME_SPEED);
    }

    private Runnable moveTextView = new Runnable() {
        @Override
        public void run() {
            if (count == 0) {
                int y = layout_fajr.getScrollY();
                y += 1;
                if (y > layout_fajr.getHeight() - 100) {
                    layout_fajr.scrollTo(0,0);
                    showData();
                } else {
                    layout_fajr.scrollTo(layout_fajr.getScrollX(), y);
                    layout_fajr.postDelayed(moveTextView, speed);
                }
            }
        }
    };
}

