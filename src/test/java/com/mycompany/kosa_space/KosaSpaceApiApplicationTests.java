package com.mycompany.kosa_space;

import java.text.DecimalFormat;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KosaSpaceApiApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("test");
		
		// 날짜 년도 구하기
//		LocalDate now = LocalDate.now();
//		System.out.println(now);
//		
//		String strYear = String.valueOf(now.getYear());
//		System.out.println(strYear);
		int a = 25;
		String formattedString = String.format("%03d", a);
		System.out.println(formattedString); // 출력: 001
		
	}

}
