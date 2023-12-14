package vttp2023.batch3.ssf.frontcontroller.respositories;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

public class AuthenticationRepository {

	// TODO Task 5
	// Use this class to implement CRUD operations on Redis
	@Autowired
	@Qualifier("redis")
	private RedisTemplate<String, String> template;

	public void save(String username){
		template.opsForValue().set(username, "User is disabled for 30 minutes", Duration.ofMinutes(30));
	}

	public boolean isUserDisabled(String username){
		String user = (String) template.opsForValue().get(username);
		if (user != null) {
			return true;
		} else {
			return false;
		}
	}

}
