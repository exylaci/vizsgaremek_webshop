package webshop.order;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import webshop.customer.Customer;
import webshop.customer.CustomerRepository;
import webshop.exception.NotFindException;
import webshop.product.*;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public List<OrderDto> getOrders() {
        Type targetListType = new TypeToken<List<OrderDto>>() {
        }.getType();
        List<Order> orders = orderRepository.findAll();
        return modelMapper.map(orders, targetListType);
    }

    public OrderDto findOrder(long id) {
        Order order = getOrderWithProducts(id);
        return modelMapper.map(order, OrderDto.class);
    }

    public OrderDto createOrder(CreateOrderCommand command) {
        Customer customer = customerRepository
                .findById(command.getCustomer_id())
                .orElseThrow(() -> new NotFindException("/api/orders", "There is no customer with this id: " + command.getCustomer_id()));
        Order order = new Order(customer);

        orderRepository.save(order);
        return modelMapper.map(order, OrderDto.class);
    }

    @Transactional
    public OrderDto addProduct(long id, AddUpdateProductCommand command) {
        Order order = getOrderWithProducts(id);
        Product product = findProduct(command.getProduct_id());
        Optional<OrderedProduct> orderedProduct =
                order.getOrderedProducts()
                        .stream()
                        .filter(op -> op.getProduct().equals(product))
                        .findAny();

        if (orderedProduct.isEmpty()) {
            order.getOrderedProducts().add(new OrderedProduct(product, command.getPieces()));
        } else {
            orderedProduct.get().setPiece(orderedProduct.get().getPiece() + command.getPieces());
        }

        return modelMapper.map(order, OrderDto.class);
    }

    @Transactional
    public OrderDto deleteProduct(long orderId, long productId) {
        Order order = getOrderWithProducts(orderId);
        List<OrderedProduct> orderedProducts = order.getOrderedProducts();
        Optional<OrderedProduct> orderedProduct = orderedProducts
                .stream()
                .filter(o -> o.getProduct().getId() == productId)
                .findAny();

        orderedProduct.ifPresent(orderedProducts::remove);

        return modelMapper.map(order, OrderDto.class);
    }

    @Transactional
    public OrderDto updateProduct(long id, AddUpdateProductCommand command) {
        Order order = getOrderWithProducts(id);
        Product product =findProduct(command.getProduct_id());
        OrderedProduct orderedProduct =
                order.getOrderedProducts()
                        .stream()
                        .filter(op -> op.getProduct().equals(product))
                        .findAny()
                        .orElseThrow(() -> new NotFindException("/api/orders", "There is no product with this id: " + command.getProduct_id()));

        orderedProduct.setPiece(command.getPieces());

        return modelMapper.map(order, OrderDto.class);
    }

    public void deleteOrder(long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new NotFindException("/api/orders", "There is no order with this id: " + id));
        if (order.getOrderDate() == null) {
            orderRepository.delete(order);
        }
    }

    @Transactional
    public OrderDto closeOrder(long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new NotFindException("/api/orders", "There is no product with this id: " + id));

        if (order.getOrderedProducts() == null || order.getOrderedProducts().isEmpty()) {
            throw new IllegalStateException("The ordered products list is empty!");
        }

        if (order.getOrderDate() == null) {
            decreaseTheStoragedPieces(order);
            order.setOrderDate(LocalDate.now());
        }

        return modelMapper.map(order, OrderDto.class);
    }

    private void decreaseTheStoragedPieces(Order order) {
        for (OrderedProduct o : order.getOrderedProducts()) {
            int orderedPieces = o.getPiece();
            Product product = productRepository
                    .findById(o.getProduct().getId())
                    .orElseThrow(() -> new NotFindException("/api/orders", "There is no this product in the store: " + o.getProduct().getId()));
            if (product.getPiece() < orderedPieces) {
                throw new IllegalStateException("There is not enough in the storage to fulfill this order! id: " + o.getProduct().getId());
            }
            product.setPiece(product.getPiece() - orderedPieces);
        }
    }

    public Order getOrderWithProducts(long id) {
        return orderRepository
                .findOrderWithProducts(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFindException("/api/orders", "There is no order with this id: " + id));
    }

    private Product findProduct(long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new NotFindException("/api/orders", "There is no product with this id: " + id));
    }
}