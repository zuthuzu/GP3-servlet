package ua.kpi.tef.zu.gp3servlet.entity.states;


import org.apache.commons.lang3.StringUtils;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;

import java.util.Arrays;

/**
 * Created by Anton Domin on 2020-03-25
 */
@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class PendingState extends AbstractState {
	public PendingState() {
		setCurrentState(OrderStatus.PENDING);
		setNextState(OrderStatus.ACCEPTED);
		setRequiredRole(RoleType.ROLE_MANAGER);
		setRequiredFields(Arrays.asList("price"));
		setAvailableFields(Arrays.asList("category", "item", "manager_comment", "price"));
		setCancelable(true);
		setPreCancelFields(Arrays.asList("manager_comment"));
		setButtonText("order.action.accept");
	}

	@Override
	public void applyAvailableFields(OrderDTO to, OrderDTO from) {
		to.setActualCategory(from.getActualCategory() != null ? from.getActualCategory() : to.getActualCategory());
		to.setItem(!StringUtils.isEmpty(from.getItem()) ? from.getItem() : to.getItem());
		to.setManagerComment(!StringUtils.isEmpty(from.getManagerComment()) ? from.getManagerComment() : to.getManagerComment());
		to.setPrice(from.getPrice());
	}
}
