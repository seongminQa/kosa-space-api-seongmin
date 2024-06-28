package com.mycompany.kosa_space.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

/*
	로그인, 회원가입, 아이디 찾기, 비밀번호 찾기
	--> 권한 인증 관련해서 처리
*/

import org.springframework.web.bind.annotation.RestController;

import com.mycompany.kosa_space.dto.Member;
import com.mycompany.kosa_space.security.JwtProvider;
import com.mycompany.kosa_space.security.KosaUserDetails;
import com.mycompany.kosa_space.security.KosaUserDetailsService;
import com.mycompany.kosa_space.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AuthController {
	@Autowired
	AuthService authService;
	
	// 로그인 ------------------------------------
	// 로그인 요청 컨트롤러
	@PostMapping("/login")
	public Map<String, String> login(String mid, String mpassword) {
		log.info("login 컨트롤러 실행");
		log.info("패스워드 : " + mpassword);
		// 응답 생성과 동시에 매핑
		Map<String, String> map = authService.userCheck(mid, mpassword);
		
		log.info("응답 생성 완료");
		return map;
	}
	
	// 회원가입 ------------------------------------
	// 회원가입 요청 컨트롤러
	@PostMapping("/signup")
	public void signUp(Member member) {
		log.info(member.getMid());
		log.info(member.getMid().substring(0,4));
		authService.memberRegister(member);
		log.info(member.toString());
	}
	
//	@GetMapping("member/list")
//	public List<Member> getMemberList() {
//		List<Member> member = authService.getMemberAllRead();
//	}
	
}
