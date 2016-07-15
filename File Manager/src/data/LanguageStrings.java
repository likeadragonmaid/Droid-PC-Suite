package data;

import java.util.Properties;

public class LanguageStrings {
	private static Properties probs;
	private String language;

	public LanguageStrings(String language) throws Exception {
		this.language = language;
		probs = new Properties();
		try {
			probs.loadFromXML(this.getClass().getResourceAsStream("/xml/" + language + ".xml"));
		} catch (Exception e) {
			Logger.writeToLog(e.getMessage());
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return probs.getProperty(key);
	}

	public String getLanguage() {
		return language;
	}
}
