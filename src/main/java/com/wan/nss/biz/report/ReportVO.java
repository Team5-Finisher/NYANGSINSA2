package com.wan.nss.biz.report;

public class ReportVO {
	private int reportNum; // 신고 PK
	private int targetNum; // 글/댓글의 PK
	private int reportStep; // 글/댓글 구분 번호 (1 : 게시글, 2 : 댓글, 3 : 대댓글)
	private int userNum; // 신고 당한 회원의 회원 번호
	private int reporterNum; // 신고한 사람의 회원 번호
	private String reportDate; // 신고 일자
	private String reportContent; // 신고 내용
	private int reportStat; // 신고 상태 (1: 정상, 2: 신고, 3: 삭제)

//	vo에만 존재하는 멤버 변수
	private String userId; // 신고 당한 회원의 ID
	private String reporterId; // 신고한 사람의 ID
	public int getReportNum() {
		return reportNum;
	}
	public void setReportNum(int reportNum) {
		this.reportNum = reportNum;
	}
	public int getTargetNum() {
		return targetNum;
	}
	public void setTargetNum(int targetNum) {
		this.targetNum = targetNum;
	}
	public int getReportStep() {
		return reportStep;
	}
	public void setReportStep(int reportStep) {
		this.reportStep = reportStep;
	}
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public int getReporterNum() {
		return reporterNum;
	}
	public void setReporterNum(int reporterNum) {
		this.reporterNum = reporterNum;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportContent() {
		return reportContent;
	}
	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}
	public int getReportStat() {
		return reportStat;
	}
	public void setReportStat(int reportStat) {
		this.reportStat = reportStat;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getReporterId() {
		return reporterId;
	}
	public void setReporterId(String reporterId) {
		this.reporterId = reporterId;
	}
	@Override
	public String toString() {
		return "ReportVO [reportNum=" + reportNum + ", targetNum=" + targetNum + ", reportStep=" + reportStep
				+ ", userNum=" + userNum + ", reporterNum=" + reporterNum + ", reportDate=" + reportDate
				+ ", reportContent=" + reportContent + ", reportStat=" + reportStat + ", userId=" + userId
				+ ", reporterId=" + reporterId + "]";
	}

}
