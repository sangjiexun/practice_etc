package workmemo.form;

import javax.swing.JPanel;
import workmemo.gui.panel.TelPanel;
import workmemo.gui.panel.WorkPanel;
import workmemo.gui.panel.ProjectPanel;

public class PanelForm {
	private TelPanel telPanel;
	private WorkPanel workPanel;
	private ProjectPanel ProjectPanel;
	
	public TelPanel getTelPanel() {
		return telPanel;
	}
	
	public void setTelPanel(TelPanel telPanel) {
		this.telPanel = telPanel;
	}
	
	public WorkPanel getWorkPanel() {
		return workPanel;
	}
	
	public void setWorkPanel(WorkPanel workPanel) {
		this.workPanel = workPanel;
	}
	
	public ProjectPanel getProjectPanel() {
		return ProjectPanel;
	}
	
	public void setProjectPanel(ProjectPanel ProjectPanel) {
		this.ProjectPanel = ProjectPanel;
	}
}