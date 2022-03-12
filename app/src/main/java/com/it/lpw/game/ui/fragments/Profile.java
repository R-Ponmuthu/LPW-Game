package com.it.lpw.game.ui.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.it.lpw.game.R;
import com.it.lpw.game.databinding.FragmentProfileBinding;
import com.it.lpw.game.paytm.ui.PaytmDevActivity;
import com.it.lpw.game.ui.activity.AboutusActivity;
import com.it.lpw.game.ui.activity.FrontLogin;
import com.it.lpw.game.ui.activity.WithdrawActivity;
import com.it.lpw.game.util.Constant_Api;
import com.it.lpw.game.util.Session;

public class Profile extends Fragment {
    FragmentProfileBinding binding;
    Session session;
    Activity activity;
    private AlertDialog dialog_logout;
    int ActivityRequestCode = 1002;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        session = new Session(getActivity());
        activity = getActivity();

        dialog_logout = new AlertDialog.Builder(activity).setView(LayoutInflater.from(activity).inflate(R.layout.layout_dialog, null)).create();
        dialog_logout.getWindow().setBackgroundDrawableResource(R.color.transprent);
        dialog_logout.getWindow().setWindowAnimations(R.style.Dialoganimation);
        dialog_logout.setCanceledOnTouchOutside(false);

        binding.email.setText("Email : " + session.getData(session.EMAIL));
        binding.username.setText("Name : " + session.getData(session.NAME));
        binding.phone.setText("Phone : " + session.getData(session.PHONE));

        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                FragmentMain NameofFragment = new FragmentMain();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, NameofFragment);
                transaction.commit();
                return true;
            }
            return false;
        });
        binding.cvAbout.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AboutusActivity.class));
        });

        binding.cvContact.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AboutusActivity.class));
        });

        binding.coin.setText("" + session.getIntData(session.WALLET));

        binding.cvFeedback.setOnClickListener(v -> {
            final String appName = getActivity().getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
            } catch (ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
            }
        });

        binding.cvHistory.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new Coins());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        binding.cvLogout.setOnClickListener(v -> {
            Logout();
        });


        binding.cvReward.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), WithdrawActivity.class));
        });

        binding.cvPrivacy.setOnClickListener(v -> {
            launchCustomTabs(activity, Constant_Api.PRIVACY_POLICY_URL);
        });

        binding.cvAddCoins.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), PaytmDevActivity.class));
        });

        return binding.getRoot();
    }

    private void Logout() {
        dialog_logout.show();
        dialog_logout.findViewById(R.id.yes).setOnClickListener(v -> {
            session.Logout();
            session.setBoolean(session.LOGIN, false);
            startActivity(new Intent(getActivity(), FrontLogin.class));
            getActivity().finish();
        });

        dialog_logout.findViewById(R.id.no).setOnClickListener(v -> {
            dialog_logout.dismiss();
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void launchCustomTabs(Activity activity, String url) {
        CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
        customIntent.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        customIntent.setExitAnimations(activity, R.anim.exit, R.anim.enter);
        customIntent.setStartAnimations(activity, R.anim.enter, R.anim.exit);
        customIntent.setUrlBarHidingEnabled(true);
        customIntent.build().launchUrl(activity, Uri.parse(url));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {
            Toast.makeText(getActivity(), data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }
    }
}
