package com.roy.security.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.roy.models.User;
import com.roy.security.Services.UserService;

@Component
public class UserValidator implements Validator {

	@Autowired
	private UserService userService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		
		ValidationUtils.rejectIfEmpty(errors, "username", "NotEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmptyPassword");
		String username = user.getUsername();
		if (!username.matches("[A-Za-z0-9א-ת\\s]+")) {
			errors.rejectValue("username", "Char.userForm.username");
			
			int count = 0;
			for (int i = 0; i < username.length(); i++) {
				if (count == 2) {
					errors.rejectValue("username", "Space.userForm.username");
					break;
				}
				if (username.charAt(i) == ' ') {
					count++;
				}
			}
		}
		
		if ( username.length() < 6  || user.getUsername().length() > 24) {
			errors.rejectValue("username", "Size.userForm.username");
		}
		
		if ( userService.findByUsername(user.getUsername()) != null) {
			errors.rejectValue("username", "Duplicate.userForm.username");
		}
		
		if ( userService.findByEmail(user.getEmail()) != null ) {
			errors.rejectValue("email", "Duplicate.userForm.email");
		}
		
		if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
			errors.rejectValue("password", "Size.userForm.password");
		}
		
		if (!user.getPasswordConfirm().equals(user.getPassword())) {
			errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
		}
	}

}
