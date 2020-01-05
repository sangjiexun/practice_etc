package workmemo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import java.text.SimpleDateFormat;

import java.lang.StringIndexOutOfBoundsException;
import java.lang.ClassLoader;
import java.lang.Throwable;
import java.lang.Runtime;

import workmemo.util.PropertyUtil;
import workmemo.util.LogUtil;

import java.net.URL;
import java.net.ServerSocket;

public class CommonService {
	
	/**
	 * 二重起動防止チェッククラス
	 * 挙動があやしいのでチェック停止
	 */
	// public boolean isSetSocket() {
		// ServerSocket serverSocket = null;
		// int port = Integer.parseInt(getProperty("port", "50047"));
		
		// try {
			// // ソケットを指定できれば単一起動
			// serverSocket = new ServerSocket(port);
		// } catch(IOException e) {
			// // 例外が発生すれば多重起動
			// LogUtil.printLog("ERROR", "ポート" + port + "は既に使用されています。エラー内容: " + e);
			// return false;
		// }
		
		// LogUtil.printLog("INFO", "ポート設定が完了しました。ポート番号: " + port);
		// return true;
	// }
	
	/**
	 * プロパティユーティリティクラスからプロパティ情報を取得する
	 */
	public String getProperty(String key) {
		return getProperty(key, "");
	}
	
	/**
	 * プロパティユーティリティクラスからプロパティ情報を取得する
	 * 存在しない場合ログを出力し、defaultValueを返す
	 */
	public String getProperty(String key, String defaultValue) {
		String value = PropertyUtil.getProperty(key);
		if(value == null) {
			// プロパティの読み込みエラーか、keyが存在しない
			LogUtil.printLog("WARN", "プロパティ値が存在しません key: " + key);
			value = defaultValue;
		}
		
		return value;
	}
	
	/** 
	 * 取得したCSV情報を表示用文字列に変換する
	 */
	public String replaceCSVtext(String text) {
		
		if(text.startsWith("\"") && text.endsWith("\"")) {
			text = text.substring(1, text.length() - 1);
		}
		
		return text;
	}
	
	/**
	 * テキスト出力（各種タブ内容）
	 */
	public void textExport(String exportTab, List<String> textList) throws Exception {
		PrintWriter pw = null;
		
		try {
			// ファイル名設定
			String dir = "export";
			Calendar c = Calendar.getInstance();
			String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(c.getTime()) + ".txt";
			
			File expFile = new File(dir + "/" + exportTab + "/" + fileName);
			
			if(!(new File(dir).exists())) {
				new File(dir).mkdir();
			}
			if(!(new File(dir + "/" + exportTab).exists())) {
				new File(dir + "/" + exportTab).mkdir();
			}
			if(!isReadFile(expFile)) {
				expFile.createNewFile();
			}
			
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(expFile, true), StandardCharsets.UTF_8));
			
			for(String text : textList) {
				pw.println(text);
				pw.flush();
			}
			pw.flush();
			
			pw.close();
			
		} catch(Exception e) {
			throw new Exception(exportTab + "ファイルのエクスポートに失敗しました: " + e);
		} finally {
			if(pw != null) {
				try{
					pw.close();
				} catch(Exception e) {
					throw new Exception(e);
				}
			}
		}
	}
	
	/** 
	 * ファイルが存在すればtrue, 存在しなければfalseを返す
	 */
	public boolean isReadFile(File file) {
		if(file.exists()) {
			if(file.isFile() && file.canRead()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 時刻文字列をhhMM形式で返す
	 */
	public String timeZeroFormat(String str) {
		// 数字判定
		String regex = "^[0-9０-９]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		
		if(!m.find()) {
			return str;
		}
		
		str = replaceZenkakuToHankaku(str);
		
		return String.format("%04d", Integer.parseInt(str));
	}
	
	/**
	 * 全角整数から半角整数に置き換える
	 */
	public String replaceZenkakuToHankaku(String str) {
		// 数字判定
		String regex = "^[0-9０-９]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if(!m.find()) {
			return "0";
		}
		
		// 全角 -> 半角
		StringBuffer sb = new StringBuffer(str);
		for(int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if('０' <= c && c <= '９') {
				sb.setCharAt(i, (char)(c - '０' + '0'));
			}
		}
		
		return new String(sb);
	}
	
	/**
	 * 文字列 str の i 文字目に 文字列 insertStr を挿入する
	 */
	public String insertText(String str, int i, String insertStr) {
		StringBuilder sb = new StringBuilder(str);
		
		if(str.length() > i) {
			try {
				sb.insert(i, insertStr);
			} catch(StringIndexOutOfBoundsException e) {
				e.printStackTrace();
			} catch(Throwable e) {
				e.printStackTrace();
			}
		}
		
		return new String(sb);
	}
	
	public List<String> getDateList() {
		List<String> dateList = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd(E)");
		
		Calendar c = Calendar.getInstance();
		
		// 月曜日を設定する
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek < 2) {
			// 日曜日に宣言した場合、先週の月曜から呼び出し
			c.add(Calendar.DAY_OF_MONTH, -6);
		} else if(dayOfWeek > 2) {
			// 火～土曜日に宣言した場合、今週の月曜から呼び出し
			c.add(Calendar.DAY_OF_MONTH, 2 - dayOfWeek);
		} else {
			// 月曜日に宣言
		}
		
		for(int i = 0; i < 5; i++) {
			String str = sdf.format(c.getTime());
			dateList.add(str);
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return dateList;
	}
	
	/**
	 * Resource文字列からImageIconクラスを作成
	 */
	public ImageIcon getImage(String resource) {
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL url = classLoader.getResource(resource);
		
		return new ImageIcon(url);
	}
	
	/**
	 * コマンド実行
	 * 引数filePath: 実行ファイル名
	 */
	public void showFileCmd(String filePath) {
		String[] commands = {"cmd", "/c", "start " + filePath};
		
		Runtime r = Runtime.getRuntime();
		try {
			r.exec(commands);
			
			LogUtil.printLog("INFO", "コマンドを実行しました: " + String.join(" ", commands));
		} catch(IOException e) {
			LogUtil.printLog(e);
		}
	}
}