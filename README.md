# CMPUT301W21T23-SmartDataBook

This is a mobile application that allows crowd-sourced testing of phenomona. <br/>

There are 4 kinds of crowd-sourced tests for each user: counts (how many did you see), binomial trials (pass fail), non-negative integer counts (each trial has 0 or more), measurement trials (like the temperature). Users in this app are experimenters who create and store such crowd-sourced tests in a Firebase database. <br/>

The Team Wiki is found [here](https://github.com/CMPUT301W21T23/CMPUT301W21T23-SmartDataBook/wiki)

SmartDataBook Usage Guide:

Signing Up:
  - Upon installing and opening up the app, a unique user ID will be generated for each user
  - No username, contact, or password inputs are required, but username and contact info may be filled in later.

Editing Profile:
  - Start the app and look at the bottom of the screen
  - On the right side of the, navigation bar click the "Settings" tab
  - Select the tab and you will be presented with a "Edit Profile" screen
  - Select "Edit" and enter in the user's "Username" and "Contact" and press enter
  - User's inputs will now be seen publicly by other users

Add An Experiment:
  - Select the "Explore" tab on the nagivation bar on the bottom left of the app interface
  - Click the Red, circle plus sign to add a new experiment
  - It will present you with a new screen, with the title "Add new experiment"
  - Input information into the presented empty fields, such as the experiment's name and description
  - Set the minimum and maximum of trials for the experiment
  - Select either binomial, count, non-negative count, and measurement for the type of tests of the experiment
  - Activate or deactivate the location of your experiment
  - Allow or deny the experiment of being public and shared its contents with others
  - Once users have filled the experiment's fields, tap the "Create" button to publish the experiment so the experiment becomes public

Select An Experiment from the Explore tab:
  - The Explore tab displays the list of experiments, as well as each of their name, owner's ID, created date, description, as well as an optional message notifying geolocation required, if the experiment doesn't have a geolocation.
  - Users can choose to follow the experiment by ticking on the check box on the experiment. Once an experiment is followed, it will appear on the Subscribe tab on the naviagation bar of the app.
  - Users can specifically click the experiment owner's name on the experiment, to see their username and email
  - Users can click the "comments" button, to see list of comments about the selected experiment
  - Once the users click to an experiment, it will lead them to the experiment details page, showing more information about the clicked experiment

Features for users in the experiment details page:
  - The Explore tab shows the list of experiments, as well as each of their name, owner's ID, created date, description, as well as an message notifying geolocation required.
  - Once the user clicked an experiment, the clicked experiment's additional information will be displayed
  - Users will be able to perform more actions regards to the experiment, like: 
  - By clicking the "View Stats" button, users can view experiment statisics like mean, median, standard deviation, and quartiles, in a separate page
  - By clicking the "Upload Trials" button, users can upload their trials onto the selected experiment.
  - By clicking the "Show map" text, experiment owners can see the locations of different trials that are uploaded in the selected experiment, through Google Maps
  - By clicking the "Generate/ Register Code" text, users can input values for the experiment's trials, then save it to a QR code
  - By clicking the "Scan QR code" text, users can add trials through scanning QR code
  - By clicking the "Ask Question" test, users can ask questions about the selected experiment, which other users can view and answer them

Additional features for experiment owners on the experiment details page:
  - When the selected experiment is not archived, By clicking the "archive" text, experiment owners can archive their own experiment, and that experiment will be appear on user's own Archive tab on the middle right side of the natvigation bar. (fix: maybe need a dialog to show that)
  - When the selected experiment is archived. By clicking the "Un-archive" text, a dialog will appear to confirm the unarchive action. Once verified, experiment owners can unarchive their own experiment, and that experiment will be appear on user's own Explore tab on the left of the navigation bar.
  - Experiment owners can check/ uncheck the "publish" checkbox to decide whether they want to show their experiments publicly or not

Select subscribed experiments:
  - On the middle left of the navigation bar on the bottom of the app, select the Subscribed tab
  - The Subscribed tab shows all the experiments that users have subscribed
  - Users can select their archived experiment, just like they do on the Explore tab, or Archived Tab
  - To remove an experiment from the Subscribed page, either uncheck the experient from the Explore page, or the Subscribed page

Select archived experiments
  - On the middle right of the navigation bar on the bottom of the app, select the Archived tab
  - The Archived tab shows all of the user's archived experiment, which users can quikcly search their desired experiment
  - Users can select their archived experiment, just like they do on the Explore tab, or Subscribed Tab
  - To remove an experiment from the Archived tab, simply select the experiment, then click the "Un-Archive text" to un-archive an experiment"
  
Upload trials on experiment
  - Select an experiment from either the Explore tab, Archived tab, or the Subscribed tab
  - Then click the "upload trial" button
  - Once the button is clicked, a list of trials on the experiment will be displayed which users can view the trial value, the user who upload the trials, and the date of the trial uploaded to the experiment
  - In addtion, experiment owners can also select trial(s) from the trial list to delete trials
  - On the bottom of the screen press the "add new trials" button to upload trials
  - Then a dialog will appear to prompt users to enter the trial value, with proper constraint
  - After successfully entering the trial values, users will return back to the upload trials page which the new trials are displayed and updated
  - Users can repeatedly uploading trials as long as the number of trials don't exceed the experiment's maximum number of trials

Generate QR or Barcodes:
  - Besides uploading trial value through the "upload trials" button, user can upload their trial values by generating QR codes or barcodes
  - In order to generate code, the users will need to select an experiment, then select the "Generate/ Register Code" text
  - Afterwards, based on the selected experiment's type, user can enter the numerical value
  - If the experiment's type is binomial, then users will need to select whether they add adding passes or failures
  - In addidiotn, a checkbox appears for user to decide whether they want to add location to the QR code
  - Once the user clicks the "generate" button, a QR code consists of the trial value will be displayed
  - If the user prefers to register a barcode instead, clicked to the "register barcode" button, and the barcode will be registered

Scan QR or Barcodes:
  - Besides uploading trial value through the "upload trials" button, user can upload their trial values by generating QR codes or barcodes
  - N/A for now

Searching experiments
  - To search an experiment, click on the the magnifying glass icon on the top right of the app on either Explore, Subscribed, or Archived tab
  - Afterwards, users are able to type in order to search for their experiment
  - If users decides to cancel searching, they can press the left arrow icon lcoated on the top left of the app, which will return the user back on the tab they were at before they decide to search.
  - The app searches experiment based on the keyword user types, with the experiments' name, experiment owners' username, experiments' date, description, and trial type.
  - A successful search will search the correct experiment, which allows users to find their desired experiment much faster.
  
Adding comments
  - In an experiment, click the "comments" button in the experiment
  - Then, a list of comments will appear, which users can see other comments, as well as their comment creator's username, date of created comment, and the comment ID
  - In the comment list page,  users can add comments, through the floating plus button, on the bottom right of the screen
  - Once the user has clicked the button, a dialog will prompt user for the comment text, which the user can either add or discard their comments
  - Then the user will return back to the comment list page, which newly added comments are updated

Reply comments
  - In an experiment, click the "comments" button in the experiment. Aftwards, a list of commments will be displayed.
  - Users can click on a comment, which they will now see other comments that respond the the seclected comment.
  - To reply a comment, click on the floating plus buttom lcoated at the bottom left of the screen, then a dialog will display which prompts user to enter a new comment that replies to the clicked comment.
  - Once the user has clicked the button, a dialog will prompt user for the comment text, which the user can either add or discard their comments
  - Then the replied comments will be updated and displayed on the screen of selected comment.

View stats and graphs
  - In an experiment, click the "comments" button in the experiment

MORE TO ADD...
  - viewing graphs and stats
  - QR codes (scan QR codes, generate QR code)
  - more I haven't think yet
