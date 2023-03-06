package com.wan.nss.biz.report;

import java.util.ArrayList;

public interface ReportService {
	public ArrayList<ReportVO> selectAll(ReportVO rvo);
	public ReportVO selectOne(ReportVO rvo);
	public boolean update(ReportVO rvo);
	public boolean delete(ReportVO rvo);
}
