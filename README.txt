My personal application is 'TransactionTracker'. I made this app as a helper application for the video game Runescape. It is used to record financial transactions as well as show current market information.

TO COMPILE:
javac -cp gson-2.8.2.jar:okhttp-3.10.0.jar:okio-1.14.0.jar *.java

TO RUN:
java -classpath gson-2.8.2.jar:okhttp-3.10.0.jar:okio-1.14.0.jar: TransactionTracker


New features I implemented:
	1) Table view
	2) Populating GUI information from files
	3) Close window that saves all entered information upon closing
	4) Displaying multiple scenes and switching between them
	5) Communicating/passing information between different scenes
	6) Making HTTP requests and interacting with Wikipedia APIs / manipulating JSON
	7) Putting CheckBoxes in TableViews
	8) Allowing users to edit TableViews when they double click (opens a TextField for them to enter information in)
	9) Menu items

New features I have implemented since the midterm:
	1) CSS Styling
	2) Implemented callback so the active offer TableView can dynamically respond to new inputs
	3) Implemented menu options for the different “Add” and “Edit” options. These use multiple different files and windows
	   and take in user input.
	4) Implemented a graph in ‘New Buy’ that is composed from data scraped off the Wikipedia paged, dynamically updates
	5) Added buttons that give the user quick access to the web pages of the selected item in ‘New Buy’
	6) Made the history table dynamically update due to user searches
	7) Enabled the profit to be re-calculated based on what was selected in the table and choice boxes

Features I have yet to implement:
	1) Watch table

