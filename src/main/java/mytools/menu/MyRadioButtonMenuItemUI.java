package mytools.menu;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.plaf.ComponentUI;

public class MyRadioButtonMenuItemUI extends MyMenuItemUI {
	public static ComponentUI createUI(JComponent b) {
		return new MyRadioButtonMenuItemUI();
	}

	protected String getPropertyPrefix() {
		return "RadioButtonMenuItem";
	}

	public void processMouseEvent(JMenuItem item, MouseEvent e,
			MenuElement path[], MenuSelectionManager manager) {
		Point p = e.getPoint();
		if (p.x >= 0 && p.x < item.getWidth() && p.y >= 0
				&& p.y < item.getHeight()) {
			if (e.getID() == MouseEvent.MOUSE_RELEASED) {
				manager.clearSelectedPath();
				item.doClick(0);
				item.setArmed(false);
			} else
				manager.setSelectedPath(path);
		} else if (item.getModel().isArmed()) {
			MenuElement newPath[] = new MenuElement[path.length - 1];
			int i, c;
			for (i = 0, c = path.length - 1; i < c; i++)
				newPath[i] = path[i];
			manager.setSelectedPath(newPath);
		}
	}

}
