package com.example.cmput301w21t23_smartdatabook.database;

/**
 * Callback interface that handles the general data, so it passes the data from 1 class to other class, and let other classes to call it in future
 * Synchronous will ask data from asynchronous, callback helps and returns data back to synchronous
 * @author Bosco Chan, Jayden Cho
 */
public interface GeneralDataCallBack {
	void onDataReturn(Object returnedObject);
}
