package com.dsths.kafkapoc.personservice.person;


import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RequestMapping(produces = "application/vnd.error")
public class PersonControllerAdvice {


	    @ResponseBody
	    @ExceptionHandler(PersonNotFoundException.class)
	    @ResponseStatus(HttpStatus.NOT_FOUND)
	    VndErrors peronNotFoundExceptionHandler(PersonNotFoundException ex) {
	        return new VndErrors("error", ex.getMessage());
	    }

	}