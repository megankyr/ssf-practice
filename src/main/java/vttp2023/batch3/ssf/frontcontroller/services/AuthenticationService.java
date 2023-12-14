package vttp2023.batch3.ssf.frontcontroller.services;

import java.net.URI;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vttp2023.batch3.model.User;
import vttp2023.batch3.ssf.frontcontroller.respositories.AuthenticationRepository;

@Service
public class AuthenticationService {

	@Autowired
	AuthenticationRepository repo;

	String url = "https://authservice-production-e8b2.up.railway.app/api/authenticate";

	// TODO: Task 2
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write the authentication method in here
	public void authenticate(String username, String password) throws Exception {
		// sends a POST request to the authentication endpoint with the provided
		// credentials.
		// checks the response status and throws an exception for authentication
		// failures
		// the success case prints a message to the console, indicating successful
		// authentication
		User user = new User(username, password);
		RequestEntity<String> request = RequestEntity
				.post(new URI(url))
				.contentType(MediaType.APPLICATION_JSON)
				.header("Accept", MediaType.APPLICATION_JSON_VALUE)
				.body(user.toJSON().toString(), String.class);

		RestTemplate template = new RestTemplate();
		ResponseEntity<String> response = template.exchange(request, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			System.out.println("Authenticated: " + username);
		} else if (response.getStatusCode().is4xxClientError()) {
			if (response.getStatusCode().value() == 400) {
				throw new Exception("Invalid payload");
			} else if (response.getStatusCode().value() == 401) {
				throw new Exception("Incorrect username and/or password");
			} else {
				throw new Exception("Invalid login details");
			}

		}

	}

	// generates captcha for multiple login attempts
	public String generateCaptcha() {
		StringBuilder captcha = new StringBuilder();
		Random random = new Random();

		int number1 = random.nextInt(1, 50);
		int number2 = random.nextInt(1, 50);
		String[] operations = { "+", "-", "*", "/" };
		String operation = operations[random.nextInt(operations.length)];

		captcha.append(number1);
		captcha.append(" ");
		captcha.append(operation);
		captcha.append(" ");
		captcha.append(number2);

		return captcha.toString();
	}

	// provides captcha answer to check against in the controller method with the
	// user answer
	public String checkCaptcha(String captcha) {
		String[] parts = captcha.split("\\s+");
		int number1 = Integer.parseInt(parts[0]);
		int number2 = Integer.parseInt(parts[2]);
		String operation = parts[1];
		int answer;

		switch (operation) {
			case "+":
				answer = number1 + number2;
				break;
			case "-":
				answer = number1 - number2;
				break;
			case "*":
				answer = number1 * number2;
				break;
			case "/":
				answer = number1 / number2;
				break;
			// insert default to meet all scenarios
			default:
				throw new IllegalArgumentException("Invalid operation: " + operation);
		}
		return String.valueOf(answer);
	}

	// copy

	// TODO: Task 3
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to disable a user account for 30 mins
	public void disableUser(String username) {
		repo.save(username);

	}

	public boolean isUserDisabled(String username){
		return repo.isUserDisabled(username);
	}

	//copy


	// TODO: Task 5
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to check if a given user's login has been disabled
	public boolean isLocked(String username) {
		return false;
	}
}
