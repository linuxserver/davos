@Client @Server
Feature: General client tests

	Scenario: Connecting to the FTP server
	
		Given there is an FTP server running
		When davos connects to the server
		Then listing the files will show the correct files
		
	Scenario: Downloading a file from the server
	
		Given there is an FTP server running
		When davos connects to the server
		And downloads a file
		Then the file is located in the specified local directory
		
    @Listener
	Scenario: Download with FTP Progress Listener
	
		Given there is an FTP server running
		When davos connects to the server
		And initialises a Progress Listener for that connection
		And downloads a file
		Then the Progress Listener will have its values updated
		
		