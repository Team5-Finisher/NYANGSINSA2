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
import org.springframework.web.bind.annotation.RequestMethod;

import com.wan.nss.biz.board.BoardService;
import com.wan.nss.biz.board.BoardVO;
import com.wan.nss.biz.common.Crawling;
import com.wan.nss.biz.product.ProductService;
import com.wan.nss.biz.product.ProductVO;
import com.wan.nss.biz.review.ReviewService;
import com.wan.nss.biz.review.ReviewVO;

@Controller
public class ProductController {

   @Autowired
   private ProductService productService;
   @Autowired
   private ReviewService reviewService;
   @Autowired
   private BoardService boardService;
   @Autowired
   private Crawling crawling;

   // 멤버, 상품 
   @RequestMapping(value="/main.do")
   public String mainView(ProductVO pvo, Model model, HttpServletRequest request) {
      // 신상품 데이터. pvo : category == all, sort == regiDesc
      System.out.println("   로그: main.do");
      
   // 크롤링     
      pvo.setCategory("all");
      pvo.setSort("regiDesc");
      
      if(productService.selectAll(pvo).size() < 48) {
         crawling.sample(request);
      }
      
      // 전체 최신순(등록일순): category = all,  sort = regiDesc
      pvo.setSearchLowPrice(0);
      pvo.setSearchHighPrice(1000000);	
      model.addAttribute("newPList", productService.selectAll(pvo)); 

      // 전체 인기순(판매량순): category == all, sort == sellDesc
      pvo.setSort("sellDesc");
      model.addAttribute("popPList", productService.selectAll(pvo));

		return "main.jsp";
	}

	// 쇼핑페이지 이동
	@RequestMapping(value="/shop.do")
	public String shopView(ProductVO pvo,Model model, HttpSession session) {
		// 파라미터별로 상이한 상품 목록들 세팅하기: shopping.do?category=???
		//디폴트값: 인기순, 찾을 가격 0 ~ 1000000
			
		//할인상품 정렬
		pvo.setpSearchCondition("dc");
		pvo.setSort("sellDesc"); //sort 종류: sellDesc (인기순:주문량순) / priceAsc (낮은 가격순) / priceDesc (높은 가격순) / regiDesc (최신순) 
		pvo.setSearchLowPrice(0);
		pvo.setSearchHighPrice(1000000);
		model.addAttribute("pList", productService.selectAll(pvo));
		
		// 쇼핑페이지 기본 이동 (shop.do?category=all)
		if(pvo.getCategory().equals("all")) {
			return "shop.jsp";
		}
		// 카테고리별 쇼핑페이지 기본 이동 (shop.do?category=food/treat/sand)
		else if(pvo.getCategory().equals("food")||pvo.getCategory().equals("treat")||pvo.getCategory().equals("sand")){
			return "shop_"+pvo.getCategory()+".jsp"; // 카테고리 별로 다른 페이지 이동 (all, food, treat, sand)
		}
		return null;//nullPointException 내서 페이지 이동하려고
	}

	// 상품세부페이지 이동
	@RequestMapping(value="/shopDetails.do")
	public String shopDetailView(ProductVO pvo,ReviewVO rvo,Model model) {
		System.out.println("pNum: "+pvo.getpNum());
		pvo = productService.selectOne(pvo); // 해당 상품 및 달려있는 리뷰 set
		ProductVO resPvo = new ProductVO();//현재 pvo
		resPvo = productService.selectOne(pvo); // 상세페이지에서 보여줄 상품num을 selectAll에 돌린 결과를 resPvo에 저장

		// 관련상품 목록 가져오기 조건 : pName==null, 카테고리 nn, 정렬 nn
		pvo.setCategory(resPvo.getCategory()); // 관련상품정보를 가져오기 위해 카테고리 set
		pvo.setSort("sellDesc"); // 관련상품 가져오기 위해 정렬 set
		pvo.setSearchLowPrice(0); 
		pvo.setSearchHighPrice(1000000);

		ArrayList<ReviewVO> rList = reviewService.selectAll(rvo); // 리뷰 리스트
		ArrayList<ProductVO> pList = productService.selectAll(pvo); // 관련 상품 리스트

		model.addAttribute("pvo", pvo); //해당 상품의 정보들을 보내줌
		model.addAttribute("rList", rList);
		model.addAttribute("pList", pList);

		return "shop_details.jsp";
	}

	// (관리자) 상품 추가 기능: model에는 있으나 view에는 아직 없음
	@RequestMapping(value="/createProduct.do", method=RequestMethod.POST)
	public String insertProduct(ProductVO pvo, Model model) {
		productService.insert(pvo); //카테고리, 상품 이름, 가격, 재고, 설명 추가
		// 2. 상품 이미지 올리기는 추후 구현 예정: 대표이미지(pImgUrl), 상세 이미지(pImgUrl2)
		return "product_manage_insert.jsp";
	}

	//(관리자)상품 수정 기능: 해당 상품 관리 페이지에서 "수정" 버튼 클릭 시 실제 수정
	@RequestMapping(value="/updateProduct.do", method=RequestMethod.POST)
	public String updateProduct(ProductVO pvo, Model model,
			HttpServletResponse response,HttpServletRequest request) {

		/*
		// 관리자 모드 : 해당 상품 관리 페이지에서 "수정" 버튼 클릭 시 실제 수정
		System.out.println("updateProduct 입장");

		//파일받기 (저장공간) > 초기세팅 (업로드 확인 > bvo.setFile(fileName);) > 본섭공간 저장 > 추가 실행
		
		// 각자 이미지 저장할 위치
		String uploadDir = this.getClass().getResource("").getPath();
		

		System.out.println(uploadDir);

		// 총 100M 까지 저장 가능하게 함
		int maxSize = 1024 * 1024 * 100;

		String encoding = "UTF-8";

		// 사용자가 전송한 파일정보 토대로 업로드 장소에 파일 업로드 수행할 수 있게 함

		MultipartRequest multipartRequest = new MultipartRequest(request, uploadDir, maxSize, encoding, new DefaultFileRenamePolicy());

		// 중복된 파일이름이 있기에 fileRealName이 실제로 서버에 저장된 경로이자 파일

		//String fileRealName = multipartRequest.getFilesystemName("file");

		// 디비에 업로드 메소드

		//new FileDAO().upload(fileName, fileName2);

		//pvo.setpImgUrl("img/"+multipartRequest.getFilesystemName("img")); // 이미지 첨부파일인데.. 어떻게 하지 수정 필요!!!!
		*/
		boolean flag = productService.update(pvo);

		if (!flag) { // 실패 시 알림창
			try {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html; charset=utf-8");
			out.println("<SCRIPT>alert('ERROR : UPDATE 실패');</SCRIPT>");
			return "product_manage_datail.jsp";
			}
			catch(Exception e) {
				System.out.println("실패 알람칭 띄우기 중 오류 발생");
				e.printStackTrace();
			}
		}
		return "redirect:product_manage.jsp";
	}

	@RequestMapping(value="/search.do")
	public String selectAllProductSearch(ProductVO pvo,BoardVO bvo,Model model) { 
		System.out.println("searchCondition: "+pvo.getpSearchCondition());
		System.out.println("searchContent: "+pvo.getpSearchContent());
	
		model.addAttribute("pList",productService.selectAll(pvo)); //View님들 search.do의 data -> pList 로 변경 부탁드립니다.
		model.addAttribute("bList",boardService.selectAll(bvo));
	
		return "search_result.jsp";
	}

}
