package domain;

/**
 * 温度周期实体类
 */
public class WdPeriod {

	private byte wd1, wd2;
	private byte jg2, jg1;

	public WdPeriod(byte wd1, byte wd2, byte jg1, byte jg2) {
		this.wd1 = wd1;
		this.wd2 = wd2;
		this.jg1 = jg1;
		this.jg2 = jg2;
	}

	public byte getWd1() {
		return wd1;
	}

	public void setWd1(byte wd1) {
		this.wd1 = wd1;
	}

	public byte getWd2() {
		return wd2;
	}

	public void setWd2(byte wd2) {
		this.wd2 = wd2;
	}

	public byte getJg2() {
		return jg2;
	}

	public void setJg2(byte jg2) {
		this.jg2 = jg2;
	}

	public byte getJg1() {
		return jg1;
	}

	public void setJg1(byte jg1) {
		this.jg1 = jg1;
	}

}
