package ua.kpi.tef.zu.gp3servlet.entity;

import ua.kpi.tef.zu.gp3servlet.entity.states.OrderStatus;

import java.time.LocalDate;

/**
 * Created by Anton Domin on 2020-05-23
 */
public class ArchiveOrder extends WorkOrder {
	private String userComment;
	private int userStars;
	//only boilerplate below

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

	public static class Builder extends WorkOrder.Builder {
		private String userComment;
		private int userStars;

		public Builder id(long i) {
			this.id = i;
			return this;
		}

		public Builder creationDate(LocalDate ld) {
			this.creationDate = ld;
			return this;
		}

		public Builder author(String s) {
			this.author = s;
			return this;
		}

		public Builder manager(String s) {
			this.manager = s;
			return this;
		}

		public Builder master(String s) {
			this.master = s;
			return this;
		}

		public Builder status(OrderStatus s) {
			this.status = s;
			return this;
		}

		public Builder category(ItemCategory i) {
			this.category = i;
			return this;
		}

		public Builder item(String s) {
			this.item = s;
			return this;
		}

		public Builder complaint(String s) {
			this.complaint = s;
			return this;
		}

		public Builder price(int i) {
			this.price = i;
			return this;
		}

		public Builder managerComment(String s) {
			this.managerComment = s;
			return this;
		}

		public Builder masterComment(String s) {
			this.masterComment = s;
			return this;
		}

		public Builder userStars(int stars) {
			this.userStars = stars;
			return this;
		}

		public Builder userComment(String s) {
			this.userComment = s;
			return this;
		}

		public ArchiveOrder build() {
			return new ArchiveOrder(this);
		}
	}

	public static Builder builder() {
		return new ArchiveOrder.Builder();
	}

	public ArchiveOrder(Builder builder) {
		super(builder);
		this.userStars = builder.userStars;
		this.userComment = builder.userComment;
	}
}
