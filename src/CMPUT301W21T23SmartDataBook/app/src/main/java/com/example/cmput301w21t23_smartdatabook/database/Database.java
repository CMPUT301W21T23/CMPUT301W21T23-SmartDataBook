
package com.example.cmput301w21t23_smartdatabook.database;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cmput301w21t23_smartdatabook.experiment.Experiment;
import com.example.cmput301w21t23_smartdatabook.comments.Comment;
import com.example.cmput301w21t23_smartdatabook.trials.Trial;
import com.example.cmput301w21t23_smartdatabook.user.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * class: Database
 * This class consists database, it has the attributes call back which handles synchronous and asychronous functions, and an arraylist of experiment
 * It passes/ takes information to firestore
 *
 * @author Bosco Chan, Afaq Nabi, Jayden Cho
 */
public class Database {

	private ArrayList<Experiment> experimentDataList = new ArrayList<>();
	private static Database database;

	FirebaseAuth mAuth;
	FirebaseFirestore db;

	/**
	 * Main function does most of database's tasks
	 *
	 * @author Bosco Chan
	 */
	public Database() {
	}

	//Singleton implementation
	public static Database getDataBase() {
		if (database == null) {
			database = new Database();
		}
		return database;
	}

	public void followStatus(DocumentReference ref, Experiment experiment, Context context, CheckBox follow, String currentID) {

		ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					DocumentSnapshot document = task.getResult();
					if (document.exists()) {

						if (!currentID.equals(experiment.getOwnerUserID())) {
							ref.delete();
						} else {
							Toast.makeText(context, "Cannot unfollow owned Experiment", Toast.LENGTH_SHORT).show();
							follow.setChecked(true);
						}
					}
				}
			}
		});//ref.get()
	}

	//Task will be executed here. Done in the background. Called Asynchronous task.

	/**
	 * This function delete trials from the database
	 */
	public void deleteFromDB(DocumentReference DocRef) {
		DocRef.delete();
	}

	/**
	 * This function adds the trial to the database
	 * @param genericDocument
	 * @param trial
	 */
	public void addTrialToDB(DocumentReference genericDocument, Trial trial) {
		LatLng latlng = trial.getLocation();
		HashMap<String, Object> data = new HashMap<>();
		data.put("Region On", trial.isGeoLocationSettingOn());
		data.put("Trial Type", trial.getExpType());
		data.put("Trial Value", trial.getValue());
		data.put("UUID", trial.getUid());
		data.put("TrialID", trial.getTrialID());
		data.put("StringDate", trial.getDate());
		if (trial.isGeoLocationSettingOn()) {
			data.put("GeoPoint", new GeoPoint(latlng.latitude, latlng.longitude));
		}

		genericDocument.set(data);
	}

	/**
	 * This function fills the trial list on the screen
	 * @param coll
	 * @param trialDataList
	 * @param trialArrayAdapter
	 */
	public void fillTrialList(CollectionReference coll, ArrayList<Trial> trialDataList, ArrayAdapter<Trial> trialArrayAdapter) {

		coll.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {
								GeoPoint geoPoint = (GeoPoint) document.get("GeoPoint");
								LatLng latlng = (geoPoint != null) ? (new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude())) : null;
								trialDataList.add(new Trial(
										(Boolean) document.get("Region On"),
										document.get("Trial Type").toString(),
										document.get("Trial Value"),
										document.get("UUID").toString(),
										document.get("TrialID").toString(),
										(String) document.get("StringDate"),
										latlng
								));
							}
							trialArrayAdapter.notifyDataSetChanged();
						}
					}
				});
	}

	/**
	 * This function adds comments to the database through hashmap
	 * @param DocRef
	 * @param comment
	 */
	public void addCommentToDB(DocumentReference DocRef, Comment comment) {
		HashMap<String, Object> data = new HashMap<>();
		data.put("CommentText", comment.getText());
		data.put("UserID", comment.getUserUniqueID());
		data.put("CommentID", comment.getCommentID());
		data.put("StringDate", comment.getDate());
		DocRef.set(data);
	}

	/**
	 * THis function fills the comment list if the task is successful
	 * @param coll
	 * @param commentList
	 * @param commentAdapter
	 */
	public void fillCommentList(CollectionReference coll, ArrayList<Comment> commentList, ArrayAdapter<Comment> commentAdapter) {
		coll
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {
								commentList.add(new Comment(
										document.get("CommentText").toString(),
										document.get("UserID").toString(),
										document.get("CommentID").toString(),
										(String) document.get("StringDate")));
							}
							commentAdapter.notifyDataSetChanged();
						}
					}
				});
	}

	/**
	 * This functions fills the username, its email and UUID by using hashtable anc callback
	 * @param generalDataCallBack
	 */
	public void fillUserName(GeneralDataCallBack generalDataCallBack) {
		db = FirebaseFirestore.getInstance();

		Hashtable<String, User> userNames = new Hashtable<String, User>();

		db.collection("Users")
				.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

			@Override
			public void onComplete(@NonNull Task<QuerySnapshot> task) {
				mAuth = FirebaseAuth.getInstance();

				String path = db.collection("Users").getPath();

				if (task.isSuccessful()) {
					for (QueryDocumentSnapshot document : task.getResult()) {
						if (document.getData().get("UUID") != null && document.getData().get("UserName") != null) {

							User user = new User(document.getData().get("UserName").toString(),
									document.getData().get("Contact").toString(),
									document.getData().get("UUID").toString());

							userNames.put(document.getData().get("UUID").toString(), user);

						}
					}
					generalDataCallBack.onDataReturn(userNames);
				} else {
					generalDataCallBack.onDataReturn(new Hashtable<String, User>());
				}
			}
		});
	}


	/**
	 * Fills the statistical views with trial value and date data. statsDataList contains a list with [Number, StringDate]
	 * for each item.
	 */
	public void fillStatsList(GeneralDataCallBack generalDataCallBack, ArrayList<ArrayList> statsDataList, CollectionReference collection) {
		db = FirebaseFirestore.getInstance();

		collection
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						mAuth = FirebaseAuth.getInstance();

						statsDataList.clear();

						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {

								Object trialValue = document.get("Trial Value");

								statsDataList.add(new ArrayList<Object>() {
									{
										Number temp;

										if (document.get("Trial Type").toString().equals("Binomial")) {

											//Convert database boolean into integer value
											if ((Boolean) trialValue) {
												temp = 1.0;
											} else {
												temp = 0.0;
											}

										} else {
											temp = Double.parseDouble(document.get("Trial Value").toString());
										}
										add(temp);
										add(document.get("StringDate").toString());
									}
								});//Adding to statsDataList

							}//for

							//Get callback to grab the populated dataList
							generalDataCallBack.onDataReturn(statsDataList);

						} else {
							generalDataCallBack.onDataReturn(new ArrayList<>());
						}
					}
				});
	}

	/**
	 * Get Experiment documents from the database and add its contents to the experimentDataList
	 * to populate the user's fragment page.
	 * (All experiments added to the experimentDataList ONLY exist in the SCOPE of the "onComplete()").
	 * (Since ArrayAdapter<Experiment> experimentAdapter
	 *
	 * @param generalDataCallBack is the callback instance from a synchronous.
	 * @author Bosco Chan
	 */
	public void fillDataList(GeneralDataCallBack generalDataCallBack, ArrayAdapter<Experiment> experimentArrayAdapter, CollectionReference collection, String currentID, Hashtable<String, User> userNames) {
		db = FirebaseFirestore.getInstance();

		collection
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						mAuth = FirebaseAuth.getInstance();

						experimentDataList.clear();

						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {
								if ((collection.getPath().equals("Experiments")) && document.getData().get("ExpID") != null && ((Boolean) document.get("PublicStatus")) ||
										(collection.getPath().equals(db.collection("Users").document(currentID).collection("Favorites").getPath())) ||
										(collection.getPath().equals("Archived"))) {

									boolean requireLocation = (Boolean) document.getData().get("requireLocation");
									GeoPoint geoPoint = (GeoPoint) document.getData().get("Location");
									LatLng latlng = requireLocation ? (new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude())) : null;

									experimentDataList.add(new Experiment(
													document.getData().get("Name").toString(),
													document.getData().get("UUID").toString(),
													userNames.get(document.getData().get("UUID").toString()).getUserName(),
													document.getData().get("Trial Type").toString(),
													document.getData().get("Description").toString(),
													requireLocation,
													Integer.parseInt(String.valueOf(document.getData().get("Minimum Trials"))),
													Integer.parseInt(String.valueOf(document.getData().get("Maximum Trials"))),
													(Boolean) document.getData().get("PublicStatus"),
													(String) document.getData().get("StringDate"),
													document.getData().get("ExpID").toString(),
													(Boolean) document.getData().get("isEnd"),
													latlng
											)
									);
								}

							}//for

							//Get callback to grab the populated dataList
							generalDataCallBack.onDataReturn(experimentDataList);
							experimentArrayAdapter.notifyDataSetChanged();

						} else {
							generalDataCallBack.onDataReturn(new ArrayList<>());
						}
					}
				});
	}

	/**
	 * Add a new experiment object to the Firebase database by assigning a unique experiment ID.
	 * An experiment is added by firstly checking if the collection contains any
	 *
	 * @param newExperiment The experiment object that is to be added to the Firebase database
	 * @author Bosco Chan
	 */
	public void addExperimentToDB(Experiment newExperiment, CollectionReference collection, String currentID) {
		mAuth = FirebaseAuth.getInstance();

		db = FirebaseFirestore.getInstance();
		HashMap<String, Object> data = new HashMap<>();
		// If there’s some data in the EditText field, then we create a new key-value pair.
		data.put("Name", newExperiment.getExpName());
		data.put("Description", newExperiment.getDescription());
		data.put("Trial Type", newExperiment.getTrialType());
		data.put("requireLocation", newExperiment.getRequireLocation());
		data.put("PublicStatus", newExperiment.isPublic());
		data.put("UUID", newExperiment.getOwnerUserID());
		data.put("Minimum Trials",  newExperiment.getMinTrials());
		data.put("Maximum Trials", newExperiment.getMaxTrials());
		data.put("StringDate", newExperiment.getDate());
		data.put("ExpID", newExperiment.getExpID());
		data.put("isEnd", newExperiment.getIsEnd());
		if (newExperiment.getRequireLocation()) {
			GeoPoint geoPoint = new GeoPoint(newExperiment.getLatLng().latitude, newExperiment.getLatLng().longitude);
			data.put("Location", geoPoint);
		}
		collection
				.document(newExperiment.getExpID())
				.set(data);

		addToFavorites(data, currentID, newExperiment);

	}//addExperimentToDB

	public void addToFavorites(HashMap<String, Object> data, String currentID, Experiment newExperiment) {
		db.collection("Users")
				.document(currentID)
				.collection("Favorites")
				.document(newExperiment.getExpID())
				.set(data);
	}

	/**
	 * Edit user profile by querying the database.
	 *
	 * @param usernameTextField contains the editText field to write username to
	 * @param contactTextField    contains the editText field to write email to
	 * @param saveButtonView    contains the save button view
	 * @param context           contains the given context from which this function is called from
	 * @author Bosco Chan
	 */
	public void editUser(EditText usernameTextField, EditText contactTextField, View saveButtonView, Context context, String currentID, User user) {

		db = FirebaseFirestore.getInstance();

		DocumentReference docRef = db.collection("Users").document(currentID);
		docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					DocumentSnapshot document = task.getResult();
					if (document.exists()) {
						Map<String, Object> data = document.getData();

						if (data.get("UserName").toString().equals("")) {
							usernameTextField.setHint("Enter Username");
						} else {
							usernameTextField.setText(data.get("UserName").toString());
						}

						if (data.get("Contact").toString().equals("")) {
							contactTextField.setHint("Enter Contact");
						} else {
							contactTextField.setText(data.get("Contact").toString());
						}

						saveButtonView.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								String username = usernameTextField.getText().toString();
								String contact = contactTextField.getText().toString();
								String message;
								if (username.equals("")) {
									message = "Username field should not be empty";
								} else {
									user.setUserName(username);
									user.setUserContact(contact);

									docRef.update("UserName", username);
									docRef.update("Contact", contact);
									message = "Profile successfully saved";
								}
								Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
			}
		});

	}

	//Temporarily sign in user as an anonymous user on first sign in
	//Source: firebase guides, https://firebase.google.com
	//License: Creative Commons Attribution 4.0 License, Apache 2.0 License
	//Code: https://firebase.google.com/docs/auth/android/anonymous-auth?authuser=0

	/**
	 * Authenticates a new app user anonymously and generates a "User document" for the user
	 * containing their respective "username and contact".
	 *
	 * @author Bosco Chan
	 */
	public void authenticateAnon(GeneralDataCallBack generalDataCallBack) {

		FirebaseAuth mAuth = FirebaseAuth.getInstance();
		mAuth.signInAnonymously()
				.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {

							// Sign in success, update UI with the signed-in user's information
							FirebaseUser currentUser = mAuth.getCurrentUser();

							assert currentUser != null;
							generalDataCallBack.onDataReturn(currentUser.getUid());

							FirebaseFirestore db = FirebaseFirestore.getInstance();
							// Get a top level reference to the collection
							final CollectionReference allUsersCollection = db.collection("Users");
							HashMap<String, String> data = new HashMap<>();

							//Source: Firebase, firebase.google.com
							//License: Creative Commons Attribution 4.0 License, Apache 2.0 License
							//Code: https://firebase.google.com/docs/firestore/query-data/get-data#java_
							DocumentReference userDoc = allUsersCollection.document(currentUser.getUid());
							userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
								@Override
								public void onComplete(@NonNull Task<DocumentSnapshot> task) {
									if (task.isSuccessful()) {
										DocumentSnapshot document = task.getResult();
										assert document != null;
										if (!document.exists()) {
											data.put("UserName", "User - " + currentUser.getUid().substring(0, 4));
											data.put("Contact", "");
											data.put("UUID", currentUser.getUid());

											allUsersCollection
													.document(currentUser.getUid())
													.set(data);
										}
									}
								}
							});
						} else {
							// If sign in fails, display a message to the user.
						}//if
					}
				});

	}//authenticationAnon

	/**
	 * This function checks an experiment is archived or not
	 * @param coll
	 * @param onOff
	 * @param experiment
	 * @param status
	 */
	public void publicOrEnd(CollectionReference coll, boolean onOff, Experiment experiment, String status) {
		coll.document(experiment.getExpID()).update(status, onOff);
	}

}//Database
