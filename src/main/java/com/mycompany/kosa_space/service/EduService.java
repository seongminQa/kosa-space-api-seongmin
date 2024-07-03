package com.mycompany.kosa_space.service;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.kosa_space.dao.EduCenterDao;
import com.mycompany.kosa_space.dao.MemberDao;
import com.mycompany.kosa_space.dao.TraineeInfoDao;
import com.mycompany.kosa_space.dto.EduCenter;
import com.mycompany.kosa_space.dto.Member;
import com.mycompany.kosa_space.dto.request.CreateTraineeDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EduService {
	@Autowired
	private EduCenterDao eduCenterDao;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private TraineeInfoDao traineeInfoDao;

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

    // 교육장 수정시 수정된 교육장 가져오기
    public EduCenter getEducenter(int ecno) {
       EduCenter eduCenter = eduCenterDao.selectByEcno(ecno);
       return eduCenter;
    }
    
    // 교육생 등록
	public void createTrainee(CreateTraineeDTO createTraineeDTO) {
		
		// 프로필 이미지 첨부는 필수이기 때문에 if문을 쓸 이유가 없다. 추후에 바꿔보자.
		if(createTraineeDTO.getTprofileimg() != null && !createTraineeDTO.getTprofileimg().isEmpty()) {
			// 첨부파일이 넘어왔을 경우 처리
			MultipartFile mf = createTraineeDTO.getTprofileimg();
			// 파일 이름을 설정
			createTraineeDTO.setTprofileoname(mf.getOriginalFilename());
			// 파일 종류를 설정
			createTraineeDTO.setTprofiletype(mf.getContentType());
			try {
				// 파일 데이터를 설정
				createTraineeDTO.setTprofiledata(mf.getBytes());
			} catch (IOException e) {
			}
		}
		
		// 프론트 단에서 데이터 유효성을 거쳐 받은 데이터로 가정.
		// 1. 해당 교육생의 mid 만들어주기.
		Member member = new Member();
		
		LocalDate now = LocalDate.now();
		String strYear = String.valueOf(now.getYear());
		
		
		// 2. 비밀번호를 '12345'로 세팅해주기.
		
		// 3. member 객체에 담아 member 테이블에 넣어주기.
		
		// 4. traineeinfo 객체에 담아 traineeinfo 테이블에 넣어주기. 
		
		// 교육생 정보 집어넣기
//		memberDao.insertMember();
//		traineeInfoDao.insertTraineeInfo();
		
	   
	   log.info(createTraineeDTO.toString());
	}
	
}
