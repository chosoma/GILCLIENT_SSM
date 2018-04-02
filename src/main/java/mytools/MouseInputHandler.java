package mytools;


import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.security.PrivilegedExceptionAction;

/**
 * The amount of space (in pixels) that the cursor is changed on.
 * MouseInputHandler is responsible for handling resize/moving of the Window.
 * It sets the cursor directly on the Window when then mouse moves over a hot
 * spot.
 */
public class MouseInputHandler implements MouseInputListener {

    private Window container;
    private MyDashedBorder myDashedBorder;

    public MouseInputHandler(Window container) {
        this.container = container;
        myDashedBorder = new MyDashedBorder();
    }

    private static final int CORNER_DRAG_WIDTH = 16;

    /**
     * Region from edges that dragging is active from.
     */
    private static final int BORDER_DRAG_THICKNESS = 5;

    /**
     * Maps from positions to cursor type. Refer to calculateCorner and
     * calculatePosition for details of this.
     */
    private static final int[] cursorMapping = new int[]{
            Cursor.NW_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
            Cursor.NW_RESIZE_CURSOR, 0, 0, 0, Cursor.NE_RESIZE_CURSOR,
            Cursor.W_RESIZE_CURSOR, 0, 0, 0, Cursor.E_RESIZE_CURSOR,
            Cursor.SW_RESIZE_CURSOR, 0, 0, 0, Cursor.SE_RESIZE_CURSOR,
            Cursor.SW_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR};

    /**
     * Used to determine the corner the resize is occuring from.
     */
    private int dragCursor;

    /**
     * X location the mouse went down on for a drag operation.
     */
    private int dragOffsetX;

    /**
     * Y location the mouse went down on for a drag operation.
     */
    private int dragOffsetY;

    /**
     * Width of the window when the drag started.
     */
    private int dragWidth;

    /**
     * Height of the window when the drag started.
     */
    private int dragHeight;

    private Cursor lastCursor;

    /*
     * PrivilegedExceptionAction needed by mouseDragged method to obtain new
     * location of window on screen during the drag.
     */
    private final PrivilegedExceptionAction getLocationAction = new PrivilegedExceptionAction() {
        @Override
        public Object run() throws Exception {
            return MouseInfo.getPointerInfo().getLocation();
        }
    };

    public void mousePressed(MouseEvent ev) {
        Point dragWindowOffset = ev.getPoint();

        dragOffsetX = dragWindowOffset.x;
        dragOffsetY = dragWindowOffset.y;
        dragWidth = container.getWidth();
        dragHeight = container.getHeight();
        dragCursor = getCursor(calculateCorner(dragWindowOffset.x, dragWindowOffset.y));

        if (ev.getClickCount() == 2) {

        } else {
            Window w = (Window) ev.getSource();
            myDashedBorder.setBounds(w.getBounds());
            myDashedBorder.setVisible(true);
        }
    }

    public void mouseReleased(MouseEvent ev) {
        if (ev.getClickCount() == 2) {

        } else {
            container.setBounds(myDashedBorder.getBounds());
            myDashedBorder.setVisible(false);
        }
//        if (dragCursor != 0 && !container.isValid()) {
//            // Some Window systems validate as you resize, others won't,
//            // thus the check for validity before repainting.
////            container.validate();
////            rootPaneRepaint();
//        }
        dragCursor = 0;
    }

    public void mouseMoved(MouseEvent ev) {
        // Update the cursor
        int cursor = getCursor(calculateCorner(ev.getX(), ev.getY()));

        container.setCursor(Cursor.getPredefinedCursor(cursor));

    }

    private void adjust(Rectangle bounds, Dimension min, int deltaX, int deltaY, int deltaWidth, int deltaHeight) {
        bounds.x += deltaX;
        bounds.y += deltaY;
        bounds.width += deltaWidth;
        bounds.height += deltaHeight;
        if (min != null) {
            if (bounds.width < min.width) {
                int correction = min.width - bounds.width;
                if (deltaX != 0) {
                    bounds.x -= correction;
                }
                bounds.width = min.width;
            }
            if (bounds.height < min.height) {
                int correction = min.height - bounds.height;
                if (deltaY != 0) {
                    bounds.y -= correction;
                }
                bounds.height = min.height;
            }
        }
//        if (dragCursor != 0 && !container.isValid()) {
//            // Some Window systems validate as you resize, others won't,
//            // thus the check for validity before repainting.
//            container.validate();
//            rootPaneRepaint();
//        }
    }

    public void mouseDragged(MouseEvent ev) {
        Window w = (Window) ev.getSource();
        Point pt = ev.getPoint();

        if (dragCursor != 0) {
            Rectangle r = w.getBounds();
            Rectangle startBounds = new Rectangle(r);
            Dimension min = w.getMinimumSize();
            switch (dragCursor) {
                case Cursor.NW_RESIZE_CURSOR:
                    adjust(r, min, pt.x - dragOffsetX, pt.y - dragOffsetY, -(pt.x - dragOffsetX), -(pt.y - dragOffsetY));
                    break;
                case Cursor.N_RESIZE_CURSOR:
                    adjust(r, min, 0, pt.y - dragOffsetY, 0, -(pt.y - dragOffsetY));
                    break;
                case Cursor.NE_RESIZE_CURSOR:
                    adjust(r, min, 0, pt.y - dragOffsetY, pt.x + (dragWidth - dragOffsetX) - r.width, -(pt.y - dragOffsetY));
                    break;
                case Cursor.W_RESIZE_CURSOR:
                    adjust(r, min, pt.x - dragOffsetX, 0, -(pt.x - dragOffsetX), 0);
                    break;
                case Cursor.E_RESIZE_CURSOR:
                    adjust(r, min, 0, 0, pt.x + (dragWidth - dragOffsetX) - r.width, 0);
                    break;
                case Cursor.SW_RESIZE_CURSOR:
                    adjust(r, min, pt.x - dragOffsetX, 0, -(pt.x - dragOffsetX), pt.y + (dragHeight - dragOffsetY) - r.height);
                    break;
                case Cursor.S_RESIZE_CURSOR:
                    adjust(r, min, 0, 0, 0, pt.y + (dragHeight - dragOffsetY) - r.height);
                    break;
                case Cursor.SE_RESIZE_CURSOR:
                    adjust(r, min, 0, 0, pt.x + (dragWidth - dragOffsetX) - r.width, pt.y + (dragHeight - dragOffsetY) - r.height);
                    break;
                default:
                    break;
            }
            if (!r.equals(startBounds)) {
                myDashedBorder.setBounds(r);
//                w.setBounds(r);
//                // Defer repaint/validate on mouseReleased unless dynamic
//                // layout is active.
//                if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
//                    w.validate();
//                    rootPaneRepaint();
//                }
            }
        }
    }

    private void rootPaneRepaint() {
        if (container instanceof JDialog) {
            ((JDialog) container).getRootPane().repaint();
        } else if (container instanceof JFrame) {
            ((JFrame) container).getRootPane().repaint();
        }
    }

    public void mouseEntered(MouseEvent ev) {
        Window w = (Window) ev.getSource();
        lastCursor = w.getCursor();
        mouseMoved(ev);
    }

    public void mouseExited(MouseEvent ev) {
        Window w = (Window) ev.getSource();
        w.setCursor(lastCursor);
    }

    public void mouseClicked(MouseEvent ev) {
    }

    /**
     * Returns the corner that contains the point <code>x</code>,
     * <code>y</code>, or -1 if the position doesn't match a corner.
     */
    private int calculateCorner(int x, int y) {
        Insets insets = container.getInsets();
        int xPosition = calculatePosition(x - insets.left, container.getWidth() - insets.left - insets.right);
        int yPosition = calculatePosition(y - insets.top, container.getHeight() - insets.top - insets.bottom);

        if (xPosition == -1 || yPosition == -1) {
            return -1;
        }
        return yPosition * 5 + xPosition;
    }

    /**
     * Returns the Cursor to render for the specified corner. This returns 0 if
     * the corner doesn't map to a valid Cursor
     */
    private int getCursor(int corner) {
        if (corner == -1) {
            return 0;
        }
        return cursorMapping[corner];
    }

    /**
     * Returns an integer indicating the position of <code>spot</code> in
     * <code>width</code>. The return value will be: 0 if <
     * BORDER_DRAG_THICKNESS 1 if < CORNER_DRAG_WIDTH 2 if >= CORNER_DRAG_WIDTH && <
     * width - BORDER_DRAG_THICKNESS 3 if >= width - CORNER_DRAG_WIDTH 4 if >=
     * width - BORDER_DRAG_THICKNESS 5 otherwise
     */
    private int calculatePosition(int spot, int width) {
        if (spot < BORDER_DRAG_THICKNESS) {
            return 0;
        }
        if (spot < CORNER_DRAG_WIDTH) {
            return 1;
        }
        if (spot >= (width - BORDER_DRAG_THICKNESS)) {
            return 4;
        }
        if (spot >= (width - CORNER_DRAG_WIDTH)) {
            return 3;
        }
        return 2;
    }

}
