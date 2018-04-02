package mytools;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MyMouseInputHandler implements MouseInputListener {

    public MyMouseInputHandler(Window container) {
        this.container = container;
        dashedBorder = new MyDashedBorder();
    }

    private Window container;

    private MyDashedBorder dashedBorder;
    private boolean pressed, released, entered, exited, dragged, moved;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    private int dragCursor;

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed();
        Point dragWindowOffset = e.getPoint();
        dragCursor = getCursor(calculateCorner(dragWindowOffset.x, dragWindowOffset.y));

    }

    private void mousePressed() {
        pressed = true;
        released = false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseReleased();
    }

    private void mouseReleased() {
        released = true;
        pressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseEntered();
    }

    private void mouseEntered() {
        entered = true;
        exited = false;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseExited();
        Window w = (Window) e.getSource();
        w.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void mouseExited() {
        entered = false;
        exited = true;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!isPressed()) {
            return;
        }
        dragged = true;
        Window w = (Window) e.getSource();
        Point pt = e.getPoint();
        dashedBorder.setBounds(w.getBounds());
        switch (dragCursor) {
            case Cursor.E_RESIZE_CURSOR:
                break;
            case Cursor.S_RESIZE_CURSOR:
                break;
            case Cursor.N_RESIZE_CURSOR:
                break;
            case Cursor.W_RESIZE_CURSOR:
                break;
            case Cursor.NE_RESIZE_CURSOR:
                break;
            case Cursor.SE_RESIZE_CURSOR:
                break;
            case Cursor.NW_RESIZE_CURSOR:
                break;
            case Cursor.SW_RESIZE_CURSOR:
                break;
            default:
                break;
        }


    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isEntered()) {
            return;
        }
        int cursor = getCursor(calculateCorner(e.getX(), e.getY()));
        container.setCursor(Cursor.getPredefinedCursor(cursor));
    }

    private static final int[] cursorMapping = new int[]{
            Cursor.NW_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
            Cursor.NW_RESIZE_CURSOR, 0, 0, 0, Cursor.NE_RESIZE_CURSOR,
            Cursor.W_RESIZE_CURSOR, 0, 0, 0, Cursor.E_RESIZE_CURSOR,
            Cursor.SW_RESIZE_CURSOR, 0, 0, 0, Cursor.SE_RESIZE_CURSOR,
            Cursor.SW_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR};

    private int getCursor(int corner) {
        if (corner == -1) {
            return 0;
        }
        return cursorMapping[corner];
    }

    private int calculateCorner(int x, int y) {
        Insets insets = container.getInsets();
        int xPosition = calculatePosition(x - insets.left, container.getWidth() - insets.left - insets.right);
        int yPosition = calculatePosition(y - insets.top, container.getHeight() - insets.top - insets.bottom);

        if (xPosition == -1 || yPosition == -1) {
            return -1;
        }
        return yPosition * 5 + xPosition;
    }

    private static final int CORNER_DRAG_WIDTH = 16;
    private static final int BORDER_DRAG_THICKNESS = 5;

    private int calculatePosition(int spot, int width) {
//        System.out.println("spot:"+spot);
//        System.out.println("width:"+width);
        if (spot < BORDER_DRAG_THICKNESS) {
            return 0;
        }
        if (spot < CORNER_DRAG_WIDTH) {
            return 1;
        }
        if (width - spot <= BORDER_DRAG_THICKNESS) {
            return 4;
        }
        if (width - spot <= CORNER_DRAG_WIDTH) {
            return 3;
        }
        return 2;
    }

    public boolean isPressed() {
        return pressed;
    }

    public boolean isReleased() {
        return released;
    }

    public boolean isEntered() {
        return entered;
    }

    public boolean isExited() {
        return exited;
    }

    public boolean isDragged() {
        return dragged;
    }

    public boolean isMoved() {
        return moved;
    }
}
