package com.dsths.kafkapoc.personservice.person;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private Date birthDate;
	
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	
	
	

	public Person() {}

	public Person(String firstName, String middleName, String lastName, String email, Date birthDate) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.birthDate = birthDate;
	}
	
}
