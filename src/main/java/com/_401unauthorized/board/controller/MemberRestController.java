package com._401unauthorized.board.controller;
import com._401unauthorized.board.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberRestController {
    @Autowired
    private MemberService memberService;

//    @GetMapping("/idcheck")
//    public ResponseEntity<String> idcheck(String m_id) {
//        log.info("idcheck m_id:{}", m_id);
//        ResponseEntity<String> result = null;
//        if(!memberService.isUsedId(m_id)) {
//            result = ResponseEntity.ok().body("사용할 수 있는 ID지롱!");
//        } else {
//            result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("이미 사용하는 ID인걸..");
//        }
//        return result;
//    }

    // 위에꺼 짧게 단축 버전
    @GetMapping("/idcheck")
    public ResponseEntity<Boolean> idcheck(String m_id) {
        log.info("idcheck m_id:{}", m_id);
         return ResponseEntity.ok(!memberService.isUsedId(m_id));
        //if(!memberService.isUsedId(m_id)) {
        //    return ResponseEntity.ok().body("사용할 수 있는 ID지롱!");
        //} else {
        //    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("이미 사용 중인 ID인걸..");
        //}
    }
}