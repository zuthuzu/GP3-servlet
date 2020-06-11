import org.junit.Test;
import ua.kpi.tef.zu.gp3servlet.entity.ArchiveOrder;
import ua.kpi.tef.zu.gp3servlet.entity.ItemCategory;
import ua.kpi.tef.zu.gp3servlet.entity.states.OrderStatus;
import ua.kpi.tef.zu.gp3servlet.service.OrderService;

import static org.junit.Assert.assertEquals;

/**
 * Created by Anton Domin on 2020-06-11
 */
public class OrderServiceTest {
	private final OrderService orderService;

	public OrderServiceTest() {
		this.orderService = new OrderService();
	}

	@Test
	public void archiveBuilderTest() {
		ArchiveOrder order = ArchiveOrder.builder()
				.id(1)
				.author("s1")
				.category(ItemCategory.APPLIANCE)
				.complaint("in pieces")
				.item("blender")
				.manager("bcawl")
				.managerComment("UNGUENTS")
				.master("sage")
				.masterComment("it's bad")
				.price(1000)
				.status(OrderStatus.ARCHIVED)
				.userStars(5)
				.userComment("it's good now!")
				.build();
		assertEquals(order.getItem(), "blender");
		assertEquals(order.getPrice(), 1000);
		assertEquals(order.getUserStars(), 5);
	}
}
