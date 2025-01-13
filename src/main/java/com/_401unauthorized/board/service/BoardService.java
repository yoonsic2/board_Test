package com._401unauthorized.board.service;

import com._401unauthorized.board.common.FileManager;
import com._401unauthorized.board.common.Paging;
import com._401unauthorized.board.dao.BoardDao;
import com._401unauthorized.board.dao.MemberDao;
import com._401unauthorized.board.dto.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j

public class BoardService {
    public static final Integer LISTCNT = 10 ;
    private static final int PAGECOUNT = 2;

    private final BoardDao boardDao;
    private final MemberDao memberDao;
    private final FileManager fm;


    public List<BoardDto> getBoardList(Integer pageNum) {

        //select * from board order b_date desc limit 0,10
        //select * from board order b_date desc limit 10,20
        //select * from board order b_date desc limit 20,10
        Map<String,Integer> pageMap = new HashMap<>();
        pageMap.put("startIndex",(pageNum-1)*10);
        pageMap.put("pageSize",10);
        return boardDao.getBoardList(pageMap);

    }

    public String getPaging(SearchDto sDto) {
        int totalNum =boardDao.getBoardCount(sDto);
        log.info("===========totalNum :" + totalNum);
        String listUrl = null;
        if(sDto.getColname()!=null){
            listUrl="/board?colname="+sDto.getColname()+"&keyword="+sDto.getKeyword(); //&pageNum=2
        }else{
            listUrl = "/board?"; //pageNum=2
        }
        Paging paging = new Paging(totalNum,sDto.getPageNum(),sDto.getListcnt(),PAGECOUNT,listUrl);
        return paging.makeHtmlPaging();
    }

    public List<BoardDto> getBoardList(SearchDto sDto) {
        Integer pageNum = sDto.getPageNum();
        //페이지 번호를 limit 시작 번호로 변경 1p:idx(0)~, 2p:idx(10)~
        sDto.setStartIdx((pageNum-1)* BoardService.LISTCNT);
        return boardDao.getBoardListSearch(sDto);
    }

    public List<BoardDto> getBoardListSearch(SearchDto sDto) {
        Integer pageNum =sDto.getPageNum();
//        sDto.setStartIdx((pageNum-1)*LISTCNT);
        sDto.setStartIdx((pageNum-1)* sDto.getListcnt());
        return boardDao.getBoardListSearch(sDto);
    }

    public List<BoardDto> getBoardListSearchNew(SearchDto sDto) {
        Integer pageNum =sDto.getPageNum();
//        sDto.setStartIdx((pageNum-1)*LISTCNT);
        sDto.setStartIdx((pageNum-1)* sDto.getListcnt());
        return boardDao.getBoardListSearch(sDto);
    }

    public BoardDto getBoardDetail(Integer b_num) {
        // return boardDao.getBoardDetail(b_num);
        return boardDao.getBoardDetailWithFiles(b_num);
    }

    @Transactional
    public void boardDelete(Integer bNum, HttpSession session) {
        // 1. 자식 데이터 삭제 (댓글리스트)
        List<ReplyDto> replyDtoList = boardDao.getReplyList(bNum);
        if(replyDtoList != null && replyDtoList.size() > 0){
            if(boardDao.deleteReply(bNum) == false){
                log.info("===deleteReply 예외발생");
                throw new RuntimeException();  // rollback
            }
        }
        // 2. 자식 데이터 삭제 (파일리스트)
        String[] sysfiles = boardDao.getSysFileName(bNum);
        if(sysfiles != null && sysfiles.length > 0){
            if(boardDao.deleteBoardFile(bNum) == false){
                log.info("====deleteBoardFile");
                throw new RuntimeException();
            }
        }
        // 3. 부모 데이터 삭제 (원글)
        if(!boardDao.boardDelete(bNum)){
            log.info("===boardDelete 예외발생");
            throw new RuntimeException();
        }
        // 4. upload 폴더에 파일 삭제
        if(sysfiles.length > 0){
            fm.fileDelete(sysfiles, session);  // session or realPath
        }
        //return true;  // 삭제 성공
    }

    public List<ReplyDto> getReplyList(Integer bNum) {
        return boardDao.getReplyList(bNum);
    }

    public List<ReplyDto> insertReply(ReplyDto replyDto) {
        List<ReplyDto> rList = null;
        if(boardDao.insertReply(replyDto)){
            rList = boardDao.getReplyList(replyDto.getR_bnum());
        }
        return rList;
    }

    public boolean boardWrite(BoardDto board, HttpSession session) {
        // 1. 글번호, 글제목, 글내용, 글쓴이... insert board 즉시 바로 글번호(b_num) 반환
        // 1-1. select b_num from board;  // 글번호 읽어와 아래에 줘야해
        // 2. 만약 첨부파일 있다면, 글번호, 원파일명, 난수파일명... insert boardfile
        boolean result = boardDao.boardWriteSelectKey(board);
        log.info("새글 번호:{}", board.getB_num());
        if(result){
            // 글쓰기 마다 회원에게 point 10점 부여
            MemberDto memberDto = (MemberDto) session.getAttribute("member");
            int point = memberDto.getM_point()+10;
            if(point > 999){
                point=999;
            }
            memberDto.setM_point(point);
            memberDao.updateMemberPoint(memberDto); // id=dbstlr2, point=10
            MemberDto member = memberDao.getMemberInfo(memberDto.getM_id());// 등급포함 최신정보
            session.setAttribute("member",member); // 세션에 새로운 회원정보 갱신
            // 첨부파일 여부 확인
            if(!board.getAttachments().get(0).isEmpty()){
                // 파일업로드, DB insert
                if(fm.fileUpload(board.getAttachments(), session, board.getB_num())){
                    log.info("====upload OK;");
                    return true; // 첨부파일+글쓰기 성공(1)
                }
            }
            return true;   // 첨부파일 없이 글쓰기 성공(0)
        }else{
            return false;  // 글쓰기 실패(-1)
        }
    }

    public List<BoardFile> getBfList(Integer bNum) {
        return boardDao.getBfList(bNum);
    }

    public ResponseEntity<Resource> fileDownload(BoardFile boardFile, HttpSession session) throws IOException {
        return fm.fileDownload(boardFile, session);
    }
} 