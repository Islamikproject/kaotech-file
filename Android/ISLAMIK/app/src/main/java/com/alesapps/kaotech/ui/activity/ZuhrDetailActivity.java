package com.alesapps.kaotech.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.alesapps.kaotech.AppConstant;
import com.alesapps.kaotech.R;
import com.alesapps.kaotech.model.SurahModel;
import com.alesapps.kaotech.utils.CommonUtil;
import java.util.ArrayList;
import java.util.List;

public class ZuhrDetailActivity extends BaseActionBarActivity implements View.OnClickListener {
    LinearLayout layout_first;
    LinearLayout layout_second;
    LinearLayout layout_third;
    LinearLayout layout_fourth;
    ScrollView scr_fajr;
    LinearLayout layout_fajr;
    TextView txt_main_chapter;
    TextView txt_main_verse;
    TextView txt_chapter1;
    TextView txt_verse1;
    TextView txt_chapter2;
    TextView txt_verse2;
    LinearLayout layout_fifth;
    LinearLayout layout_sixth;
    LinearLayout layout_seventh;

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
        setContentView(R.layout.activity_zuhr_detail);
        layout_first = findViewById(R.id.layout_first);
        layout_second = findViewById(R.id.layout_second);
        layout_third = findViewById(R.id.layout_third);
        layout_fourth = findViewById(R.id.layout_fourth);
        scr_fajr = findViewById(R.id.scr_fajr);
        layout_fajr = findViewById(R.id.layout_fajr);
        txt_main_chapter = findViewById(R.id.txt_main_chapter);
        txt_main_verse = findViewById(R.id.txt_main_verse);
        txt_chapter1 = findViewById(R.id.txt_chapter1);
        txt_verse1 = findViewById(R.id.txt_verse1);
        txt_chapter2 = findViewById(R.id.txt_chapter2);
        txt_verse2 = findViewById(R.id.txt_verse2);
        layout_fifth = findViewById(R.id.layout_fifth);
        layout_sixth = findViewById(R.id.layout_sixth);
        layout_seventh = findViewById(R.id.layout_seventh);
        initialize();
    }

    private void initialize() {
        txt_main_chapter.setText(AppConstant.MAIN_CHAPTER);
        txt_main_verse.setText(CommonUtil.GetStrMainVerse(this));

        SurahModel model1 = mDataList.get(0);
        txt_chapter1.setText(model1.surah);
        txt_verse1.setText(CommonUtil.GetStrVerse(this, model1.surahId, model1.verseStart, model1.verseEnd));

        SurahModel model2 = mDataList.get(1);
        txt_chapter2.setText(model2.surah);
        txt_verse2.setText(CommonUtil.GetStrVerse(this, model2.surahId, model2.verseStart, model2.verseEnd));

        speed = AppConstant.SPEED_VALUE_ARRAY[model1.speed];
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
        layout_seventh.setVisibility(View.GONE);
        txt_chapter1.setVisibility(View.GONE);
        txt_verse1.setVisibility(View.GONE);
        txt_chapter2.setVisibility(View.GONE);
        txt_verse2.setVisibility(View.GONE);
        if (count == 0) {
            scr_fajr.setVisibility(View.VISIBLE);
            txt_chapter1.setVisibility(View.VISIBLE);
            txt_verse1.setVisibility(View.VISIBLE);
            layout_fajr.scrollTo(0,0);
            layout_fajr.post(moveTextView);
        } else if (count == 6) {
            scr_fajr.setVisibility(View.VISIBLE);
            txt_chapter2.setVisibility(View.VISIBLE);
            txt_verse2.setVisibility(View.VISIBLE);
            layout_fajr.scrollTo(0,0);
            layout_fajr.post(moveTextView);
        } else if (count == 13) {
            scr_fajr.setVisibility(View.VISIBLE);
            layout_fajr.scrollTo(0,0);
            layout_fajr.post(moveTextView);
        } else if (count == 19) {
            scr_fajr.setVisibility(View.VISIBLE);
            layout_fajr.scrollTo(0,0);
            layout_fajr.post(moveTextView);
        } else if (count == 1 || count == 7 || count == 14 || count == 20) {
            layout_first.setVisibility(View.VISIBLE);
        } else if (count == 2 || count == 8 || count == 15 || count == 21) {
            layout_second.setVisibility(View.VISIBLE);
        } else if (count == 3 || count == 9 || count == 16 || count == 22) {
            layout_third.setVisibility(View.VISIBLE);
        } else if (count == 4 || count == 10 || count == 17 || count == 23) {
            layout_fourth.setVisibility(View.VISIBLE);
        } else if (count == 5 || count == 11 || count == 18 || count == 24) {
            layout_third.setVisibility(View.VISIBLE);
        } else if (count == 12) {
            layout_fifth.setVisibility(View.VISIBLE);
        } else if (count == 25) {
            layout_sixth.setVisibility(View.VISIBLE);
        } else if (count == 26) {
            layout_seventh.setVisibility(View.VISIBLE);
        } else if (count == 27) {
            myBack();
        }
        gotoNext();
    }

    private void gotoNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count != 0 && count != 6 && count != 13 && count != 19 && count < 27)
                    showData();
            }
        }, AppConstant.TIME_SPEED);
    }

    private Runnable moveTextView = new Runnable() {
        @Override
        public void run() {
            if (count == 0 || count == 6 || count == 13 || count == 19) {
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

