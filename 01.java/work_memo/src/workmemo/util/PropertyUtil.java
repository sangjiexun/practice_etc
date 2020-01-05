package workmemo.util;

import java.util.Properties;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

/**
 * Config情報取得用クラス
 * システム起動時に1回だけロードが行われる
 */
public class PropertyUtil {
	private static boolean isLoad = true;	// プロパティファイル読み込みフラグ(true: 成功)
	private static final String INIT_FILE_PATH = "resource/application.properties";
	private static final Properties properties;
	
	private PropertyUtil() throws Exception {
	}
	
	static {
		properties = new Properties();
		try {
			// プロパティファイル読み込み
			properties.load(Files.newBufferedReader(Paths.get(INIT_FILE_PATH), StandardCharsets.UTF_8));
		} catch(IOException e) {
			// プロパティファイル読み込み失敗
			isLoad = false;
		}
	}
	
	/**
	 * プロパティが読み込めたかを返す
	 */
	public static boolean isLoadable() {
		return isLoad;
	}
	
	/**
	 * プロパティファイル名を返す
	 */
	public static String getInitFilePath() {
		return INIT_FILE_PATH;
	}
	
	/**
	 * プロパティ値を取得する
	 * keyがない場合nullが返る
	 */
	public static String getProperty(final String key) {
		return properties.getProperty(key);
	}
}