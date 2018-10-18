package com.dsths.kafkatpoc.personservice;

public class GenerateInserts {

	public static void main(String[] args) {
		
		for (int i = 1; i < 1001; i++) {
			String firstName = "firstName" + i;
			String middleName = "middleName" + i;
			String lastName = "lastName" + i;
			String email = firstName + "." + lastName + "@DSTHealthSolutions.com";
			System.out.println(String.format("insert into person values (%d, '%s', '%s','%s', '%s', '1980-01-01');", i, firstName, middleName, lastName, email));
		}
		//insert into person values (1, 'John', 'Thomas', 'Grove', 'JGrove@DSTHealthSolutions.com', '1965-01-07');
		
		
		

	}

}
