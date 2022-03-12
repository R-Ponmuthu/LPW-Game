package com.it.lpw.game.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.it.lpw.game.BuildConfig;
import com.it.lpw.game.R;
import com.it.lpw.game.Responsemodel.CreditResponse;
import com.it.lpw.game.adapters.ViewpagerAdapter;
import com.it.lpw.game.databinding.FragmentMainBinding;
import com.it.lpw.game.paytm.PaytmUtil;
import com.it.lpw.game.paytm.beans.ChecksumResponse;
import com.it.lpw.game.paytm.PaytmChecksum;
import com.it.lpw.game.paytm.ui.PaytmDevActivity;
import com.it.lpw.game.restApi.ApiClient;
import com.it.lpw.game.restApi.ApiInterface;
import com.it.lpw.game.ui.activity.SpinGamesTypesActivity;
import com.it.lpw.game.util.Method;
import com.it.lpw.game.util.Session;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMain extends Fragment {
    FragmentMainBinding binding;
    private ViewpagerAdapter catadapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Session session;
    boolean doubleBackToExitPressedOnce = false;
    RelativeLayout layoutCoin;
    String custid, orderId;
    int ActivityRequestCode = 1002;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(getLayoutInflater(), container, false);

        session = new Session(getActivity());
        getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);

        viewPager = binding.catviewpager;
        tabLayout = binding.tablayout;
        layoutCoin = binding.layoutCoin;
        catadapter = new ViewpagerAdapter(getChildFragmentManager());
        catadapter.AddFragment(new HomeNew(), "test");
        catadapter.AddFragment(new Games(), "test");
        viewPager.setAdapter(catadapter);
        viewPager.setOffscreenPageLimit(1);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(getString(R.string.Earn_Cash));
        tabLayout.getTabAt(1).setText(getString(R.string.play_zone));


        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(0);
                } else if (viewPager.getCurrentItem() == 0) {
                    if (doubleBackToExitPressedOnce) {
                        RatingDialogFragment dialog = RatingDialogFragment.newInstance();
                        dialog.show(getActivity().getSupportFragmentManager(), "rating_dialog");
                    } else {
                        doubleBackToExitPressedOnce = true;
                    }
                }
            }
            return false;
        });

        binding.refresh.setOnClickListener(v -> {
            binding.refresh.setEnabled(false);
            Glide.with(getActivity()).asGif().load(R.drawable.loading).into(binding.refresh);
            reload_coin();
        });

        binding.layoutCoin.setOnClickListener(v -> {
//            loadFragment(new Coins());

            startActivity(new Intent(getActivity(), PaytmDevActivity.class));
        });

        return binding.getRoot();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume() {
        binding.coins.setText(Method.coolNumberFormat(Long.parseLong(String.valueOf(session.getIntData(session.WALLET)))));
        super.onResume();
    }


    private void reload_coin() {
        Call<CreditResponse> call = ApiClient.getClient(getActivity()).create(ApiInterface.class).Mycoin();
        call.enqueue(new Callback<CreditResponse>() {
            @Override
            public void onResponse(Call<CreditResponse> call, Response<CreditResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        binding.coins.setText(Method.coolNumberFormat(Long.parseLong(String.valueOf(response.body().getBalance()))));
                        session.setIntData(session.WALLET, response.body().getBalance());
                        Toast.makeText(getActivity(), "Balance Updated", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> binding.refresh.setEnabled(true), 5000);
                        binding.refresh.setImageResource(R.drawable.ic_baseline_autorenew_24);

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<CreditResponse> call, Throwable t) {

            }
        });
    }
}