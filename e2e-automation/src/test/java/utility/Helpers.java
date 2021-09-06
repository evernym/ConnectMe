package test.java.utility;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.Platform;
import test.java.utility.Config;
import java.util.*;

public class Helpers {

	public static String randomString() {
		int leftLimit = 97;
		int rightLimit = 122;
		int targetStringLength = 10;
		Random random = new Random();

		return random.ints(leftLimit, rightLimit + 1)
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
	}

	public static String UUID4() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static List<String> oneAttributes() {
		return Arrays.asList("age");
	}

	public static List<String> twoAttributes() {
		return Arrays.asList("Label", "Attachment_link");
	}

	public static List<String> fourAttributes() {
		return Arrays.asList("FirstName", "LastName", "Years", "Status");
	}

	public static List<String> nAttributes(int n) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < n; i++)
			list.add(randomString());
		return list;
	}

	public static List<String> nAttributesNew(int n) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < n; i++)
			list.add("Attribute " + i);
		return list;
	}

	public static List<String> allAttachmentsAttributes() {
//		return Arrays.asList("Label", "Photo_link", "PDF_link", "DOCX_link");
		return Arrays.asList("Photo_link", "PDF_link", "DOCX_link", "CSV_link");
	}

	public static Platform getPlatformType(){
        String deviceType = Config.Device_Type;
        if (deviceType.equals("iOS") || deviceType.equals("iOSSimulator") || deviceType.equals("awsiOS"))
            return Platform.IOS;
        else if(deviceType.equals("android") || deviceType.equals("awsAndroid"))
            return Platform.ANDROID;
        else throw new NotImplementedException("Platform " + deviceType + " is not supported");
    }
}
