package com.kimjiome.lpw.games;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kimjiome.library.luckywheel.LuckyWheelView;
import kimjiome.library.luckywheel.model.LuckyItem;


public class MainActivity extends Activity {

    List<LuckyItem> data = new ArrayList<>();

    AppCompatButton playBtn, rankingBtn, exitBtn;
    LuckyWheelView luckyWheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        luckyWheelView = findViewById(R.id.luckyWheel);
        playBtn = findViewById(R.id.play);
        rankingBtn = findViewById(R.id.ranking);
        exitBtn = findViewById(R.id.exit);

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.topText = "1";
//        luckyItem1.icon = R.drawable.test1;
        luckyItem1.color = 0xffFFF3E0;
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.topText = "2";
//        luckyItem2.icon = R.drawable.test2;
        luckyItem2.color = 0xffFFE0B2;
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.topText = "3";
//        luckyItem3.icon = R.drawable.test3;
        luckyItem3.color = 0xffFFCC80;
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.topText = "4";
//        luckyItem4.icon = R.drawable.test4;
        luckyItem4.color = 0xffFFF3E0;
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.topText = "5";
//        luckyItem5.icon = R.drawable.test5;
        luckyItem5.color = 0xffFFE0B2;
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.topText = "6";
//        luckyItem6.icon = R.drawable.test6;
        luckyItem6.color = 0xffFFCC80;
        data.add(luckyItem6);

        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.topText = "7";
//        luckyItem7.icon = R.drawable.test7;
        luckyItem7.color = 0xffFFF3E0;
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.topText = "8";
//        luckyItem8.icon = R.drawable.test8;
        luckyItem8.color = 0xffFFE0B2;
        data.add(luckyItem8);

        LuckyItem luckyItem9 = new LuckyItem();
        luckyItem9.topText = "9";
//        luckyItem9.icon = R.drawable.test9;
        luckyItem9.color = 0xffFFCC80;
        data.add(luckyItem9);

        LuckyItem luckyItem10 = new LuckyItem();
        luckyItem10.topText = "10";
//        luckyItem10.icon = R.drawable.test10;
        luckyItem10.color = 0xffFFE0B2;
        data.add(luckyItem10);

//        LuckyItem luckyItem11 = new LuckyItem();
//        luckyItem11.topText = "2000";
//        luckyItem11.icon = R.drawable.test10;
//        luckyItem11.color = 0xffFFE0B2;
//        data.add(luckyItem11);
//
//        LuckyItem luckyItem12 = new LuckyItem();
//        luckyItem12.topText = "3000";
//        luckyItem12.icon = R.drawable.test10;
//        luckyItem12.color = 0xffFFE0B2;
//        data.add(luckyItem12);

        /////////////////////

        luckyWheelView.setData(data);
        luckyWheelView.setRound(5);

        /*luckyWheelView.setLuckyWheelBackgrouldColor(0xff0000ff);
        luckyWheelView.setLuckyWheelTextColor(0xffcc0000);
        luckyWheelView.setLuckyWheelCenterImage(getResources().getDrawable(R.drawable.icon));
        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);*/

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int index = getRandomIndex();
//                luckyWheelView.startLuckyWheelWithTargetIndex(index);

                startActivity(new Intent(MainActivity.this, PlayActivity.class));
            }
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                Toast.makeText(getApplicationContext(), data.get(index).topText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getRandomIndex() {
        Random rand = new Random();
        return rand.nextInt(data.size() - 1) + 0;
    }

    private int getRandomRound() {
        Random rand = new Random();
        return rand.nextInt(10) + 15;
    }
}
