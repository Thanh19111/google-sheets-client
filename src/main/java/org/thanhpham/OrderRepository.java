package org.thanhpham;

import org.thanhpham.entity.Order;
import org.thanhpham.repository.JpaRepository;
import org.thanhpham.service.IGoogleSheetClient;

public class OrderRepository extends JpaRepository<Order, String> {

    public OrderRepository(IGoogleSheetClient client) {
        super(client);
    }
}
