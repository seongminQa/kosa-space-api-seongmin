package com.mycompany.kosa_space.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.kosa_space.dto.EduCenter;

@Mapper
public interface EduCenterDao {

	public int centerInsert(EduCenter educenter);
	public int updateByEcno(EduCenter educenter);
	public EduCenter selectByEcno(int ecno);
}