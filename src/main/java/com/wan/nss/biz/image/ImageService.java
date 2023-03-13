package com.wan.nss.biz.image;

import java.util.ArrayList;

public interface ImageService {
	public ArrayList<ImageVO> selectAll(ImageVO ivo);
	public ImageVO selectOne(ImageVO ivo);
	public boolean insert(ImageVO ivo);
	public boolean update(ImageVO ivo);
	public boolean delete(ImageVO ivo);
}
