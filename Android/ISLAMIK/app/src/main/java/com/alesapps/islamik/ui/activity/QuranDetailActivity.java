package com.alesapps.islamik.ui.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alesapps.islamik.AppConstant;
import com.alesapps.islamik.R;
import com.alesapps.islamik.model.SurahModel;
import com.alesapps.islamik.utils.CommonUtil;
import java.util.ArrayList;
import java.util.List;

public class QuranDetailActivity extends BaseActionBarActivity implements View.OnClickListener {
    LinearLayout layout_quran;
    TextView txt_chapter;
    TextView txt_verse;

    public static String language = "en";
    public static List<SurahModel> mDataList = new ArrayList<>();
    int speed = 50;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetTitle("", -1);
        ShowActionBarIcons(true, R.id.action_back);
        CommonUtil.SetLocale(this, language);
        setContentView(R.layout.activity_quran_detail);
        layout_quran = findViewById(R.id.layout_quran);
        txt_chapter = findViewById(R.id.txt_chapter);
        txt_verse = findViewById(R.id.txt_verse);
        initialize();
    }

    private void initialize() {
        SurahModel model = mDataList.get(0);
        txt_chapter.setText(model.surah);
        txt_verse.setText(CommonUtil.GetStrVerse(this, model.surahId, model.verseStart, model.verseEnd));

        speed = AppConstant.SPEED_VALUE_ARRAY[model.speed];
        showData();

        mediaPlayer = MediaPlayer.create(this, CommonUtil.GetReciterPath(model.surahId + 2));
        mediaPlayer.start();
    }

    private void showData() {
        layout_quran.scrollTo(0,0);
        layout_quran.post(moveTextView);
    }

    private Runnable moveTextView = new Runnable() {
        @Override
        public void run() {
            int y = layout_quran.getScrollY();
            y += 1;
            if (y > layout_quran.getHeight() - 100) {
                layout_quran.scrollTo(0,0);
                myBack();
            } else {
                layout_quran.scrollTo(layout_quran.getScrollX(), y);
                layout_quran.postDelayed(moveTextView, speed);
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null)
            mediaPlayer.release();
        super.onDestroy();
    }
}

