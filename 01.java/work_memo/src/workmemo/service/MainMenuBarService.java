package workmemo.service;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import workmemo.form.PanelForm;
import workmemo.gui.panel.TelPanel;
import workmemo.gui.panel.WorkPanel;
import workmemo.gui.panel.ProjectPanel;

import java.util.Calendar;
import java.text.SimpleDateFormat;

import workmemo.util.LogUtil;
import workmemo.service.CommonService;

public class MainMenuBarService {
	CommonService commonService = new CommonService();
	
	public void menuAction(PanelForm panelForm, String title) {
		switch(title) {
			case "TELフォルダ": 
				commonService.showFileCmd(new File(".").getAbsoluteFile().getParent().replace("\\", "/") + "/export/tel/");
				break;
			case "WORKフォルダ": 
				commonService.showFileCmd(new File(".").getAbsoluteFile().getParent().replace("\\", "/") + "/export/work/");
				break;
			case "PJフォルダ": 
				commonService.showFileCmd(new File(".").getAbsoluteFile().getParent().replace("\\", "/") + "/export/project/");
				break;
			case "ログフォルダ": 
				commonService.showFileCmd(new File(".").getAbsoluteFile().getParent().replace("\\", "/") + "/log/");
				break;
			case "TEL情報出力": 
				telExport(panelForm.getTelPanel());
				break;
			case "WORK情報出力": 
				workExport(panelForm.getWorkPanel());
				break;
			case "PJ情報出力": 
				projectExport(panelForm.getProjectPanel());
				break;
			case "TELタブ曜日クリア": 
				telClear(panelForm.getTelPanel());
				break;
			case "WORKタブ曜日クリア": 
				workClear(panelForm.getWorkPanel());
				break;
			case "PJタブ進捗率/ToDoクリア": 
				projectClear(panelForm.getProjectPanel());
				break;
			case "全タブクリア":
				allClear(panelForm);
				break;
			case "有給日数カウントbat": 
				commonService.showFileCmd(new File(".").getAbsoluteFile().getParent().replace("\\", "/") + "/bat/" + commonService.getProperty("salariedCnt"));
				break;
			case "電話帳": 
				showTelFrame();
				break;
			case "週報作成":
				createWeekExcel();
				break;
			case "勤務表作成":
				createMonthExcel();
				break;
			case "休暇願作成":
				createHolidayExcel();
				break;
			case "Brainf***":	// ベタ打ち問題と、bfコンパイラの配置は再配布になるのでコメントアウト。
				commonService.showFileCmd(new File(".").getAbsoluteFile().getParent().replace("\\", "/") + "/tools/bfCoder.html");
				// commonService.showFileCmd(new File(".").getAbsoluteFile().getParent().replace("\\", "/") + "/tools/bfi.exe");
				break;
			default: 
				break;
		}
	}
	
	/**
	 * TEL情報出力
	 */
	public void telExport(TelPanel telPanel) {
		telPanel.textExport();
	}
	
	/**
	 * WORK情報出力
	 */
	public void workExport(WorkPanel workPanel) {
		workPanel.textExport();
	}
	
	/**
	 * PJ情報出力
	 */
	public void projectExport(ProjectPanel projectPanel) {
		projectPanel.textExport();
	}
	
	/**
	 * TELタブ曜日クリア
	 */
	public void telClear(TelPanel telPanel) {
		telPanel.telMonTxt.setText("");
		telPanel.telTueTxt.setText("");
		telPanel.telWedTxt.setText("");
		telPanel.telThuTxt.setText("");
		telPanel.telFriTxt.setText("");
	}
	
	/**
	 * WORKタブ曜日クリア
	 */
	public void workClear(WorkPanel workPanel) {
		workPanel.weekMonTxt.setText("");
		workPanel.weekTueTxt.setText("");
		workPanel.weekWedTxt.setText("");
		workPanel.weekThuTxt.setText("");
		workPanel.weekFriTxt.setText("");
		
		workPanel.weekMonMemoTxt.setText("");
		workPanel.weekTueMemoTxt.setText("");
		workPanel.weekWedMemoTxt.setText("");
		workPanel.weekThuMemoTxt.setText("");
		workPanel.weekFriMemoTxt.setText("");
	}
	
	/**
	 * PJタブ曜日クリア
	 */
	public void projectClear(ProjectPanel projectPanel) {
		projectPanel.pjRecordTxt.setText("0");
		
		projectPanel.pjToDo1Txt.setText("");
		projectPanel.pjToDo2Txt.setText("");
		projectPanel.pjToDo3Txt.setText("");
	}
	
	/**
	 * 全タブ曜日クリア
	 */
	public void allClear(PanelForm panelForm) {
		telClear(panelForm.getTelPanel());
		panelForm.getTelPanel().otherArea.setText("");
		
		workClear(panelForm.getWorkPanel());
		panelForm.getWorkPanel().otherArea.setText("");
		
		projectClear(panelForm.getProjectPanel());
		panelForm.getProjectPanel().otherArea.setText("");
	}
	
	/**
	 * sourceStrファイルをコピーしtargetStrで保存する
	 */
	public boolean fileCopy(String sourceStr, String targetStr) {
		
		try {
			Path sourcePath = Paths.get(sourceStr);
			Path targetPath = Paths.get(targetStr);
			Files.copy(sourcePath, targetPath);
			LogUtil.printLog("INFO", "ファイル『" + targetStr + "』を作成しました");
		} catch(IOException e) {
			LogUtil.printLog(e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 電話帳画面起動
	 */
	public void showTelFrame() {
		System.out.println("電話帳画面起動をしたいがレイアウトすら決まっていない");
		// 意訳: 詰んでる
	}
	
	/**
	 * 週報作成
	 */
	public void createWeekExcel() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -7);	// 起動日から7日前を週報開始日に設定
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		
		// 週報ファイルパス作成。他にもちらほらと見られるが、ファイル名をベタ打ちは今見るとひどい。
		String fileDir = commonService.getProperty("weekExcelDir");																				// ディレクトリ名
		String fileName = "週報(" + sdf.format(c.getTime()) + "_" + commonService.getProperty("excelUserName", "社員番号4桁_氏名") + ").xls";	// 作成ファイル名
		String formatFileName = commonService.getProperty("weekExcelFormatFileName", "週報(YYYYMM_社員番号4桁_氏名).xls");						// 作成ファイル名
		
		File file = new File(fileDir + fileName);
		if(file.exists()) {
			// ファイルが存在する場合、ファイルを開く
			commonService.showFileCmd(fileDir + fileName);
			LogUtil.printLog("INFO", "週報『" + fileName + "』を表示しました");
		} else {
			// ファイルが存在しない場合、フォーマットファイルをコピーしたのちにファイルを開く
			if(fileCopy(fileDir + formatFileName, fileDir + fileName)) {
				commonService.showFileCmd(fileDir + fileName);
				LogUtil.printLog("INFO", "週報『" + fileName + "』を表示しました");
			} else {
				// ファイル作成失敗
				LogUtil.printLog("ERROR", "週報『" + fileDir + fileName + "』の作成に失敗しました");
			}
		}
	}
	
	/**
	 * 勤務表作成
	 */
	public void createMonthExcel() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		
		// 勤務表ファイルパス作成
		String fileDir = commonService.getProperty("monthExcelDir");
		// String fileName = "勤務表(" + sdf.format(c.getTime()) + "_" + commonService.getProperty("excelUserName", "社員番号4桁_氏名") + ").xls";
		String fileName = "勤務表_" + commonService.getProperty("excelUserName", "社員番号4桁_氏名") + "_" + sdf.format(c.getTime()) + ".xls";
		String formatFileName = commonService.getProperty("monthExcelFormatFileName", "勤務表_社員番号4桁_氏名_yyyymm.xls");
		
		File file = new File(fileDir + fileName);
		if(file.exists()) {
			// ファイルが存在する場合、ファイルを開く
			commonService.showFileCmd(fileDir + fileName);
			LogUtil.printLog("INFO", "勤務表『" + fileName + "』を表示しました");
		} else {
			// ファイルが存在しない場合、フォーマットファイルをコピーしたのちにファイルを開く
			if(fileCopy(fileDir + formatFileName, fileDir + fileName)) {
				commonService.showFileCmd(fileDir + fileName);
				LogUtil.printLog("INFO", "勤務表『" + fileName + "』を表示しました");
			} else {
				// ファイル作成失敗
				LogUtil.printLog("ERROR", "勤務表『" + fileDir + fileName + "』の作成に失敗しました");
			}
		}
	}
	
	/**
	 * 休暇願作成
	 * ファイル名が不定なので仮作成でしかない
	 */
	public void createHolidayExcel() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		// 休暇願ファイルパス作成
		String fileDir = commonService.getProperty("holidayExcelDir");
		String fileName = "休暇願_" + commonService.getProperty("excelUserName", "社員番号4桁_氏名") + "_" + sdf.format(c.getTime()) + ".xls";
		String formatFileName = commonService.getProperty("holidayExcelFormatFileName", "休暇願(YYYYMMdd_社員番号4桁_氏名).xls");
		
		File file = new File(fileDir + fileName);
		if(file.exists()) {
			// ファイルが存在する場合、ファイルを開く
			commonService.showFileCmd(fileDir + fileName);
			LogUtil.printLog("INFO", "休暇願『" + fileName + "』を表示しました");
		} else {
			// ファイルが存在しない場合、フォーマットファイルをコピーしたのちにファイルを開く
			if(fileCopy(fileDir + formatFileName, fileDir + fileName)) {
				commonService.showFileCmd(fileDir + fileName);
				LogUtil.printLog("INFO", "休暇願『" + fileName + "』を表示しました");
			} else {
				// ファイル作成失敗
				LogUtil.printLog("ERROR", "休暇願『" + fileDir + fileName + "』の作成に失敗しました");
			}
		}
	}
	
}