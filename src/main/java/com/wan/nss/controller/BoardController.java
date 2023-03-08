package com.wan.nss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wan.nss.biz.blike.BlikeVO;
import com.wan.nss.biz.board.BoardService;
import com.wan.nss.biz.board.BoardVO;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	// 고양이 자랑 게시판 페이지 진입
	@RequestMapping(value="/boardView.do")
	public String boardView(BoardVO bvo, Model model) {
		
		System.out.println("boardView.do 진입");
		
		model.addAttribute("bList", boardService.selectAll(bvo));
		
		return "board.jsp";
	}

	// 고양이 자랑 게시판 게시글 상세보기 페이지 진입
	@RequestMapping(value="/boardPostView.do")
	public String boardPostView(BoardVO bvo, Model model) {
		
		System.out.println("boardPostView.do 진입");
		
		model.addAttribute("data", boardService.selectOne(bvo));
		
		return "board_post";
		
	}

	// 고양이 자랑 게시판 검색 수행 및 검색결과 페이지 진입
	@RequestMapping(value="/selectAllSearchBoard.do")
	public String selectAllSearchBoard(BoardVO bvo, Model model) {
		
		System.out.println("selectAllSearchBoard.do 진입");
		
		model.addAttribute("bList", boardService.selectAll(bvo));
		
		return "";
		
	}
	
	// 고양이 자랑 게시판 새 게시글 작성하기 페이지 진입
	@RequestMapping(value="/insertBoardView.do")
	public String insertBoardView() {
		
		System.out.println("insertBoardView.do 진입");
		
		return "";
		
	}
	
	// 고양이 자랑 게시판 새 게시글 작성하기 페이지에서 
	// 게시글 작성(insert) 수행 및 해당 글 상세 보기(작성 결과 보기) 페이지로 이동
	@RequestMapping(value="/insertBoard.do")
	public String insertBoard(BoardVO bvo, Model model) {
		
		System.out.println("insertBoard.do 진입");
		
		boardService.insert(bvo);
		
		return "";
		
	}
	
	// 고양이 자랑 게시판 게시글 수정하기 페이지 진입
	@RequestMapping(value="/updateBoardView.do")
	public String updateBoardView(BoardVO bvo, Model model) {
		
		System.out.println("updateBoardView.do 진입");
		
		boardService.update(bvo);
		
		return "";
		
	}
	
	// 고양이 자랑 게시판 게시글 수정하기 페이지에서
	// 게시글 수정(update) 및 해당 글 상세 보기(수정 결과 보기) 페이지로 이동
	@RequestMapping(value="/updateBoard.do")
	public String updateBoard(BoardVO bvo, Model model) {
		
		System.out.println("updateBoard.do 진입");
		
		boardService.update(bvo);
		
		return "";
		
	}
	
	// 고양이 자랑 게시판 게시글 삭제 수행 및 전체 목록으로 이동
	@RequestMapping(value="/deleteBoard.do")
	public String deleteBoard(BoardVO bvo, Model model) {
		
		System.out.println("deleteBoard.do 진입");
		
		boardService.delete(bvo);
		
		return "";
		
	}
	
	// 고양이 자랑 게시글 공유하기
	@RequestMapping(value="/shareBoard.do")
	public String shareBoard(BoardVO bvo, Model model) {
		
		System.out.println("shareBoard.do 진입");
		
		return "";
		
	}
	
	// 고양이 자랑 게시글 좋아요/취소 수행
	@RequestMapping(value="/updateBlike.do")
	public String updateLike(BlikeVO blvo, Model model) {
		
		System.out.println("updateBlike.do 진입");
		
//		// 좋아요 할 때
//		BlikeService.insert(blvo);
		
//		// 좋아요 취소할 때
//		BlikeService.delete(blvo);
		
		return "";
		
	}
}
