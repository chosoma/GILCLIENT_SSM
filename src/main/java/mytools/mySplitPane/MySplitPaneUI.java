package mytools.mySplitPane;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class MySplitPaneUI extends BasicSplitPaneUI {

	public static ComponentUI createUI(JComponent x) {
		return new MySplitPaneUI();
	}

	public BasicSplitPaneDivider createDefaultDivider() {
		return new MySplitPaneDivider(this);
	}

}
