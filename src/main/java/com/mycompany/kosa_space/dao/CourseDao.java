package com.mycompany.kosa_space.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.kosa_space.dto.Course;
import com.mycompany.kosa_space.dto.request.CreateTraineeRequestDto;

@Mapper
public interface CourseDao {
	public Course readCourse(String cname);
}
