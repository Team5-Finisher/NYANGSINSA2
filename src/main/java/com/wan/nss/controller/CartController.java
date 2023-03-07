package com.wan.nss.controller;

import java.util.ArrayList;

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
import com.wan.nss.biz.order.OrderVO;
import com.wan.nss.biz.product.ProductService;
import com.wan.nss.biz.product.ProductVO;

@Controller
public class CartController {
	
	@Autowired
	private ProductService productService;

	// 장바구니 페이지로 이동
	@RequestMapping(value="/shopingCart.do")
	public String shopingCartView() {
		
		System.out.println("shopingCart.do 진입");
		
		return "shoping_cart.jsp";
		
	}
	
	// 주문하기 페이지에서 결제 및 최종 주문 수행
	@RequestMapping(value="/insertCart.do")
	public void insertOrder(ProductVO pvo, Model model, HttpSession session, HttpServletResponse response) {
		
		System.out.println("insertCart.do 진입");
		
		ArrayList<ProductVO> cList = (ArrayList) session.getAttribute("cList"); // 세션에 담긴 장바구니 목록을 cList 객체에 저장
		if(cList == null) {
			System.out.println("로그: cList 생성함!");
			cList = new ArrayList<>();
		}
		else {
			System.out.println("로그: cList가 존재하여 생성하지 않음!");
		}
		
		if(pvo.getpCnt() == 0) {
			System.out.println("로그: pCnt == 0 썸네일에서 장바구니에 추가함");
			pvo.setpCnt(1); // 메인에서 장바구니 추가를 눌렀을 경우 : 추가할 수량 1개
		}
		else {
			System.out.println("로그: pCnt != 0 상세페이지에서 장바구니에 추가함");
		}
		
		// selectOne을 위한 ProductVO 생성
		ProductVO product = new ProductVO();;
		product.setpNum(pvo.getpNum());

		product = productService.selectOne(product); // 상품번호가 일치하는 해당 상품 정보

		boolean isExist = false; // 세션의 장바구니에 지금 담는 상품이 이미 있는지 여부
		for (int i = 0; i < cList.size(); i++) {
			if (cList.get(i).getpNum() == product.getpNum()) { // 세션에 담긴 장바구니 가져와서 뒤져본 뒤 있으면 수량 ++
				cList.get(i).setpCnt(cList.get(i).getpCnt() + pvo.getpCnt());
				isExist = true;
				System.out.println("로그: 상품이 장바구니에 있음 → 장바구니의 상품 수량 " + pvo.getpCnt() + "개 증가");
				break;
			}
		}
		
		if (!isExist) { // 없으면 cList에 한 개 추가
			product.setpCnt(pvo.getpCnt());
			cList.add(product);
			System.out.println("로그: 상품이 장바구니에 없음 → 장바구니에 상품 " + pvo.getpCnt() + "개 추가!");
		}

		// session에 담기
		session.setAttribute("cList", cList);
		System.out.println("로그: 갱신된 cList를 세션에 저장함!");
		for(ProductVO v : (ArrayList<ProductVO>) session.getAttribute("cList")) {
			System.out.println(v.getpNum() + ": " + v.getpCnt() + "개");
		}
		
		System.out.println("insertCart.do 종료");
		try {
			response.getWriter().println("<SCRIPT>history.go(-1)</SCRIPT>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// 주문 내역 페이지로 이동
	@RequestMapping(value="/deleteCart.do")
	public String selectAllOrderList(OrderVO vo, Model model, HttpSession session) {
		
		System.out.println("deleteCart.do 진입");

		return "shopingCart.do";
		
	}
	
	// 장바구니 업데이트
	@ResponseBody
	@RequestMapping(value="/updateCart.do")
	public JsonArray updateCart(HttpSession session, HttpServletRequest request) {
		
		System.out.println("updateCart.do 진입");
		
		int pNum = Integer.parseInt(request.getParameter("pNum"));
		String action = request.getParameter("action");
		
		System.out.println("실행 조건: ");
		System.out.println("pNum: " + pNum);
		System.out.println("action: " + action);
		
		// 세션에 저장된 장바구니 상품 목록 가져오기
		ArrayList<ProductVO> cList = (ArrayList) session.getAttribute("cList");
		
		// 장바구니 상품 수량 수정하기
		for (int i = 0; i < cList.size(); i++) {
			if (cList.get(i).getpNum() == pNum) {
				if (action.equals("+")) { // plus 하는 경우
					cList.get(i).setpCnt(cList.get(i).getpCnt() + 1);
				} else if (action.equals("-")) { // minus 하는 경우
					if (cList.get(i).getpCnt() > 1) {
						cList.get(i).setpCnt(cList.get(i).getpCnt() - 1);
					} else if (cList.get(i).getpCnt() == 1) {
						cList.remove(i);
					}
				}
				break;
			}
		}
		
		// 수정된 장바구니 목록들을 세션에도 저장
		session.setAttribute("cList", cList);

		// ArrayList를 JsonArray 형식으로 변환
		JsonArray datas = new Gson().toJsonTree(cList).getAsJsonArray(); // JsonArry로 변경하여 반환

		return datas;
		
	}
	
}
