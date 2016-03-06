@Client
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