package mytools.myScrollBar;

import java.awt.Color;

import mytools.MyUtil;

public class ScrollBarUtil {
	// ?????
	public static Color Background = Color.WHITE;
	// ??????77,97,133 98, 164, 214
	public static Color Button_Pressed = new Color(77, 97, 133);
	public static Color Button_Rollover = new Color(98, 164, 214);
	public static Color Button_Default = new Color(187,195,201);//155, 185, 245

	public static Color ControlDisabled = MyUtil.InactiveControlTextColor;
	public static Color ThumbColor = Button_Default;
	public static Color[] TrackColors = new Color[] { new Color(232, 231, 233),
		new Color(254,254,251) };
}
