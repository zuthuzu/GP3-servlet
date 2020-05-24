package ua.kpi.tef.zu.gp3servlet.entity;

/**
 * Created by Anton Domin on 2020-05-23
 */
public class ArchiveOrder extends WorkOrder {
	private String userComment;
	private int userStars;

	public ArchiveOrder(String userComment, int userStars) {
		this.userComment = userComment;
		this.userStars = userStars;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	public int getUserStars() {
		return userStars;
	}

	public void setUserStars(int userStars) {
		this.userStars = userStars;
	}
}
