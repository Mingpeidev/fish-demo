package com.mao.service;

import java.util.List;

import com.mao.proj.Fish;

public interface FishService {

	public void updataSetting(int id, int smart, int water, int wendu, int o2);
	public List<Fish> getSettingAll();

}
