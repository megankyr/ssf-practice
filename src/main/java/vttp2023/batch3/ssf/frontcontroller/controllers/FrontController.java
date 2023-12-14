package vttp2023.batch3.ssf.frontcontroller.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import vttp2023.batch3.model.User;
import vttp2023.batch3.ssf.frontcontroller.services.AuthenticationService;

@Controller
@RequestMapping
public class FrontController {
	// TODO: Task 2, Task 3, Task 4, Task 6

	@Autowired
	AuthenticationService service;

	private int loginAttempts = 0;

	@GetMapping("/")
	// maps to the landing page and provides an empty user object to the model
	// which is appropriate for rendering the login form
	public String landingPage(Model model) {
		model.addAttribute("user", new User());
		return "view0";
	}

	@PostMapping(consumes = "application/x-www-form-urlencoded", produces = "text/html", path = "/login")
	// handles form submissions, validates the user object
	// uses the AuthenticationService to authenticate the user
	// handles errors, increments login attempts, and returns to view0 if
	// authentication fails
	public String loginPage(@Valid User user, BindingResult result, HttpSession session, Model model,
			@RequestParam(value = "captcha", required = false) String captcha,
			@RequestParam(value = "captchaanswer", required = false) String answer) throws Exception {

		// if validation has errors - return to view0
		if (result.hasErrors()) {
			return "view0";
		}

		// check if the user is disabled before each login attempt
		if (service.isUserDisabled(user.getUsername())) {
			model.addAttribute("username", user.getUsername());
			return "view2";
		}
		// if has captcha answer + and is correct, proceed to authenticate
		if (answer != null && answer.equals(service.checkCaptcha((String) session.getAttribute("captcha")))) {
			try {
				service.authenticate(user.getUsername(), user.getPassword());
				return "view1";
			}
			// if there's an error with authentication, return view 0 and increment login
			// if there are 3 login attempts, then disable user
			catch (Exception e) {
				loginAttempts++;
				if (loginAttempts == 3) {
					service.disableUser(user.getUsername());
					model.addAttribute("username", user.getUsername());
					return "view2";
				}
				String errorMessage;
				if (e.getMessage().contains("invalid payload")) {
					errorMessage = "Invalid payload";
				} else if (e.getMessage().contains("Incorrect username and/or password")) {
					errorMessage = "Incorrect username and/or password";
				} else {
					errorMessage = "An error occured";
				}
				model.addAttribute("exception", errorMessage);
				model.addAttribute("login attempts", loginAttempts);
				return "view0";

			}
		}
		// if has captcha answer + and is incorrect, return view 0 and increment login
		// attempt
		else if (answer != null && !answer.equals(service.checkCaptcha((String) session.getAttribute("captcha")))) {

			String newCaptcha = service.generateCaptcha();
			session.setAttribute("captcha", newCaptcha);
			model.addAttribute("captcha", newCaptcha);
			model.addAttribute("exception", "Incorrect answer, try again");
			loginAttempts++;
			return "view0";
		}

		// if there is no captcha (first login attempt), proceed to authenticate
		try {
			service.authenticate(user.getUsername(), user.getPassword());
			return "view1";
		}
		// if there's an error with authentication, return view 0 and increment login
		// attempt
		catch (Exception e) {
			loginAttempts++;
			String errorMessage;
			if (e.getMessage().contains("invalid payload")) {
				errorMessage = "Invalid payload";
			} else if (e.getMessage().contains("Incorrect username and/or password")) {
				errorMessage = "Incorrect username and/or password";
			} else {
				errorMessage = "An error occured";
			}
			model.addAttribute("exception", errorMessage);
			model.addAttribute("login attempts", loginAttempts);

			return "view0";

		}
	}

}
