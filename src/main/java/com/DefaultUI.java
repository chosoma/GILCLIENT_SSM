package com;

import mytools.MyBorders;
import mytools.MyUtil;
import mytools.myComboBox.ComboBoxUtil;
import view.icon.MyIconFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultUI {

    public static List<Image> icons;

    public DefaultUI() {
        icons = new ArrayList<>();
        icons.add(new ImageIcon(("com/tek16.png")).getImage());
        icons.add(new ImageIcon(("com/tek24.png")).getImage());
        icons.add(new ImageIcon(("com/tek32.png")).getImage());
        icons.add(new ImageIcon(("com/tek48.png")).getImage());

        this.setFont();
        this.setColor();
        this.setBorder();
        this.setIcon();
        // 菜单栏美化
        UIManager.put("ScrollBarUI", "mytools.myScrollBar.MyScrollBarUI");
        UIManager.put("ScrollBar.width", 6);
        UIManager.put("PopupMenuUI", "mytools.menu.MyPopupMenuUI");
        UIManager.put("MenuItemUI", "mytools.menu.MyMenuItemUI");
        UIManager.put("MenuUI", "mytools.menu.MyMenuUI");
        UIManager.put("ComboBoxUI", "mytools.myComboBox.MyComboBoxUI");
        // UIManager.put("ButtonUI", "mytools.MyButtonUI");
        UIManager.put("FileChooserUI", "mytools.MyFileChooserUI");
        UIManager.put("TableHeaderUI", "mytools.MyTableHeaderUI");
        UIManager.put("TabbedPaneUI", "mytools.MyTabbedPaneUI");
//        UIManager.put("SplitPaneUI", "mytools.mySplitPane.MySplitPaneUI");
//        UIManager.put("SplitPaneDivider.border", BorderFactory.createEmptyBorder());
//        UIManager.put("SplitPane.dividerSize", 9);
        UIManager.put("TableHeader.cellBorder", BorderFactory.createEmptyBorder());

        UIManager.put("CheckBoxMenuItemUI", "mytools.menu.MyCheckBoxMenuItemUI");

        UIManager.put("RadioButtonMenuItemUI", "mytools.menu.MyRadioButtonMenuItemUI");

        // 设置按钮的enter键触发
        UIManager.put("Button.focusInputMap", new UIDefaults.LazyInputMap(new Object[]{"ENTER", "pressed", "released ENTER", "released"}));

    }

    private void setIcon() {
        UIManager.put("CheckBox.icon", MyIconFactory.getCheckBoxIcon());
        UIManager.put("CheckBoxMenuItem.checkIcon", MyIconFactory.getCheckBoxIcon());

        UIManager.put("RadioButton.icon", MyIconFactory.getRadioButtonIcon());
        UIManager.put("RadioButtonMenuItem.checkIcon", MyIconFactory.getRadioButtonIcon());

        UIManager.put("OptionPane.informationIcon", MyIconFactory.getOptionPaneInformationIcon());
        UIManager.put("OptionPane.errorIcon", MyIconFactory.getOptionPaneErrorIcon());
        UIManager.put("OptionPane.questionIcon", MyIconFactory.getOptionPaneQuestionIcon());
        UIManager.put("OptionPane.warningIcon", MyIconFactory.getOptionPaneWarningIcon());

    }

    private void setBorder() {
        UIManager.put("Button.border", MyBorders.getButtonBorder());
        UIManager.put("TextField.border", MyUtil.Component_Border);
        UIManager.put("PasswordField.border", MyUtil.Component_Border);
        UIManager.put("ScrollPane.border", MyUtil.Component_Border);
        UIManager.put("Spinner.border", MyUtil.Component_Border);
        UIManager.put("Spinner.arrowButtonBorder", MyUtil.Component_Border);
        UIManager.put("Table.scrollPaneBorder", MyUtil.Component_Border);

    }

    private void setColor() {

        UIManager.put("ToolTip.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("SplitPane.background", Color.WHITE);
        UIManager.put("Viewport.background", Color.WHITE);
        UIManager.put("ScrollPane.background", Color.WHITE);

//        // JSPinner背景色
        UIManager.put("Spinner.background", Color.WHITE);
        UIManager.put("Spinner.foreground", Color.black);

        UIManager.put("FormattedTextField.background", Color.WHITE);

        // ----------滚动条------------
        Color c = new Color(245, 244, 239);
        // 滚动条背景色
        UIManager.put("ScrollBar.background", Color.WHITE);
        // 滚动条的外边框
        // UIManager.put("ScrollBar.darkShadow", Color.WHITE);
        // 滚动条的内边框
        UIManager.put("ScrollBar.shadow", c);
        // 滚动条滑块外边框
        // UIManager.put("ScrollBar.thumbShadow", Color.blue);
        // 滚动条滑块内边框
        // UIManager.put("ScrollBar.thumbHighlight", Color.red);

        // UIManager.put("ComboBox.buttonBackground", Color.WHITE);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", Color.BLACK);
        Color DisabledBackground = new Color(245, 244, 234);
        UIManager.put("ComboBox.disabledBackground", DisabledBackground);
        Color DisabledForeground = new Color(153, 153, 153);
        UIManager.put("ComboBox.disabledForeground", DisabledForeground);
        Color SelectionBackground = new Color(214, 222, 235);
        UIManager.put("ComboBox.selectionBackground", SelectionBackground);

        // UIManager.put("TabbedPane.background", Color.WHITE);
        // UIManager.put("TabbedPane.selected", Color.WHITE);
        // UIManager.put("TabbedPane.tabAreaBackground", Color.WHITE);
        UIManager.put("TabbedPane.contentAreaColor", Color.WHITE);

        UIManager.put("Pane.background", Color.WHITE);

        Color foreground = new Color(51, 51, 51);
        UIManager.put("TableHeader.background", null);
        UIManager.put("Label.foreground", foreground);
        UIManager.put("Label.background", Color.WHITE);
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.focus", new Color(255, 187, 0));
        UIManager.put("TitledBorder.titleColor", foreground);

        UIManager.put("RadioButton.disabledText", MyUtil.InactiveControlTextColor);
        UIManager.put("RadioButton.foreground", Color.BLACK);
        UIManager.put("RadioButton.background", Color.WHITE);

        // JSpinner内部可用与不可用颜色
        UIManager.put("FormattedTextField.background", Color.WHITE);
        UIManager.put("FormattedTextField.foreground", Color.BLACK);
        UIManager.put("FormattedTextField.inactiveBackground", Color.WHITE);
        UIManager.put("FormattedTextField.inactiveForeground", MyUtil.InactiveControlTextColor);

        UIManager.put("TextArea.foreground", Color.BLACK);
        UIManager.put("TextArea.inactiveForeground", Color.BLACK);

        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);
        UIManager.put("TextField.inactiveBackground", Color.WHITE);
        UIManager.put("TextField.inactiveForeground", MyUtil.InactiveControlTextColor);

        UIManager.put("MenuItem.selectionForeground", Color.WHITE);
        UIManager.put("MenuItem.background", Color.WHITE);
        UIManager.put("CheckBoxMenuItem.selectionForeground", Color.WHITE);
        UIManager.put("CheckBoxMenuItem.background", Color.WHITE);
        UIManager.put("CheckBox.background", Color.WHITE);
        UIManager.put("RadioButtonMenuItem.selectionForeground", Color.WHITE);
        UIManager.put("RadioButtonMenuItem.background", Color.WHITE);
        // 网格线颜色(215,221,232)(208, 215, 229)(222,227,236)
        UIManager.put("Table.gridColor", new Color(211, 217, 230));
        UIManager.put("Table.selectionForeground", Color.BLACK);

        // UIManager.put("Table.rowHeight", 22);

    }

    private void setFont() {

        Font font = MyUtil.FONT_12;
        UIManager.put("Button.font", font);
        UIManager.put("CheckBox.font", font);
        UIManager.put("CheckBoxMenuItem.font", font);
        UIManager.put("CheckBoxMenuItem.acceleratorFont", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("DesktopIcon.font", font);
        UIManager.put("EditorPane.font", font);
        UIManager.put("FormattedTextField.font", font);
        UIManager.put("InternalFrame.titleFont", font);
        UIManager.put("Label.font", font);
        UIManager.put("List.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("Menu.acceleratorFont", font);
        UIManager.put("MenuBar.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("MenuItem.acceleratorFont", font);
        UIManager.put("OptionPane.buttonFont", font);
        UIManager.put("OptionPane.messageFont", font);
        UIManager.put("Panel.font", font);
        UIManager.put("PasswordField.font", font);
        UIManager.put("PopupMenu.font", font);
        UIManager.put("ProgressBar.font", font);
        UIManager.put("RadioButton.font", font);
        UIManager.put("RadioButtonMenuItem.font", font);
        UIManager.put("RadioButtonMenuItem.acceleratorFont", font);
        UIManager.put("ScrollPane.font", font);
        UIManager.put("Spinner.font", font);
        UIManager.put("TabbedPane.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("TextPane.font", font);
        UIManager.put("TitledBorder.font", font);
        UIManager.put("ToggleButton.font", font);
        UIManager.put("ToolBar.font", font);
        UIManager.put("ToolTip.font", font);
        UIManager.put("Tree.font", font);
        UIManager.put("Viewport.font", font);
    }

}