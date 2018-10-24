package com.dsths.kafkapoc.personservice.personevent;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.dsths.kafkapoc.personservice.person.Person;


public interface PersonEventRepository extends PagingAndSortingRepository<PersonEvent, Long>{

	List<Person> findByEventType(@Param("personEventType") PersonEventType personEventType);
}
