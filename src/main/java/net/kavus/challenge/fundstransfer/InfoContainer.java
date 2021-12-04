package net.kavus.challenge.fundstransfer;

import org.springframework.http.ResponseEntity;

/**
 * To use with restfull services after a successfull opertion
 * @author yavuz
 *
 */
public class InfoContainer {
	public static final String KEY_NOTFOUND = "NotFound";

	private String key;
	private String info;
	public InfoContainer(String key, String error) {
		super();
		this.key = key;
		this.info = error;
	}

	public String getKey() {
		return key;
	}

	public String getInfo() {
		return info;
	}

	public static ResponseEntity<InfoContainer> entity(String key, String info) {
		return ResponseEntity.ok( new InfoContainer(key, info) );
	}

	public static ResponseEntity<Object> entity(String info) {
		return ResponseEntity.ok( new InfoContainer(null, info) );
	}
}
