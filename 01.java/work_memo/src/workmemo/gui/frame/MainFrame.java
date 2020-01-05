package workmemo.gui.frame;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.awt.Image;
import java.awt.BorderLayout;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;

import workmemo.form.PanelForm;
import workmemo.gui.bar.MainMenuBar;
import workmemo.service.MainFrameService;
import workmemo.util.PropertyUtil;
import workmemo.service.CommonService;
import workmemo.util.LogUtil;
import workmemo.util.TaskTrayUtil;
import workmemo.gui.panel.TelPanel;
import workmemo.gui.panel.WorkPanel;
import workmemo.gui.panel.ProjectPanel;

/**
 * ウインドウ作成と各自処理
 */
public class MainFrame extends JFrame {
	PanelForm panelForm = new PanelForm();
	CommonService commonService = new CommonService();
	MainFrameService mainFrameService = new MainFrameService();
	
	public MainFrame() {
		
		if(PropertyUtil.isLoadable()) {
			// プロパティファイル読み込み成功
			LogUtil.printLog("INFO", "プロパティファイルの読み込みに成功しました");
		} else {
			// プロパティファイル読み込み失敗
			LogUtil.printLog("ERROR", "プロパティファイルの読み込みに失敗しました。ファイル名: " + PropertyUtil.getInitFilePath());
		}
		
		try {
			// フレームアイテム設定
			panelForm.setTelPanel(new TelPanel());
			panelForm.setWorkPanel(new WorkPanel());
			panelForm.setProjectPanel(new ProjectPanel());
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);
			setBounds(10, 10, 300, 350);
			setTitle("WorkMemo");
			setIconImage(commonService.getImage(commonService.getProperty("windowIconImage")).getImage());	// システムに使用するアイコンを指定
			addWindowListener(new MyListener());
			setJMenuBar(new MainMenuBar(panelForm));
			getContentPane().add(mainFrameService.getTabbedPane(panelForm.getTelPanel(), panelForm.getWorkPanel(), panelForm.getProjectPanel()), 
									BorderLayout.CENTER);
			
			// タスクトレイ処理初期化
			TaskTrayUtil.createTray(this, commonService.getImage(commonService.getProperty("taskTrayIconImage")).getImage(), null);
			
			LogUtil.printLog("INFO", "メインフレーム表示処理完了");
			
		} catch(Exception e) {
			e.printStackTrace();
			LogUtil.printLog(e);
		}
	}
	
	public PanelForm getPanelForm() {
		return panelForm;
	}
	
	public class MyListener extends WindowAdapter {
		
		// 閉じる押下後処理
		public void windowClosing(WindowEvent evt) {
			try {
				// 入力データ保管処理
				// mainFrameService.saveTabbedPane(panelForm);
				LogUtil.printLog("INFO", "×ボタンが押されました。システムを終了します");
			} catch(Exception e) {
				LogUtil.printLog(e);
			}
		}
		
		// ウインドウ最小化時処理
		// タスクトレイ収納/タスクバー収納時に発火
		public void windowIconified(WindowEvent evt) {
			// タスクトレイに格納する
			setVisible(false);
		}
		
		// ウインドウ最小化解除時処理
		// タスクバーから再表示時にのみ発火
		public void windowDeiconified(WindowEvent evt) {
			if(!isVisible()) {
				// フレーム情報の再表示
				setVisible(true);
			}
		}
		
		public void windowClosed(WindowEvent evt) {
			// 入力データ保管処理
			// mainFrameService.saveTabbedPane(panelForm);
			LogUtil.printLog("INFO", "タスクトレイから終了を選択しました。システムを終了します");
		}
		
	}
	
}