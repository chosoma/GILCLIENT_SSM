package com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

import domain.*;
import protocol.Protocol;
import service.CollectService;
import service.NetService;
import util.MyDecodeUitl;
import view.debugs.Debugs;
import data.DataBuffer;
import data.FormatTransfer;

public class CollectSocket implements Runnable {

	private Socket socket;
	private OutputStream out;// socket输出流

	private Debugs debugShow = Debugs.getInstance();
	private DataBuffer dataFactory;// 数据工厂

	private Byte netType, netID;
	private NetBean netBean;

	CollectSocket(Socket socket) {
		this.socket = socket;
		CollectServer.getInstance().addSocket(this);
		dataFactory = new DataBuffer();
	}

	@Override
	public void run() {
		String offlineMSG = "";
		try {
			offlineMSG = "连接成功";
			debugShow.showMsg(offlineMSG + socket.getInetAddress() + ":" + socket.getPort());
			System.out.println("链接成功");
			InputStream in = socket.getInputStream();
			out = socket.getOutputStream();
			byte[] b = new byte[1024];
			int num;
//			int count = 0;
			while ((num = in.read(b)) != -1) {
				// System.out.println("接受数据:" + num);
				Date time = Calendar.getInstance().getTime();
				debugShow.rec(b, num, time, " " + socket.getPort());
				if (b[0] == Protocol.HEAD) {
					netType = b[1];
					netID = b[2];
					byte cmdType = b[3];
					byte cmdID = b[4];
					if (netBean == null) {
						netBean = NetService.getNetBean(netType, netID);
						// if (netBean != null) {
						// CollectServer.getInstance().addSocket(this);
						// }
					}
					if (cmdType == Protocol.cmdHeartR) {// 心跳
						byte[] msgH = getReply(Protocol.cmdHeartT, cmdID);
						sendMSG(msgH);
						// if (netBean == null) {
						// netBean = NetService.getNetBean(netType, netID);
						// if (netBean != null) {
						// CollectServer.getInstance().addSocket(this);
						// }
						// }
					} else if (cmdType == Protocol.cmdDataR) {// 数据
//						count++;
//						System.out.println("count:" + count);
						byte[] data = new byte[num];
						System.arraycopy(b, 0, data, 0, num);
						byte[] msgD = getReply(Protocol.cmdDataT, cmdID);
						sendMSG(msgD);
						dataFactory.receDatas(new RawData(data, time));
					} else if (cmdType == Protocol.cmdSetR) {// 设置应答
						System.out.println("设置应答");
						try {
							byte unitType = b[8];
							byte unitId = b[9];
							CollectService.savePeriod(unitType, unitId);
							CollectService.cacelWarnTempPeriodTimer(unitType, unitId);
						} catch (ArrayIndexOutOfBoundsException aiobe) {
							aiobe.printStackTrace();
						}
					} else if (cmdType == Protocol.cmdMsgR) {// 短信应答
						System.out.println("短信应答");
					} else if (cmdType == 0x09) {
						System.out.println("报警应答");
					}
				}
			}
			offlineMSG = "已下线,port: ";
		} catch (SocketException e) {
			// CollectOperate.getInstance().getJbtOpen1().setSelected(false);
			e.printStackTrace();
			offlineMSG = "接受数据超时,port: ";
			// } catch (SQLException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			offlineMSG = "连接已被关闭,port: ";
		} finally {
			try {
				this.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			dataFactory.close();
			CollectServer.getInstance().removeSocket(this);
			debugShow.showMsg(offlineMSG);
		}
	}

	// 如果socket没有关闭
	public void close() throws IOException {
		if (!socket.isClosed()) {
			socket.close();
		}
	}

	boolean isConnected() {
		return socket.isConnected();
	}

	// 是否关闭
	boolean isClosed() {
		return socket.isClosed();
	}

	boolean isUseful() {
		return !socket.isClosed() && socket.isConnected();
	}

	byte getNetId() {
		return netID;
	}

	void setJg(UnitBean unitBean, byte jg) throws IOException {
		System.out.println("设置参数");
		byte[] msg;
		msg = getSetJg(unitBean, jg);
		sendMSG(msg);

	}

	void setAlarm(byte time) throws IOException {
		sendMSG(getAlarm(time));
	}

	// 发送指令
	private synchronized void sendMSG(byte[] msg) throws IOException {
		try {
			out.write(msg);
			out.flush();
			debugShow.send(msg, msg.length, "");
		} catch (IOException e) {
			try {
				this.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw new IOException(" 发送命令时连接中断");
		}
	}

	private byte[] getAlarm(byte time) {
		byte[] b = new byte[10];
		b[0] = getNetType();
		b[1] = getNetID();
		b[2] = 0x08;
		b[3] = (byte) 0xb4;
		b[4] = 0;
		b[5] = 2;
		b[6] = 0;
		b[7] = time;
		FormatTransfer.calcCRC16_X(b);// CRC16
		return MyDecodeUitl.Encryption(b);
	}

	private byte[] getReply(byte cmdT, byte cmdI) {
		byte[] b = new byte[15];
		b[0] = getNetType();
		b[1] = getNetID();
		b[2] = cmdT;
		b[3] = cmdI;
		b[4] = 0x00;
		b[5] = Protocol.LenHeartT;
		b[6] = 0x00;
		Calendar cal = Calendar.getInstance();
		b[7] = (byte) (cal.get(Calendar.YEAR) % 100);
		b[8] = (byte) (cal.get(Calendar.MONTH) + 1);
		b[9] = (byte) cal.get(Calendar.DAY_OF_MONTH);
		b[10] = (byte) cal.get(Calendar.HOUR_OF_DAY);
		b[11] = (byte) cal.get(Calendar.MINUTE);
		b[12] = (byte) cal.get(Calendar.SECOND);
		FormatTransfer.calcCRC16_X(b);// CRC16
		return MyDecodeUitl.Encryption(b);
	}

	private byte[] getSetJg(UnitBean unitBean, byte jg) {
		byte[] b = new byte[20];
		b[0] = getNetType();
		b[1] = getNetID();
		b[2] = Protocol.cmdSetT;
		b[3] = Protocol.cmdSetIDR;
		b[4] = 0x00;
		b[5] = Protocol.LenSetWDT;
		b[6] = 0x00;
		b[7] = unitBean.getType();
		b[8] = unitBean.getNumber();
		NetBean netBean = NetService.getNetBean(unitBean.getGatewaytype(), unitBean.getGatewaynumber());
		if (netBean != null) {
			b[9] = netBean.getChannel();
		} else {
			b[9] = 0x01;
		}
		b[10] = jg;
		switch (unitBean.getType()) {
		case Protocol.UnitTypeWD:
			b[11] = jg;
			WdPeriod wdPeriod = Configure.getWdPeriod();
			b[12] = wdPeriod.getWd1();
			b[13] = wdPeriod.getJg1();
			b[14] = wdPeriod.getWd2();
			b[15] = wdPeriod.getJg2();
			b[16] = 0;
			b[17] = 0;
			break;
		case Protocol.UnitTypeHV:
			byte[] bytes = FormatTransfer.float2Bytes(unitBean.getVolwarn());
			// System.out.println(Arrays.toString(bytes));
			// System.out.println(unitBean);
			System.arraycopy(bytes, 0, b, 11, bytes.length);
			break;
		}

		FormatTransfer.calcCRC16_X(b);// CRC16
		return MyDecodeUitl.Encryption(b);
	}

	Byte getNetType() {
		if (netType == null) {
			return 4;
		}
		return netType;
	}

	private Byte getNetID() {
		if (netID == null) {
			return 1;
		}
		return netID;
	}
}
