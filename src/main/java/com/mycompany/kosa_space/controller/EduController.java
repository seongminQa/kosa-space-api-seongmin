package com.mycompany.kosa_space.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
	교육장, 강의실, 교육과정
*/

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.kosa_space.dto.EduCenter;
import com.mycompany.kosa_space.dto.request.CreateTraineeDTO;
import com.mycompany.kosa_space.service.EduService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EduController {

	@Autowired
	private EduService eduService;
	
	// 교육장 등록
	@PostMapping("/educenter/create")
	public EduCenter create(EduCenter educenter) {
		eduService.centerRegister(educenter);
		return null;
	}
	
	// 경섭
	//교육장 수정
   @PutMapping("/educenter/update")
   public void update(EduCenter eduCenter) {
      log.info("update 메소드 실행");
      log.info(eduCenter.toString());
      //교육장 수정
      eduService.educenterUpdate(eduCenter);
//      log.info(Integer.toString(test));
      //수정된 내용의 EduCenter 객체 얻기
//      eduCenter = eduService.getEducenter(eduCenter.getEcno());

   }
   
   // 성민 ------------------------------------------------------------
   // 교육생 등록
   @PostMapping("/admin/trainee/register")
   public void traineeRegister(CreateTraineeDTO createTraineeDTO) {
	   log.info("traineeRegister 실행");
	   log.info("createTraineeDTO = " + createTraineeDTO);
	   eduService.createTrainee(createTraineeDTO);
   }
	
}
