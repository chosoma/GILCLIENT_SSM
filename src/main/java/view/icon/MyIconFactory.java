package view.icon;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;


public class MyIconFactory {

    private static Icon CheckBoxIcon;
    private static Icon RadioButtonIcon;

    private static Icon OptionPaneErrorIcon;
    private static Icon OptionPaneInformationIcon;
    private static Icon OptionPaneQuestionIcon;
    private static Icon OptionPaneWarningIcon;

    private static Icon ReFreshIcon;

    private static Icon FoldIcon, VioceWarn, ShowDebug;


    public static Icon getFoldIcon() {
        if (FoldIcon == null) {
            FoldIcon = new FoldIcon();
        }
        return FoldIcon;
    }

    public static Icon getCheckBoxIcon() {
        if (CheckBoxIcon == null) {
            CheckBoxIcon = new MyCheckBoxIcon();
        }
        return CheckBoxIcon;
    }

    public static Icon getRadioButtonIcon() {
        if (RadioButtonIcon == null) {
            RadioButtonIcon = new MyRadioButtonIcon();
        }
        return RadioButtonIcon;
    }

    public static Icon getOptionPaneErrorIcon() {
        if (OptionPaneErrorIcon == null) {
            OptionPaneErrorIcon = new ImageIcon(
                    MyIconFactory.class.getClassLoader().getResource("icon/error.png"));
//                    MyIconFactory.class.getResource("error.png"));
        }
        return OptionPaneErrorIcon;
    }

    public static Icon getOptionPaneQuestionIcon() {
        if (OptionPaneQuestionIcon == null) {
            OptionPaneQuestionIcon = new ImageIcon(
//                    MyIconFactory.class.getResource("question.png"));
                    MyIconFactory.class.getClassLoader().getResource("icon/question.png"));
        }
        return OptionPaneQuestionIcon;
    }

    public static Icon getOptionPaneInformationIcon() {
        if (OptionPaneInformationIcon == null) {
            OptionPaneInformationIcon = new ImageIcon(
//                    MyIconFactory.class.getResource("info.png"));
                    MyIconFactory.class.getClassLoader().getResource("icon/info.png"));
        }
        return OptionPaneInformationIcon;
    }

    public static Icon getOptionPaneWarningIcon() {
        if (OptionPaneWarningIcon == null) {
            OptionPaneWarningIcon = new ImageIcon(
//                    MyIconFactory.class.getResource("warning.png"));
                    MyIconFactory.class.getClassLoader().getResource("icon/warning.png"));
        }
        return OptionPaneWarningIcon;
    }

    public static Icon getReFreshIcon() {
        if (ReFreshIcon == null) {
//            ReFreshIcon = new ImageIcon("images/refresh.png");
            ReFreshIcon = new ImageIcon("images/refresh.png");
        }
        return ReFreshIcon;
    }

    public static Icon getVoiceWarnIcon() {
        if (VioceWarn == null) {
            VioceWarn = new VioceWarn_16();
        }
        return VioceWarn;
    }


    public static Icon getShowDebugIcon() {
        if (ShowDebug == null) {
            ShowDebug = new ShowDebug_16();
        }
        return ShowDebug;
    }

    public static Image sound_16, sound_muted_16, showDebug_16, showDebug_muted_16, msg_16, msg_muted_16,
            temp, temp_28,
            vari, vari_28,
            SF6, SF6_28,
            ladder, ladder_28,
            unknown,
            warn, warn_28,
            hitchwarn, hitcherror;

    static {
        try {
//            System.out.println(MyIconFactory.class.getClassLoader().getResource(""));
            sound_16 = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/sound_16.png"));
            sound_muted_16 = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/sound_muted_16.png"));
            showDebug_16 = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/showDebug_16.png"));
            showDebug_muted_16 = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/showDebug_muted_16.png"));
            msg_16 = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/msg_16.png"));
            msg_muted_16 = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/msg_muted_16.png"));
            temp = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/temp.png"));
            temp_28 = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/temp_28.png"));
            vari = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/vari.png"));
            vari_28 = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/vari_28.png"));
            SF6 = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/sf6.png"));
            SF6_28 = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/sf6_28.png"));
            ladder = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/ladder.png"));
            ladder_28 = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/ladder_28.png"));
            unknown = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/unknown.png"));
            warn = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/warn.png"));
            warn_28 = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/warn_28.png"));
            hitchwarn = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/hitchwarn.png"));
            hitcherror = ImageIO.read(MyIconFactory.class.getClassLoader().getResource("icon/hitcherror.png"));
            /*
            sound_16 = ImageIO.read(MyIconFactory.class.getResource("sound_16.png"));
            sound_muted_16 = ImageIO.read(MyIconFactory.class.getResource("sound_muted_16.png"));
            showDebug_16 = ImageIO.read(MyIconFactory.class.getResource("showDebug_16.png"));
            showDebug_muted_16 = ImageIO.read(MyIconFactory.class.getResource("showDebug_muted_16.png"));
            msg_16 = ImageIO.read(MyIconFactory.class.getResource("msg_16.png"));
            msg_muted_16 = ImageIO.read(MyIconFactory.class.getResource("msg_muted_16.png"));
            temp = ImageIO.read(MyIconFactory.class.getResource("temp.png"));
            temp_28 = ImageIO.read(MyIconFactory.class.getResource("temp_28.png"));
            vari = ImageIO.read(MyIconFactory.class.getResource("vari.png"));
            vari_28 = ImageIO.read(MyIconFactory.class.getResource("vari_28.png"));
            SF6 = ImageIO.read(MyIconFactory.class.getResource("sf6.png"));
            SF6_28 = ImageIO.read(MyIconFactory.class.getResource("sf6_28.png"));
            ladder = ImageIO.read(MyIconFactory.class.getResource("ladder.png"));
            unknown = ImageIO.read(MyIconFactory.class.getResource("unknown.png"));
            warn = ImageIO.read(MyIconFactory.class.getResource("warn.png"));
            */

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
