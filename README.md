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
  - Select "Create" to publish the experiment so other users, including you, may see it

Select An Experiment from the Explore tab:
  - The Explore tab displays the list of experiments, as well as each of their name, owner's ID, created date, description, as well as an optional message notifying geolocation required, if the experiment doesn't have a geolocation.
  - Users can choose to follow the experiment by ticking on the check box on the experiment. Once an experiment is followed, it will appear on the Subscribe tab on the naviagation bar of the app.
  - Users can specifically click the experiment owner's name on the experiment, to see their username and email
  - Users can click the "comments" button, to see list of comments about the selected experiment
  - Once the users click to an experiment, it will lead them to the experiment details page, showing more information about the clicked experiment

Showing more details about selected experiment in the experiment details page:
  - The Explore tab shows the list of experiments, as well as each of their name, owner's ID, created date, description, as well as an message notifying geolocation required.
  - Once the user clicked an experiment, the clicked experiment's additional information will be displayed
  - Users will be able to perform more actions regards to the experiment, like: 
  - By clicking the "View Stats" button, users can view experiment statisics like mean, median, standard deviation, and quartiles, in a separate page
  - By clicking the "Upload Trials" button, users can upload their trials onto the selected experiment.
  - By clicking the "Generate/ Register code" text, users can input values for the experiment's trials, then save it to a QR code
  - By clicking the "Scan QR code" text, users can add trials through scanning QR code
  - By clicking the "Ask Question" test, users can ask questions about the selected experiment, which other users can view and answer them
  - If the experiment owners select their own experiment, there are additional functions that only owners can access, such as:
  - By clicking the "Show map" text, experiment owners can see the locations of different trials that are uploaded in the selected experiment, through Google Maps
  - When the selected experiment is not archived, By clicking the "archive" text, experiment owners can archive their own experiment, and that experiment will be appear on user's own Archive tab on the middle right side of the natvigation bar. (fix: maybe need a dialog to show that)
  - When the selected experiment is archived. By clicking the "Un-archive" text, a dialog will appear to confirm the unarchive action. Once verified, experiment owners can unarchive their own experiment, and that experiment will be appear on user's own Explore tab on the left of the navigation bar.

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

MORE TO ADD...
  - upload trial all steps (all 4 scenarios/ special case)
  - remove experiment
  - Comments (adding comments, replying comments)
  - viewing graphs
  - QR codes (scan QR codes, generate QR code)
  - more I haven't think yet
