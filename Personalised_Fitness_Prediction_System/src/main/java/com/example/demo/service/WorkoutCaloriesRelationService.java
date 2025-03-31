package com.example.demo.service;

import java.util.*;
import com.example.demo.model.*;

public interface WorkoutCaloriesRelationService {

	public boolean add(WorkoutCaloriesRelation workoutcalories);

	public List<WorkoutCaloriesRelation> view();

	public boolean update(WorkoutCaloriesRelation workoutcalories, Integer record_id);

	public boolean delete(Integer recordid);

}
