package com.wan.nss.biz.member;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("memberService")
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberDAO memberDAO;

	@Override
	public boolean insert(MemberVO vo) {
		return memberDAO.insert(vo);
	}

	@Override
	public boolean update(MemberVO vo) {
		return memberDAO.update(vo);
	}

	@Override
	public boolean delete(MemberVO vo) {
		return memberDAO.delete(vo);
	}

	@Override
	public MemberVO selectOne(MemberVO vo) {
		return memberDAO.selectOne(vo);
	}

	@Override
	public ArrayList<MemberVO> selectAll(MemberVO vo) {
		return memberDAO.selectAll(vo);
	}

}
