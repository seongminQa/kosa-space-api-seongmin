package com.mycompany.kosa_space.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.kosa_space.dto.Member;

@Mapper
public interface MemberDao {
	// 회원가입
	public void memberInsert(Member member);
	// 시큐리티를 이용한 아이디 조회
	public Member selectByMid(String username);

}
