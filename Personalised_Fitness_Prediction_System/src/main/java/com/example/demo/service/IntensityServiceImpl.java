package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Intensity;
import com.example.demo.repository.IntensityRepo;

@Service("intensityservice")
public class IntensityServiceImpl implements IntensityService {

	@Autowired
	IntensityRepo intensityrepo;
	@Override
	public List<Intensity> viewIntensity() {
		// TODO Auto-generated method stub
		return intensityrepo.viewIntensity();
	}

}
