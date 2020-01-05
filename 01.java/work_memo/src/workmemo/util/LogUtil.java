package workmemo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.nio.charset.StandardCharsets;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import java.text.SimpleDateFormat;

/**
 * ログ出力用クラス
 */
public class LogUtil {
	private static String dir = "";
	private static String logFileName = "";
	
	private LogUtil() throws Exception {
	}
	
	static {
		// 起動時にログファイルがなかったら作っとく
		createLogFile();
		// 過去2週間のログのみを保持する
		refleshLogFile();
	}
	
	/**
	 * コンフィグで指定した期間日のログファイルまでを保持する
	 * それ以外は削除
	 */
	public static void refleshLogFile() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			
			// 保存期間設定（デフォルト14日）
			String periodStr = PropertyUtil.getProperty("logPeriod");
			int period = Integer.parseInt(periodStr != null ? periodStr : "14");
			
			// ログディレクトリ配下のファイルを取得
			String dirPath = new File(".").getAbsoluteFile().getParent().replace("\\", "/") + "/log/";
			File dir = new File(dirPath);
			File[] files = dir.listFiles();
			
			if(files != null) {
				for(File file : files) {
					if(!file.exists())
						continue;
					else if(file.isDirectory())
						continue;
					else if(file.isFile()) {
						// ファイル名を日付に変更
						String fileName = file.getName();
						fileName = fileName.substring(0, fileName.lastIndexOf('.'));
						
						Date logDate = sdf.parse(fileName);
						Calendar logCal = Calendar.getInstance();
						logCal.setTime(logDate);
						
						// 現在年月日を取得
						Calendar cal = Calendar.getInstance();
						
						// ログファイルと現在日を比較
						long diffTime = cal.getTimeInMillis() - logCal.getTimeInMillis();
						int diffDays = (int)(diffTime / (1000 * 60 * 60 * 24));
						
						if(diffDays > period) {
							// 差がperiod日より大きければ対象のログファイルを削除
							if(file.delete()) {
								printLog("INFO", "ログファイル " + file.getName() + " を削除しました");
							} else {
								// ファイル削除失敗
								printLog("ERROR", "ログファイル " + file.getName() + " の削除に失敗しました");
							}
						}
					}
				}
			}
			
		} catch(Exception e) {
			printLog(e);
		}
	}
	
	/**
	 * 同年月日のログファイルが存在しなければ新規作成する
	 * 保存期間はとりあえず14日を考え中
	 */
	public static void createLogFile() {
		try {
			// ファイル名設定
			dir = "log";
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			logFileName = sdf.format(c.getTime()) + ".log";
			
			File f = new File(dir + "/" + logFileName);
			
			if(!(new File(dir).exists())) {
				// ディレクトリが存在しない
				new File(dir).mkdir();
			}
			if(!f.exists()) {
				if(!f.isFile() || !f.canRead()) {
					// ファイルが存在しない
					f.createNewFile();
					printLog("INFO", "ログファイル作成");	// ログ新規作成時にのみ出力することで定時退社時間がわかるかっきてきなしくみ
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ログ情報を出力する(ERROR以外)
	 */
	public static void printLog(String logType, String msg) {
		PrintWriter pw = null;
		
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			String timestamp = sdf.format(c.getTime());
			File f = new File(dir + "/" + logFileName);
			
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f, true), StandardCharsets.UTF_8));
			pw.println(timestamp + " [" + logType + "] " + msg);
			pw.flush();
			pw.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(pw != null) {
				try {
					pw.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * ログ情報を出力する(ERROR)
	 */
	public static void printLog(Exception exception) {
		PrintWriter pw = null;
		
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			String timestamp = sdf.format(c.getTime());
			File f = new File(dir + "/" + logFileName);
			
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f, true), StandardCharsets.UTF_8));
			pw.println(timestamp + " [ERROR] " + exception);
			pw.flush();
			pw.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(pw != null) {
				try {
					pw.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}