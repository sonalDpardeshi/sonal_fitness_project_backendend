package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.example.demo.model.User;
import com.example.demo.model.UserWorkoutData;

public interface UserService {

public boolean add(User user);//Register

public boolean validate(String username, String password);//Validate At Login

public boolean fetch(UserWorkoutData userworkout);

public String readHistory(int userid);//view csv file of history

public double totalcount(int userid);//total calories burned

public boolean addPath(int userid, File f);//add csv file  into db

public boolean updateUser(int id,User user);//Profile update

public User getUserByEId(String email);// for search user by email

public String viewSuggestedPlan(Integer userid)throws IOException;// see recomended plan

public String requestforplan(int userid) throws Exception;

}
