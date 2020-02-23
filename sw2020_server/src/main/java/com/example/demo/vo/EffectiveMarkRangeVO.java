package com.example.demo.vo;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EffectiveMarkRangeVO implements Comparable<EffectiveMarkRangeVO> {
	private String text; // 标记块的文本部分
	private int count; // 标记次数

	@Override
	public int hashCode() {
		return Arrays.hashCode(new int[] { text.hashCode(), Integer.hashCode(count) });
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EffectiveMarkRangeVO)) {
			return false;
		} else {
			EffectiveMarkRangeVO o = (EffectiveMarkRangeVO) obj;
			return this.text.equals(o.text) && this.count == o.count;
		}
	}

	@Override
	public int compareTo(EffectiveMarkRangeVO o) {
		return o.count - this.count;
	}
}
