package protocol;

/**
 * 数据协议参数
 */
public class Protocol {

	/************ 转义 ***************/
	public final static byte HEAD = 0x7E;// 头
	public final static byte TAIL = 0x7D;// 尾
	public final static byte TURN = 0x7C;//
	public final static byte IST1 = 0x0D;//
	public final static byte IST2 = 0x0A;//

	public final static byte HEADT = 0x5E;// 头
	public final static byte TAILT = 0x5D;// 尾
	public final static byte TURNT = 0x5C;//
	public final static byte IST1T = 0x5B;//
	public final static byte IST2T = 0x5A;//
	/*********************************/



	public final static byte UnitTypeSF6 = 0x01;// SF6 单元类型
	public final static byte UnitTypeSSJ = 0x02;// 伸缩节 单元类型
	public final static byte UnitTypeWD = 0x03;// 温度 单元类型
	public final static byte UnitTypeHV = 0x04;// 温度 单元类型

	public final static byte LenHeartR1 = 0x00;// 旧心跳包 长度
	public final static byte LenHeartR2 = 0x01;// 新心跳包 长度
	public final static byte LenHeartT = 0x07;// 心跳包应答 长度
	public final static byte LenSetR = 0x03;// 设置应答 长度
	public final static byte LenSetWDT = 0x0C;// 设置温度 长度
	public final static byte LenMsgR = LenSetWDT;// 短信应答 长度

	public final static byte cmdHeartR = 0x06;// 心跳接受
	public final static byte cmdHeartT = 0x07;// 心跳应答
	public final static byte cmdDataR = 0x00;// 数据接受
	public final static byte cmdDataT = 0x01;// 数据应答
	public final static byte cmdSetR = 0x03;// 设置应答
	public final static byte cmdSetT = 0x02;// 设置类型
	public final static byte cmdSetIDR = (byte) 0xB1;// 设置id
	public final static byte cmdMsgR = 0x05;// 短信应答

}
