package vttp2023.batch3.ssf.frontcontroller.services;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vttp2023.batch3.model.User;

@Service
public class AuthenticationService {

	String url = "https://authservice-production-e8b2.up.railway.app/api/authenticate";

	// TODO: Task 2
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write the authentication method in here
	public void authenticate(String username, String password) throws Exception {
		// sends a POST request to the authentication endpoint with the provided credentials.
		// checks the response status and throws an exception for authentication failures
		// the success case prints a message to the console, indicating successful authentication
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
			throw new Exception("Invalid login details");
		}

	}

	// TODO: Task 3
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to disable a user account for 30 mins
	public void disableUser(String username) {
	}

	// TODO: Task 5
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to check if a given user's login has been disabled
	public boolean isLocked(String username) {
		return false;
	}
}
