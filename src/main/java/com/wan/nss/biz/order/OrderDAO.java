package com.wan.nss.biz.order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.wan.nss.biz.orderdetail.OrderDetailVO;



@Repository("orderDAO")
public class OrderDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// 주문 추가
	private final String SQL_INSERT = "INSERT INTO ORDER (M_NO, O_DT, RCV_NM, RCV_PHONE_NO, RCV_ADDRESS, O_PAY) VALUES((SELECT M_NO FROM MEMBER WHERE M_ID = ?), ?, ?, ?, ?, ?)";
	
	// 주문 전체 목록 (관리자)
	private final String SQL_SELECTALL = "SELECT * FROM ORDER ORDER BY O_NO DESC ";
	
	// 주문 완료 했을 때 해당 회원의 주문 전체 내역
	private final String SQL_SELECTALL_ORDER = "SELECT * FROM ORDER WHERE M_NO =? ORDER BY O_NO DESC ";
	
	// 주문 총 가격
	private final String SQL_SELECTONE_TOTAL_PRICE = "	SELECT SUM(PRODUCT.P_PRICE*((100-PRODUCT.DC_PERCENT)/100)*ORDER_DETAIL.OD_CNT) AS TOTAL "
			+ " FROM ORDER_DETAIL od INNER JOIN PRODUCT p ON od.P_NO = p.P_NO WHERE od.O_NO = ?";
	
	// 현재 회원이 가장 최근에 추가한 주문
	final String SQL_SELECTONE_LATESTORDER = "SELECT O_NO FROM ORDER WHERE M_NO =? AND ROWNUM <=1 ORDER BY O_NO DESC";
	
	// 주문 날짜 (관리자)
	final String SQL_SELECTALL_DATE = " SELECT o.O_DT, SUM(p.P_PRICE*((100-p.DC_PERCENT)/100)*od.OD_CNT) AS TOTAL FROM ORDER o "
			+ " INNER JOIN ORDER_DETAIL od ON o.O_NO = od.O_NO " + "INNER JOIN PRODUCT p ON p.P_NO =od.P_NO "
			+ "	WHERE o.O_DT " + "BETWEEN TO_DATE(?, 'YYYYMMDD') AND TO_DATE(?) " + "GROUP BY od.O_NO, o.O_DT "
			+ "	ORDER BY o.O_DT DESC";
	
	// 주문 삭제
	private final String SQL_DELETE = "DELETE FROM ORDER WHERE O_NO=? ";

	// 주문 추가
	public boolean insert(OrderVO vo) {
		jdbcTemplate.update(SQL_INSERT, vo.getoNum(), vo.getoDate(), vo.getRcvName(), vo.getRcvPhoneNum(),
				vo.getRcvAddress(), vo.getoPay());
		return true;
	}

	//주문 삭제
	public boolean delete(OrderVO vo) {
		jdbcTemplate.update(SQL_DELETE, vo.getoNum());
		return true;
	}

	public OrderVO selectOne(OrderVO vo) {
		
		if (vo.getoSearchCondition() != null) {
			if (vo.getoSearchCondition().equals("totalPrice")) {
				// 주문 총 가격
				OrderDetailVO odvo = new OrderDetailVO();
				Object[] args = { odvo.getOdNum() };
				return jdbcTemplate.queryForObject(SQL_SELECTONE_TOTAL_PRICE, args, new OrderRowMapper());
					
			} else if (vo.getoSearchCondition().equals("lastOrder")) {
				// 현재 회원이 가장 최근에 추가한 주문
				Object[] args = { vo.getUserId() };
				return jdbcTemplate.queryForObject(SQL_SELECTONE_LATESTORDER, args, new OrderRowMapper());
			}
		}
		return null;
	}
	
	public ArrayList<OrderVO> selectAll(OrderVO vo) {
		
			if(vo.getUserId() != null) {
				// 주문 완료 했을 때 해당 회원의 주문 전체 내역
				Object[] args = { vo.getUserNum() };
				return (ArrayList<OrderVO>) jdbcTemplate.query(SQL_SELECTALL_ORDER, args, new OrderRowMapper());

			} else if (vo.getoSearchCondition().equals("all")) {
				// 주문 전체 내역 
				return (ArrayList<OrderVO>) jdbcTemplate.query(SQL_SELECTALL, new OrderRowMapper());	
				
			} else if (vo.getoSearchCondition().equals("date")) {
				// 주문 날짜 (관리자)
				Object [] args = { vo.getoDate() };
				return (ArrayList<OrderVO>) jdbcTemplate.query(SQL_SELECTALL_DATE, args, new OrderRowMapper());
			}
		return null;
	}

	class OrderRowMapper implements RowMapper<OrderVO> { 

		@Override
		public OrderVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			OrderVO vo = new OrderVO();
			vo.setUserNum(rs.getInt("M_NO"));
			vo.setoDate(rs.getString("O_DT"));
			vo.setoNum(rs.getInt("O_NO"));
			vo.setoPay(rs.getString("O_PAY"));
			vo.setRcvAddress(rs.getString("RCV_ADDRESS"));
			vo.setRcvName(rs.getString("RCV_NM"));
			vo.setRcvPhoneNum(rs.getString("RCV_PHONE_NO"));
			return vo;

		}
	}
}
