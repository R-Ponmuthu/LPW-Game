package com.it.lpw.game.paytm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.it.lpw.game.BuildConfig;
import com.it.lpw.game.R;
import com.it.lpw.game.Responsemodel.CreditResponse;
import com.it.lpw.game.databinding.AddCoinsLayoutBinding;
import com.it.lpw.game.paytm.PaytmUtil;
import com.it.lpw.game.restApi.ApiClient;
import com.it.lpw.game.restApi.ApiInterface;
import com.it.lpw.game.ui.activity.MainActivity;
import com.it.lpw.game.ui.activity.SpinGamesTypesActivity;
import com.it.lpw.game.util.Constant_Api;
import com.it.lpw.game.util.Constants;
import com.it.lpw.game.util.Method;
import com.it.lpw.game.util.Session;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaytmDevActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    AddCoinsLayoutBinding binding;
    int ActivityRequestCode = 2;
    private Session session;
    private AlertDialog bonus_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddCoinsLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        binding.layoutToolbar.toolbar.setTitle("Add Cash");
        setSupportActionBar(binding.layoutToolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new Session(this);

        bonus_dialog = new AlertDialog.Builder(this).setView(LayoutInflater.from(this).inflate(R.layout.layout_collect_bonus, null)).create();
        bonus_dialog.getWindow().setBackgroundDrawableResource(R.color.transprent);
        bonus_dialog.getWindow().setWindowAnimations(R.style.Dialoganimation);
        bonus_dialog.setCanceledOnTouchOutside(false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        binding.currentBalance.setText("Current Balance: " +
                Method.coolNumberFormat(Long.parseLong(String.valueOf(session.getIntData(session.WALLET)))) + " Coins");

        binding.addAmountBtn.setOnClickListener(v -> {

//            if (Integer.parseInt(binding.amount.getText().toString()) < 20)
//                Toast.makeText(this, "Minimum amount should be Rs.20", Toast.LENGTH_SHORT).show();
//            else
            addAmount(Integer.parseInt(binding.amount.getText().toString()));
        });

        binding.amount50.setOnClickListener(v -> addAmount(50));

        binding.amount100.setOnClickListener(v -> addAmount(100));

        binding.amount250.setOnClickListener(v -> addAmount(250));

        binding.amount500.setOnClickListener(v -> addAmount(500));
    }

    private void addAmount(int amount) {

        String orderId = "ORDERID_" + GenerateRandomNo();
        String custid = "CUSTID_" + GenerateRandomNo();

        PaytmUtil paytmUtil = new PaytmUtil(this);
        String token = paytmUtil.initiateTransaction(amount, orderId, custid);

        PaytmOrder paytmOrder = new PaytmOrder(orderId, BuildConfig.PAYTM_MID, token, String.valueOf(amount),
                "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp?ORDER_ID=" + orderId);

        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle bundle) {
                Log.e("checksum", " response--" + bundle.toString());

                try {
                    JSONObject object = new JSONObject(bundle.getString("response"));
                    String RESPCODE = object.getString("RESPCODE");
                    String STATUS = object.getString("STATUS");
                    String TXNAMOUNT = object.getString("TXNAMOUNT");

                    if (RESPCODE.equals(Constants.RESPCODE) && STATUS.equals(Constants.STATUS)) {

                        float txtAmt = Float.parseFloat(TXNAMOUNT);
                        int points = Math.round(txtAmt);
                        Log.e("txtAmt", String.valueOf(txtAmt));
                        Log.e("Points", String.valueOf(points));
                        credit(points);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void networkNotAvailable() {

            }

            @Override
            public void onErrorProceed(String s) {

            }

            @Override
            public void clientAuthenticationFailed(String s) {

            }

            @Override
            public void someUIErrorOccurred(String s) {
                Log.e("checksum ", " ui fail respon  " + s);
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Log.e("checksum ", " error loading page response true " + s + "  s1 " + s1);
            }

            @Override
            public void onBackPressedCancelTransaction() {
                Log.e("checksum ", " cancel call back response  ");
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Log.e("checksum ", "  transaction cancel ");
            }
        });
        transactionManager.setShowPaymentUrl(BuildConfig.URL + "/theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(this, ActivityRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum", " response" + bundle.toString());
    }

    @Override
    public void networkNotAvailable() {

    }

    @Override
    public void onErrorProceed(String s) {

    }

    @Override
    public void clientAuthenticationFailed(String s) {

    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  " + s);
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading page response true " + s + "  s1 " + s1);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back response  ");
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public int GenerateRandomNo() {
        int _max = 99999;
        Random _rdm = new Random();
        return _rdm.nextInt(_max);
    }

    private void credit(int points) {
        Constant_Api.TID = "" + points;
        Call<CreditResponse> call = ApiClient.getClient(this).create(ApiInterface.class).CreditSpin();
        call.enqueue(new Callback<CreditResponse>() {
            @Override
            public void onResponse(Call<CreditResponse> call, Response<CreditResponse> response) {
                if (response.isSuccessful() && response.body().getSuccess() == 1) {
                    session.setIntData(session.WALLET, response.body().getBalance());
                    showBonus(response.body().getData(), "");
                } else {
                    showBonus(response.body().getData(), "error");
                }
            }

            @Override
            public void onFailure(Call<CreditResponse> call, Throwable t) {
            }
        });
    }

    void showBonus(String msg, String type) {
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
            finish();
        });
    }
}

