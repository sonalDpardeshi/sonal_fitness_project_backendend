package com.example.demo.repository;

import java.io.File;
import java.util.*;
import com.example.demo.model.*;

public interface UserRepository {

	public boolean add(User user);

	public boolean validate(String username, String password);

	public boolean fetch(UserWorkoutData userworkout);

	public String readHistory(int userid);

	public double totalCount(int userid);

	public boolean addpath(int userid, File f);

	public boolean updateUser(int id,User user);

	   public User getUserByEId(String email);
}
