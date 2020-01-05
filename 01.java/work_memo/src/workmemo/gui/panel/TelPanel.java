package workmemo.gui.panel;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.awt.event.FocusListener;

import workmemo.form.TelForm;
import workmemo.service.CommonService;
import workmemo.util.LogUtil;

/**
 * 電話タブ
 */
public class TelPanel extends JPanel {
	CommonService commonService = new CommonService();
	public TelForm telForm = new TelForm();
	
	public JTextArea otherArea;
	public JScrollPane scrollpane;
	public JTextField telMonTxt;
	public JTextField telTueTxt;
	public JTextField telWedTxt;
	public JTextField telThuTxt;
	public JTextField telFriTxt;
	
	public TelPanel() {
		telForm = importTelCSV();
		
		add(new JLabel("月 "));
		telMonTxt = new JTextField(telForm.getTelMon(), 22);
		add(telMonTxt);
		
		add(new JLabel("火 "));
		telTueTxt = new JTextField(telForm.getTelTue(), 22);
		add(telTueTxt);
		
		add(new JLabel("水 "));
		telWedTxt = new JTextField(telForm.getTelWed(), 22);
		add(telWedTxt);
		
		add(new JLabel("木 "));
		telThuTxt = new JTextField(telForm.getTelThu(), 22);
		add(telThuTxt);
		
		add(new JLabel("金 "));
		telFriTxt = new JTextField(telForm.getTelFri(), 22);
		add(telFriTxt);
		
		otherArea = new JTextArea(6, 24);
		// otherArea.setLineWrap(true);	// 横スクロール有
		otherArea.setText(telForm.getTelOtherText());
		scrollpane = new JScrollPane(otherArea);
		add(new JLabel("その他 "));
		add(scrollpane);
		LogUtil.printLog("INFO", "telタブインスタンス生成完了");
	}
	
	/** 
	 * tel.csvから文字列を読込み
	 * TelFormへ格納する
	 */
	public TelForm importTelCSV() {
		TelForm result = new TelForm();
		BufferedReader br = null;
		
		try {
			FileReader fr = new FileReader("csv/tel.csv");
			br = new BufferedReader(fr);
			
			String line = br.readLine();
			line = br.readLine();
			if(line != null) {
				String[] projects = line.split(",", -1);
				
				result.setTelMon(commonService.replaceCSVtext(projects[0].replaceAll("\\[0x2c\\]", ",")));
				result.setTelTue(commonService.replaceCSVtext(projects[1].replaceAll("\\[0x2c\\]", ",")));
				result.setTelWed(commonService.replaceCSVtext(projects[2].replaceAll("\\[0x2c\\]", ",")));
				result.setTelThu(commonService.replaceCSVtext(projects[3].replaceAll("\\[0x2c\\]", ",")));
				result.setTelFri(commonService.replaceCSVtext(projects[4].replaceAll("\\[0x2c\\]", ",")));
				result.setTelOtherText(projects[5].replaceAll("\\[0x2c\\]", ","));
				
				while((line = br.readLine()) != null) {
					result.setTelOtherText(result.getTelOtherText() + "\n" + line.replaceAll("\\[0x2c\\]", ","));
				}
				
				result.setTelOtherText(commonService.replaceCSVtext(result.getTelOtherText()));
				LogUtil.printLog("INFO", "csv/tel.csv情報読み込み完了");
			}
		} catch(Exception e) {
			LogUtil.printLog(e);
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch(IOException e) {
					LogUtil.printLog(e);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 入力情報からCSVを出力する
	 */
	public void exportTelCSV() {
		PrintWriter pw = null;
		
		try {
			File file = new File("csv/tel.csv");
			if(!file.canWrite()) { file.setWritable(true); }
			FileWriter fw = new FileWriter(file, false);
			pw = new PrintWriter(new BufferedWriter(fw));
			
			pw.print("telMon");
			pw.print(",");
			pw.print("telTue");
			pw.print(",");
			pw.print("telWed");
			pw.print(",");
			pw.print("telThu");
			pw.print(",");
			pw.print("telFri");
			pw.print(",");
			pw.print("telOtherText");
			pw.println();
			
			pw.print(telMonTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(telTueTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(telWedTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(telThuTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(telFriTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(otherArea.getText().replaceAll(",", "[0x2c]"));
			pw.println();
			
			pw.close();
			LogUtil.printLog("INFO", "csv/tel.csv出力完了");
		} catch(IOException e) {
			LogUtil.printLog(e);
		}
	}
	
	/**
	 * 入力情報からテキストファイルを出力する
	 */
	public void textExport() {
		List<String> textList = new ArrayList<String>();
		List<String> dateList = commonService.getDateList();
		
		textList.add(dateList.get(0) + ": " + telMonTxt.getText());
		textList.add(dateList.get(1) + ": " + telTueTxt.getText());
		textList.add(dateList.get(2) + ": " + telWedTxt.getText());
		textList.add(dateList.get(3) + ": " + telThuTxt.getText());
		textList.add(dateList.get(4) + ": " + telFriTxt.getText());
		textList.add("");
		textList.add("---");
		textList.add(otherArea.getText().replaceAll("\n", "\r\n"));
		
		try {
			commonService.textExport("tel", textList);
			LogUtil.printLog("INFO", "tel情報エクスポート完了");
		} catch(Exception e) {
			LogUtil.printLog(e);
		}
	}
}