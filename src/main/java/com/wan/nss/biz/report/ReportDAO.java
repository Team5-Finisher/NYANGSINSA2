package com.wan.nss.biz.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("reportDAO")
public class ReportDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/*
	 * C : 게시글, 댓글/대댓글 신고. TARGET_NO 에는 게시글 PK 혹은 댓글 PK가 들어가야 함. RP_STEP = 1(글),
	 * 2(댓글), 3(대댓글)
	 */
	private final String INSERT = "INSERT INTO REPORT (TARGET_NO, RP_STEP, M_NO, RP_CONTENT) VALUES (?, ?, (SELECT M_NO FROM MEMBER WHERE M_ID = ?), ?)";

	// R : Report 전체 보기
	private final String SELECT_ALL = "SELECT r.*, m1.M_ID AS M_ID, m2.M_ID AS REPORTER_ID FROM REPORT r INNER JOIN `MEMBER` m1 ON r.M_NO = m1.M_NO INNER JOIN `MEMBER` m2 ON r.RP_M_NO  = m2.M_NO ORDER BY RP_NO DESC";

	// R : Report 상세보기
	// ? : rpNum
	private final String SELECT_ONE = "SELECT r.*, m1.M_ID AS M_ID, m2.M_ID AS REPORTER_ID FROM REPORT r INNER JOIN `MEMBER` m1 ON r.M_NO = m1.M_NO INNER JOIN `MEMBER` m2 ON r.RP_M_NO  = m2.M_NO WHERE RP_NO = ?";

	// U : Report (처리 전 : 1, 처리 후 : 2)
	private final String UPDATE = "UPDATE REPORT SET STATUS = ? WHERE RP_NO = ?";

	public boolean insert(ReportVO vo) {
		if (jdbcTemplate.update(INSERT, vo.getTargetNum(), vo.getReportStep(), vo.getUserId(),
				vo.getReportContent()) < 1) {
			return false;
		}
		return true;
	}

	public ArrayList<ReportVO> selectAll(ReportVO vo) {
		return (ArrayList<ReportVO>) jdbcTemplate.query(SELECT_ALL, new ReportRowMapper());
	}

	public ReportVO selectOne(ReportVO vo) {
		return jdbcTemplate.queryForObject(SELECT_ONE, new ReportRowMapper(), vo.getReportNum());
	}

	public boolean update(ReportVO vo) {
		// status => 처리 전 : 1, 처리 후 : 2
		if (jdbcTemplate.update(UPDATE, vo.getReportStat(),vo.getReportNum()) < 1) {
			return false;
		}
		return true;
	}

}

class ReportRowMapper implements RowMapper<ReportVO> {
	@Override
	public ReportVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReportVO data = new ReportVO();
		data.setReportNum(rs.getInt("RP_NO"));
		data.setTargetNum(rs.getInt("TARGET_NO"));
		data.setReportStep(rs.getInt("RP_STEP"));
		data.setUserNum(rs.getInt("M_NO"));
		data.setReporterNum(rs.getInt("RP_M_NO"));
		data.setReportDate(rs.getString("RP_DATE"));
		data.setReportContent(rs.getString("RP_CONTENT"));
		data.setReportStat(rs.getInt("STATUS"));

		data.setUserId(rs.getString("M_ID"));
		data.setReporterId(rs.getString("REPORTER_ID"));
		return data;
	}

}
