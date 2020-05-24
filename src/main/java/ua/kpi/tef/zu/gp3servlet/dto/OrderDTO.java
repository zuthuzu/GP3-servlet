package ua.kpi.tef.zu.gp3servlet.dto;


import ua.kpi.tef.zu.gp3servlet.entity.ItemCategory;
import ua.kpi.tef.zu.gp3servlet.entity.User;
import ua.kpi.tef.zu.gp3servlet.entity.states.AbstractState;
import ua.kpi.tef.zu.gp3servlet.entity.states.OrderStatus;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by Anton Domin on 2020-03-22
 */

public class OrderDTO {
	private long id;
	private String creationDate; //representation in user's locale, refreshed at controller
	private LocalDate actualCreationDate; //value from DB
	private String author; //displayed name, loaded separately by login
	private String authorLogin; //value from DB
	private String manager; //displayed name, loaded separately by login
	private String managerLogin; //value from DB
	private String master; //displayed name, loaded separately by login
	private String masterLogin; //value from DB
	private String category; //representation in user's locale, refreshed at controller
	private ItemCategory actualCategory; //value from DB
	private String item;
	private String complaint;
	private String status; //representation in user's locale, refreshed at controller
	private OrderStatus actualStatus; //value from DB
	private AbstractState liveState;
	private boolean isArchived;
	private int price;
	private String managerComment;
	private String masterComment;
	private String userComment;
	private int userStars;
	private String action; //when received from frontend, determines the direction of state change
	private User initiator; //user which initiated state change request

	public final static String ACTION_PROCEED = "proceed";
	public final static String ACTION_CANCEL = "cancel";

	public boolean proceed() {
		return action.equals(ACTION_PROCEED);
	}

	/** We don't need a pure toString() for DTO: it'd be poorly informative since most fields are often blank.
	 * @return a DTO text representation consisting of all its' non-empty fields
	 */
	public String toStringSkipEmpty() {
		return "OrderDTO{" +
				"id=" + id +
				", archived=" + isArchived +
				(Objects.nonNull(creationDate) ? ", creationDate='" + creationDate + '\'' : "") +
				(Objects.nonNull(actualCreationDate) ? ", actualCreationDate='" + actualCreationDate + '\'' : "") +
				(Objects.nonNull(author) ? ", author='" + author + '\'' : "") +
				(Objects.nonNull(authorLogin) ? ", authorLogin='" + authorLogin + '\'' : "") +
				(Objects.nonNull(manager) ? ", manager='" + manager + '\'' : "") +
				(Objects.nonNull(managerLogin) ? ", managerLogin='" + managerLogin + '\'' : "") +
				(Objects.nonNull(master) ? ", master='" + master + '\'' : "") +
				(Objects.nonNull(masterLogin) ? ", masterLogin='" + masterLogin + '\'' : "") +
				(Objects.nonNull(category) ? ", category='" + category + '\'' : "") +
				(Objects.nonNull(actualCategory) ? ", actualCategory='" + actualCategory + '\'' : "") +
				(Objects.nonNull(item) ? ", item='" + item + '\'' : "") +
				(Objects.nonNull(complaint) ? ", complaint='" + complaint + '\'' : "") +
				(Objects.nonNull(status) ? ", status='" + status + '\'' : "") +
				(Objects.nonNull(actualStatus) ? ", actualStatus='" + actualStatus + '\'' : "") +
				(Objects.nonNull(liveState) ? ", liveState='" + liveState.getCurrentState() + '\'' : "") +
				(price != 0 ? ", price='" + price + '\'' : "") +
				(Objects.nonNull(managerComment) ? ", managerComment='" + managerComment + '\'' : "") +
				(Objects.nonNull(masterComment) ? ", masterComment='" + masterComment + '\'' : "") +
				(Objects.nonNull(userComment) ? ", userComment='" + userComment + '\'' : "") +
				(userStars != 0 ? ", userStars='" + userStars + '\'' : "") +
				(Objects.nonNull(action) ? ", action='" + action + '\'' : "") +
				(Objects.nonNull(initiator) ? ", initiator='" + initiator.getLogin() + '\'' : "") +
				'}';
	}
	//only boilerplate below

	public OrderDTO() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDate getActualCreationDate() {
		return actualCreationDate;
	}

	public void setActualCreationDate(LocalDate actualCreationDate) {
		this.actualCreationDate = actualCreationDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorLogin() {
		return authorLogin;
	}

	public void setAuthorLogin(String authorLogin) {
		this.authorLogin = authorLogin;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManagerLogin() {
		return managerLogin;
	}

	public void setManagerLogin(String managerLogin) {
		this.managerLogin = managerLogin;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getMasterLogin() {
		return masterLogin;
	}

	public void setMasterLogin(String masterLogin) {
		this.masterLogin = masterLogin;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ItemCategory getActualCategory() {
		return actualCategory;
	}

	public void setActualCategory(ItemCategory actualCategory) {
		this.actualCategory = actualCategory;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public OrderStatus getActualStatus() {
		return actualStatus;
	}

	public void setActualStatus(OrderStatus actualStatus) {
		this.actualStatus = actualStatus;
	}

	public AbstractState getLiveState() {
		return liveState;
	}

	public void setLiveState(AbstractState liveState) {
		this.liveState = liveState;
	}

	public boolean isArchived() {
		return isArchived;
	}

	public void setArchived(boolean archived) {
		isArchived = archived;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public User getInitiator() {
		return initiator;
	}

	public void setInitiator(User initiator) {
		this.initiator = initiator;
	}

	public static class Builder {
		private long id;
		private String creationDate;
		private LocalDate actualCreationDate;
		private String author;
		private String authorLogin;
		private String manager;
		private String managerLogin;
		private String master;
		private String masterLogin;
		private String category;
		private ItemCategory actualCategory;
		private String item;
		private String complaint;
		private String status;
		private OrderStatus actualStatus;
		private AbstractState liveState;
		private boolean isArchived;
		private int price;
		private String managerComment;
		private String masterComment;
		private String userComment;
		private int userStars;
		private String action;
		private User initiator;

		public Builder id(long id) {
			this.id = id;
			return this;
		}

		public Builder creationDate(String creationDate) {
			this.creationDate = creationDate;
			return this;
		}

		public Builder actualCreationDate(LocalDate actualCreationDate) {
			this.actualCreationDate = actualCreationDate;
			return this;
		}

		public Builder author(String author) {
			this.author = author;
			return this;
		}

		public Builder authorLogin(String authorLogin) {
			this.authorLogin = authorLogin;
			return this;
		}

		public Builder manager(String manager) {
			this.manager = manager;
			return this;
		}

		public Builder managerLogin(String managerLogin) {
			this.managerLogin = managerLogin;
			return this;
		}

		public Builder master(String master) {
			this.master = master;
			return this;
		}

		public Builder masterLogin(String masterLogin) {
			this.masterLogin = masterLogin;
			return this;
		}

		public Builder category(String category) {
			this.category = category;
			return this;
		}

		public Builder actualCategory(ItemCategory actualCategory) {
			this.actualCategory = actualCategory;
			return this;
		}

		public Builder item(String item) {
			this.item = item;
			return this;
		}

		public Builder complaint(String complaint) {
			this.complaint = complaint;
			return this;
		}

		public Builder status(String status) {
			this.status = status;
			return this;
		}

		public Builder actualStatus(OrderStatus actualStatus) {
			this.actualStatus = actualStatus;
			return this;
		}

		public Builder liveState(AbstractState liveState) {
			this.liveState = liveState;
			return this;
		}

		public Builder isArchived(boolean isArchived) {
			this.isArchived = isArchived;
			return this;
		}

		public Builder price(int price) {
			this.price = price;
			return this;
		}

		public Builder managerComment(String managerComment) {
			this.managerComment = managerComment;
			return this;
		}

		public Builder masterComment(String masterComment) {
			this.masterComment = masterComment;
			return this;
		}

		public Builder userComment(String userComment) {
			this.userComment = userComment;
			return this;
		}

		public Builder userStars(int userStars) {
			this.userStars = userStars;
			return this;
		}

		public Builder action(String action) {
			this.action = action;
			return this;
		}

		public Builder initiator(User initiator) {
			this.initiator = initiator;
			return this;
		}

		public OrderDTO build() {
			return new OrderDTO(this);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	OrderDTO(Builder builder) {
		this.id = builder.id;
		this.creationDate = builder.creationDate;
		this.actualCreationDate = builder.actualCreationDate;
		this.author = builder.author;
		this.authorLogin = builder.authorLogin;
		this.manager = builder.manager;
		this.managerLogin = builder.managerLogin;
		this.master = builder.master;
		this.masterLogin = builder.masterLogin;
		this.category = builder.category;
		this.actualCategory = builder.actualCategory;
		this.item = builder.item;
		this.complaint = builder.complaint;
		this.status = builder.status;
		this.actualStatus = builder.actualStatus;
		this.liveState = builder.liveState;
		this.isArchived = builder.isArchived;
		this.price = builder.price;
		this.managerComment = builder.managerComment;
		this.masterComment = builder.masterComment;
		this.userComment = builder.userComment;
		this.userStars = builder.userStars;
		this.action = builder.action;
		this.initiator = builder.initiator;
	}
}
