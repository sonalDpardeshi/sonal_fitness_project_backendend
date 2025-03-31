package com.example.demo.repository;

import java.util.*;
import com.example.demo.model.*;

public interface UserRepository {

	public boolean add(User user);

	public boolean validate(String username, String password);


}
