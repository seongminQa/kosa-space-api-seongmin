package com.mycompany.kosa_space.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
	교육장, 강의실, 교육과정
*/

import org.springframework.web.bind.annotation.RestController;

import com.mycompany.kosa_space.dto.EduCenter;
import com.mycompany.kosa_space.dto.request.CreateTraineeRequestDto;
import com.mycompany.kosa_space.dto.request.UpdateTraineeRequestDto;
import com.mycompany.kosa_space.dto.response.TraineeResponseDto;
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
   public void traineeRegister(@RequestParam("ecname") String ecname,
		   @RequestParam("cname") String cname, CreateTraineeRequestDto request) {
	   log.info("traineeRegister 실행");
	   log.info("createTraineeDTO = " + request);
	   log.info("ecname = " + ecname);
	   log.info("cname = " + cname);
	   eduService.createTrainee(request);
   }

   // 교육생 단건 조회
   @GetMapping("/admin/trainee/info")
   public TraineeResponseDto traineeInfo(@RequestParam String mid) {
	   log.info("교육생 단건 조회 실행");
	   log.info("mid = " + mid);
	   return eduService.infoTrainee(mid);
   }
   
   // 교육생 수정
   @PutMapping("/admin/trainee/update")
   public void traineeUpdate(@RequestParam String mid, UpdateTraineeRequestDto request) {
	   log.info("교육생 수정 실행");
	   log.info("mid = " + mid);
	   log.info("request = " + request);
	   eduService.updateTrainee(mid, request);
   }
   
}
