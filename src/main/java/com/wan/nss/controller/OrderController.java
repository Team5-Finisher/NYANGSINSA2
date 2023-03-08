package com.wan.nss.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wan.nss.biz.order.OrderService;
import com.wan.nss.biz.order.OrderVO;
import com.wan.nss.biz.orderdetail.OrderDetailService;
import com.wan.nss.biz.orderdetail.OrderDetailVO;
import com.wan.nss.biz.product.ProductService;
import com.wan.nss.biz.product.ProductVO;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private ProductService productService;
	
	// 주문하기 페이지로 이동
	@RequestMapping(value="/buyProducts.do")
	public String checkoutView(OrderVO vo, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("buyProducts.do");
		
		String userId = (String) session.getAttribute("memberId");

		response.setContentType("text/html; charset=utf-8");
		
		if (userId == null) { // 로그인 안 돼 있으면 로그인 창으로 이동
			try {
				PrintWriter out = response.getWriter();
				out.println("<script>alert('로그인이 필요합니다.');location.href='login.do';</script>");
				out.flush();
				return "login.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else { // 로그인 돼 있으면 주문하기 페이지로 이동
			ArrayList<ProductVO> cList = (ArrayList) session.getAttribute("cList"); // 장바구니 불러오기
			
			if (cList.isEmpty()) { // 장바구니가 비었을 때
				try {
					PrintWriter out = response.getWriter();
					out.println("<script>alert('장바구니에 담긴 상품이 없습니다.');history.go(-1);</script>");
					out.flush();
					return "checkout.jsp";
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			} else {
				return "checkout.jsp";
			}
		}
		
	}

	// 주문하기 페이지에서 결제 및 최종 주문 수행
	@RequestMapping(value = "/insertOrder.do")
	public String insertOrder(OrderVO ovo, OrderDetailVO odvo, ProductVO pvo, Model model, HttpSession session,
			HttpServletResponse response) {

		System.out.println("insertOrder.do 진입");

		String userId = (String) session.getAttribute("memberId"); // 로그인 무조건 돼 있음

		// Order insert

		if (!orderService.insert(ovo)) { // insert 에서 실패했다면
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<script>alert('주문내역 생성 실패...관리자에게 문의하세요.');history.go(-1);</script>");
				return "checkout.jsp";
			} catch (Exception e) {
				return null;
			}
		}
		System.out.println("오더 인서트 성공");

		// ovo insert 성공
		// → odvo insert!

		OrderVO thisOvo = orderService.selectOne(ovo); // 방금 추가한 ovo
		// SELECT O_NO FROM ORDER_INFO WHERE USER_ID = ? ORDER BY O_NO DESC ;

		ArrayList<ProductVO> cList = (ArrayList) session.getAttribute("cList"); // 장바구니에 담긴 상품들
		if (cList == null) {
			cList = new ArrayList<ProductVO>();
		}
		for (int i = 0; i < cList.size(); i++) {
			orderDetailService.insert(odvo); // 주문 상세 내역 DB에 저장

			// 장바구니에 있는 상품들의 pNum과 pCnt(개수)를 받아서
			// DB 에 업데이트
			pvo.setpNum(cList.get(i).getpNum());
			pvo.setpCnt(cList.get(i).getpCnt());
			if (!productService.update(pvo)) {
				try {
					response.setContentType("text/html; charset=utf-8");
					response.getWriter().println("<SCRIPT>alert('ERROR : UPDATE 실패');</SCRIPT>");
					return null;
				} catch (Exception e) {
					return null;
				}
			}
		}

		session.removeAttribute("cList"); // 장바구니 비우기
		return "orderList.do";

	}

	// 주문 내역 페이지로 이동
	@RequestMapping(value="/orderList.do")
	public String selectAllOrderList(OrderVO vo, Model model, HttpSession session) {
		
		System.out.println("orderList.do 진입");

		// 현재 로그인한 회원의 주문내역을 가져와야 함
		String userId = (String) session.getAttribute("memberId");

		vo.setUserId(userId);

		List<OrderVO> oList;
		oList = orderService.selectAll(vo); // 현재 로그인한 회원의 주문 내역 리스트
		
		for(int i = 0; i< oList.size();i++) { // 주문 내역 한 개당 총 금액 넣기
			oList.get(i).setoDate(oList.get(i).getoDate().substring(0, 19)); // 주문 날짜 뒤 ".000" 잘라서 저장
			
			vo.setoNum(oList.get(i).getoNum());
			// totalPrice : 주문 당 총 금액
			int totalPrice = orderService.selectOne(vo).getoPrice(); 
			oList.get(i).setoPrice(totalPrice);
		}
	
		model.addAttribute("oList", oList);

		return "order_list.jsp";
		
	}
	
	// 주문 상세 내역 페이지로 이동
	@RequestMapping(value="/orderDetailList.do")
	public String selectAllOrderDetailList(OrderVO ovo, OrderDetailVO odvo, Model model) {
		
		System.out.println("orderDetailList.do 진입");
		
		odvo.setoNum(ovo.getoNum());
		List<OrderDetailVO> odList = orderDetailService.selectAll(odvo); // 주문 번호가 oNum인 주문 상세 내역들
		
		model.addAttribute("odList", odList); // 주문 상세 보내주기
		return "order_detail.jsp";
		
	}
	
}
