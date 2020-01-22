package org.openhds.server.constraints.impl;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.server.constraints.CheckFieldNotBlank;

public class CheckFieldNotBlankImpl implements ConstraintValidator<CheckFieldNotBlank, String> {

	public void initialize(CheckFieldNotBlank arg0) {	}

	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		if (arg0 == null) {
			return false;
		}
		
	
		if (arg0.trim().length() == 0) {
			return false;
		}	
		return true;
	}

}