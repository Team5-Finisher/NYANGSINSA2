package com.wan.nss.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wan.nss.biz.blike.BlikeVO;
import com.wan.nss.biz.board.BoardService;
import com.wan.nss.biz.board.BoardVO;
import com.wan.nss.biz.image.ImageVO;
import com.wan.nss.biz.member.MemberDAO;
import com.wan.nss.biz.member.MemberService;
import com.wan.nss.biz.member.MemberVO;
import com.wan.nss.biz.order.OrderService;
import com.wan.nss.biz.order.OrderVO;
import com.wan.nss.biz.orderdetail.OrderDetailService;
import com.wan.nss.biz.orderdetail.OrderDetailVO;
import com.wan.nss.biz.product.ProductService;
import com.wan.nss.biz.product.ProductVO;
import com.wan.nss.biz.report.ReportService;
import com.wan.nss.biz.report.ReportVO;
import com.wan.nss.biz.review.ReviewService;

@Controller
public class AdminController { //관리자 페이지 단순 이동(View, Detail)으로만 구성됨. 기능을 보고 싶다면 해당 기능에 속하는 컨트롤러를 참고!

	@Autowired
	private MemberService memberService;
	@Autowired
	private ProductService productService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private BoardService boardService;
	
	@RequestMapping(value = "/crawling.do") // 관리자 페이지 회원 관리 페이지 열기
	public String crawling() {
			return "crawling.jsp";
	}
  
// (관리자) 관리자 메인 페이지 이동
	@RequestMapping(value = "/adminIndex.do")
	public String adminIndexView(MemberVO mvo, MemberDAO memberDAO, OrderVO ovo, OrderDetailVO odvo, Model model, HttpSession session, HttpServletResponse response) {
		
		String id = (String) session.getAttribute("userId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
				return "main.do";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			
			List<OrderVO> oList;
			ovo.setoSearchCondition("all");
			oList = orderService.selectAll(ovo); // 전체 주문 상품 불러오기 (관리자용)

			for (int i = 0; i < oList.size(); i++) {
				ovo.setoNum(oList.get(i).getoNum());
				// totalPrice : 주문 당 총 금액
				int totalPrice = orderService.selectOne(ovo).getoPrice();
				oList.get(i).setoPrice(totalPrice); // 불러온 주문에 총 결제금액 set
			}
			System.out.println(oList);
			model.addAttribute("oList", oList); // 주문 내역 데이터
			model.addAttribute("memberTotal", memberDAO.selectAll(mvo).size()); // 총 회원 데이터

			return "admin_index.jsp";
		}
		
	}
	
	
	// (관리자) 회원 관리 페이지 이동
	@RequestMapping(value = "/memberManagePage.do") 
	public String selectAllMemberManage(HttpSession session, HttpServletResponse response) {
		
		String id = (String) session.getAttribute("userId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
				return "main.do";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return "member_manage.jsp";
		}
		
	}
	
	
	// (관리자) 상품 관리 페이지 이동
	@RequestMapping(value = "/productManagePage.do") 
	public String selectAllProductManage(HttpSession session, HttpServletResponse response) {
		
		String id = (String) session.getAttribute("userId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
				return "main.do";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return "product_manage.jsp";
		}
		
	}
	
	//상품 받아오는 이슈 해결하면 바로 할 듯
	// (관리자) 상품 상세보기 페이지 이동: model에는 있으나 view에는 아직 없음
	@RequestMapping(value="/updateProductPage.do")
	public String updateProuctView(ProductVO pvo, ImageVO ivo, Model model) {
		productService.selectOne(pvo); // pNum을 받아 해당 번호를 갖고 있는 상품 가져오기
		//model.addAttribute("image",); //오늘 수정 예정
		return "product_manage_detail.jsp";
	}
	
	@RequestMapping(value = "/orderManagePage.do") // 관리자 페이지 주문 관리 페이지 열기
	public String selectAllorderDetailManage(HttpSession session, HttpServletResponse response) {
		
		String id = (String) session.getAttribute("userId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
				return "main.do";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return "order_manage.jsp";
		}
		
	}
	
	
	// (관리자) 리뷰 관리 페이지 이동
	@RequestMapping(value = "/reviewManagePage.do") 
	public String selectAllReviewManage(HttpSession session, HttpServletResponse response) {
		
		String id = (String) session.getAttribute("userId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
				return "main.do";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return "review_manage.jsp";
		}
		
	}
	
	
	// (관리자) 게시글 관리 페이지 이동 
	@RequestMapping(value = "/boardManageView.do") 
	public String boardManageView(BoardVO bvo, BlikeVO lvo, Model model) { 
		model.addAttribute("userId", bvo.getUserId()); //멤버 ID, 멤버 ID로 정보를 주면 model에서 알아서 글을 찾아줌!
		model.addAttribute("bList", boardService.selectAll(bvo)); //좋아요 정보도 있음
		return "board_manage.jsp";
	}
	
	// (관리자) 게시글 상세보기 페이지 이동
	@RequestMapping(value = "/boardDetialView.do") 
	public String boardDetailView(BoardVO bvo, BlikeVO lvo, Model model) { 
		model.addAttribute("userId", bvo.getUserId());
		model.addAttribute("bList", boardService.selectOne(bvo)); //좋아요 정보도 있음
		return "board_manage.jsp"; //새창은 주소를 어떻게 리턴해야돼요??
	}
	
	// (관리자) Board 게시글 삭제 처리
	@RequestMapping(value = "/deleteBoardDetail.do") 
	public String deleteBoardDetail(BoardVO bvo) {
		boardService.delete(bvo);
		return "board_manage.jsp"; 
	}	
	
	
	// (관리자) 신고글 관리 페이지 이동 
	@RequestMapping(value = "/ReportView.do") 
	public String reportManageView(ReportVO rvo, Model model) {//rvo? vpVo? 일단 전자로 씁니다
		model.addAttribute("userId", rvo.getUserId()); //멤버 ID
		model.addAttribute("rpList", reportService.selectAll(rvo)); //나머지 정보등 꺼내쓸 수 있는 곳: 여기서 맞게 꺼내쓰세요
		return "report_manage.jsp";
	}
	
	// (관리자) 신고 게시글 상세보기 페이지 이동 (확실하면 주석처리 예정)
	@RequestMapping(value = "/reportDetailView.do") 
	public String reportDetailView(ReportVO rvo, Model model) {
		model.addAttribute("userId", rvo.getUserId()); //멤버 ID
		model.addAttribute("rpList", reportService.selectOne(rvo));		
		return "report_manage_detail.jsp";
	}
	
	// (관리자) Report 게시글 삭제 처리
	@RequestMapping(value = "/deleteReport.do") 
	public String deleteReport(ReportVO rvo) {
		reportService.delete(rvo);
		return "report_manage.jsp";
	}
	
	// (관리자) Report 게시글 신고 취소
	@RequestMapping(value = "/updateReport.do") 
	public String updateReport(ReportVO rvo) {
		reportService.update(rvo);
		return "report_manage.jsp";
	}
	
}
