package com.mycompany.kosa_space.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycompany.kosa_space.dao.MemberDao;
import com.mycompany.kosa_space.dto.Member;
import com.mycompany.kosa_space.security.JwtProvider;
import com.mycompany.kosa_space.security.KosaUserDetails;
import com.mycompany.kosa_space.security.KosaUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {
	@Autowired
	MemberDao memberDao;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private KosaUserDetailsService userDetailsService;
	
	// 회원가입
	public void memberRegister(Member member) {
		/* 파라미터의 member 객체에는
		 * 아이디, 비밀번호, 이름, 휴대폰 번호, 이메일 등의 정보가 담겨있다.
		 * 따라서 mrole, menable의 값을 셋팅해주면 된다.
		 * '회원가입' 부분에 있어서 '운영진' 로그인 페이지와 '교육생' 로그인 페이지를 분리시킬지 고려해야 함.
		 * 	--> 이에 따라 권한 부여에 관한 비즈니스 로직이 변경 될 것임. 
		 */
		
		// 비밀번호를 DB에 암호화하여 저장한다.
		// 따라서 비밀번호를 암호화하기 위한 인코더 변수 선언과 초기화
		PasswordEncoder passwordEncoder = PasswordEncoderFactories
				.createDelegatingPasswordEncoder();
		
		// 가입할 회원의 비밀번호에 인코더를 적용하여 세팅
		member.setMpassword(passwordEncoder.encode(member.getMpassword()));
		
		/* 
		 	가입하는 회원에 대한 권한 부여
		 	회원가입은 '운영진'만 가능하도록 설계함.
		 	따라서 '운영진'의 권한을 얻으려면 앞 글자는 'kosa'를 붙여야 함.
		 	Ex) kosa001
		 */
		if(member.getMid().substring(0,4).equals("kosa")) {
			member.setMrole("ROLE_ADMIN");			
		} else {
			member.setMrole("ROLE_USER");
		}
		
		// 아이디 활성화
		member.setMenable(true);
		
		// 회원가입 처리 (Dao의 추상 메소드를 사용하여 DB에 값을 저장) 
		memberDao.memberInsert(member);
	}
	
	// 로그인
	public Map<String, String> userCheck(String mid, String mpassword) {
		log.info("userCheck 실행");
		// # 사용자 상세 정보 얻기 (KosaUserDetailsService에서 유저의 아이디가 없다면 예외를 발생시키도록 했음.)
		// 1. loadUserByUsername(mid)는 DB로부터 사용자가 입력한 mid에 해당하는 Member 객체를 가져온다.
		// 2. 강제 형변환을 사용하여 userDetails 객체에 Member 객체의 값을 저장한다. 
		KosaUserDetails userDetails = (KosaUserDetails) userDetailsService.loadUserByUsername(mid);
		
		// 비밀번호 체크 ( DB에서 '{알고리즘} 암호화된 비밀번호' 형식을 기억해보자. )
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		
		// 매개값 첫번째 : 사용자에게 입력받은 Password
		// 매개값 두번째 : DB에서 암호화된 Password
		boolean checkResult = passwordEncoder.matches(
				mpassword, userDetails.getMember().getMpassword());
		
		// Spring 시큐리티 인증 처리
		if(checkResult) {
			Authentication authentication =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		// 응답 생성 (Response)
		Map<String, String> map = new HashMap<>();
		
		if(checkResult) {
			// true일 경우 AccessToken을 생성한다.
			// JwtProvider의 createAccessToken메소드를 사용하여 토큰 생성 후 accessToken 변수에 저장
			String accessToken = jwtProvider.createAccessToken(mid, userDetails.getMember().getMrole());
			
			// JSON 응답 구성
			map.put("result", "success");
			map.put("mid", mid);
			map.put("accessToken", accessToken);
		} else {
			map.put("result", "fail");
		}
		
		return map;
	}
}
