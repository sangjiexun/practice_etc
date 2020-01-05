package workmemo.form;

/**
 * プロジェクトタブ変数
 * 2018/05/29
 **/
public class ProjectForm {
	
	/** プロジェクト進捗率（パーセント） */
	private String prjRecordPer;
	
	/** プロジェクトToDo１ */
	private String prjTodo1;
	/** プロジェクトToDo２ */
	private String prjTodo2;
	/** プロジェクトToDo３ */
	private String prjTodo3;
	
	/** その他プロジェクトメモ */
	private String prjOtherText;
	
	public void setPrjRecordPer(String prjRecordPer) {
		this.prjRecordPer = prjRecordPer;
	}
	
	public String getPrjRecordPer() {
		return prjRecordPer;
	}
	
	public void setPrjTodo1(String prjTodo1) {
		this.prjTodo1 = prjTodo1;
	}
	
	public String getPrjTodo1() {
		return prjTodo1;
	}
	
	public void setPrjTodo2(String prjTodo2) {
		this.prjTodo2 = prjTodo2;
	}
	
	public String getPrjTodo2() {
		return prjTodo2;
	}
	
	public void setPrjTodo3(String prjTodo3) {
		this.prjTodo3 = prjTodo3;
	}
	
	public String getPrjTodo3() {
		return prjTodo3;
	}
	
	public void setPrjOtherText(String prjOtherText) {
		this.prjOtherText = prjOtherText;
	}
	
	public String getPrjOtherText() {
		return prjOtherText;
	}
	
}