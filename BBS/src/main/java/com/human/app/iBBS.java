package com.human.app;

import java.util.ArrayList;

public interface iBBS {
	void writebbs(String sTitle, String sContent, String sWriter, String fileName);
	ArrayList<Listinfo> getList(int start,int end);
	void updatebbs(int nBbsID, String sTitle, String sContent);
	void deletebbs(int dBbsId);
	Listinfo getPost(int bbs_id); //불러올 데이터가 하나일경우 배열을 쓰지않고 옆에 클랙스명 추가
}
