package com.mycompany.kosa_space.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.kosa_space.dto.Member;

@Mapper
public interface MemberDao {

	public void memberInsert(Member member);

}
