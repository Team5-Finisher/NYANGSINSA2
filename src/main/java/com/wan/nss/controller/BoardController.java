package com.wan.nss.controller;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wan.nss.biz.blike.BlikeService;
import com.wan.nss.biz.blike.BlikeVO;
import com.wan.nss.biz.board.BoardService;
import com.wan.nss.biz.board.BoardVO;
import com.wan.nss.biz.image.ImageService;
import com.wan.nss.biz.image.ImageVO;
import com.wan.nss.biz.member.MemberService;
import com.wan.nss.biz.member.MemberVO;

@Controller
public class BoardController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private BoardService boardService;
	@Autowired
	private BlikeService blikeService;
	@Autowired
	private ImageService imageService;
	

	// 고양이 자랑 게시판 페이지 진입
	@RequestMapping(value = "/boardView.do")
	public String boardView(BoardVO bvo, Model model) {

		System.out.println("boardView.do 진입");

		// 전체 자랑글 목록 : bList
		model.addAttribute("bList", boardService.selectAll(bvo));

		// TOP3 자랑글 목록 : top3List
		bvo.setSearchCondition("top3");
		model.addAttribute("top3List", boardService.selectAll(bvo));

		System.out.println();

		return "board.jsp";
	}

	// 고양이 자랑 게시판 게시글 상세보기 페이지 진입
	@RequestMapping(value = "/boardPostView.do")
	public String boardPostView(MemberVO mvo, BoardVO bvo, Model model, HttpSession session) {

		System.out.println("boardPostView.do 진입");
		
		// 게시글 상세페이지에서 수정 버튼 활성화를 위한 memberId 
		mvo.setUserId((String) session.getAttribute("memberId"));
		MemberVO loginMvo = memberService.selectOne(mvo);
		
		// 게시글 상세 데이터
		model.addAttribute("board", boardService.selectOne(bvo));
		model.addAttribute("member", loginMvo);

		return "board_detail.jsp";

	}

	// 고양이 자랑 게시판 검색 수행 및 검색결과 페이지 진입
	@RequestMapping(value = "/selectAllSearchBoard.do")
	public String selectAllSearchBoard(BoardVO bvo, Model model) {

		System.out.println("selectAllSearchBoard.do 진입");

		model.addAttribute("bList", boardService.selectAll(bvo));

		return "board_result.jsp";

	}

	// 고양이 자랑 게시판 새 게시글 작성하기 페이지 진입
	@RequestMapping(value = "/insertBoardView.do")
	public String insertBoardView(MemberVO mvo, HttpSession session, HttpServletResponse response) {
		// TODO
		// 세션 아이디 가져와서
		mvo.setUserId((String) session.getAttribute("memberId"));
		// 역할 가져와서
		MemberVO loginMvo = memberService.selectOne(mvo);

		// 차단된 회원은 보드홈으로 이동(얼럿 띄우기 : 글쓰기 기능이 차단된 회원입니다. 관리자에게 문의하세요.)
		if (loginMvo.getRole().equals("BLOCKED")) {
			try {
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('글쓰기 기능이 차단된 회원입니다. 관리자에게 문의하세요');</script>");
				out.flush();
				return "boardView.do";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			System.out.println("insertBoardView.do 진입");

			return "insert_board.jsp";
		}

	}

	// 고양이 자랑 게시판 새 게시글 작성하기 페이지에서
	// 게시글 작성(insert) 수행 및 해당 글 상세 보기(작성 결과 보기) 페이지로 이동
	@RequestMapping(value = "/insertBoard.do")
	public String insertBoard(BoardVO bvo, Model model, MultipartFile mFile) {

		System.out.println("insertBoard.do 진입");
		// 이미지 객체 생성
		ImageVO ivo = new ImageVO();
		// 가장 최근 게시글 찾아내서 B_NO 가져오기
		bvo.setSearchCondition("newest");
		BoardVO bvo2 = boardService.selectOne(bvo);
		// 게시글 추가
		boardService.insert(bvo);
		
//		ArrayList<ImageVO> images = new ArrayList<ImageVO>();
//		for(int a=0; a <= mFile.getSize(); a++) {
			String image = bvo.getBoardContent();
			// 이미지 태그 값 잘라내기
			String imageUrl = (String) image.subSequence(bvo.getBoardContent().indexOf("boardimages/"), bvo.getBoardContent().indexOf("\" style="));
			System.out.println(imageUrl);
			// 이미지 이름 세팅
			ivo.setImageName(imageUrl);
//			ivo.setImageName(mFile.getOriginalFilename());
			// 이미지 번호 세팅
			ivo.setTargetNum(bvo2.getBoardNum()+1);
			// 이미지 구분 번호 세팅
			ivo.setTypeNum(201);
//			images.add(ivo);
//			for(int b=0; b <= images.size(); b++) {
				// 이미지 추가
				imageService.insert(ivo);
//			}
//		}
		
		return "board_detail.jsp";

	}

	// 고양이 자랑 게시판 게시글 수정하기 페이지 진입
	@RequestMapping(value = "/updateBoardView.do")
	public String updateBoardView(MemberVO mvo, BoardVO bvo, Model model, HttpSession session) {

		System.out.println("updateBoardView.do 진입");

		mvo.setUserId((String) session.getAttribute("memberId"));
		// 수정할 게시글 데이터
		model.addAttribute("board", boardService.selectOne(bvo));
		// 작성자 정보
		model.addAttribute("member", memberService.selectOne(mvo));

		return "update_board.jsp";
	}

	// 고양이 자랑 게시판 게시글 수정하기 페이지에서
	// 게시글 수정(update) 및 해당 글 상세 보기(수정 결과 보기) 페이지로 이동
	@RequestMapping(value = "/updateBoard.do")
	public String updateBoard(BoardVO bvo, Model model) {

		System.out.println("updateBoard.do 진입");

		boardService.update(bvo);

		return "board_detail.jsp";

	}

	// 고양이 자랑 게시판 게시글 삭제 수행 및 전체 목록으로 이동
	@RequestMapping(value = "/deleteBoard.do")
	public String deleteBoard(BoardVO bvo, Model model) {

		System.out.println("deleteBoard.do 진입");

		boardService.delete(bvo);

		return "board.jsp";

	}

	// 고양이 자랑 게시글 공유하기
	@RequestMapping(value = "/shareBoard.do")
	public String shareBoard(BoardVO bvo, Model model) {

		System.out.println("shareBoard.do 진입");

		return "board_detail.jsp";

	}

	// 고양이 자랑 게시글 좋아요/취소 수행
	@ResponseBody
	@RequestMapping(value = "/updateBlike.do")
	public String updateLike(BlikeVO blvo, BoardVO bvo, Model model, HttpServletRequest request) {
		blvo.setUserId((String) request.getSession().getAttribute("memberId"));
		bvo.setUserId((String) request.getSession().getAttribute("memberId"));
		System.out.println("updateBlike.do 진입");
		System.out.println(blvo);

		if (blvo.getUpOrDown().equals("up")) {
			System.out.println("좋아요 업");
			blikeService.insert(blvo);
		} else {
			System.out.println("좋아요 다운");
			blikeService.delete(blvo);
		}

		bvo = boardService.selectOne(bvo);

//		// 좋아요 할 때
//		BlikeService.insert(blvo);

//		// 좋아요 취소할 때
//		BlikeService.delete(blvo);

		return Integer.toString(bvo.getLikeCnt());

	}
}
