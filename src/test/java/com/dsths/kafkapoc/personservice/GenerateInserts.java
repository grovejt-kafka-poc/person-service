package com.dsths.kafkapoc.personservice;

public class GenerateInserts {

	public static void main(String[] args) {
		
		for (int i = 1; i < 1001; i++) {
			String firstName = "firstName-" + i;
			String middleName = "middleName-" + i;
			String lastName = "lastName-" + i;
			String email = firstName + "." + lastName + "@DSTHealthSolutions.com";
			String birthDate = "1980-01-01";
			String addressLine1 = "addressLine1-" + i;
			String addressLine2 = "addressLine2-" + i;
			String city = "city-" + i;
			String state = "state-" + i;
			System.out.println(String.format("insert into person values "
					+ "(%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');", 
						i, firstName, middleName, lastName, email, birthDate, addressLine1, addressLine2, city, state));
		}
		//insert into person values (1, 'John', 'Thomas', 'Grove', 'JGrove@DSTHealthSolutions.com', '1965-01-07');
		
		
		

	}

}
