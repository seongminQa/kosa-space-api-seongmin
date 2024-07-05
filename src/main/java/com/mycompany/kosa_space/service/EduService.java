package com.mycompany.kosa_space.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.kosa_space.dao.CourseDao;
import com.mycompany.kosa_space.dao.EduCenterDao;
import com.mycompany.kosa_space.dao.MemberDao;
import com.mycompany.kosa_space.dao.TraineeInfoDao;
import com.mycompany.kosa_space.dto.Course;
import com.mycompany.kosa_space.dto.EduCenter;
import com.mycompany.kosa_space.dto.Member;
import com.mycompany.kosa_space.dto.TraineeInfo;
import com.mycompany.kosa_space.dto.request.CreateTraineeRequestDto;
import com.mycompany.kosa_space.dto.request.UpdateTraineeRequestDto;
import com.mycompany.kosa_space.dto.response.TraineeResponseDto;

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
	
	@Autowired
	private CourseDao courseDao;

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
    
    // ------------------------------------------------------------------
    // 교육생 등록 (성민)
    @Transactional
	public void createTrainee(CreateTraineeRequestDto request) {
		
		// 프론트 단에서 데이터 유효성을 거쳐 받은 데이터로 가정.
		// 해당 교육생의 mid 만들어주기. --> 등록되어있는 ccode에서 가져오면 됨
		// cname을 넘기고, 현재 등록되어 있는 인원수 세기 --> 현재 교육생을 등록하는 과정이기 때문에 조회를 못함
		int cnt = traineeInfoDao.readTraineeCnt(request.getCname()) + 1;
		log.info(String.valueOf(cnt)); // O
		// cno, ccode, ctotalnum, cstatus 값 가져오기. 
		Course course = courseDao.readCourse(request.getCname());
		log.info(course.toString()); // O
		
		if(cnt < course.getCtotalnum()) {
			// mid생성. "연도 + 과정코드 + 등록순" -> ex) "2024M2001"
			String formatter = String.format("%03d", cnt);
			String mid = course.getCcode() + formatter;
			log.info("mid = " + mid);
			
			PasswordEncoder passwordEncoder = PasswordEncoderFactories
					.createDelegatingPasswordEncoder();

			// member에 값 넣어주기 --> builder()이용
			Member member = Member.builder()
					.mid(mid)
					.mpassword(passwordEncoder.encode("12345"))
					.mname(request.getMname())
					.mphone(request.getMphone())
					.memail(request.getMemail())
					.mrole("ROLE_USER")
					.menable(true)
					.build();
			
			// DB에 값 넣기.
			memberDao.insert(member);
			log.info("member 삽입 성공");
			
			// traineeinfo에 값 넣어주기
			TraineeInfo traineeInfo = TraineeInfo.builder()
					.mid(mid)
					.cno(course.getCno())
					.tsex(request.isTsex())
					.tage(request.getTage())
					.tpostcode(request.getTpostcode())
					.taddress(request.getTaddress() +  " " + request.getTaddressdetail())
					.tfield(request.isTfield())
					.tacademic(request.getTacademic())
					.tschoolname(request.getTschoolname())
					.tmajor(request.getTmajor())
					.tminor(request.getTminor())
					.tgrade(request.getTgrade())
					.tstatus(course.getCstatus())
					.build();
			// # 문제점 : 우편번호와 주소는 Daum 주소 API를 사용. 따라서 값을 따로 입력하는 경우에 프론트에서 null로 넘어온다.
			// '우편번호 찾기' 버튼을 눌러야만 값이 잘 넘어옴. //-> 프론트 단에서 수정이 필요함.
			
			// 프로필 이미지 첨부는 필수이기 때문에 if문을 쓸 이유가 없다. 추후에 바꿔보자.
			if(request.getTprofiledata() != null && !request.getTprofiledata().isEmpty()) {
				// 첨부파일이 넘어왔을 경우 처리
				MultipartFile mf = request.getTprofiledata();
				// 파일 이름을 설정
				traineeInfo.setTprofileoname(mf.getOriginalFilename());
				// 파일 종류를 설정
				traineeInfo.setTprofiletype(mf.getContentType());
				try {
					// 파일 데이터를 설정
					traineeInfo.setTprofileimg(mf.getBytes());
				} catch (IOException e) {
				}
			}
			
			// DB에 값 넣기.
			traineeInfoDao.insert(traineeInfo);
			log.info("traineeInfo 삽입 성공");
		}

	}

    // ------------------------------------------------------------------
    // 교육생 단건 조회 (성민)
	public TraineeResponseDto infoTrainee(String mid) {
		log.info("infoTrainee 서비스 실행");
		log.info("mid = " + mid);
		
		Member member = memberDao.selectByMid(mid);
		log.info("member = " + member.toString());
		
		TraineeInfo traineeInfo = traineeInfoDao.selectByMid(mid);
		log.info("traineeInfo = " + traineeInfo.toString());
		
		// 조인문 만들어서 단번에 TraineeResponseDTO 객체에 삽입하여 반환할 수 있는지?
		
		TraineeResponseDto response = traineeInfoDao.detailInfo(mid);
		log.info("response 데이터 = " + response.toString());

		return response;
	}

	// 교육생 수정
	@Transactional
	public void updateTrainee(String mid, UpdateTraineeRequestDto request) {
		log.info("updateTrainee 실행");
		log.info("mid = " + mid);
	    log.info("request = " + request);
	    
	    // 값에 예외가 있는지는 우선 프론트에서 유효성을 검사.
	    // 7.5 현재는 수정버튼을 눌렀을 때, 조회된 값을 띄워주고 그 부분에 변경된 값을 넣어주기만 한다.
	    
		if(request.getTprofiledata() != null && !request.getTprofiledata().isEmpty()) {
			// 첨부파일이 넘어왔을 경우 처리
			MultipartFile mf = request.getTprofiledata();
			// 파일 이름을 설정
			request.setTprofileoname(mf.getOriginalFilename());
			// 파일 종류를 설정
			request.setTprofiletype(mf.getContentType());
			try {
				// 파일 데이터를 설정
				request.setTprofileimg(mf.getBytes());
			} catch (IOException e) {
			}
		}
		
		log.info("traineeInfoDao.updateTrainee(request) 실행 전");
		traineeInfoDao.updateTrainee(request);
		log.info("traineeInfoDao.updateTrainee(request) 실행 완료");
		
		log.info("memberDao.updateTrainee(request) 실행 전");
		memberDao.updateTrainee(request);
		log.info("memberDao.updateTrainee(request) 실행 완료");
		
	}
	
}
