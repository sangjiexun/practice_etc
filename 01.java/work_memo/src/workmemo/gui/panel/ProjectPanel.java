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

import workmemo.form.ProjectForm;
import workmemo.service.CommonService;
import workmemo.util.LogUtil;

/**
 * PJタブ
 */
public class ProjectPanel extends JPanel {
	CommonService commonService = new CommonService();
	public ProjectForm projectForm = new ProjectForm();
	
	public JTextArea otherArea;
	public JScrollPane scrollpane;
	public JPanel recordPnl;
	public JTextField pjRecordTxt;
	public JTextField pjToDo1Txt;
	public JTextField pjToDo2Txt;
	public JTextField pjToDo3Txt;
	
	public ProjectPanel() {
		projectForm = importProjectCSV();
		
		recordPnl = new JPanel();
		recordPnl.setPreferredSize(new Dimension(280, 45));
		recordPnl.add(new JLabel("進捗　"));
		pjRecordTxt = new JTextField(projectForm.getPrjRecordPer(), 2);
		recordPnl.add(pjRecordTxt);
		recordPnl.add(new JLabel(" / 100%"));
		add(recordPnl);
		
		add(new JLabel("ToDo "));
		pjToDo1Txt = new JTextField(projectForm.getPrjTodo1(), 20);
		add(pjToDo1Txt);
		
		add(new JLabel("ToDo "));
		pjToDo2Txt = new JTextField(projectForm.getPrjTodo2(), 20);
		add(pjToDo2Txt);
		
		add(new JLabel("ToDo "));
		pjToDo3Txt = new JTextField(projectForm.getPrjTodo3(), 20);
		add(pjToDo3Txt);
		
		otherArea = new JTextArea(6, 24);
		// otherArea.setLineWrap(true);	// 横スクロール有
		otherArea.setText(projectForm.getPrjOtherText());
		scrollpane = new JScrollPane(otherArea);
		add(new JLabel("その他 "));
		add(scrollpane);
		LogUtil.printLog("INFO", "projectタブインスタンス生成完了");
	}
	
	/** 
	 * project.csvから文字列を読込み
	 * ProjectFormへ格納する
	 */
	public ProjectForm importProjectCSV() {
		
		ProjectForm result = new ProjectForm();
		BufferedReader br = null;
		
		try {
			FileReader fr = new FileReader("csv/project.csv");
			br = new BufferedReader(fr);
			
			String line = br.readLine();
			line = br.readLine();
			if(line != null) {
				String[] projects = line.split(",", -1);
				
				result.setPrjRecordPer(commonService.replaceCSVtext(projects[0].replaceAll("\\[0x2c\\]", ",")));
				result.setPrjTodo1(commonService.replaceCSVtext(projects[1].replaceAll("\\[0x2c\\]", ",")));
				result.setPrjTodo2(commonService.replaceCSVtext(projects[2].replaceAll("\\[0x2c\\]", ",")));
				result.setPrjTodo3(commonService.replaceCSVtext(projects[3].replaceAll("\\[0x2c\\]", ",")));
				result.setPrjOtherText(projects[4].replaceAll("\\[0x2c\\]", ","));
				
				while((line = br.readLine()) != null) {
					result.setPrjOtherText(result.getPrjOtherText() + "\n" + line.replaceAll("\\[0x2c\\]", ","));
				}
				
				result.setPrjOtherText(commonService.replaceCSVtext(result.getPrjOtherText()));
				
			}
			LogUtil.printLog("INFO", "csv/project.csv情報読み込み完了");
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
	public void exportProjectCSV() {
		PrintWriter pw = null;
		
		try {
			File file = new File("csv/project.csv");
			if(!file.canWrite()) { file.setWritable(true); }
			FileWriter fw = new FileWriter(file, false);
			pw = new PrintWriter(new BufferedWriter(fw));
			
			pw.print("prjRecordPer");
			pw.print(",");
			pw.print("prjTodo1");
			pw.print(",");
			pw.print("prjTodo2");
			pw.print(",");
			pw.print("prjTodo3");
			pw.print(",");
			pw.print("prjOtherText");
			pw.println();
			
			pw.print(pjRecordTxt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(pjToDo1Txt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(pjToDo2Txt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(pjToDo3Txt.getText().replaceAll(",", "[0x2c]"));
			pw.print(",");
			pw.print(otherArea.getText().replaceAll(",", "[0x2c]"));
			pw.println();
			
			pw.close();
			LogUtil.printLog("INFO", "csv/project.csv出力完了");
		} catch(IOException e) {
			LogUtil.printLog(e);
		}
	}
	
	/**
	 * 入力情報からテキストファイルを出力する
	 */
	public void textExport() {
		List<String> textList = new ArrayList<String>();
		
		textList.add("プロジェクト進捗率 " + pjRecordTxt.getText() + " / 100%");
		textList.add("");
		textList.add("ToDo1: " + pjToDo1Txt.getText());
		textList.add("ToDo2: " + pjToDo2Txt.getText());
		textList.add("ToDo3: " + pjToDo3Txt.getText());
		textList.add("");
		textList.add("---");
		textList.add(otherArea.getText().replaceAll("\n", "\r\n"));
		
		try {
			commonService.textExport("project", textList);
			LogUtil.printLog("INFO", "project情報エクスポート完了");
		} catch(Exception e) {
			LogUtil.printLog(e);
		}
	}
}