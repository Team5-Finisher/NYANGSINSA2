package com.wan.nss.controller.servlet;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.wan.nss.biz.product.ProductService;
import com.wan.nss.biz.product.ProductVO;

// ajax 호출해서 리스트들 비동기 처리 시
// ex) 리뷰, 상품 목록 정렬
@Controller
public class ListController {
	@Autowired
	private ProductService productService;

	@ResponseBody
	@RequestMapping(value="/getList.do")
	public JsonArray sendList(ProductVO pvo, Model model) {
		
		System.out.println("getList.do 진입");
		
		System.out.println("ListController 실행 조건: ");
		System.out.println("pSearchCondition: " + pvo.getpSearchCondition());
		System.out.println("category: " + pvo.getCategory());
		System.out.println("sort: " + pvo.getSort());
		System.out.println("searchLowPrice: " + pvo.getSearchLowPrice());
		System.out.println("searchHighPrice: " + pvo.getSearchHighPrice());
		
		// 뷰에서 넘어온 조건으로 상품리스트 가져오기
		ArrayList<ProductVO> list = productService.selectAll(pvo); // 결과 상품 목록
		
		// 뷰에서 전체상품 최고가 가져온 후 response에 세팅
		pvo.setpSearchCondition("max");
		model.addAttribute("maxPrice", productService.selectOne(pvo).getPrice());
		
		JsonArray datas = new Gson().toJsonTree(list).getAsJsonArray(); // JsonArry로 변경하여 반환

		return datas;
	}

}
