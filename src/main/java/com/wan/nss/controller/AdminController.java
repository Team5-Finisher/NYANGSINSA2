package com.wan.nss.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wan.nss.biz.blike.BlikeVO;
import com.wan.nss.biz.board.BoardService;
import com.wan.nss.biz.board.BoardVO;
import com.wan.nss.biz.image.ImageService;
import com.wan.nss.biz.image.ImageVO;
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
import com.wan.nss.biz.review.ReviewVO;

@Controller
public class AdminController { // 관리자 페이지 단순 이동(View, Detail)으로만 구성됨. 기능을 보고 싶다면 해당 기능에 속하는 컨트롤러를 참고!

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
	private BoardService boardService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private ReportService reportService;

	// (관리자) 관리자 메인 페이지 이동
	@RequestMapping(value = "/adminIndex.do")
	public String adminIndexView(MemberVO mvo, OrderVO ovo, Model model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {

		String id = (String) session.getAttribute("memberId");

		System.out.println("id: " + id);

		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.

			try {

				System.out.println("관리자 식별 불가! 관리자홈 접근 권한 없음!");

				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");

				model.addAttribute("lang", request.getParameter("lang"));
				return "main.do";

			} catch (Exception e) {

				e.printStackTrace();

				return null;

			}

		} else {

			System.out.println("관리자 식별 성공! 관리자홈 진입!");

			List<OrderVO> oList;
			ovo.setoSearchCondition("adminOrderChart");
			oList = orderService.selectAll(ovo); // 전체 주문 상품 불러오기 (관리자용)

			for (int i = 0; i < oList.size(); i++) {
				ovo.setoNum(oList.get(i).getoNum());

				System.out.println("orderService: " + orderService);

				// totalPrice : 주문 당 총 금액
				OrderVO selectOvo = orderService.selectOne(ovo);
				if (selectOvo != null) {
					int totalPrice = selectOvo.getoPrice();
					oList.get(i).setoPrice(totalPrice); // 불러온 주문에 총 결제금액 set
				}
			}

			System.out.println(oList);

			model.addAttribute("oList", oList); // 주문 내역 데이터
			model.addAttribute("memberTotal", memberService.selectAll(mvo).size()); // 총 회원 데이터

			return "admin_index.jsp";

		}

	}

	// 관리자 홈 도넛차트 데이터 가져오기
	@ResponseBody
	@RequestMapping(value = "/getChart.do")
	public JsonObject sendChart(OrderVO ovo, OrderDetailVO odvo) {
		System.out.println("getChart.do 진입");
		
		String[] category = {"food", "treat", "sand"};
		JsonObject data = new JsonObject();
		
		for (int i = 0; i < category.length; i++) {
			
			odvo.setCategory(category[i]);
			System.out.println("odvo.category: " + odvo.getCategory());
			odvo = orderDetailService.selectOne(odvo); // cnt, sum 받아옴
			System.out.println("odvo");
			System.out.println(odvo);
			
			data.addProperty("categoryCnt" + (i+1), odvo.getOdCnt()); // categoryCnt1~
			data.addProperty("categorySum" + (i+1), odvo.getSum()); // categorySum1~
			
		}

		// 올해 작년 수익 비교 차트
		// 연도별 수익 데이터 저장 부분 Begin
		// 연도별 수익 저장할 변수

		// 작년 수익
		ovo.setoSearchCondition("lastYear");
		System.out.println(orderService.selectOne(ovo));
//		int lastYearSum = orderService.selectOne(ovo).getoPrice();
		int lastYearSum = 0;

		// data 리스트에 넣기
		System.out.println("lastYearSum: " + lastYearSum);
		data.addProperty("lastYearSum", lastYearSum);

		// 2023년 수익
		ovo.setoSearchCondition("thisYear");
		int thisYearSum = orderService.selectOne(ovo).getoPrice();

		// data 리스트에 넣기
		System.out.println("thisYearSum: " + thisYearSum);
		data.addProperty("thisYearSum", thisYearSum);
		// 연도별 수익 데이터 저장 부분 End

		System.out.println("data: " + data);
		return data;
	}

	// (관리자) 회원 관리 페이지 이동
	@RequestMapping(value = "/memberManagePage.do")
	public String selectAllMemberManage(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model) {

		String id = (String) session.getAttribute("memberId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
				
				model.addAttribute("lang", request.getParameter("lang"));
				return "main.do";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return "member_manage.jsp";
		}

	}


	// (관리자) 상품 관리 페이지 이동
	@RequestMapping(value = "/productManagePage.do")
	public String adminProductDetailView(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {

		String id = (String) session.getAttribute("memberId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
				
				model.addAttribute("lang", request.getParameter("lang"));
				return "main.do";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return "product_manage.jsp";
		}

	}

	// 상품 받아오는 이슈 해결하면 바로 할 듯
	// (관리자) 상품 상세보기 페이지 이동: model에는 있으나 view에는 아직 없음
	@RequestMapping(value = "/adminProductDetail.do")
	public String updateProuctView(ProductVO pvo, ImageVO ivo, Model model) {
		
		model.addAttribute("pvo", productService.selectOne(pvo)); // pNum을 받아 해당 번호를 갖고 있는 상품 가져오기
		
		return "product_manage_detail.jsp";
	}

	@RequestMapping(value = "/orderManagePage.do") // 관리자 페이지 주문 관리 페이지 열기
	public String selectAllorderDetailManage(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {

		String id = (String) session.getAttribute("memberId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
				
				model.addAttribute("lang", request.getParameter("lang"));
				return "main.do";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return "order_manage.jsp";
		}

	}

	// (관리자) 리뷰 관리 페이지 이동
	@RequestMapping(value = "/reviewManagePage.do")
	public String selectAllReviewManage(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {

		String id = (String) session.getAttribute("memberId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
				
				model.addAttribute("lang", request.getParameter("lang"));
				return "main.do";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return "review_manage.jsp";
		}

	}

	// (관리자) 게시글 관리 페이지 이동
	@RequestMapping(value = "/boardManageView.do")
	public String boardManageView(BoardVO bvo, BlikeVO lvo, Model model) {
		model.addAttribute("userId", bvo.getUserId()); // 멤버 ID, 멤버 ID로 정보를 주면 model에서 알아서 글을 찾아줌!
		model.addAttribute("bList", boardService.selectAll(bvo)); // 좋아요 정보도 있음
		return "board_manage.jsp";
	}

	// (관리자) 게시글 상세보기 페이지 이동
	@RequestMapping(value = "/boardDetialView.do")
	public String boardDetailView(BoardVO bvo, BlikeVO lvo, Model model) {
		model.addAttribute("userId", bvo.getUserId());
		model.addAttribute("bList", boardService.selectOne(bvo)); // 좋아요 정보도 있음
		return "board_manage.jsp"; // 새창은 주소를 어떻게 리턴해야돼요??
	}

	// (관리자) Board 게시글 삭제 처리
	@RequestMapping(value = "/deleteBoardDetail.do")
	public String deleteBoardDetail(BoardVO bvo) {
		boardService.delete(bvo);
		return "board_manage.jsp";
	}

	// 관리자 파트별 관리페이지
	@ResponseBody
	@RequestMapping(value = "/getAdminList.do")
	public JsonArray sendAdminList(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8"); // 인코딩

		System.out.println("getAdminList.do 진입");
		
		// part = member, product, order, review 중 1
		String part = request.getParameter("part");

		System.out.println("part: " + part);

		// 파트 별로 JsonArray를 전달

		List list = null;

		if (part.equals("member")) { //

			MemberVO mvo = new MemberVO();

			list = memberService.selectAll(mvo); // 결과 상품 목록

		} else if (part.equals("product")) { //

			ProductVO pvo = new ProductVO();
			pvo.setCategory("all");
			pvo.setSort("regiDesc");
			pvo.setSearchLowPrice(0);
			pvo.setSearchHighPrice(10000000);

			// 뷰에서 넘어온 조건으로 상품리스트 가져오기
			list = productService.selectAll(pvo); // 결과 상품 목록

		} else if (part.equals("order")) { //

			OrderDetailVO odvo = new OrderDetailVO();

			list = orderDetailService.selectAll(odvo);

		} else if (part.equals("review")) { //

			ReviewVO rvo = new ReviewVO();

			list = reviewService.selectAll(rvo);

		} else if (part.equals("board")) { //
		
			BoardVO bvo = new BoardVO();
			
			list = boardService.selectAll(bvo);
		
		} else if (part.equals("report")) { //
			
			ReportVO rpVo = new ReportVO();
			
			list = reportService.selectAll(rpVo);
			
		}

		JsonArray datas = new Gson().toJsonTree(list).getAsJsonArray(); // JsonArry로 변경하여 반환

		return datas;

	}

}
