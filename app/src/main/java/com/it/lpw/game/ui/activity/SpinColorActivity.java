package com.it.lpw.game.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.it.lpw.game.Responsemodel.CreditResponse;
import com.it.lpw.game.Responsemodel.JoinGameResponse;
import com.it.lpw.game.Responsemodel.SpinResponse;
import com.it.lpw.game.restApi.ApiClient;
import com.it.lpw.game.restApi.ApiInterface;
import com.it.lpw.game.util.Constant_Api;
import com.it.lpw.game.util.Constants;
import com.it.lpw.game.R;
import com.it.lpw.game.util.Session;
import com.it.lpw.game.databinding.ActivitySpinBinding;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdRevenueListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class SpinColorActivity extends AppCompatActivity implements MaxAdViewAdListener, MaxAdRevenueListener {

    private static final String TAG = "SpinActivity";
    public static boolean creditbal = false;
    ActivitySpinBinding binding;
    List<LuckyItem> data = new ArrayList<>();
    private int points;
    TextView spinvideopoint;
    Activity activity;
    Session session;
    private MaxAdView adView;
    private AlertDialog bonus_dialog;
    private static int spin;
    private int spinCount = 0;
    private Socket mSocket;
    private LuckyWheelView luckyWheelView;
    private boolean joined = true;
    private String selected = "Red";
    private int match = 0;
    private String earnedPoints = "2";

    private Emitter.Listener onConnect = new Emitter.Listener() {
        public void call(Object... args) {
            Log.d(TAG, "connected...");
        }
    };
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        public void call(Object... args) {
            Log.d(TAG, "Error connecting...");
            Log.e("Error", args[0].toString());
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        public void call(Object... args) {
            Log.d(TAG, "disconnected...");
        }
    };
    private Emitter.Listener onTimer = new Emitter.Listener() {
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                public void run() {
                    String mins;
                    String secs;
                    //String countdown = ((JSONObject) args[0]).getString("countdown");
                    String countdown = args[0].toString();
                    long minutes = (Long.parseLong(countdown) / 1000) / 60;
                    int seconds = (int) ((Long.parseLong(countdown) / 1000) % 60);
                    if (minutes < 10) {
                        mins = "0" + minutes;
                    } else {
                        mins = String.valueOf(minutes);
                    }
                    if (seconds < 10) {
                        secs = "0" + seconds;
                    } else {
                        secs = String.valueOf(seconds);
                    }

                    spinvideopoint.setText(mins + ":" + secs);

                    if (!joined) {
                        if (Integer.parseInt(countdown) <= 30000) {
                            findViewById(R.id.join).setEnabled(false);
                            findViewById(R.id.join).setAlpha(.5f);
                            findViewById(R.id.chooseOption).setEnabled(false);

                            if (spinCount < 3) {

                                int index = getRandomIndex();
                                luckyWheelView.startLuckyWheelWithTargetIndex(index);
                            }

                        } else {
                            findViewById(R.id.join).setEnabled(true);
                            findViewById(R.id.join).setAlpha(1f);
                            findViewById(R.id.chooseOption).setEnabled(true);
                        }
                    }
                }
            });
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.layoutToolbar.toolbar.setTitle(Constant_Api.TITLE);
        setSupportActionBar(binding.layoutToolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new Session(this);
        activity = SpinColorActivity.this;

        try {
            this.mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            Log.e("Exception", e.getMessage());
        }
        this.mSocket.on(Socket.EVENT_CONNECT, onConnect);
        this.mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        this.mSocket.on(Socket.EVENT_CONNECT_ERROR, this.onConnectError);
        this.mSocket.on("timer", this.onTimer);
        this.mSocket.connect();

        bonus_dialog = new AlertDialog.Builder(activity).setView(LayoutInflater.from(activity).inflate(R.layout.layout_collect_bonus, null)).create();
        bonus_dialog.getWindow().setBackgroundDrawableResource(R.color.transprent);
        bonus_dialog.getWindow().setWindowAnimations(R.style.Dialoganimation);
        bonus_dialog.setCanceledOnTouchOutside(false);
//        load_bannerads();

        spinvideopoint = findViewById(R.id.spinvideopoint);
//        checklimit();

        luckyWheelView = (LuckyWheelView) findViewById(R.id.luckyWheel);
//        findViewById(R.id.join).setEnabled(true);
//        findViewById(R.id.join).setAlpha(1f);

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.text = "";
        luckyItem1.color = Color.parseColor(session.getData(session.PC_1));
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.text = "";
        luckyItem2.color = Color.parseColor(session.getData(session.PC_2));
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.text = "";
        luckyItem3.color = Color.parseColor(session.getData(session.PC_3));
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.text = "";
        luckyItem4.color = Color.parseColor(session.getData(session.PC_4));
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.text = "";
        luckyItem5.color = Color.parseColor(session.getData(session.PC_5));
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.text = "";
        luckyItem6.color = Color.parseColor(session.getData(session.PC_6));
        data.add(luckyItem6);

        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.text = "";
        luckyItem7.color = Color.parseColor(session.getData(session.PC_7));
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.text = "";
        luckyItem8.color = Color.parseColor(session.getData(session.PC_8));
        data.add(luckyItem8);

        luckyWheelView.setData(data);
        luckyWheelView.setRound(getRandomRound());

        binding.chooseOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected = getResources().getStringArray(R.array.colours)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.join).setOnClickListener(view -> {

            spinCount = 0;
            match = 0;
            earnedPoints = "2";

            findViewById(R.id.join).setEnabled(false);
            findViewById(R.id.join).setAlpha(.5f);

            Session session = new Session(this);
            session.setGameId();

            Constant_Api.API_TYPE = "join_game";
            Constant_Api.P6 = "Type10";
            Constant_Api.P2 = session.getGameId();
            Constant_Api.P3 = selected;
            Constant_Api.P4 = "0";
            Constant_Api.P5 = "0";
            Constant_Api.P7 = Constant_Api.P8;

            Call<JoinGameResponse> call = ApiClient.getClient(this).create(ApiInterface.class).JoinGame();
            call.enqueue(new Callback<JoinGameResponse>() {
                @Override
                public void onResponse(Call<JoinGameResponse> call, Response<JoinGameResponse> response) {
                    if (response.isSuccessful() && response.body().getSuccess() == 1) {
                        joined = true;
                    }
                }

                @Override
                public void onFailure(Call<JoinGameResponse> call, Throwable t) {

                }
            });
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(index -> {

            if (spinCount == 0) {
                updateColor(index, binding.match1);
            } else if (spinCount == 1) {
                updateColor(index, binding.match2);
            } else if (spinCount == 2) {
                updateColor(index, binding.match3);
            }

            spinCount++;

            String spinItem = getResources().getStringArray(R.array.colours)[index - 1];

//            if (selected.equalsIgnoreCase(spinItem)) {
//                match = match + 1;
//            }

//            binding.match.setText("Matches: " + match);
//            Toast.makeText(SpinActivity.this, "You have " + match + " matches", Toast.LENGTH_SHORT).show();

            if (spinCount == 3) {

                updatePoints();

                showAlert();

                joined = false;
            }

//            showvideoads();
        });
    }

    private void updateColor(int index, ImageView match) {

        switch (index - 1) {
            case 0:
                match.setColorFilter(Color.parseColor(session.getData(session.PC_1)), PorterDuff.Mode.SRC_IN);
                break;
            case 1:
                match.setColorFilter(Color.parseColor(session.getData(session.PC_2)), PorterDuff.Mode.SRC_IN);
                break;
            case 2:
                match.setColorFilter(Color.parseColor(session.getData(session.PC_3)), PorterDuff.Mode.SRC_IN);
                break;
            case 3:
                match.setColorFilter(Color.parseColor(session.getData(session.PC_4)), PorterDuff.Mode.SRC_IN);
                break;
            case 4:
                match.setColorFilter(Color.parseColor(session.getData(session.PC_5)), PorterDuff.Mode.SRC_IN);
                break;
            case 5:
                match.setColorFilter(Color.parseColor(session.getData(session.PC_6)), PorterDuff.Mode.SRC_IN);
                break;
            case 6:
                match.setColorFilter(Color.parseColor(session.getData(session.PC_7)), PorterDuff.Mode.SRC_IN);
                break;
            case 7:
                match.setColorFilter(Color.parseColor(session.getData(session.PC_8)), PorterDuff.Mode.SRC_IN);
                break;
        }
    }

    private void showAlert() {

        AlertDialog dialog = new AlertDialog.Builder(SpinColorActivity.this).setView(LayoutInflater.from(this).inflate(R.layout.layout_congrats, null)).create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transprent);
        dialog.getWindow().setWindowAnimations(R.style.Dialoganimation);
        dialog.setCanceledOnTouchOutside(false);
        Window w = dialog.getWindow();
        if (w != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        if (match == 1) {
            earnedPoints = "2";
        } else if (match == 2) {
            earnedPoints = "8";
        } else if (match == 3) {
            earnedPoints = "10";
        }

        dialog.show();

        TextView textView = dialog.findViewById(R.id.txt);
        textView.setText("Congrats! You got " + match + " matches and have won " + earnedPoints + " points");

        dialog.findViewById(R.id.close).setOnClickListener(v -> {

            binding.match1.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
            binding.match2.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
            binding.match3.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);

            dialog.dismiss();
        });
    }

    private void updatePoints() {

        Session session = new Session(this);

        Constant_Api.API_TYPE = "update_game";
        Constant_Api.P2 = session.getGameId();
        Constant_Api.P3 = String.valueOf(match);
        Constant_Api.P4 = earnedPoints;

        Call<JoinGameResponse> call = ApiClient.getClient(this).create(ApiInterface.class).UpdateGame();
        call.enqueue(new Callback<JoinGameResponse>() {
            @Override
            public void onResponse(Call<JoinGameResponse> call, Response<JoinGameResponse> response) {
                if (response.isSuccessful() && response.body().getSuccess() == 1) {

                    Log.e("SpinActivity", response.body().getData());
                } else {
                    Log.e("SpinActivity", response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<JoinGameResponse> call, Throwable t) {
            }
        });
    }

    private void credit() {
        Constant_Api.TID = "" + points;
        Call<CreditResponse> call = ApiClient.getClient(this).create(ApiInterface.class).CreditSpin();
        call.enqueue(new Callback<CreditResponse>() {
            @Override
            public void onResponse(Call<CreditResponse> call, Response<CreditResponse> response) {
                if (response.isSuccessful() && response.body().getSuccess() == 1) {
                    session.setIntData(session.WALLET, response.body().getBalance());
                    showbonus(response.body().getData(), "");
                    findViewById(R.id.join).setEnabled(true);
                    findViewById(R.id.join).setAlpha(1f);
                    if (spin > 0) {
                        spin = spin - 1;
                        spinvideopoint.setText("" + spin);
                    } else {
                        spinvideopoint.setText("" + spin);
                    }
                } else {
                    showbonus(response.body().getData(), "error");
                }
            }

            @Override
            public void onFailure(Call<CreditResponse> call, Throwable t) {
            }
        });
    }

    void showbonus(String msg, String type) {
        bonus_dialog.show();
        TextView tv, congrats;
        tv = bonus_dialog.findViewById(R.id.txt);
        congrats = bonus_dialog.findViewById(R.id.congrts);
        tv.setText(msg);
        if (type.equals("error")) {
            congrats.setText(getString(R.string.oops));
            congrats.setTextColor(getResources().getColor(R.color.red));
        }
        bonus_dialog.findViewById(R.id.closebtn).setOnClickListener(view -> {
            bonus_dialog.dismiss();
        });
    }


    void showvideoads() {
        Constant_Api.ADTYPE = "spin";
        startActivity(new Intent(activity, RewardedAds.class));
    }

    private void checklimit() {
        Call<SpinResponse> call = ApiClient.getClient(this).create(ApiInterface.class).CheckSpin();
        call.enqueue(new Callback<SpinResponse>() {
            @Override
            public void onResponse(Call<SpinResponse> call, Response<SpinResponse> response) {
                int spinlimt = response.body().getSpinlimit();
                int count = response.body().getCount();
                if (count == 0) {
                    spinvideopoint.setText(String.valueOf(spinlimt));
                } else {
                    int tot = spinlimt - count;
                    spin = tot;
                    spinvideopoint.setText(String.valueOf(tot));
                }
            }

            @Override
            public void onFailure(Call<SpinResponse> call, Throwable t) {
            }
        });
    }

    private int getRandomIndex() {
        int[] ind = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int rand = new Random().nextInt(ind.length);
        return ind[rand];
    }

    private int getRandomRound() {
        Random rand = new Random();
        return rand.nextInt(10) + 15;
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(SpinColorActivity.this, SpinGamesTypesActivity.class));
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        if (creditbal) {
            creditbal = false;
//            credit();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onAdExpanded(MaxAd ad) {
    }

    @Override
    public void onAdCollapsed(MaxAd ad) {
    }

    @Override
    public void onAdLoaded(MaxAd ad) {
    }

    @Override
    public void onAdDisplayed(MaxAd ad) {
    }

    @Override
    public void onAdHidden(MaxAd ad) {
    }

    @Override
    public void onAdClicked(MaxAd ad) {
    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {
    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
    }

    @Override
    public void onAdRevenuePaid(MaxAd ad) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}