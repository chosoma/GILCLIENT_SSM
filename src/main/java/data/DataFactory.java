package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import domain.*;
import protocol.Protocol;
import service.*;
import view.dataCollect.datacollect.AbcUnitView;
import view.dataCollect.datacollect.CollectShow;

public class DataFactory {
	private List<DataBean> dataList = new ArrayList<DataBean>();

	private CollectShow show = CollectShow.getInstance();

	private DataFactory() {

	}

	private static DataFactory DB = new DataFactory();

	public static DataFactory getInstance() {
		return DB;
	}

	// 将数据集合中的数据保存到数据库中
	public synchronized void saveData() {
		if (dataList.size() > 0) {
			try {
				DataService.saveCollData(dataList);
				dataList.clear();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// if (hitchList.size() > 0) {
		// try {
		// HitchService.save(hitchList);
		// hitchList.clear();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// }
	}

	public void processData_X(byte[] data, Date time) {
		byte gatewayType = data[1];// 网关类型
		byte gatewaynumber = data[2];// 网关ID
		UnitPacket unitPacket = SensorService.getUnitPacket(gatewayType, gatewaynumber);
		if (unitPacket == null) {
			System.out.println("未找到网关");
			return;
		}

		int length = FormatTransfer.getDataLen(data[5], data[6]);// 总数据长度

		int dataCount = data[7];// 数据条数
		int off = 7;
		int count = 0;
		for (int i = 0; i < dataCount; i++) {
//			System.out.println("count:"+count++);
			try {
				byte unitType = data[++off];// 单元类型
				byte unitNumber = data[++off];// 单元ID
				off++;
				byte[] bytes1 = new byte[4];
				System.arraycopy(data, off, bytes1, 0, 4);
				off += 4;
				byte[] bytes2 = new byte[4];
				System.arraycopy(data, off, bytes2, 0, 4);
				off += 4;
				byte[] bytes3 = new byte[4];
				System.arraycopy(data, off, bytes3, 0, 4);
				off += 4;
				float dy = data[off] / 10.0f;

				// if (unitType == 4) {
				// HitchBean hitchBean = new HitchBean();
				// hitchBean.setUnittype(unitType);
				// hitchBean.setUnitnumber(unitNumber);
				// Float f3 = FormatTransfer.bytesL2Float2(bytes3);
				// System.out.println("f3:" + f3);
				// hitchBean.setHitchvol(f3);
				// hitchBean.setDate(time);
				// hitchBean.setBatlv(dy);
				// show.checkHitch(hitchBean);
				// if (!hitchList.contains(hitchBean)) {
				// hitchList.add(hitchBean);
				// }
				// } else {
				DataBean databean = new DataBean();
				databean.setDate(time);// 时间
				databean.setUnitType(unitType);
				databean.setUnitNumber(unitNumber);
				UnitBean unit = unitPacket.getUnit(unitType, unitNumber);
				if (unit == null) {
					unit = UnitService.getUnitBean(unitType, unitNumber);
					if (unit == null) {
						System.out.println("单元不存在");
						continue;
					}
				}
				databean.setBatlv(dy);// 电量
				if (unitType == Protocol.UnitTypeSF6) {// ----SF6单元

					if (bytes3[0] == 1) {
						databean.setLowPres(true);
					}
					if (bytes3[1] == 1) {
						databean.setLowLock(true);
					}

					boolean flag = valueSF6(bytes1, databean);// true 数据无效 false 数据有效

					if (flag && !databean.isLowLock() && !databean.isLowPres()) {
						DataService.saveValidCollData(databean);
						continue;
					}
				} else if (unitType == Protocol.UnitTypeSSJ) {
					boolean flag = valueVari(bytes3, databean);
					if (flag) {
						DataService.saveValidCollData(databean);
						continue;
					}
				} else if (unitType == Protocol.UnitTypeWD) {// 温度
					Float f3 = FormatTransfer.bytesL2Float2(bytes3);
					if (unit.isIsinit()) {
						databean.setTemp(f3);
						show.receInitTemp(databean);
						dataList.add(databean);
						continue;
					}
					valueTemp(bytes3, databean);

				} else if (unitType == Protocol.UnitTypeHV) {
					Float f3 = FormatTransfer.bytesL2Float2(bytes3);
					databean.setHitchvol(f3);
				}
				checkData(databean);

				dataList.add(databean);

				show.receData(databean, true);
				// }
			} catch (SQLException e) {
				System.out.println("数据库连接异常");
				e.printStackTrace();
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				System.out.println("数据长度不足");
				aioobe.printStackTrace();
			}
		}

	}

	private void checkData(DataBean dataBean) {
		if (dataList.contains(dataBean)) {
			for (DataBean d : dataList) {
				if (d.equals(dataBean)) {
					if (d.isLowPres() != dataBean.isLowPres()) {
						plusDate(dataBean);
					} else if (d.isLowLock() != dataBean.isLowLock()) {
						plusDate(dataBean);
					} else {
						dataList.remove(dataBean);
					}
					break;
				}
			}
			checkData(dataBean);
		}
	}

	private boolean valueSF6(byte[] bytes, DataBean dataBean) {
		Float f1 = FormatTransfer.bytesL2Float3(bytes, 0, 2, 10000);
		dataBean.setPres(f1);
		dataBean.setDen(f1);
		byte bflag = -1;
		boolean flag = true;
		for (byte b : bytes) {
			if (b != bflag) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	private void valueTemp(byte[] bytes, DataBean dataBean) {
		Float f3 = FormatTransfer.bytesL2Float2(bytes);
		float amtemp = AbcUnitView.getAmtemp();
		float temp = FormatTransfer.newScale(f3, amtemp);
		if (temp < 0) {
			temp = 0;
		}
		dataBean.setTemp(temp);
		UnitBean unitBean = UnitService.getUnitBean(dataBean.getUnitType(), dataBean.getUnitNumber());
		if (temp > 20) {
			if (unitBean.isTempflag()) {
				// 触发高温,已发送高温周期
				return;
			} else {
				// 触发高温,未发送高温周期
				unitBean.setTempflag(true);
				// 触发发送周期
				CollectService.setWarnTempPeriod(unitBean);
			}
		} else {
			if (unitBean.isTempflag()) {
				// 未触发高温报警,已发送高温周期
				unitBean.setTempflag(false);
				// 触发发送周期
				CollectService.setWarnTempPeriod(unitBean);
			} else {
				// 未触发高温报警,未发送高温周期
				return;
			}
		}
	}

	private boolean valueVari(byte[] bytes, DataBean dataBean) {
		Float f3 = FormatTransfer.bytesL2Float2(bytes);
		dataBean.setVari(f3);
		return f3 > 125 || f3 < 0;
	}

	private void plusDate(DataBean dataBean) {
		Date date1 = new Date();
		Date date = dataBean.getDate();
		long timelong = date.getTime();
		timelong += 1000;
		date1.setTime(timelong);
		dataBean.setDate(date1);
	}

}
