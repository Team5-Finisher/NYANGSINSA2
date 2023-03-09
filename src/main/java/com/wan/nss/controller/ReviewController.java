package com.wan.nss.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.wan.nss.biz.product.ProductService;
import com.wan.nss.biz.product.ProductVO;
import com.wan.nss.biz.review.ReviewDAO;
import com.wan.nss.biz.review.ReviewService;
import com.wan.nss.biz.review.ReviewVO;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ProductService productService;

	//리뷰 페이지 이동 (이미 쓴 리뷰를 또 쓰려고 접근하면 돌려보냄)
	@RequestMapping(value="/reviewPage.do")
	public String reviewView(ReviewVO rvo,ProductVO pvo,Model model,HttpSession session,
			HttpServletResponse response) {
		String userId = (String)session.getAttribute("userId");//로그인한 회원아이디
	    
		ArrayList<ReviewVO> rdatas = reviewService.selectAll(rvo); //리뷰 전체보기
		
		System.out.println("pNum: " + pvo.getpNum()); 	
		pvo = productService.selectOne(pvo); // 리뷰 작성 창 세팅하기

		model.addAttribute("pNum", pvo.getpNum());
		//model.addAttribute("pImgUrl", pvo.getpImgUrl());
		model.addAttribute("pName", pvo.getpName());
		try {
			
		for (ReviewVO v : rdatas) {
			if (v.getUserId().equals(userId)) { // 해당 상품의 리뷰목록의 작성자 중에 현재 로그인한 회원이 있을 경우
				response.getWriter().println("<SCRIPT>alert('이미 리뷰를 작성하셨습니다.'); window.close();</SCRIPT>"); // 리뷰 작성 창 닫기
				return "order_detail.jsp";
			}
		}
		}catch(Exception e) {
			System.out.println("리뷰페이지 이동 오류");
			e.printStackTrace();
		}
		
		return "review.jsp";
	}

	// 사용자 리뷰 추가
	@RequestMapping(value="/insertReview.do")
	public String insertReview(ReviewVO rvo, Model model,HttpServletResponse response) {
		response.setContentType("text/html; charset=utf-8");

		try {
			if(reviewService.insert(rvo)) { // 업데이트 성공  
				response.getWriter().println("<SCRIPT>alert('리뷰가 등록되었습니다.'); window.close();</SCRIPT>");
			}
			else { // 실패 시 
				response.getWriter().println("<SCRIPT>alert('에러 발생. 잠시 후 다시 시도해주세요.'); window.close();</SCRIPT>");
			}
		}catch(Exception e) { //실패 시
			System.out.println("insertReview오류: 알림창 에러 발생");
		}

		return "order_detail.jsp";
	}
	
	// 내 리뷰 모아보기 페이지 
	@RequestMapping(value = "/myReviewView.do") //View님들 myreview.do -> myReviewView.do로 수정 부탁드립니다
	public String myReviewView(ReviewVO rvo,Model model) { 
		model.addAttribute("rList", reviewService.selectAll(rvo));
		return "myreview.jsp";
	}

	// (관리자)리뷰 삭제
	@RequestMapping(value="/deleteReview.do")
	public String deleteReview(ReviewVO rvo, ReviewDAO rdao, HttpSession session,
			HttpServletResponse response, HttpServletRequest request) {
		System.out.println("deleteReview 입장");

		String id = (String)session.getAttribute("userId");

		try {
			if (id == null || !(id.equals("admin"))) {
				// 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
				return "main.do";
			}
		}catch(Exception e) {
			System.out.println("deleteReview 오류: 접근 권한 없음 알림창 오류");
			e.printStackTrace();
		}

		try {
			if (!reviewService.delete(rvo)) { // 리뷰 삭제 실패 시
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('Delete 실패. 잠시 후 다시 시도하세요.');</SCRIPT>");
			}
		}catch(Exception e) {
			System.out.println("deleteReview 오류: delete 실패 알림창 오류");
			e.printStackTrace();
		}

		return "redirect:review_manage.jsp"; //null, forward 상황
	}
	
	//ReviewListController
	@ResponseBody
	@RequestMapping(value="getReviewList.do", method = RequestMethod.POST)
	public JsonArray getReviewList(ReviewVO rvo) {
		ArrayList<ReviewVO> list = reviewService.selectAll(rvo); // ajax로 받은 pNum을 selectAll (리뷰리스트 결과)
		JsonArray datas = new Gson().toJsonTree(list).getAsJsonArray(); // JsonArry로 변경하여 반환
		
		return datas;
	}


}
