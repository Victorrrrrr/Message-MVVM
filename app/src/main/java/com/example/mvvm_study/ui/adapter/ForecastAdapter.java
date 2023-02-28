package com.example.mvvm_study.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.services.weather.LocalDayWeatherForecast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.ItemForecastBinding;


import java.util.List;

public class ForecastAdapter extends BaseQuickAdapter<LocalDayWeatherForecast, BaseDataBindingHolder<ItemForecastBinding>> {


    public ForecastAdapter(@Nullable List<LocalDayWeatherForecast> data) {
        super(R.layout.item_forecast, data);
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemForecastBinding> bindingHolder, LocalDayWeatherForecast localDayWeatherForecast) {
        ItemForecastBinding binding = bindingHolder.getDataBinding();
        if(binding != null) {
            binding.setForecast(localDayWeatherForecast);
            binding.executePendingBindings();
        }
    }
}
