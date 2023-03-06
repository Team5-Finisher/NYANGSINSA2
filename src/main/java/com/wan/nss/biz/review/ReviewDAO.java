package com.wan.nss.biz.review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("reviewDAO")
public class ReviewDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 리뷰 작성	
	private final String SQL_INSERT = "INSERT INTO REVIEW (P_NO, M_NO, R_CONTENT, R_RATE) VALUES(?, (SELECT M_NO FROM MEMBER WHERE M_ID = ?), ?, ?)";
	
	// 리뷰 전체보기 (관리자)
	private final String SQL_SELECTALL = "SELECT r.*, m.M_ID FROM REVIEW r INNER JOIN MEMBER m ON r.M_NO = m.M_NO ORDER BY R_NO DESC";
	
	// 제품 번호에 대한 리뷰 보기
	private final String SQL_SELECTALL_PRODUCT = "SELECT * FROM REVIEW WHERE P_NO=? ORDER BY R_NO DESC";
	
	// 내가 작성한 리뷰 보기
	private final String SQL_SELECTALL_USER = "SELECT r.R_NO, p.P_NM, r.R_CONTENT, r.R_RATE, m.M_ID, r.R_DT FROM REVIEW r INNER JOIN PRODUCT p ON r.P_NO = p.P_NO INNER JOIN MEMBER m ON r.M_NO = m.M_NO WHERE m.M_NO=(SELECT M_NO FROM MEMBER WHERE M_ID = ?) ORDER BY R_NO DESC";

	// 리뷰 삭제
	private final String SQL_DELETE = "DELETE FROM REVIEW WHERE R_NO=?";
	
	// 리뷰 검색
//	private final String SQL_SELECTALL_SEARCH = "SELECT * FROM REVIEW WHERE M_NO LIKE CONCAT('%',?,'%')";

	
	public boolean insert(ReviewVO rvo) { 
		// 리뷰 작성
		jdbcTemplate.update(SQL_INSERT, rvo.getpNum(), rvo.getrWriter(), rvo.getrContent(), rvo.getrDate(),
				rvo.getrRate());
		return true;
	}

	public boolean delete(ReviewVO rvo) { 
		// 리뷰 삭제
		jdbcTemplate.update(SQL_DELETE, rvo.getrNum());
		return true;
	}

	public ArrayList<ReviewVO> selectAll(ReviewVO rvo) {
		if (rvo.getrSearchCondition() == null) { 
			// 전체 리뷰 보기 (관리자)
			return (ArrayList<ReviewVO>) jdbcTemplate.query(SQL_SELECTALL, new ReviewRowMapper());
			
	} else if (rvo.getrSearchCondition().equals("pNum")) {
			// 제품 번호 리뷰 보기 
			Object [] args = { rvo.getpNum() };
			return (ArrayList<ReviewVO>) jdbcTemplate.query(SQL_SELECTALL_PRODUCT, args, new ReviewRowMapper());
			
	} else if (rvo.getrSearchCondition().equals("rWriter")) {
		// 내가 작성한 리뷰 보기 
		Object [] args = { rvo.getrWriter() };
		return (ArrayList<ReviewVO>) jdbcTemplate.query(SQL_SELECTALL_USER, args, new ReviewRowMapper());
	}
		return null;
	}

	class ReviewRowMapper implements RowMapper<ReviewVO> {

		@Override
		public ReviewVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReviewVO rvo = new ReviewVO();
			rvo.setpNum(rs.getInt("P_NO"));
			rvo.setrNum(rs.getInt("R_NO"));
			rvo.setrWriter(rs.getInt("M_NO"));
			rvo.setrContent(rs.getString("R_CONTENT"));
			rvo.setrDate(rs.getString("DATE"));
			rvo.setrRate(rs.getInt("R_RATE"));
			rvo.setUserId(rs.getString("M_ID"));
			rvo.setpName(rs.getString("P_NM"));
			

			return rvo;
		}
	}
}
