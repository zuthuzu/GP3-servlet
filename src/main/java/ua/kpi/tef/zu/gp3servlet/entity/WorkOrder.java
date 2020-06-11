package ua.kpi.tef.zu.gp3servlet.entity;

import ua.kpi.tef.zu.gp3servlet.entity.states.OrderStatus;

import java.time.LocalDate;

/**
 * Created by Anton Domin on 2020-05-23
 */
public class WorkOrder {
	private long id;
	private LocalDate creationDate;
	private String author;
	private String manager;
	private String master;
	private OrderStatus status;
	private ItemCategory category;
	private String item;
	private String complaint;
	private int price;
	private String managerComment;
	private String masterComment;
	//only boilerplate below

	public WorkOrder() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public ItemCategory getCategory() {
		return category;
	}

	public void setCategory(ItemCategory category) {
		this.category = category;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getComplaint() {
		return complaint;
	}

	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getManagerComment() {
		return managerComment;
	}

	public void setManagerComment(String managerComment) {
		this.managerComment = managerComment;
	}

	public String getMasterComment() {
		return masterComment;
	}

	public void setMasterComment(String masterComment) {
		this.masterComment = masterComment;
	}

	public static class Builder {
		protected long id;
		protected LocalDate creationDate;
		protected String author;
		protected String manager;
		protected String master;
		protected OrderStatus status;
		protected ItemCategory category;
		protected String item;
		protected String complaint;
		protected int price;
		protected String managerComment;
		protected String masterComment;

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

		public WorkOrder build() {
			return new WorkOrder(this);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public WorkOrder(Builder builder) {
		this.id = builder.id;
		this.creationDate = builder.creationDate;
		this.author = builder.author;
		this.manager = builder.manager;
		this.master = builder.master;
		this.status = builder.status;
		this.category = builder.category;
		this.item = builder.item;
		this.complaint = builder.complaint;
		this.price = builder.price;
		this.managerComment = builder.managerComment;
		this.masterComment = builder.masterComment;
	}
}
