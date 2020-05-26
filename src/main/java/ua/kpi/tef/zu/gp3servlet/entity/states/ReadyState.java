package ua.kpi.tef.zu.gp3servlet.entity.states;

import org.apache.commons.lang3.StringUtils;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;

import java.util.Arrays;

/**
 * Created by Anton Domin on 2020-03-25
 */
@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class ReadyState extends AbstractState {
	public ReadyState() {
		setCurrentState(OrderStatus.READY);
		setNextState(OrderStatus.ARCHIVED);
		setRequiredRole(RoleType.ROLE_MANAGER);
		setAvailableFields(Arrays.asList("manager_comment"));
		setCancelable(false);
		setButtonText("order.action.delivered");
	}

	@Override
	public void applyAvailableFields(OrderDTO to, OrderDTO from) {
		to.setManagerComment(!StringUtils.isEmpty(from.getManagerComment()) ? from.getManagerComment() : to.getManagerComment());
	}
}
