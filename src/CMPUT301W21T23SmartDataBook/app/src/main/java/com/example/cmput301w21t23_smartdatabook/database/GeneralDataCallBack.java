package com.example.cmput301w21t23_smartdatabook.database;

/**
 * Callback interface that handles the general data, so it passes the data to someone, and let other users to call it in future
 * @author Bosco Chan
 */
public interface GeneralDataCallBack {
	void onDataReturn(Object returnedObject);
}
