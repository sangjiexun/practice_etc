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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import workmemo.form.WorkForm;
import workmemo.service.CommonService;
import workmemo.util.LogUtil;

/**
 * 日常タブ
 */
public class WorkPanel extends JPanel{
	CommonService commonService = new CommonService();
	public WorkForm workForm = new WorkForm();
	
	public JTextArea otherArea;
	public JScrollPane scrollpane;
	public JTextField weekMonTxt;
	public JTextField weekTueTxt;
	public JTextField weekWedTxt;
	public JTextField weekThuTxt;
	public JTextField weekFriTxt;
	public JTextField weekMonMemoTxt;
	public JTextField weekTueMemoTxt;
	public JTextField weekWedMemoTxt;
	public JTextField weekThuMemoTxt;
	public JTextField weekFriMemoTxt;
	
	public WorkPanel() {
		workForm = importWorkCSV();
		
		add(new JLabel("月　09:00 ～"));
		weekMonTxt = new JTextField(workForm.getQuittingMon(), 3);
		weekMonTxt.addFocusListener(new WeekTimeFocusListener(weekMonTxt));
		add(weekMonTxt);
		weekMonMemoTxt = new JTextField(workForm.getWorkMon(), 13);
		add(weekMonMemoTxt);
		
		add(new JLabel("火　09:00 ～"));
		weekTueTxt = new JTextField(workForm.getQuittingTue(), 3);
		weekTueTxt.addFocusListener(new WeekTimeFocusListener(weekTueTxt));
		add(weekTueTxt);
		weekTueMemoTxt = new JTextField(workForm.getWorkTue(), 13);
		add(weekTueMemoTxt);
		
		add(new JLabel("水　09:00 ～"));
		weekWedTxt = new JTextField(workForm.getQuittingWed(), 3);
		weekWedTxt.addFocusListener(new WeekTimeFocusListener(weekWedTxt));
		add(weekWedTxt);
		weekWedMemoTxt = new JTextField(workForm.getWorkWed(), 13);
		add(weekWedMemoTxt);
		
		add(new JLabel("木　09:00 ～"));
		weekThuTxt = new JTextField(workForm.getQuittingThu(), 3);
		weekThuTxt.addFocusListener(new WeekTimeFocusListener(weekThuTxt));
		add(weekThuTxt);
		weekThuMemoTxt = new JTextField(workForm.getWorkThu(), 13);
		add(weekThuMemoTxt);
		
		add(new JLabel("金　09:00 ～"));
		weekFriTxt = new JTextField(workForm.getQuittingFri(), 3);
		weekFriTxt.addFocusListener(new WeekTimeFocusListener(weekFriTxt));
		add(weekFriTxt);
		weekFriMemoTxt = new JTextField(workForm.getWorkFri(), 13);
		add(weekFriMemoTxt);
		
		otherArea = new JTextArea(6, 24);
		// otherArea.setLineWrap(true);	// 横スクロール有
		otherArea.setText(workForm.getWorkOtherText());
		scrollpane = new JScrollPane(otherArea);
		add(new JLabel("その他 "));
		add(scrollpane);
		LogUtil.printLog("INFO", "workタブインスタンス生成完了");
	}
	
	/** 
	 * project.csvから文字列を読込み
	 * ProjectFormへ格納する
	 */
	public WorkForm importWorkCSV() {
		WorkForm result = new WorkForm();
		BufferedReader br = null;
		
		try {
			FileReader fr = new FileReader("csv/work.csv");
			br = new BufferedReader(fr);
			
			String line = br.readLine();
			line = br.readLine();
			if(line != null) {
				String[] projects = line.split(",", -1);
				
				result.setQuittingMon(commonService.replaceCSVtext(projects[0].replaceAll("\\[0x2c\\]", ",")));
				result.setQuittingTue(commonService.replaceCSVtext(projects[1].replaceAll("\\[0x2c\\]", ",")));
				result.setQuittingWed(commonService.replaceCSVtext(projects[2].replaceAll("\\[0x2c\\]", ",")));
				result.setQuittingThu(commonService.replaceCSVtext(projects[3].replaceAll("\\[0x2c\\]", ",")));
				result.setQuittingFri(commonService.replaceCSVtext(projects[4].replaceAll("\\[0x2c\\]", ",")));
				
				result.setWorkMon(commonService.replaceCSVtext(projects[5].replaceAll("\\[0x2c\\]", ",")));
				result.setWorkTue(commonService.replaceCSVtext(projects[6].replaceAll("\\[0x2c\\]", ",")));
				result.setWorkWed(commonService.replaceCSVtext(projects[7].replaceAll("\\[0x2c\\]", ",")));
				result.setWorkThu(commonService.replaceCSVtext(projects[8].replaceAll("\\[0x2c\\]", ",")));
				result.setWorkFri(commonService.replaceCSVtext(projects[9].replaceAll("\\[0x2c\\]", ",")));
				
				result.setWorkOtherText(projects[10].replaceAll("\\[0x2c\\]", ","));
				
				while((line = br.readLine()) != null) {
					result.setWorkOtherText(result.getWorkOtherText() + "\n" + line.replaceAll("\\[0x2c\\]", ","));
				}
				
				result.setWorkOtherText(commonService.replaceCSVtext(result.getWorkOtherText()));
				
			}
			LogUtil.printLog("INFO", "csv/work.csv情報読み込み完了");
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
	public void exportWorkCSV() {
		PrintWriter pw = null;
		
		try {
			File file = new File("csv/work.csv");
			if(!file.canWrite()) { file.setWritable(true); }
			FileWriter fw = new FileWriter(file, false);
			pw = new PrintWriter(new BufferedWriter(fw));
			
			pw.print("quittingMon");
			pw.print(",");
			pw.print("quittingTue");
			pw.print(",");
			pw.print("quittingWed");
			pw.print(",");
			pw.print("quittingThu");
			pw.print(",");
			pw.print("quittingFri");
			pw.print(",");
			
			pw.print("workMon");
			pw.print(",");
			pw.print("workTue");
			pw.print(",");
			pw.print("workWed");
			pw.print(",");
			pw.print("workThu");
			pw.print(",");
			pw.print("workFri");
			pw.print(",");
			
			pw.print("workOtherText");
			pw.println();
			
			pw.print(weekMonTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(weekTueTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(weekWedTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(weekThuTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(weekFriTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			
			pw.print(weekMonMemoTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(weekTueMemoTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(weekWedMemoTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(weekThuMemoTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(weekFriMemoTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			
			pw.print(otherArea.getText().replaceAll(",", "[0x2c]"));
			pw.println();
			
			pw.close();
			LogUtil.printLog("INFO", "csv/work.csv出力完了");
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
		
		textList.add(dateList.get(0) + " 09:00～" + commonService.insertText(commonService.timeZeroFormat(weekMonTxt.getText()), 2, ":") + "(実働 " + calcWorkingHours(weekMonTxt.getText()) + " 時間)");
		textList.add("　" + weekMonMemoTxt.getText());
		textList.add(dateList.get(1) + " 09:00～" + commonService.insertText(commonService.timeZeroFormat(weekTueTxt.getText()), 2, ":") + "(実働 " + calcWorkingHours(weekTueTxt.getText()) + " 時間)");
		textList.add("　" + weekTueMemoTxt.getText());
		textList.add(dateList.get(2) + " 09:00～" + commonService.insertText(commonService.timeZeroFormat(weekWedTxt.getText()), 2, ":") + "(実働 " + calcWorkingHours(weekWedTxt.getText()) + " 時間)");
		textList.add("　" + weekWedMemoTxt.getText());
		textList.add(dateList.get(3) + " 09:00～" + commonService.insertText(commonService.timeZeroFormat(weekThuTxt.getText()), 2, ":") + "(実働 " + calcWorkingHours(weekThuTxt.getText()) + " 時間)");
		textList.add("　" + weekThuMemoTxt.getText());
		textList.add(dateList.get(4) + " 09:00～" + commonService.insertText(commonService.timeZeroFormat(weekFriTxt.getText()), 2, ":") + "(実働 " + calcWorkingHours(weekFriTxt.getText()) + " 時間)");
		textList.add("　" + weekFriMemoTxt.getText());
		textList.add("");
		textList.add("---");
		textList.add(otherArea.getText().replaceAll("\n", "\r\n"));
		
		try {
			commonService.textExport("work", textList);
			LogUtil.printLog("INFO", "work情報エクスポート完了");
		} catch(Exception e) {
			LogUtil.printLog(e);
		}
	}
	
	/**
	 * 帰社時間から実働時間を判定する
	 */
	public double calcWorkingHours(String endTimeStr) {
		double workingHours = 7.5;
		
		if(endTimeStr != null || endTimeStr.length() > 0) {
			// double型で出社/帰社時刻を設定
			double startTime = 9.;		// 9時～
			double endTime = Double.parseDouble(commonService.replaceZenkakuToHankaku(endTimeStr));
			
			// 分換算 - 休憩60分
			if(startTime > endTime) {
				workingHours = 0;
			} else {
				// 15分刻みで計算
				double endMin = endTime % 100;
				
				endTime = (endTime - endMin) / 100;
				endTime += (endMin - (endMin % 15)) / 60;
				
				workingHours = endTime - startTime;
			}
			
			if(workingHours >= 8.5) {
				workingHours -= 1;
			}
		}
		
		return workingHours;
	}
	
	/** 
	 * 時刻入力時Listener
	 */
	public class WeekTimeFocusListener implements FocusListener {
		JTextField jTextField = null;
		
		WeekTimeFocusListener(JTextField jTextField) {
			this.jTextField = jTextField;
		}
		
		public void focusGained(FocusEvent evt) {
			/* フォーカスが当たった時の処理 */
		}
		
		public void focusLost(FocusEvent evt) {
			/* フォーカスが外れた時の処理 */
			if(jTextField.getText().length() > 4) {
				jTextField.setText(jTextField.getText().substring(0, 4));
			}
			jTextField.setText(commonService.timeZeroFormat(jTextField.getText()));
		}
		
	}
}