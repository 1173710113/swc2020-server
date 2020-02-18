package com.example.demo.domain;

import java.util.Arrays;

public class EffectiveMarkRange implements Comparable<EffectiveMarkRange> {
	private String rangeText; // 标记块的文本部分
	private int markNum; // 标记次数

	public EffectiveMarkRange(String rangeText, int markNum) {
		super();
		this.rangeText = rangeText;
		this.markNum = markNum;
	}

	public String getRangeText() {
		return rangeText;
	}

	public void setRangeText(String rangeText) {
		this.rangeText = rangeText;
	}

	public int getMarkNum() {
		return markNum;
	}

	public void setMarkNum(int markNum) {
		this.markNum = markNum;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(new int[] { rangeText.hashCode(), Integer.hashCode(markNum) });
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EffectiveMarkRange)) {
			return false;
		} else {
			EffectiveMarkRange o = (EffectiveMarkRange) obj;
			return this.rangeText.equals(o.rangeText) && this.markNum == o.markNum;
		}
	}

	@Override
	public int compareTo(EffectiveMarkRange o) {
		return o.markNum - this.markNum;
	}
}
