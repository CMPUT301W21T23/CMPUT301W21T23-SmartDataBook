package com.example.cmput301w21t23_smartdatabook;

import java.util.ArrayList;

//Source:Somenath Mukhopadhyay; https://www.youtube.com/channel/UCa_GP2OYWZBneHRB92qD2cQ
//Code: https://www.youtube.com/watch?v=u45aCzy5w0M
/**
 * Acts as a coordinator between classes that are split by synchronous and asynchronous
 * methods in their respective classes. Currently used by homePage.java (Synchronous), favPage.java (Synchronous)
 * and Database.java (Asynchronous).
 * @author Bosco Chan
 */
public interface FillDataCallBack {
    void getExpDataList(ArrayList<Experiment> DataList);
}
