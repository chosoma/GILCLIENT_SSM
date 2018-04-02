package mytools.menu;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Popup;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;

public class MyPopupMenuUI extends BasicPopupMenuUI {
	public static ComponentUI createUI(JComponent c) {
		return new MyPopupMenuUI();
	}

	public void installUI(JComponent c) {
		super.installUI(c);
		// popupMenu.setOpaque(false);
		popupMenu.setBackground(Color.WHITE);
	}

	public Popup getPopup(JPopupMenu popup, int x, int y) {
		Popup pp = super.getPopup(popup, x, y);
		JPanel panel = (JPanel) popup.getParent();
//		panel.setOpaque(false);
		panel.setBackground(Color.WHITE);
		return pp;
	}
}
