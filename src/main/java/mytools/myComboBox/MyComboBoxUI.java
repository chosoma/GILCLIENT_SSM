package mytools.myComboBox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;

import sun.swing.DefaultLookup;

public class MyComboBoxUI extends BasicComboBoxUI {
	private Insets padding;

	public static ComponentUI createUI(JComponent c) {
		return new MyComboBoxUI();
	}

	/**
	 * If necessary paints the currently selected item.
	 * 
	 *            Graphics to paint to
	 *            Region to paint current value to
	 *            whether or not the JComboBox has focus
	 * @throws NullPointerException
	 *             if any of the arguments are null.
	 * @since 1.5
	 */
	protected void installDefaults() {
		super.installDefaults();
		padding = UIManager.getInsets("ComboBox.padding");
	}

	public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
		// This is really only called if we're using ocean.
		bounds.x += 2;
		bounds.width -= 3;
		if (arrowButton != null) {
			Insets buttonInsets = arrowButton.getInsets();
			bounds.y += buttonInsets.top;
			bounds.height -= (buttonInsets.top + buttonInsets.bottom);
		} else {
			bounds.y += 2;
			bounds.height -= 4;
		}

		ListCellRenderer renderer = comboBox.getRenderer();
		Component c = renderer.getListCellRendererComponent(listBox,
				comboBox.getSelectedItem(), -1, true, false);
		c.setFont(comboBox.getFont());
		if (comboBox.isEnabled()) {
			c.setForeground(comboBox.getForeground());
			c.setBackground(comboBox.getBackground());
		} else {
			c.setForeground(DefaultLookup.getColor(comboBox, this,
					"ComboBox.disabledForeground", null));
			c.setBackground(DefaultLookup.getColor(comboBox, this,
					"ComboBox.disabledBackground", null));
		}

		// Fix for 4238829: should lay out the JPanel.
		boolean shouldValidate = false;
		if (c instanceof JPanel) {
			shouldValidate = true;
		}

		int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
		if (padding != null) {
			x = bounds.x + padding.left;
			y = bounds.y + padding.top;
			w = bounds.width - (padding.left + padding.right);
			h = bounds.height - (padding.top + padding.bottom);
		}

		currentValuePane.paintComponent(g, c, comboBox, x, y, w, h,
				shouldValidate);

	}

	/**
	 * If necessary paints the background of the currently selected item.
	 * 
	 * @param g
	 *            Graphics to paint to
	 * @param bounds
	 *            Region to paint background to
	 * @param hasFocus
	 *            whether or not the JComboBox has focus
	 * @throws NullPointerException
	 *             if any of the arguments are null.
	 * @since 1.5
	 */
	public void paintCurrentValueBackground(Graphics g, Rectangle bounds,
			boolean hasFocus) {
		Color borderColor, background;
		if (comboBox.isEnabled()) {
			borderColor = ComboBoxUtil.Border_Color;
			background = UIManager.getColor("ComboBox.background");
		} else {
			borderColor = UIManager.getColor("ComboBox.disabledForeground");
			background = UIManager.getColor("ComboBox.disabledBackground");
		}
		g.setColor(borderColor);
		g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height - 1);
		g.setColor(background);
		Insets buttonInsets = arrowButton.getInsets();
		if (buttonInsets.top > 2) {
			g.fillRect(bounds.x + 2, bounds.y + 2, bounds.width - 3,
					buttonInsets.top - 2);
		}
		if (buttonInsets.bottom > 2) {
			g.fillRect(bounds.x + 2, bounds.y + bounds.height
					- buttonInsets.bottom, bounds.width - 3,
					buttonInsets.bottom - 2);
		}

	}

	/**
	 * Returns the baseline.
	 * 
	 * @throws NullPointerException
	 *             {@inheritDoc}
	 * @throws IllegalArgumentException
	 *             {@inheritDoc}
	 * @see JComponent#getBaseline(int, int)
	 * @since 1.6
	 */
	public int getBaseline(JComponent c, int width, int height) {
		int baseline;
		height -= 4;
		baseline = super.getBaseline(c, width, height);
		if (baseline >= 0) {
			baseline += 2;
		}
		return baseline;
	}

	protected ComboBoxEditor createEditor() {
		return new MyComboBoxEditor.UIResource();
	}

	protected ComboPopup createPopup() {
		return super.createPopup();
	}

	protected JButton createArrowButton() {
		JButton button = new MyComboBoxButton(comboBox, new MyComboBoxIcon(),
				true, currentValuePane, listBox);
		button.setMargin(new Insets(0, 1, 1, 3));
		updateButton(button);
		return button;
	}

	/**
	 * Resets the necessary state on the ComboBoxButton for ocean.
	 */
	private void updateButton(JButton button) {
		button.setFocusPainted(comboBox.isEditable());
	}

	public PropertyChangeListener createPropertyChangeListener() {
		return new MyPropertyChangeListener();
	}

	/**
	 * This inner class is marked &quot;public&quot; due to a compiler bug. This
	 * class should be treated as a &quot;protected&quot; inner class.
	 * Instantiate it only within subclasses of <FooUI>.
	 */
	public class MyPropertyChangeListener extends
			PropertyChangeHandler {
		public void propertyChange(PropertyChangeEvent e) {
			super.propertyChange(e);
			String propertyName = e.getPropertyName();

			if (propertyName == "editable") {
				if (arrowButton instanceof MyComboBoxButton) {
					MyComboBoxButton button = (MyComboBoxButton) arrowButton;
					button.setIconOnly(true);
				}
				comboBox.repaint();
				updateButton(arrowButton);
			} else if (propertyName == "background") {
				Color color = (Color) e.getNewValue();
				arrowButton.setBackground(color);
				listBox.setBackground(color);

			} else if (propertyName == "foreground") {
				Color color = (Color) e.getNewValue();
				arrowButton.setForeground(color);
				listBox.setForeground(color);
			}
		}
	}

	/**
	 * As of Java 2 platform v1.4 this method is no longer used. Do not call or
	 * override. All the functionality of this method is in the
	 * MetalPropertyChangeListener.
	 * 
	 * @deprecated As of Java 2 platform v1.4.
	 */
	@Deprecated
	protected void editablePropertyChanged(PropertyChangeEvent e) {
	}

	protected LayoutManager createLayoutManager() {
		return new MetalComboBoxLayoutManager();
	}

	/**
	 * This inner class is marked &quot;public&quot; due to a compiler bug. This
	 * class should be treated as a &quot;protected&quot; inner class.
	 * Instantiate it only within subclasses of <FooUI>.
	 */
	public class MetalComboBoxLayoutManager extends
			ComboBoxLayoutManager {
		public void layoutContainer(Container parent) {
			layoutComboBox(parent, this);
		}

		public void superLayout(Container parent) {
			super.layoutContainer(parent);
		}
	}

	// This is here because of a bug in the compiler.
	// When a protected-inner-class-savvy compiler comes out we
	// should move this into MetalComboBoxLayoutManager.
	public void layoutComboBox(Container parent,
			MetalComboBoxLayoutManager manager) {
		if (arrowButton != null) {
			Insets insets = comboBox.getInsets();
			int buttonWidth = arrowButton.getMinimumSize().width;
			arrowButton.setBounds(
					(comboBox.getWidth() - insets.right - buttonWidth),
					insets.top, buttonWidth, comboBox.getHeight() - insets.top
							- insets.bottom);
		}

		if (editor != null) {
			Rectangle cvb = rectangleForCurrentValue();
			editor.setBounds(cvb);
		}
	}

	/**
	 * As of Java 2 platform v1.4 this method is no longer used.
	 * 
	 * @deprecated As of Java 2 platform v1.4.
	 */
	@Deprecated
	protected void removeListeners() {
		if (propertyChangeListener != null) {
			comboBox.removePropertyChangeListener(propertyChangeListener);
		}
	}

	// These two methods were overloaded and made public. This was probably a
	// mistake in the implementation. The functionality that they used to
	// provide is no longer necessary and should be removed. However,
	// removing them will create an uncompatible API change.

	public void configureEditor() {
		super.configureEditor();
	}

	public void unconfigureEditor() {
		super.unconfigureEditor();
	}

	public Dimension getMinimumSize(JComponent c) {
		if (!isMinimumSizeDirty) {
			return new Dimension(cachedMinimumSize);
		}

		Dimension size = null;

		if (!comboBox.isEditable() && arrowButton != null) {
			Insets buttonInsets = arrowButton.getInsets();
			Insets insets = comboBox.getInsets();

			size = getDisplaySize();
			size.width += insets.left + insets.right;
			size.width += buttonInsets.right;
			size.width += arrowButton.getMinimumSize().width;
			size.height += insets.top + insets.bottom;
			size.height += buttonInsets.top + buttonInsets.bottom;
		} else if (comboBox.isEditable() && arrowButton != null
				&& editor != null) {
			size = super.getMinimumSize(c);
			Insets margin = arrowButton.getMargin();
			size.height += margin.top + margin.bottom;
			size.width += margin.left + margin.right;
		} else {
			size = super.getMinimumSize(c);
		}

		cachedMinimumSize.setSize(size.width, size.height);
		isMinimumSizeDirty = false;

		return new Dimension(cachedMinimumSize);
	}
}
