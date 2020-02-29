package com.example.demo.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EffectiveMarkRange {
	private String id;//标记快id
	private String text; // 标记块的文本部分
	private int count; // 标记次数
	private String classId;//标记快对应的课堂id

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EffectiveMarkRange)) {
			return false;
		} else {
			EffectiveMarkRange o = (EffectiveMarkRange) obj;
			return this.id.equals(o.id);
		}
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
