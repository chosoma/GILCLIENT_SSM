package mytools.myScrollBar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MyScrollBarUI extends BasicScrollBarUI {

	private static Color shadowColor;
	private static Color highlightColor;
	private static Color darkShadowColor;
	private static Color thumbColor;
	private static Color thumbShadow;
	private static Color thumbHighlightColor;

	protected MyScrollButton increaseButton;
	protected MyScrollButton decreaseButton;

	protected int scrollBarWidth;

	public static final String FREE_STANDING_PROP = "JScrollBar.isFreeStanding";
	protected boolean isFreeStanding = true;

	public static ComponentUI createUI(JComponent c) {
		return new MyScrollBarUI();
	}

	protected void installDefaults() {
		scrollBarWidth = ((Integer) (UIManager.get("ScrollBar.width")))
				.intValue();
		super.installDefaults();
	}

	protected void installListeners() {
		super.installListeners();
		((ScrollBarListener) propertyChangeListener)
				.handlePropertyChange(scrollbar
						.getClientProperty(FREE_STANDING_PROP));
	}

	protected PropertyChangeListener createPropertyChangeListener() {
		return new ScrollBarListener();
	}

	protected void configureScrollBarColors() {
		super.configureScrollBarColors();
		shadowColor = UIManager.getColor("ScrollBar.shadow");
		highlightColor = UIManager.getColor("ScrollBar.highlight");
		darkShadowColor = UIManager.getColor("ScrollBar.darkShadow");
		thumbColor = UIManager.getColor("ScrollBar.thumb");
		thumbShadow = UIManager.getColor("ScrollBar.thumbShadow");
		thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");

	}

	public Dimension getPreferredSize(JComponent c) {
		if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			return new Dimension(scrollBarWidth, scrollBarWidth * 3 + 10);
		} else // Horizontal
		{
			return new Dimension(scrollBarWidth * 3 + 10, scrollBarWidth);
		}

	}

	/**
	 * Returns the view that represents the decrease view.
	 */
	protected JButton createDecreaseButton(int orientation) {
		decreaseButton = new MyScrollButton(orientation, scrollBarWidth,
				isFreeStanding);
		return decreaseButton;
	}

	/** Returns the view that represents the increase view. */
	protected JButton createIncreaseButton(int orientation) {
		increaseButton = new MyScrollButton(orientation, scrollBarWidth,
				isFreeStanding);
		return increaseButton;
	}

	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		/*
		g.translate(trackBounds.x, trackBounds.y);
		boolean leftToRight = c.getComponentOrientation().isLeftToRight();
		Graphics2D g2 = (Graphics2D) g.create();

		if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			g2.setPaint(new GradientPaint(0, 0, ScrollBarUtil.TrackColors[0],
					trackBounds.width, 0, ScrollBarUtil.TrackColors[1]));
			if (!isFreeStanding) {
				trackBounds.width += 2;
				if (!leftToRight) {
					g.translate(-1, 0);
				}
			}

			if (c.isEnabled()) {
				g2.fillRect(0, 0, trackBounds.width, trackBounds.height);
				// g.setColor(darkShadowColor);
				// g.drawLine(trackBounds.width - 2, 0, trackBounds.width - 2,
				// trackBounds.height - 1);
				// g.drawLine(2, trackBounds.height - 1, trackBounds.width - 1,
				// trackBounds.height - 1);
				// g.drawLine(2, 0, trackBounds.width - 2, 0);
				//
				// g.setColor(shadowColor);
				// // g.setColor( Color.red);
				// g.drawLine(1, 1, 1, trackBounds.height - 2);
				// g.drawLine(1, 1, trackBounds.width - 3, 1);
				// if (scrollbar.getValue() != scrollbar.getMaximum()) { //
				// thumb
				// // shadow
				// int y = thumbRect.y + thumbRect.height - trackBounds.y;
				// g.drawLine(1, y, trackBounds.width - 1, y);
				// }
				// g.setColor(highlightColor);
				// g.drawLine(trackBounds.width - 1, 0, trackBounds.width - 1,
				// trackBounds.height - 1);
			} else {
				MyUI.drawDisabledBorder(g, 0, 0, trackBounds.width,
						trackBounds.height);
			}

			if (!isFreeStanding) {
				trackBounds.width -= 2;
				if (!leftToRight) {
					g.translate(1, 0);
				}
			}
		} else // HORIZONTAL
		{
			g2.setPaint(new GradientPaint(0, 0, ScrollBarUtil.TrackColors[0],
					0, trackBounds.height, ScrollBarUtil.TrackColors[1]));
			if (!isFreeStanding) {
				trackBounds.height += 2;
			}

			if (c.isEnabled()) {
				g2.fillRect(0, 0, trackBounds.width, trackBounds.height);
				// g.setColor(darkShadowColor);
				// g.drawLine(0, 0, trackBounds.width - 1, 0); // top
				// g.drawLine(0, 2, 0, trackBounds.height - 2); // left
				// g.drawLine(0, trackBounds.height - 2, trackBounds.width - 1,
				// trackBounds.height - 2); // bottom
				// g.drawLine(trackBounds.width - 1, 2, trackBounds.width - 1,
				// trackBounds.height - 1); // right
				//
				// g.setColor(shadowColor);
				// g.drawLine(1, 1, trackBounds.width - 2, 1); // top
				// g.drawLine(1, 1, 1, trackBounds.height - 3); // left
				// g.drawLine(0, trackBounds.height - 1, trackBounds.width - 1,
				// trackBounds.height - 1); // bottom
				// if (scrollbar.getValue() != scrollbar.getMaximum()) { //
				// thumb
				// // shadow
				// int x = thumbRect.x + thumbRect.width - trackBounds.x;
				// g.drawLine(x, 1, x, trackBounds.height - 1);
				// }
			} else {
				MyUI.drawDisabledBorder(g, 0, 0, trackBounds.width,
						trackBounds.height);
			}

			if (!isFreeStanding) {
				trackBounds.height -= 2;
			}
		}
		g2.dispose();
		g.translate(-trackBounds.x, -trackBounds.y);
		*/
	}

	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		if (!c.isEnabled()) {
			return;
		}
		
		boolean leftToRight = c.getComponentOrientation().isLeftToRight();

		g.translate(thumbBounds.x, thumbBounds.y);

		g.setColor(ScrollBarUtil.ThumbColor);
		/*
		if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			if (!isFreeStanding) {
				thumbBounds.width += 2;
				if (!leftToRight) {
					g.translate(-1, 0);
				}
				g.fillRect(0, 0, thumbBounds.width - 2, thumbBounds.height);
				thumbBounds.width -= 2;
				if (!leftToRight) {
					g.translate(1, 0);
				}
			} else {
				g.fillRect(2, 0, thumbBounds.width - 4, thumbBounds.height);
			}
		} else { // HORIZONTAL
			if (!isFreeStanding) {
				thumbBounds.height += 2;
				g.fillRect(0, 0, thumbBounds.width, thumbBounds.height - 2);
				thumbBounds.height -= 2;
			} else {
				g.fillRect(0, 2, thumbBounds.width, thumbBounds.height - 4);
			}
		}
		*/
		
		if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			thumbBounds.width = getTrackBounds().width;
		} else { // HORIZONTAL
			thumbBounds.height = getTrackBounds().height;
		}
			
		g.fillRect(0, 0, thumbBounds.width, thumbBounds.height );
		g.translate(-thumbBounds.x, -thumbBounds.y);
	}

	protected Dimension getMinimumThumbSize() {
		return new Dimension(scrollBarWidth, scrollBarWidth);
	}

	/**
	 * This is overridden only to increase the invalid area. This ensures that
	 * the "Shadow" below the thumb is invalidated
	 */
	protected void setThumbBounds(int x, int y, int width, int height) {
		/*
		 * If the thumbs bounds haven't changed, we're done.
		 */
		if ((thumbRect.x == x) && (thumbRect.y == y)
				&& (thumbRect.width == width) && (thumbRect.height == height)) {
			return;
		}

		/*
		 * Update thumbRect, and repaint the union of x,y,w,h and the old
		 * thumbRect.
		 */
		int minX = Math.min(x, thumbRect.x);
		int minY = Math.min(y, thumbRect.y);
		int maxX = Math.max(x + width, thumbRect.x + thumbRect.width);
		int maxY = Math.max(y + height, thumbRect.y + thumbRect.height);

		thumbRect.setBounds(x, y, width, height);
		scrollbar.repaint(minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
	}

	class ScrollBarListener extends PropertyChangeHandler {
		public void propertyChange(PropertyChangeEvent e) {
			String name = e.getPropertyName();
			if (name.equals(FREE_STANDING_PROP)) {
				handlePropertyChange(e.getNewValue());
			} else {
				super.propertyChange(e);
			}
		}

		public void handlePropertyChange(Object newValue) {
			if (newValue != null) {
				boolean temp = ((Boolean) newValue).booleanValue();
				boolean becameFlush = temp == false && isFreeStanding == true;
				boolean becameNormal = temp == true && isFreeStanding == false;

				isFreeStanding = temp;

				if (becameFlush) {
					toFlush();
				} else if (becameNormal) {
					toFreeStanding();
				}
			} else {

				if (!isFreeStanding) {
					isFreeStanding = true;
					toFreeStanding();
				}
			}

			if (increaseButton != null) {
				increaseButton.setFreeStanding(isFreeStanding);
			}
			if (decreaseButton != null) {
				decreaseButton.setFreeStanding(isFreeStanding);
			}
		}

		protected void toFlush() {
			scrollBarWidth -= 2;
		}

		protected void toFreeStanding() {
			scrollBarWidth += 2;
		}
	} // end class ScrollBarListener
}
