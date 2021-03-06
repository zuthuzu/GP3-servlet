package ua.kpi.tef.zu.gp3servlet.entity.states;


import org.apache.commons.lang3.StringUtils;
import ua.kpi.tef.zu.gp3servlet.controller.DatabaseException;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;
import ua.kpi.tef.zu.gp3servlet.service.OrderService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton Domin on 2020-03-25
 */
public abstract class AbstractState {
	private OrderStatus currentState;
	private OrderStatus nextState;

	private RoleType requiredRole; //who can call initiate a change from this state to another
	private List<String> requiredFields = new ArrayList<>(); //what he MUST fill before proceeding
	private List<String> availableFields = new ArrayList<>(); //what he CAN change before proceeding

	private boolean isCancelable;
	private List<String> preCancelFields = new ArrayList<>(); //what he MUST fill before cancelling

	private String buttonText;

	public void verifyRequest(OrderDTO from) throws IllegalArgumentException {
		if (!from.getAction().equals(OrderDTO.ACTION_PROCEED) && !from.getAction().equals(OrderDTO.ACTION_CANCEL)) {
			throw new IllegalArgumentException("Illegal action in update request: " + from.toStringSkipEmpty());
		}
		if (!from.proceed() && !isCancelable()) {
			throw new IllegalArgumentException("Illegal cancel attempt in update request: " + from.toStringSkipEmpty());
		}
		if (from.getInitiator().getRole() != getRequiredRole()) {
			throw new IllegalArgumentException("Illegal user role in update request: " + from.toStringSkipEmpty());
		}
		verifyRequiredFields(from);
	}


	public void verifyRequiredFields(OrderDTO from) throws IllegalArgumentException {
		for (String field : from.proceed() ? requiredFields : preCancelFields) {
			switch (field) {
				case "price":
					if (from.getPrice() == 0) {
						throw new IllegalArgumentException("Incomplete data: missing price in update request: "
								+ from.toStringSkipEmpty());
					}
					break;
				case "manager_comment":
					if (StringUtils.isEmpty(from.getManagerComment())) {
						throw new IllegalArgumentException("Incomplete data: missing manager comment in update request: "
								+ from.toStringSkipEmpty());
					}
					break;
				case "master_comment":
					if (StringUtils.isEmpty(from.getMasterComment())) {
						throw new IllegalArgumentException("Incomplete data: missing master comment in update request: "
								+ from.toStringSkipEmpty());
					}
					break;
				case "user_stars":
					if (from.getUserStars() <= 0 || from.getUserStars() > 5) {
						throw new IllegalArgumentException("Incomplete data: missing user rating in update request: "
								+ from.toStringSkipEmpty());
					}
					break;
			}
		}
	}

	public void processOrder(OrderService service, OrderDTO order) throws DatabaseException {
		if (moveToArchive(order.proceed())) {
			service.archiveOrder(order);
		} else {
			service.saveExistingOrder(order);
		}
	}

	/**
	 * Carefully applies front end data onto DB data where it is necessitated by the current state.
	 *
	 * @param dbOrder    order the way it's currently present in DB
	 * @param modelOrder order the way it arrived from frontend
	 * @return an entity ready for updating into DB
	 */
	public OrderDTO assembleOrder(OrderDTO dbOrder, OrderDTO modelOrder) {
		dbOrder.setManagerLogin((requiredRole == RoleType.ROLE_MANAGER && StringUtils.isEmpty(dbOrder.getManagerLogin()))
				? modelOrder.getInitiator().getLogin() : dbOrder.getManagerLogin()); //first authorised initiator gets recorded
		dbOrder.setMasterLogin((requiredRole == RoleType.ROLE_MASTER && StringUtils.isEmpty(dbOrder.getMasterLogin()))
				? modelOrder.getInitiator().getLogin() : dbOrder.getMasterLogin()); //first authorised initiator gets recorded

		dbOrder.setAction(modelOrder.getAction());
		dbOrder.setInitiator(modelOrder.getInitiator());
		dbOrder.setActualStatus(dbOrder.proceed() ? nextState : OrderStatus.CANCELLED);

		applyAvailableFields(dbOrder, modelOrder);
		return dbOrder;
	}

	public abstract void applyAvailableFields(OrderDTO to, OrderDTO from);

	/**
	 * @param proceed direction of state change (true = to next state, false = cancel the order)
	 * @return whether we need to move the order to archive during this state change
	 */
	public boolean moveToArchive(boolean proceed) {
		if (!proceed && isCancelable) return true;
		return proceed && nextState.isArchived() && !currentState.isArchived();
	}

	//only boilerplate getters and setters below
	public OrderStatus getCurrentState() {
		return currentState;
	}

	public void setCurrentState(OrderStatus currentState) {
		this.currentState = currentState;
	}

	public OrderStatus getNextState() {
		return nextState;
	}

	public void setNextState(OrderStatus nextState) {
		this.nextState = nextState;
	}

	public RoleType getRequiredRole() {
		return requiredRole;
	}

	public void setRequiredRole(RoleType requiredRole) {
		this.requiredRole = requiredRole;
	}

	public List<String> getRequiredFields() {
		return requiredFields;
	}

	public void setRequiredFields(List<String> requiredFields) {
		this.requiredFields = requiredFields;
	}

	public List<String> getAvailableFields() {
		return availableFields;
	}

	public void setAvailableFields(List<String> availableFields) {
		this.availableFields = availableFields;
	}

	public boolean isCancelable() {
		return isCancelable;
	}

	public void setCancelable(boolean cancelable) {
		isCancelable = cancelable;
	}

	public List<String> getPreCancelFields() {
		return preCancelFields;
	}

	public void setPreCancelFields(List<String> preCancelFields) {
		this.preCancelFields = preCancelFields;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}
}
