package com.human.app;

import java.util.ArrayList;

public interface iReply {
		void addReply (int rbbs_id, String rContent, String rWriter);
		ArrayList<Replyinfo> getReplyList(int bbs_id);
		void delete(int reply_id);
		void update(String uContent, int uReply_id);
}
