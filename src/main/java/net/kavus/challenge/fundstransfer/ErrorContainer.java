package net.kavus.challenge.fundstransfer;

import org.springframework.http.ResponseEntity;

/**
 * To use with restfull services when there is a usual error
 * @author yavuz
 *
 */
public class ErrorContainer {
	public static final String KEY_NOTFOUND = "NotFound";

	private String key;
	private String error;
	public ErrorContainer(String key, String error) {
		super();
		this.key = key;
		this.error = error;
	}

	public String getKey() {
		return key;
	}

	public String getError() {
		return error;
	}

	public static ResponseEntity<Object> entity(String key, String error) {
		return ResponseEntity.ok( new ErrorContainer(key, error) );
	}

	public static ResponseEntity<Object> entity(String error) {
		return ResponseEntity.ok( new ErrorContainer(null, error) );
	}
}
