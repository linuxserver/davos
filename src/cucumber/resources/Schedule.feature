@Schedule @Server
Feature: Scheduling

	Scenario: Finding files that match filters
	
		Given there is an FTP server running
		And a schedule exists for that server, with filters
		When that schedule is run
		Then only the filtered files are downloaded
		
	Scenario: Should delete files once matched and downloaded
	
		Given there is an FTP server running
		And a schedule exists for that server, with filters
		And the schedule is set to delete host files
		When that schedule is run
		Then only the filtered files are downloaded
		And those files are deleted on the host
		
	Scenario: Should download all files not matching filters if inverted
	
		Given there is an FTP server running
		And a schedule exists for that server, with filters
		And the schedule is set to invert filters
		When that schedule is run
		Then all files not matching the filters are downloaded
		
	Scenario: Should not download any files if filters are mandatory but not set
	
		Given there is an FTP server running
		And a schedule exists for that server, without filters
		And the schedule is set to have mandatory filters
		When that schedule is run
		Then no files are downloaded
		
