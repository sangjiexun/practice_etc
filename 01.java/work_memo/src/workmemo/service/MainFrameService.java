package workmemo.service;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import workmemo.form.PanelForm;
import workmemo.gui.panel.TelPanel;
import workmemo.gui.panel.WorkPanel;
import workmemo.gui.panel.ProjectPanel;

public class MainFrameService {
	
	public JTabbedPane getTabbedPane(TelPanel telPanel, WorkPanel workPanel, ProjectPanel projectPanel) {
		JTabbedPane tabPane = new JTabbedPane();
		
		tabPane.addTab("TEL", telPanel);
		tabPane.addTab("WORK", workPanel);
		tabPane.addTab("PJ", projectPanel);
		
		return tabPane;
	}
	
	public void saveTabbedPane(PanelForm panelForm) {
		
		panelForm.getTelPanel().exportTelCSV();
		panelForm.getWorkPanel().exportWorkCSV();
		panelForm.getProjectPanel().exportProjectCSV();
		
	}
}