package workmemo.gui.bar;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import workmemo.form.PanelForm;
import workmemo.service.MainMenuBarService;
import workmemo.util.LogUtil;
import workmemo.gui.panel.TelPanel;
import workmemo.gui.panel.WorkPanel;
import workmemo.gui.panel.ProjectPanel;

public class MainMenuBar extends JMenuBar implements ActionListener {
	MainMenuBarService mainMenuBarService = null;
	
	JMenu fileMenu;
	JMenuItem mTelDir;
	JMenuItem mWorkDir;
	JMenuItem mPjDir;
	JMenuItem mLogDir;
	
	JMenu toolMenu;
	JMenu mFileExport;
	JMenu mClear;
	// JMenu mBatList;
	JMenuItem bf;
	JMenuItem mTelList;
	
	JMenu excelMenu;
	JMenuItem weekExcel;
	JMenuItem monthExcel;
	JMenuItem holidayExcel;
	JMenuItem otherExcel;
	
	PanelForm panelForm;
	
	public MainMenuBar(PanelForm panelForm) {
		mainMenuBarService = new MainMenuBarService();
		this.panelForm = panelForm;
		
		// ファイルメニュー
		mTelDir = createJMenuItem("TELフォルダ");
		mWorkDir = createJMenuItem("WORKフォルダ");
		mPjDir = createJMenuItem("PJフォルダ");
		mLogDir = createJMenuItem("ログフォルダ");
		
		// Brainf***
		bf = createJMenuItem("Brainf***");
		
		// エクスポートメニュー
		mFileExport = createJMenu("エクスポート");
		mFileExport.add(createJMenuItem("TEL情報出力"));
		mFileExport.add(createJMenuItem("WORK情報出力"));
		mFileExport.add(createJMenuItem("PJ情報出力"));
		
		// クリアメニュー
		mClear = createJMenu("クリア");
		mClear.add(createJMenuItem("TELタブ曜日クリア"));
		mClear.add(createJMenuItem("WORKタブ曜日クリア"));
		mClear.add(createJMenuItem("PJタブ進捗率/ToDoクリア"));
		mClear.add(createJMenuItem("全タブクリア"));
		
		// 外部bat起動メニュー（作ったけどいらなくなってるので除外）
		// mBatList = createJMenu("外部bat実行");
		// mBatList.add(createJMenuItem("有給日数カウントbat"));
		
		// 電話帳（検討中の為コメントアウトです）
		// mTelList = createJMenuItem("電話帳");
		
		// 週報
		weekExcel = createJMenuItem("週報作成");
		
		// 勤務表
		monthExcel = createJMenuItem("勤務表作成");
		
		// 休暇願
		holidayExcel = createJMenuItem("休暇願作成");
		
		// その他提出物メニュー
		otherExcel = createJMenu("その他提出物");
		
		fileMenu = createJMenu("ファイル");
		add(fileMenu);
		fileMenu.add(mTelDir);
		fileMenu.add(mWorkDir);
		fileMenu.add(mPjDir);
		fileMenu.add(mLogDir);
		
		toolMenu = createJMenu("機能");
		add(toolMenu);
		// toolMenu.add(mBatList);
		toolMenu.add(bf);
		toolMenu.add(mFileExport);
		toolMenu.add(mClear);
		// toolMenu.add(mTelList);
		
		excelMenu = createJMenu("提出物");
		add(excelMenu);
		excelMenu.add(weekExcel);
		excelMenu.add(monthExcel);
		excelMenu.add(holidayExcel);
		excelMenu.add(otherExcel);
		
		LogUtil.printLog("INFO", "メニューバーインスタンス生成完了");
	}
	
	public void actionPerformed(ActionEvent e) {
		
		// メニュー押下時処理
		mainMenuBarService.menuAction(panelForm, e.getActionCommand());
	}
	
	public JMenu createJMenu(String title) {
		JMenu jMenu = new JMenu(title);
		jMenu.setFont(new Font("MS Gothic", Font.PLAIN, 12));
		
		return jMenu;
	}
	
	public JMenuItem createJMenuItem(String title) {
		JMenuItem jMenuItem = new JMenuItem(title);
		jMenuItem.setFont(new Font("MS Gothic", Font.PLAIN, 12));
		jMenuItem.addActionListener(this);
		
		return jMenuItem;
	}
}