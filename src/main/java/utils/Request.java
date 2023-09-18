package utils;

import core.HPlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Request {
	
	private static final String API = "";
	
	public static String getPlayerInfo(String uuid) {
		try {
			URL url = new URL("https://api.hypixel.net/player?uuid="+uuid+"&key="+API);
		    URLConnection con = url.openConnection();
		    InputStream is =con.getInputStream();
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
		    StringBuilder builder = new StringBuilder();
			String line = null;
			while ( (line = br.readLine()) != null) {
			   builder.append(line);
			   builder.append(System.getProperty("line.separator"));
			}
			return builder.toString();
		} catch (Exception e) {
			System.err.println("Hypixel api issue");
			return null;
		}
	}
	
	public static Integer getPlayerScore(Player p) {
		Integer scoreQualification = null;
		Integer scoreFinals = null;
		if (new File("./plugins/HitW/player data/"+p.getUniqueId().toString().replace("-", "")+".yml").exists()) {
			YamlConfiguration playerData = YamlConfiguration.loadConfiguration(new File("./plugins/HitW/player data/"+p.getUniqueId().toString().replace("-", "")+".yml"));
			scoreQualification = playerData.getInt("score.Q");
			scoreFinals = playerData.getInt("score.F");
		}
		if (scoreQualification == null) {
			return null;
		}
		return Math.max(scoreQualification, scoreFinals);
	}
}
