package view.dataCollect.hitch;

import domain.IconConfig;

import javax.swing.*;

/**
 * SF6 管道变色的图标标签
 */
public class HitchIconLabel extends JLabel {

    private IconConfig iconConfig;//label 所在位置及大小属性

    public HitchIconLabel(Icon image, IconConfig iconConfig) {
        super(image);
        this.iconConfig = iconConfig;
        this.setVisible(false);
    }

    public void setIconBounds(int x, int y, int width, int height) {
        this.setBounds((int) (x * iconConfig.getX()), (int) (y * iconConfig.getY()), (int) (width * iconConfig.getWidth()), (int) (height * iconConfig.getHeight()));
    }


    public int getPoint() {
        return iconConfig.getPoint();
    }

    public String getXw() {
        return iconConfig.getXw();
    }

    @Override
    public Icon getIcon() {
        return super.getIcon();
    }

    @Override
    public void setIcon(Icon icon) {
        super.setIcon(icon);
    }

    public IconConfig getIconConfig() {
        return iconConfig;
    }

    public void setIconConfig(IconConfig iconConfig) {
        this.iconConfig = iconConfig;
    }


}
