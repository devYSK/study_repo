package com.ys.actuator.custom;

import org.springframework.stereotype.Component;

@Component
public class MyStockManager {

	public long getStock() {
		// 재고수량을 리턴해야 하지만, 테스트용도이니 현재시간의 long 값을 재고수량으로 대체함.
		return System.currentTimeMillis();
	}
}