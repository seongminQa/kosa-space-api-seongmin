package com.mycompany.kosa_space.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.kosa_space.dao.MemberDao;
import com.mycompany.kosa_space.dto.Member;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {

	@Autowired
	MemberDao memberDao;
	
	public void memberRegister(Member member) {
		/* 파라미터의 member 객체에는
		 * 아이디, 비밀번호, 이름, 휴대폰 번호, 이메일 등의 정보가 담겨있다.
		 * 따라서 mrole, menable의 값을 셋팅해주면 된다.
		 */
		
		if(member.getMid().substring(0,3).equals("kosa")) {
			member.setMrole("ROLE_ADMIN");			
		} else {
			member.setMrole("ROLE_USER");
		}
		member.setMenable(true);
		
		memberDao.memberInsert(member);
	}
}
