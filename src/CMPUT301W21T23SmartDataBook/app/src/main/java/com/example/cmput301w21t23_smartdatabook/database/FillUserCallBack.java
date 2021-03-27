package com.example.cmput301w21t23_smartdatabook.database;

import com.example.cmput301w21t23_smartdatabook.user.User;

import java.util.Hashtable;

public interface FillUserCallBack {
	void getUserTable(Hashtable<String, User> UserName);
}
