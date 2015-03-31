package sk.branislavremen.universityapp;

import sk.branislavremen.universityapp.vo.MessageData;
import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class InitApplication extends Application {

	String APPLICATION_ID = "QAXTQDnjlfBVpNvl67dJI4j5hJgW9m9P4YKy1T7y";
	String CLIENT_KEY = "ZF2NbAEnM6gLKSncS2MaGcz6fZ70iCR6DHOK46ZS";
	
	@Override
	public void onCreate() {
	    super.onCreate();
	    ParseObject.registerSubclass(MessageData.class);
	    Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
	}
}
