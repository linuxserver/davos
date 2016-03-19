@Schedule @Server
Feature: Scheduling

	Scenario: Finding files that match filters
	
		Given there is an FTP server running
		And a schedule exists for that server, with filters
		When that schedule is run
		Then only the filtered files are downloaded
		
