package com.wan.nss.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wan.nss.biz.member.MemberService;
import com.wan.nss.biz.member.MemberVO;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;

	// 로그인 페이지로 이동
	@RequestMapping(value="/login.do", method=RequestMethod.GET)
	public String loginView() {
		return "login.jsp";
	}
	
	// 회원가입 페이지로 이동
	@RequestMapping(value="/register.do", method=RequestMethod.GET)
	public String registerView() {
		return "register.jsp";
	}
	
	// 아이디 찾기 페이지로 이동
	@RequestMapping(value="/findId.do", method=RequestMethod.GET)
	public String findIdView() {
		return "find_id.jsp";
	}
	
	// 비밀번호 찾기 페이지로 이동
	@RequestMapping(value="/findPw.do", method=RequestMethod.GET)
	public String findPwView() {
		return "find_pw.jsp";
	}
	
	// 회원정보변경 클릭시 비밀번호 확인 페이지로 이동
	@RequestMapping(value="/checkPassword.do", method=RequestMethod.GET)
	public String checkPasswordView() {
		return "check_password.jsp";
	}
	
	// CONTACT 페이지로 이동
	@RequestMapping(value="/contact.do", method=RequestMethod.GET)
	public String contactView() {
		return "contact.jsp";
	}
	
	// 로그아웃 수행
	@RequestMapping(value = "/logout.do")
	public String logoutView(HttpSession session) {
		session.removeAttribute("memberId");
		session.removeAttribute("memberName");
		return "main.do";
	}

	// 회원가입 수행
	@RequestMapping(value = "/signUp.do")
	public String insertBoard(MemberVO vo, HttpServletResponse response) {

		if (memberService.insert(vo)) { // 회원가입 성공시
			return "signUpResultPage.do";
		} else { // 회원가입 실패시
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out;
			try {
				out = response.getWriter();
				out.println("<SCRIPT>alert('회원가입에 실패하였습니다... nyangsinsa@gmail.com로 문의해주세요.');history.go(-1);</SCRIPT>");
				out.flush();
				return "register.jsp";
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

	}

	// 비밀번호 찾기 결과에서 비밀번호 변경하기 수행
	@RequestMapping(value = "/changePw.do")
	public String updateMemberChangePw(MemberVO vo, HttpServletResponse response) {

		if (!memberService.update(vo)) { // 업데이트 실패 시 알림창
			try {
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('회원 정보 수정 실패. 잠시 후 다시 시도하세요');</script>"); // 이전 화면으로 이동
				out.flush();
				return "change_pw.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return "login.jsp";
		}

	}

	// 회원정보변경에서 비밀번호 변경하기 수행
	@RequestMapping(value = "/updatePw.do")
	public String updateMemberPw(MemberVO vo, HttpServletRequest request, HttpServletResponse response) {

		if (memberService.selectOne(vo) == null) { // 현재 비밀번호가 일치하지 않으면
			try {
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('현재 비밀번호를 확인하세요.');</script>"); // 이전 화면으로 이동
				out.flush();
				return "profile.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		String newPw = (String) request.getParameter("newPassword"); // 새 비밀번호
		vo.setUserPw(newPw);
		if (!memberService.update(vo)) { // 업데이트에 실패하면 알림창
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<script>alert('업데이트 실패. 잠시 후 다시 시도해주세요.');</script>"); // 이전 화면으로 이동
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return "profile.jsp";
	}

	// 회원정보 수정 수행
	@RequestMapping(value = "/updateMember.do")
	public String updateMemberProfile(MemberVO vo, HttpServletRequest request, HttpServletResponse response) {

		String address = request.getParameter("postNum") + request.getParameter("address") + request.getParameter("addressPlus") + request.getParameter("addressDetail"); // 주소
		
		vo.setAddress1(address); // 주소를 세팅

		if (!memberService.update(vo)) {
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<script>alert('회원 정보 수정 실패. 잠시 후 다시 시도하세요');</script>"); // 이전 화면으로 이동
				return "profile.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return "profile.jsp";
		}

	}

	// 관리자 페이지 - 회원 삭제(강퇴)
	@RequestMapping(value = "/deleteMem.do")
	public String deleteMember(MemberVO vo, HttpSession session, HttpServletResponse response) {

		String id = (String)session.getAttribute("memberId"); // 세션의 멤버 Id 를 가져오기
		
		if (id == null || !(id.equals("admin"))) { // 로그인을 안 하거나 admin이 아니면 접근 권한 없음.
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<SCRIPT>alert('접근 권한이 없습니다.');</SCRIPT>");
				return "memberManagePage.do";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		if (!memberService.delete(vo)) {
			try {
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().println("<SCRIPT>alert('Delete 실패. 잠시 후 다시 시도하세요.');</SCRIPT>");
			return "memberManagePage.do";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return "memberManagePage.do";
		}

	}

	// 로그인 수행
	@RequestMapping(value = "/login.do", method=RequestMethod.POST)
	public String selectOneMemberLogin(MemberVO vo, HttpSession session, HttpServletResponse response) {

		MemberVO member = memberService.selectOne(vo);

		if (member == null) { // 로그인 실패시
			try {
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<SCRIPT>alert('아이디/비밀번호를 확인해주세요');</SCRIPT>");
				out.flush();
				return "login.jsp"; // 현재 페이지에 머무름
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else { // 로그인 성공시
			session.setAttribute("memberId", member.getUserId()); // 세션에 로그인한 회원의 아이디, 이름 저장
			session.setAttribute("memberName", member.getUserName());
			return "main.do"; // 메인으로 이동
		}

	}

	// 카카오톡으로 로그인시 회원가입되어있는지 체크
	@RequestMapping(value = "/loginCheck.do")
	public String selectOneMemberKakaoLogin(MemberVO vo, HttpSession session, Model model, HttpServletRequest request) {

		MemberVO member = memberService.selectOne(vo);

		if (member != null) { // 가입정보가 있으면 메인으로
			// 이름, 아이디, 패스워드 (id, pw는 고유 번호)
			// 받아서 회원 정보가 있으면 로그인
			session.setAttribute("memberId", member.getUserId()); // 세션에 로그인한 회원의 아이디, 이름 저장
			session.setAttribute("memberName", member.getUserName());
			return "main.do";
		} else { // 가입정보가 없으면 카카오 회원가입
//			request.setAttribute("userName", request.getParameter("userName"));
//			request.setAttribute("userId", request.getParameter("d"));
//			request.setAttribute("userPw", request.getParameter("d"));
			model.addAttribute("data", member);
			return "kakao_register.jsp";
		}

	}

	// 아이디 찾기 수행
	@RequestMapping(value = "/findId.do")
	public String selectOneMemberId(MemberVO vo, Model model, HttpServletResponse response) {

		MemberVO member = memberService.selectOne(vo); // id, 이름이 담긴 멤버
		if (member == null) { // 가입정보가 없는 경우
			try {
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('회원정보가 존재하지 않습니다. Please check your information.');</script>");
				out.flush();
				return "find_id.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else { // 가입정보가 있는 경우
			model.addAttribute("memberId", member.getUserId());
			model.addAttribute("memberName", member.getUserName());
			return "result_find_id.jsp";
		}

	}

	// 비밀번호 찾기 수행
	@RequestMapping(value = "/findPw.do")
	public String selectOneMemberPw(MemberVO vo, Model model, HttpServletResponse response) {

		// 인증번호로 폰 본인 건지 확인 후,
		// 폰 번호로 비밀번호 찾기 버튼 누르면 :

		// mdao에서 해당하는 멤버 가져오고, 아이디만 전달
		MemberVO member = memberService.selectOne(vo);
		if (member == null) { // 없는 회원인 경우\
			try {
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('존재하지 않는 회원입니다. Please check your information.');</script>");
				out.flush();
				return "find_pw.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			model.addAttribute("memberId", member.getUserId());
			return "result_find_pw.jsp";
		}

	}

	// 회원정보변경 클릭시 비밀번호 확인 페이지에서 비밀번호 확인 수행
	@RequestMapping(value = "/CheckPw.do") // 
	public String selectOneMemberCheckPw(MemberVO vo, Model model, HttpServletResponse response) {

		MemberVO resMvo = memberService.selectOne(vo); // id, pw가 일치하는 회원이 있는경우만 not null

		if (resMvo == null) { // 비밀번호가 일치하지 않으면, 알림창 뜨고 뒤로 돌아가야 함
			try {
				response.setContentType("text/html; charset=utf-8"); // 알림창 인코딩
				PrintWriter out = response.getWriter();
				out.println("<script>alert('비밀번호가 일치하지 않습니다.');</script>");
				out.flush();
				return "check_password.jsp"; // 비밀번호 확인 페이지에 머물기
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			// 비밀번호가 일치한다면 전달 :
			// 1. 프로필 사진, 2. 고양이 이름, 3. 전화번호, 4. 주소
			model.addAttribute("cName", resMvo.getCatName());
			model.addAttribute("phoneNum", resMvo.getPhoneNum());
			model.addAttribute("address", resMvo.getAddress1());
			return "profile.jsp"; // 회원정보변경 페이지로 진행
		}
		
	}

	// 마이페이지로 이동
	@RequestMapping(value = "/mypage.do")
	public String selectOneMemberMyPage(MemberVO vo, Model model, HttpSession session, HttpServletResponse response) {

		String id = (String) session.getAttribute("memberId"); // 세션에 저장된 '로그인한 회원의 아이디'
		if (id == null) { // 로그인한 회원이 없다면 로그인 페이지로 이동
			try {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().println("<script>alert('로그인을 해주세요.');</script>"); // 로그인 화면으로 이동
				return "login.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			MemberVO member = memberService.selectOne(vo); // 로그인한 회원 VO를 member에 저장
			
			model.addAttribute("data", member);

			return "mypage.jsp";
		}

	}

}
