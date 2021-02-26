package com.mao.service;

import com.mao.proj.Fish;

import java.util.List;

public interface FishService {

    public void updateSetting(int id, int smart, int water, int wendu, int o2);

    public List<Fish> getSettingAll();
}
