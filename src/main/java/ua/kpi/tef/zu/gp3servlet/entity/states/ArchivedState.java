package ua.kpi.tef.zu.gp3servlet.entity.states;

import org.apache.commons.lang3.StringUtils;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.entity.RoleType;

import java.util.Arrays;

/**
 * Created by Anton Domin on 2020-03-25
 */
@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class ArchivedState extends AbstractState {
	public ArchivedState() {
		setCurrentState(OrderStatus.ARCHIVED);
		setNextState(OrderStatus.ARCHIVED);
		setRequiredRole(RoleType.ROLE_USER);
		setRequiredFields(Arrays.asList("user_stars"));
		setAvailableFields(Arrays.asList("user_stars", "user_comment"));
		setCancelable(false);
		setButtonText("order.action.rate");
	}

	@Override
	public void applyAvailableFields(OrderDTO to, OrderDTO from) {
		to.setUserComment(!StringUtils.isEmpty(from.getUserComment()) ? from.getUserComment() : to.getUserComment());
		to.setUserStars(from.getUserStars());
	}
}
