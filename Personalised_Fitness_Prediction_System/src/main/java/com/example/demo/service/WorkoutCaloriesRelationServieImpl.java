package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.*;
import com.example.demo.repository.*;

@Service("WorkoutCaloriesRelationservice")
public class WorkoutCaloriesRelationServieImpl implements WorkoutCaloriesRelationService {
	
	@Autowired
	WorkoutCaloriesRelationRepository WorkoutCaloriesRelationrepo;

	@Override
	public boolean add(WorkoutCaloriesRelation workoutcalories) {
		return WorkoutCaloriesRelationrepo.add(workoutcalories);
	}

	@Override
	public List<WorkoutCaloriesRelation> view() {
		return WorkoutCaloriesRelationrepo.view();
	}

	@Override
	public boolean update(WorkoutCaloriesRelation workoutcalories, Integer record_id) {
		return WorkoutCaloriesRelationrepo.update(workoutcalories,record_id);
	}

	@Override
	public boolean delete(Integer recordid) {
		return WorkoutCaloriesRelationrepo.delete(recordid);
	}

	@Override
	public WorkoutCaloriesRelation getworkoutcalbyrid(Integer recordid) {
		// TODO Auto-generated method stub
		return WorkoutCaloriesRelationrepo.getworkoutcalbyrid(recordid);
	}

	
	

}
