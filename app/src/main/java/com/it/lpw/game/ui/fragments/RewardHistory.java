package com.it.lpw.game.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.it.lpw.game.Responsemodel.RewardHistoryResponse;
import com.it.lpw.game.adapters.RewardHistoryAdapter;
import com.it.lpw.game.restApi.ApiClient;
import com.it.lpw.game.restApi.ApiInterface;
import com.it.lpw.game.util.Method;
import com.it.lpw.game.R;
import com.it.lpw.game.util.Session;
import com.it.lpw.game.databinding.FragmentRewardHistoryBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardHistory extends Fragment {
    FragmentRewardHistoryBinding binding;
    RecyclerView.LayoutManager layoutManager;
    Context context;
    Session session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentRewardHistoryBinding.inflate(getLayoutInflater());
        context = getActivity();
        session = new Session(context);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (Method.isConnected(context)){
            getdata();
        }else {
            Method.Error(getActivity(),getString(R.string.no_internet));
        }

        return binding.getRoot();
    }

    private void getdata(){
        Call<RewardHistoryResponse> call= ApiClient.getClient(getActivity()).create(ApiInterface.class).RewardHistory();
        call.enqueue(new Callback<RewardHistoryResponse>() {
            @Override
            public void onResponse(Call<RewardHistoryResponse> call, Response<RewardHistoryResponse> response) {
                if(response.isSuccessful() && response.body().getData()!=null){
                    RewardHistoryAdapter adapter = new RewardHistoryAdapter(getActivity(),response.body().getData());
                    binding.recyclerView.setAdapter(adapter);
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }else {
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    binding.noResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<RewardHistoryResponse> call, Throwable t) {
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.noResult.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}