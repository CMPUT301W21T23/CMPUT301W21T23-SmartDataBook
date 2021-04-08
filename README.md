# CMPUT301W21T23-SmartDataBook

This is a mobile application that allows crowd-sourced testing of phenomona. <br/>

There are 4 kinds of crowd-sourced tests for each user: counts (how many did you see), binomial trials (pass fail), non-negative integer counts (each trial has 0 or more), measurement trials (like the temperature). Users in this app are experimenters who create and store such crowd-sourced tests in a Firebase database. <br/>

The Team Wiki is found [here](https://github.com/CMPUT301W21T23/CMPUT301W21T23-SmartDataBook/wiki)

SmartDataBook Usage Guide:

Signing Up:
  - Upon installing and opening up the app, a unique user ID will be generated for said user
  - No username, contact, or password inputs are required, but username and contact info may be filled in later.

Editing Profile:
  - Start the app and look at the bottom of the screen
  - There is a navigation bar will a "Settings" tab
  - Select the tab and you will be presented with a "Edit Profile" screen
  - Select "Edit" and enter in your "Username" and "Contact" and press enter
  - Your inputs will now be seen publicly by you and other users

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

Select An Experiment:
  - The home page shows the list of experiments, as well as each of their name, owner's ID, created date, description, as well as an message notifying geolocation required.
  - Users can choose to follow the experiment by ticking on the check box on the experiment. Once an experiment is followed, it will appear on the Favourite page on the bottom of the app.
  - Users can also specifically click the experiment owner's name on the experiment, to see their username and email
  - Once clicked to an experiment, the user is transferred to experiment details page and the clicked experiment's additional information will be displayed
  - Users will be able to perform more actions regards to the experiment, like: 
  - By clicking the "View Stats" button, users can view experiment statisics like mean, median, standard deviation, and quartiles
  - By clicking the "Upload Trials" button, users can upload their trials onto the selected experiment.
  - By clicking the "generate map" text, users can see the locations of different trials that are uploaded in the selected experiment, through google maps
  - By clicking the "generate QR code" text, users can add trials through scanning QR code
  - By clicking the "asking question" test, users can ask questions about the selected experiment, which other users can view and answer them
  - If the experiment owners select their own experiment:
  - ...More to be added...


On The Favoutie page:
  - The favourite page shows all the experiments you have followed
  - When clicked onto an experiment, it will act directly leads to the experiment' details page and show all of the experiment's content.

...More to be added...
