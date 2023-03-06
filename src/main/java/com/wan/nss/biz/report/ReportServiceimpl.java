package com.wan.nss.biz.report;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("reportService")
public class ReportServiceimpl implements ReportService {
	
	@Autowired
	private ReportDAO reportDAO;
	
	@Override
	public ArrayList<ReportVO> selectAll(ReportVO rvo){
		return reportDAO.selectAll(rvo);
	}

	@Override
	public ReportVO selectOne(ReportVO rvo) {
		return reportDAO.selectOne(rvo);
	}

	@Override
	public boolean update(ReportVO rvo) {
		return reportDAO.update(rvo);
	}

	@Override
	public boolean delete(ReportVO rvo) {
		return reportDAO.delete(rvo);
	}
}
