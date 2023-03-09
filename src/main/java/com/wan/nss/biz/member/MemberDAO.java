package com.wan.nss.biz.member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("memberDAO")
public class MemberDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 회원가입
	private final String SQL_INSERT = "INSERT INTO MEMBER (M_ID, M_PW, M_NM, CAT_NM, PHONE_NO, POST_NO, ADDRESS1, ADDRESS2) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	// 회원 아이디, 비밀번호 검색
	private final String SQL_SELECTONE = "SELECT * FROM MEMBER WHERE M_ID = ? AND M_PW = ?";
	
	// 회원 아이디 중복 검사 할 때 필요한 것
	private final String SQL_SELECTONE_ID = "SELECT M_ID FROM MEMBER WHERE M_ID = ? ";
	
	// 핸드폰 번호 중복 검사할 때 필요한 것
	private final String SQL_SELECTONE_PHONE = "SELECT PHONE_NO FROM MEMBER WHERE PHONE_NO = ?";
	
	// private final String SQL_SELECTONE_MYPAGE = "SELECT * FROM MEMBER WHERE M_ID
	// = ? "; // 회원(작성자) 아이디 검색
	
	// 회원 전체 목록 보기
	private final String SQL_SELECTALL = "SELECT * FROM MEMBER";
	
	// 회원 (작성자) 아이디 검색
	private final String SQL_SELECTALL_ID = "SELECT * FROM MEMBER WHERE M_ID LIKE CONCAT ('%',?,'%')";
	
	// 아이디 찾기 할 때 필요한 폰 번호 입력
	private final String SQL_SELECTONE_FIND_ID = "SELECT * FROM MEMBER WHERE PHONE_NO= ?";
	
	// 비밀번호 찾기 할 때 필요
	private final String SQL_SELECTONE_FIND_PW = "SELECT * FROM MEMBER WHERE M_ID = ? AND M_NM = ? AND PHONE_NO= ? ";
	
	// 회원정보 업데이트
	private final String SQL_UPDATE = "UPDATE MEMBER SET M_NM = ?, CAT_NM = ?, PHONE_NO = ?, POST_NO = ? ADDRESS1 = ?, ADDRESS2 = ? WHERE M_NO = ?";
	
	// 회원 비밀번호 수정
	private final String SQL_UPDATE_PW = "UPDATE MEMBER SET M_PW = ? WHERE M_ID=?";
	
	// 회원 삭제
	private final String SQL_DELETE = "DELETE FROM MEMBER WHERE M_NO = ?";

	
	// 회원가입
	public boolean insert(MemberVO vo) { 
		jdbcTemplate.update(SQL_INSERT, vo.getUserId(), vo.getUserPw(), vo.getUserName(), vo.getCatName(), vo.getPhoneNum(),
				vo.getPostNum(), vo.getAddress1(), vo.getAddress2());
		return true;
	}

	 // 회원 정보 변경
	public boolean update(MemberVO vo) {
		if (vo.getUserPw() != null) {
			jdbcTemplate.update(SQL_UPDATE_PW, vo.getUserPw(), vo.getUserId());
			return true;
		} else {
			jdbcTemplate.update(SQL_UPDATE, vo.getUserName(), vo.getCatName(), vo.getPhoneNum(), vo.getPostNum(),
					vo.getAddress1(), vo.getAddress2(), vo.getUserNum());
			return true;
		}
	}

	// 회원탈퇴, 회원 삭제
	public boolean delete(MemberVO vo) { 
		jdbcTemplate.update(SQL_DELETE, vo.getUserNum());
		return true;
	}

	 // 중복된 아이디가 있으면 true / 없으면 false
	public MemberVO selectOne(MemberVO vo) {
		try {
			if (vo.getUserPw() != null) {
				// 로그인할 때 필요한 아이디, 비밀번호 입력
				// SELECETONE
				Object[] args = { vo.getUserId(), vo.getUserPw() };
				return jdbcTemplate.queryForObject(SQL_SELECTONE, args, new MemberRowMapper());
			} else if (vo.getUserId() != null) {
				// 1. 회원가입 할 때 아이디 중복 확인 / vo에 memberId만 존재
				// SELECETONE_ID
				return jdbcTemplate.queryForObject(SQL_SELECTONE_ID, (rs, rowNum) -> {
					MemberVO mdata= new MemberVO();
					mdata.setUserId(rs.getString("M_ID"));
					return mdata;
				});
			} else if (vo.getPhoneNum() != null) {
				// 2. 회원가입 할 때 핸드폰 번호 확인 / vo에 phoneNum만 존재
				// SELECETONE_PHONE
				return jdbcTemplate.queryForObject(SQL_SELECTONE_PHONE, (rs, rowNum) -> {
					MemberVO mdata = new MemberVO();
					mdata.setPhoneNum(rs.getString("PHONE_NO"));
					return mdata;
				});
			} 
		} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}

	// 회원 ID, PW 찾기
	public MemberVO findMember(MemberVO vo) {
		if (vo.getUserId() != null && vo.getUserPw() != null && vo.getPhoneNum() != null) {
			Object[] args = { vo.getUserId(), vo.getUserPw(), vo.getPhoneNum() };
			return jdbcTemplate.queryForObject(SQL_SELECTONE_FIND_PW, args, new MemberRowMapper());
		} else {
			Object[] args = { vo.getPhoneNum() };
			return jdbcTemplate.queryForObject(SQL_SELECTONE_FIND_ID, args, new MemberRowMapper());
		}
	}
	
	// 회원 아이디 검색, 회원 전체 출력
	public ArrayList<MemberVO> selectAll(MemberVO vo) {
		if (vo.getUserId() != null) {
			return (ArrayList<MemberVO>) jdbcTemplate.query(SQL_SELECTALL_ID, (rs, rowNum) -> {
				MemberVO mdata = new MemberVO();
				mdata.setUserId(rs.getString("M_ID"));
				return mdata;
			});
		} else {
			return (ArrayList<MemberVO>) jdbcTemplate.query(SQL_SELECTALL, new MemberRowMapper());
		}
	}

	class MemberRowMapper implements RowMapper<MemberVO> {

		@Override
		public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MemberVO vo = new MemberVO();
			vo.setUserName(rs.getString("M_NM"));
			vo.setUserId(rs.getString("M_ID"));
			vo.setUserPw(rs.getString("M_PW"));
			vo.setUserNum(rs.getInt("M_NO"));
			vo.setCatName(rs.getString("CAT_NM"));
			vo.setPhoneNum(rs.getString("PHONE_NO"));
			vo.setPostNum(rs.getString("POST_NO"));
			vo.setAddress1(rs.getString("ADDRESS1"));
			vo.setAddress1(rs.getString("ADDRESS2"));
			vo.setWarnCnt(rs.getInt("WARN_CNT"));
			vo.setRole(rs.getNString("ROLE"));

			return vo;
		}
	}
}
