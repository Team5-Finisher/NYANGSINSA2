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
import com.wan.nss.biz.blike.BlikeVO;
import com.wan.nss.biz.board.BoardService;
import com.wan.nss.biz.board.BoardVO;
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
	private ReportService reportService;
	@Autowired
	private BoardService boardService;

	// (관리자) 관리자 메인 페이지 이동
	@RequestMapping(value = "/adminIndex.do")
	public String adminIndexView(MemberVO mvo, OrderVO ovo, Model model, HttpSession session, HttpServletResponse response) {

		String id = (String) session.getAttribute("memberId");
		
		System.out.println("id: " + id);
		
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			
			try {
		
				System.out.println("관리자 식별 불가! 관리자홈 접근 권한 없음!");
				
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
				
				return "main.do";
			
			} catch (Exception e) {
			
				e.printStackTrace();
				
				return null;
			
			}
			
		}
		else {

			System.out.println("관리자 식별 성공! 관리자홈 진입!");
			
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
			model.addAttribute("memberTotal", memberService.selectAll(mvo).size()); // 총 회원 데이터

			return "admin_index.jsp";
			
		}

	}
	
//	// 관리자 홈 도넛차트 데이터 가져오기
//		@RequestMapping(value = "getDonutChart.do")
//		protected void sendDonutChart(OrderVO ovo, OrderDetailVO odvo, HttpServletRequest request, HttpServletResponse response) {
//			System.out.println("getDonutChart.do 진입");
//			ArrayList<OrderDetailVO> list = new ArrayList<>(); // 카테고리별 cnt / sum 넣을 list
//			
//			ArrayList<OrderVO> list2022 = new ArrayList<>(); // 연도별 수익 넣을 list
//			ArrayList<OrderVO> list2023 = new ArrayList<>(); // 연도별 수익 넣을 list
//
//			odvo.setCategory("food"); // 카테고리 지정해주고
//			odvo = orderDetailService.selectOne(odvo); // cnt / sum 받아옴
//			list.add(odvo); // 리스트에 추가
//			odvo.setCategory("treat");
//			odvo = orderDetailService.selectOne(odvo);
//			list.add(odvo);
//			odvo.setCategory("sand");
//			odvo = orderDetailService.selectOne(odvo);
//			list.add(odvo);
//
//			JsonArray datas = new JsonArray();
//			for (int i = 0; i < list.size(); i++) {
//				JsonObject data = new JsonObject();
//				data.put("cnt", list.get(i).getCnt());
//				System.out.println(list.get(i).getCnt());
//				data.put("sum", list.get(i).getSum());
//				System.out.println(list.get(i).getSum());
//				datas.add(data);
//			}
//			
//			// 연도별 수익 데이터 저장 부분 Begin
//			// 연도별 수익 저장할 변수
//			int sum2022=0;
//			int sum2023=0;
//			
//			// 2022년 수익 
//			ovo.setoDate("2022");
//			ovo.setoSearchCondition("date");
//			list2022 = orderService.selectAll(ovo);
//			
//			
//			System.out.println("list2022: "+list2022);
//			
//			for(OrderVO v : list2022) {
//				sum2022 += v.getoPrice();
//			}
//			
//			// data 리스트에 넣기
//			System.out.println("sum2022: "+sum2022);
//			JSONObject data2022 = new JSONObject();
//			data2022.put("year", sum2022);
//			datas.add(data2022);
//			
//			// 2023년 수익
//			ovo.setoDate("2023");
//			ovo.setoSearchCondition("date");
//			list2023 = orderService.selectAll(ovo);
//			
//			System.out.println("list2023: "+list2023);
//			
//			for(OrderVO v : list2023) {
//				sum2023 += v.getoPrice();
//			}
//			
//			// data 리스트에 넣기
//			System.out.println("sum2023: "+sum2023);
//			JSONObject data2023 = new JSONObject();
//			data2023.put("year", sum2023);
//			datas.add(data2023);
//			// 연도별 수익 데이터 저장 부분 End
//
//			
//			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//			return datas;
//		}


	// (관리자) 회원 관리 페이지 이동
	@RequestMapping(value = "/memberManagePage.do")
	public String selectAllMemberManage(HttpSession session, HttpServletResponse response) {

		String id = (String) session.getAttribute("memberId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
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
	public String selectAllProductManage(HttpSession session, HttpServletResponse response) {

		String id = (String) session.getAttribute("memberId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
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
	@RequestMapping(value = "/updateProductPage.do")
	public String updateProuctView(ProductVO pvo, ImageVO ivo, Model model) {
		productService.selectOne(pvo); // pNum을 받아 해당 번호를 갖고 있는 상품 가져오기
		// model.addAttribute("image",); //오늘 수정 예정
		return "product_manage_detail.jsp";
	}

	@RequestMapping(value = "/orderManagePage.do") // 관리자 페이지 주문 관리 페이지 열기
	public String selectAllorderDetailManage(HttpSession session, HttpServletResponse response) {

		String id = (String) session.getAttribute("memberId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
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
	public String selectAllReviewManage(HttpSession session, HttpServletResponse response) {

		String id = (String) session.getAttribute("memberId");
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
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

	// (관리자) 신고글 관리 페이지 이동
	@RequestMapping(value = "/ReportView.do")
	public String reportManageView(ReportVO rvo, Model model) {// rvo? vpVo? 일단 전자로 씁니다
		model.addAttribute("userId", rvo.getUserId()); // 멤버 ID
		model.addAttribute("rpList", reportService.selectAll(rvo)); // 나머지 정보등 꺼내쓸 수 있는 곳: 여기서 맞게 꺼내쓰세요
		return "report_manage.jsp";
	}

	// (관리자) 신고 게시글 상세보기 페이지 이동 (확실하면 주석처리 예정)
	@RequestMapping(value = "/reportDetailView.do")
	public String reportDetailView(ReportVO rvo, Model model) {
		model.addAttribute("userId", rvo.getUserId()); // 멤버 ID
		model.addAttribute("rpList", reportService.selectOne(rvo));
		return "report_manage_detail.jsp";
	}

	// (관리자) Report 게시글 신고 취소
	@RequestMapping(value = "/updateReport.do")
	public String updateReport(ReportVO rvo) {
		reportService.update(rvo);
		return "report_manage.jsp";
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
		
		if (part.equals("member")) {
			
			MemberVO mvo = new MemberVO();
			
			list = memberService.selectAll(mvo); // 결과 상품 목록

		} else if (part.equals("product")) {
			
			ProductVO pvo = new ProductVO();
			pvo.setCategory("all");
			pvo.setSort("regiDesc");
			pvo.setSearchLowPrice(0);
			pvo.setSearchHighPrice(10000000);

			// 뷰에서 넘어온 조건으로 상품리스트 가져오기
			list = productService.selectAll(pvo); // 결과 상품 목록
			
		} else if (part.equals("order")) {
			
			OrderDetailVO odvo = new OrderDetailVO();
			
			odvo.setoNum(0);
			
			list = orderDetailService.selectAll(odvo);
			
		} else if (part.equals("review")) {
			
			ReviewVO rvo = new ReviewVO();
			
			list = reviewService.selectAll(rvo);
			
		}
		
		JsonArray datas = new Gson().toJsonTree(list).getAsJsonArray(); // JsonArry로 변경하여 반환
		
		return datas;
		
	}

}
