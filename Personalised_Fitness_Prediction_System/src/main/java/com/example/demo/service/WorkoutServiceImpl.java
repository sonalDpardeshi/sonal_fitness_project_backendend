package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Workout;
import com.example.demo.repository.WorkoutRepository;

@Service("workoutservice")
public class WorkoutServiceImpl implements WorkoutService {

	@Autowired
	WorkoutRepository workoutrepo;
	
	@Override
	public boolean add(Workout workout) {
		return workoutrepo.add(workout);
	}

	@Override
	public List<Workout> view() {
		return workoutrepo.view();
	}

}
