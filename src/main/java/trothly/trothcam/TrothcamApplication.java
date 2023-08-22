package trothly.trothcam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrothcamApplication {

	// S3
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}


	public static void main(String[] args) {
		SpringApplication.run(TrothcamApplication.class, args);
	}

}
