package com.mycompany.kosa_space.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

/*
	로그인, 회원가입, 아이디 찾기, 비밀번호 찾기
	--> 권한 인증 관련해서 처리
*/

import org.springframework.web.bind.annotation.RestController;

import com.mycompany.kosa_space.dto.Member;
import com.mycompany.kosa_space.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@PostMapping("/signup")
	public void signUp(Member member) {
		authService.memberRegister(member);
		log.info(member.toString());
	}
}
