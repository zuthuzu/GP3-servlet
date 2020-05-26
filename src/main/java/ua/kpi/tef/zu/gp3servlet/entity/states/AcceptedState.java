package ua.kpi.tef.zu.gp3servlet.entity.states;


import org.apache.commons.lang3.StringUtils;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;

import java.util.Arrays;

/**
 * Created by Anton Domin on 2020-03-25
 */
@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class AcceptedState extends AbstractState {
	public AcceptedState() {
		setCurrentState(OrderStatus.ACCEPTED);
		setNextState(OrderStatus.WORKING);
		setRequiredRole(RoleType.ROLE_MASTER);
		//setRequiredFields(); //no required fields at this state
		setAvailableFields(Arrays.asList("master_comment"));
		setCancelable(true);
		setPreCancelFields(Arrays.asList("master_comment"));
		setButtonText("order.action.take");
	}

	@Override
	public void applyAvailableFields(OrderDTO to, OrderDTO from) {
		to.setMasterComment(!StringUtils.isEmpty(from.getMasterComment()) ? from.getMasterComment() : to.getMasterComment());
	}
}
