package workmemo.util;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.TrayIcon;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.SystemTray;
import java.awt.AWTException;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;

import workmemo.gui.frame.MainFrame;

/**
 * タスクトレイアイテム設定クラス
 */
public class TaskTrayUtil {
	private static MainFrame frame;
	private static TrayIcon trayIcon;
	private static PopupMenu defaultMenu;
	private static SystemTray tray = SystemTray.getSystemTray();
	
	static {
		createPopupMenu();
	}
	
	/**
	 * タスクトレイアイコン右クリック時のメニューバーを設定する
	 */
	private static void createPopupMenu() {
		
		MenuItem getTrayItem = new MenuItem("開く");
		getTrayItem.addActionListener(new ActionListener() {
			// 開くコマンド押下時処理
			public void actionPerformed(ActionEvent e) {
				// フレームを表示する
				active();
			}
		});
		
		MenuItem exitItem = new MenuItem("終了");
		exitItem.addActionListener(new ActionListener() {
			// 終了コマンド押下時処理
			public void actionPerformed(ActionEvent e) {
				// システムを終了する
				removeTrayIcon();
				TaskTrayUtil.frame.setVisible(false);
				TaskTrayUtil.frame.dispose();
				// exit関数からの終了処理はJFrame側のListernerが機能しないのでdispose関数のみで終了処理を行う
				// System.exit(0);
			}
		});
		
		defaultMenu = new PopupMenu();
		defaultMenu.add(getTrayItem);
		defaultMenu.add(exitItem);
	}
	
	/**
	 * タスクトレイにアイコンを作成する
	 */
	public static void createTray(MainFrame targetFrame) throws Exception {
		try {
			createTray(targetFrame, null, null);
		} catch(Exception e) {
			throw new Exception(e);
		}
	}
	
	/**
	 * タスクトレイにアイコンを作成する
	 */
	public static void createTray(final MainFrame targetFrame, Image image, PopupMenu menu) throws Exception {
		if(!SystemTray.isSupported()) {
			// システム・トレイがサポートされていなければ終了する
			return;
		}
		
		// タスクトレイアイコンに対応するウインドウ情報を設定
		TaskTrayUtil.frame = targetFrame;
		
		if(image == null) {
			// タスクトレイアイコンに設定するアイコンをウインドウアイコンから指定
			image = targetFrame.getIconImage();
		}
		
		if(menu == null) {
			// タスクトレイに表示するメニューバーを設定
			menu = defaultMenu;
		}
		
		// タスクトレイアイコン作成/表示
		trayIcon = new TrayIcon(image, targetFrame.getTitle(), menu);
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseListener(new MouseAdapter() {
			// タスクトレイアイコンのマウス操作イベント
			
			// クリックevent
			public void mouseClicked(MouseEvent evt) {
				if(SwingUtilities.isLeftMouseButton(evt)) {
					if(evt.getClickCount() >= 2) {
						// 左ダブルクリック時にウインドウを表示する
						active();
					}
				}
			}
		});
		
		try {
			tray.add(trayIcon);
		} catch(AWTException e) {
			e.printStackTrace();
			throw new Exception("タスクトレイアイコン作成にエラーが発生しました 内容: " + e);
		}
	}
	
	/**
	 * タスクトレイアイコンを削除する
	 */
	public static void removeTrayIcon() {
		tray.remove(trayIcon);
	}
	
	/**
	 * ウインドウを表示する
	 */
	private static void active() {
		TaskTrayUtil.frame.setExtendedState(JFrame.NORMAL);
		TaskTrayUtil.frame.setAlwaysOnTop(true);
		TaskTrayUtil.frame.setVisible(true);
		TaskTrayUtil.frame.setAlwaysOnTop(false);
	}
	
	
}