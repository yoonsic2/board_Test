package com._401unauthorized.board.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com._401unauthorized.board.dto.BoardFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLEncoder;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com._401unauthorized.board.dao.BoardDao;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class FileManager {

	@Autowired
	private BoardDao bDao;
    //private fullPath="c:/data/upload"
	// 파일 업로드, DB저장
	public boolean fileUpload(List<MultipartFile> attachments, HttpSession session, int b_num) {
		log.info("FileManager class");
		// 프로젝트의 upload경로 찾기
		String realPath = session.getServletContext().getRealPath("/");
		log.info("realPath={}", realPath); // ..../webapp/
		realPath += "upload/";
		log.info("realPath={}", realPath); // ..../webdapp/upload/

		// 2.폴더 생성을 꼭 할것(upload 폴더가 삭제를 대비해서)
		File dir = new File(realPath);
		if (dir.isDirectory() == false) { // upload폴더 없다면
			dir.mkdir(); // upload폴더 생성
		}
		//파일의 정보를 BoardFile or HashMap에 저장
		//Map 또는 BoardFile 사용 가능
		Map<String, String> fMap = new HashMap<>();
		fMap.put("b_num", String.valueOf(b_num));  
		//System.out.println("size:"+attachments.size());
		boolean result = false;
		for( MultipartFile mf : attachments) {
			// 파일 메모리에 저장
			String oriFileName = mf.getOriginalFilename();
			if(oriFileName.equals("")){
				return false;
			}
			System.out.println("원조 파일:"+oriFileName);
			fMap.put("oriFileName", oriFileName);
			// 4.시스템파일이름 생성 a.txt ==>112323242424.txt

			//UUID 활용 식별자 생성후 파일명으로
			//String 확장자 = oriFileName.substring(oriFileName.lastIndexOf(".") + 1);
			String 확장자 = FilenameUtils.getExtension(oriFileName); // jpg
			String sysFileName = UUID.randomUUID().toString()+"."+확장자;

			//String sysFileName = System.currentTimeMillis() + "."
			//		+ oriFileName.substring(oriFileName.lastIndexOf(".") + 1);
			System.out.println("서버 파일:"+sysFileName);
			fMap.put("sysFileName", sysFileName);
			
			// 5.메모리->실제 파일 업로드
			try {
				mf.transferTo(new File(realPath + sysFileName)); // 서버upload에 파일 저장
				//Map버전
				result= bDao.fileInsertMap(fMap);
				if(result==false) break;
			} catch (IOException e) {
				System.out.println(e.getMessage());
				System.out.println("파일업로드 예외 발생");
				e.printStackTrace();
				result=false;
				break;  //반복 중단
			}
		} // For end
		return result;
	}
	// 파일 다운로드
	public ResponseEntity<Resource> fileDownload(BoardFile bfile, HttpSession session)
			throws IOException {
		log.info("fileDownload()");
		// 파일 저장 경로
		String realpath = session.getServletContext().getRealPath("/");  // ..../webapp/
		log.info("rootpath={}", realpath);
		realpath += "upload/" + bfile.getBf_sysfilename();
		// 실제 파일이 저장된 하드디스크까지 경로를 수립.
		InputStreamResource fResource = new InputStreamResource(new FileInputStream(realpath));

		//MIME (Multipurpose Internet Mail Extensions) 타입
		//인터넷에서 다양한 유형의 파일(텍스트, 이미지, 비디오, 오디오 등)을 전송하기 위해 사용되는 표준 형식
		//주 타입/하위 타입
		//text/plain: 일반 텍스트 파일.
		//image/jpeg: JPEG 형식의 이미지.
		//application/json: JSON 형식의 데이터.
		//application/octet-stream: 일반 바이너리 파일 (주로 다운로드 용도로 사용).
		String mimeType = Files.probeContentType(Paths.get(realpath));
		log.info("==========mimeType={}", mimeType);
		// 파일명이 한글인 경우의 처리(UTF-8로 인코딩)
		String fileName = URLEncoder.encode(bfile.getBf_orifilename(), "UTF-8");
		if (mimeType != null && mimeType.startsWith("image")) {
			// 이미지 파일이면 브라우저로 열기
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(mimeType))
					.cacheControl(CacheControl.noCache())
					.body(fResource);
		} else {
			// 이미지 파일이 아니면 다운로드
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_OCTET_STREAM)  //다운로드
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
					//브라우저는 항상 서버로부터 최신 버전의 리소스를 다운로드
					.cacheControl(CacheControl.noCache())
					.body(fResource);
		}
	}  //fileDownload end
	
	public void fileDelete(String[] sysfiles, HttpSession session) {
		String realPath = session.getServletContext().getRealPath("/");
		realPath += "upload/";
		log.info("delete realPath: {}",realPath);
		for (String sysname : sysfiles) {  
			File file = new File(realPath + sysname);
			log.info("++++AbsoluteFile: {}",file.getAbsoluteFile());
			if (file.exists()) {
				file.delete();
				log.info("서버 파일 삭제 완료");
			} else {
				log.info("이미 삭제된 파일");
			}
		}//for end
	}
}
