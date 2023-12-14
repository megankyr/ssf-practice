package vttp2023.batch3.ssf.frontcontroller.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	// handles errors, increments login attempts, and returns to view0 if authentication fails
	public String loginPage(@Valid User user, BindingResult result, HttpSession session, Model model) {
		if (result.hasErrors()) {
			return "view0";
		}

		try {
			service.authenticate(user.getUsername(), user.getPassword());
			return "view1";
		} catch (Exception e) {
			loginAttempts++;
			model.addAttribute("exception", "Invalid login details");
			model.addAttribute("login attempts", loginAttempts);
			return "view0";

		}
	}

}
