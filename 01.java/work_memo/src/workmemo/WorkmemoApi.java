package workmemo;

import java.util.Calendar;

import workmemo.gui.frame.MainFrame;
import workmemo.service.CommonService;
import workmemo.service.MainMenuBarService;
import workmemo.service.MainFrameService;
import workmemo.util.LogUtil;

public class WorkmemoApi {
	static CommonService commonService = new CommonService();
	static MainMenuBarService mainMenuBarService = new MainMenuBarService();
	static MainFrameService mainFrameService = new MainFrameService();
	
	public static void main(String[] args) {
		// 開始時にport指定実行をして二重起動防止させようとしたが
		// 挙動がわからず断念中。。
		// if(commonService.isSetSocket()) {
			// MainFrame frame = new MainFrame();
			// frame.setVisible(true);
		// }
		
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
		
		Runtime.getRuntime().addShutdownHook(new Thread( () -> {
			mainFrameService.saveTabbedPane(frame.getPanelForm());
			LogUtil.printLog("INFO", "アプリケーションを終了します。");
		}));
		
		// 週報作成・送付
		if(isWeekExcel()) {
			mainMenuBarService.createWeekExcel();
		}
		
		// 勤務表作成・送付
		if(isMonthExcel()) {
			mainMenuBarService.createMonthExcel();
		}
		
	}
	
	/**
	 * 月曜日ならtrue, それ以外ならfalse
	 * ※固定判定中のため他に流用するには要検討/対応
	 */
	static boolean isWeekExcel() {
		Calendar c = Calendar.getInstance();
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		
		if(dayOfWeek == Calendar.MONDAY) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 毎月21日、または土日じゅうに21日が存在する場合はtrue
	 * それ以外ならfalse
	 * ※固定判定中のため他に流用するには要検討/対応
	 * ※現在土日じゅうに21日が存在する場合の判定がうまくいっていないので直すべき
	 */
	static boolean isMonthExcel() {
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DATE);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		
		if(day == 21) {
			return true;
		} else if(day > 20) {
			if(dayOfWeek == Calendar.MONDAY) {
				if(day - 3 < 21) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}