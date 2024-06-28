package com.mycompany.kosa_space.service;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.kosa_space.dao.EduCenterDao;
import com.mycompany.kosa_space.dto.EduCenter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EduService {
	@Autowired
	private EduCenterDao eduCenterDao;

	public int centerRegister(EduCenter educenter) {
		// 첨부파일이 있다면.. --> (우리는 첨부파일을 필수로 받고있다. 따라서 조건을 반대로 바꾸어야 함.) --> 일단 실습파일 따라해보고 수정
		if (educenter.getEcattachdata() != null && !educenter.getEcattachdata().isEmpty()) {
			MultipartFile mf = educenter.getEcattachdata();
			// 파일 이름 설정
			educenter.setEcattachoname(mf.getOriginalFilename());
			// 파일 종류 설정
			educenter.setEcattachtype(mf.getContentType());
			try {
			      // 파일 데이터 설정
			      educenter.setEcattach(mf.getBytes());
			   } catch (IOException e) {
		   }
		}
//		// MultipartFile 타입으로 첨부파일의 값을 받음
//		MultipartFile mf = educenter.getEcattachdata();
//		// 파일 이름 설정
//		educenter.setEcattachoname(mf.getOriginalFilename());
//		// 파일 종류 설정
//		educenter.setEcattachtype(mf.getContentType());
//		// 파일 데이터 설정
//		try {
//			educenter.setEcattach(mf.getBytes());
//		} catch (IOException e) {
//			log.info("첨부파일 byte배열 형식 변환 실패");
//		}		
		
		/* 우리가 입력받은 educenter에는 eccreatedat(등록일시)와 
		 * ecupdatedat(수정일시)를 제외한 모든 데이터는 값을 가진다.
		 * 등록일시는 필수값, 수정일시는 update를 적용할 때만 값을 넣어준다.
		 * --> DB 시간으로 넣어준다. sysdate
		*/

	    log.info(educenter.toString());

		return eduCenterDao.centerInsert(educenter);
	}
	
	// 경섭
	//교육장 수정
	public int educenterUpdate(EduCenter eduCenter) {
		log.info("educenterUpdate 실행");
      if(eduCenter.getEcattachdata() != null && !eduCenter.getEcattachdata().isEmpty()) {
         MultipartFile mf = eduCenter.getEcattachdata();
         eduCenter.setEcattachoname(mf.getOriginalFilename());
         eduCenter.setEcattachtype(mf.getContentType());
         try {
            eduCenter.setEcattach(mf.getBytes());
         } catch(IOException e) {
            
         }
      }
      log.info("attach 확인 실행");
      
      return eduCenterDao.updateByEcno(eduCenter);
   }

   //교육장 수정시 수정된 교육장 가져오기
   public EduCenter getEducenter(int ecno) {
      EduCenter eduCenter = eduCenterDao.selectByEcno(ecno);
      return eduCenter;
   }
	
}
