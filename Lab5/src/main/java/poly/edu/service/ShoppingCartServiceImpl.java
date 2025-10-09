package poly.edu.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import poly.edu.entity.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SessionScope
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    // Dùng Map để quản lý các mặt hàng trong giỏ, key là id mặt hàng
    Map<Integer, Item> map = new HashMap<>();

    // Thêm hoặc tăng số lượng nếu đã tồn tại
    @Override
    public Item add(Integer id) {
        Item item = map.get(id);
        if (item == null) {
            // Ở đây ta mô phỏng việc tạo 1 mặt hàng (vì chưa có database)
            item = new Item(id, "Sản phẩm " + id, 100.0 * id, 1);
            map.put(id, item);
        } else {
            item.setQty(item.getQty() + 1);
        }
        return item;
    }

    // Xóa mặt hàng khỏi giỏ
    @Override
    public void remove(Integer id) {
        map.remove(id);
    }

    // Cập nhật số lượng mới
    @Override
    public Item update(Integer id, int qty) {
        Item item = map.get(id);
        if (item != null) {
            item.setQty(qty);
            if (qty <= 0) {
                map.remove(id);
            }
        }
        return item;
    }

    // Xóa sạch giỏ hàng
    @Override
    public void clear() {
        map.clear();
    }

    // Lấy danh sách mặt hàng
    @Override
    public Collection<Item> getItems() {
        return map.values();
    }

    // Tổng số lượng
    @Override
    public int getCount() {
        return map.values().stream()
                .mapToInt(Item::getQty)
                .sum();
    }

    // Tổng tiền
    @Override
    public double getAmount() {
        return map.values().stream()
                .mapToDouble(item -> item.getPrice() * item.getQty())
                .sum();
    }
}
