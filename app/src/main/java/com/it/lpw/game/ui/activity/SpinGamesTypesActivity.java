package com.it.lpw.game.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.it.lpw.game.R;
import com.it.lpw.game.databinding.ActivitySpinGamesTypesBinding;
import com.it.lpw.game.paytm.ui.PaytmDevActivity;
import com.it.lpw.game.util.Constant_Api;
import com.it.lpw.game.util.Session;

public class SpinGamesTypesActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySpinGamesTypesBinding binding;
    Session session;
    int ActivityRequestCode = 1002;
    String gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinGamesTypesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getExtras() != null)
            gameType = getIntent().getExtras().getString("GameType");

        session = new Session(this);

        binding.layoutToolbar.toolbar.setTitle(Constant_Api.TITLE);
        setSupportActionBar(binding.layoutToolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.type1.setOnClickListener(this);
        binding.type2.setOnClickListener(this);
        binding.type3.setOnClickListener(this);
        binding.joinGame1.setOnClickListener(this);
        binding.joinGame2.setOnClickListener(this);
        binding.joinGame3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (session.getIntData(session.WALLET) >= 50) {
            if (v.getId() == R.id.type1 || v.getId() == R.id.joinGame1) {
                Constant_Api.TITLE = "Spin";
                Constant_Api.P8 = "10";
//                startActivity(new Intent(this, SpinColorActivity.class));
            } else if (v.getId() == R.id.type2 || v.getId() == R.id.joinGame2) {
                Constant_Api.TITLE = "Spin";
                Constant_Api.P8 = "20";
//                startActivity(new Intent(this, SpinColorActivity.class));
            } else if (v.getId() == R.id.type3 || v.getId() == R.id.joinGame3) {
                Constant_Api.TITLE = "Spin";
                Constant_Api.P8 = "100";
//                startActivity(new Intent(this, SpinColorActivity.class));
            }

            if (gameType.equalsIgnoreCase("Number"))
                startActivity(new Intent(this, SpinNumbersActivity.class));
            else
                startActivity(new Intent(this, SpinColorActivity.class));
        } else {
            Snackbar snackbar = Snackbar.make(binding.parentLayout, "No enough coins to play. Add more coins to continue.", 5000);
            snackbar.setAction("ADD COINS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SpinGamesTypesActivity.this, PaytmDevActivity.class));
                }
            });
            snackbar.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(SpinGamesTypesActivity.this, MainActivity.class));
        super.onBackPressed();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }
    }
}