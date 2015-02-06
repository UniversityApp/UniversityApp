package sk.branislavremen.universityapp;

import com.parse.Parse;

import android.app.Application;

public class InitApplication extends Application {

	String APPLICATION_ID = "QAXTQDnjlfBVpNvl67dJI4j5hJgW9m9P4YKy1T7y";
	String CLIENT_KEY = "ZF2NbAEnM6gLKSncS2MaGcz6fZ70iCR6DHOK46ZS";
	
	@Override
	public void onCreate() {
	    super.onCreate();
	 
	    Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
	 
	}
	
}
