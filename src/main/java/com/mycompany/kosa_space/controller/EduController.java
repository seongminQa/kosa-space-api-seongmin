package com.mycompany.kosa_space.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/*
	교육장, 강의실, 교육과정
*/

import org.springframework.web.bind.annotation.RestController;

import com.mycompany.kosa_space.dto.EduCenter;
import com.mycompany.kosa_space.service.EduService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EduController {
	@GetMapping("/")
	public void test() {
		log.info("테스트 실행");
	}
	
	// 포스트맨에서 객체 값 얻기 테스트
	@PostMapping("/login")
	public Map<String, String> userLogin(String mid, String mpassword) {
		log.info("login 실행");
		Map<String, String> map = new HashMap<>();
		map.put("mid", mid);
		map.put("mpassword", mpassword);
		
		return map;
	}
	
	// 
	
	@Autowired
	private EduService eduService;
	
	// 교육장 등록
	@PostMapping("/educenter/create")
	public EduCenter create(EduCenter educenter) {
		eduService.centerRegister(educenter);
		return null;
	}
	
	
}
