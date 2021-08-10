package webshop.product;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import webshop.category.ProductCategoryType;
import webshop.category.ProductCategoryTypeRepository;
import webshop.exception.NotFindException;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository repository;
    private ProductCategoryTypeRepository categoryRepository;
    private ModelMapper modelMapper;

    public List<ProductDto> getProducts() {
        Type targetListType = new TypeToken<List<ProductDto>>() {
        }.getType();
        List<Product> products = repository.findAll();
        return modelMapper.map(products, targetListType);
    }

    public ProductDto findProduct(long id) {
        Product product = getProductWithRatings(id);
        return modelMapper.map(product, ProductDto.class);
    }

    public List<ProductDto> getProductsIncreasingPrices() {
        Type targetListType = new TypeToken<List<ProductDto>>() {
        }.getType();
        List<Product> products = repository.findProductOrderedByPrices();
        return modelMapper.map(products, targetListType);
    }

    public List<ProductDto> getProductsDecreasingRatings() {
        Type targetListType = new TypeToken<List<ProductDto>>() {
        }.getType();

        List<Product> products = repository.getProductsWithRatings()
                .stream()
                .peek(Product::calculateRating)
                .distinct()
                .sorted(Comparator.comparing(Product::getRating).reversed())
                .toList();

        return modelMapper.map(products, targetListType);
    }

    public List<ProductDto> getProductsFilteredByCategory(long id) {
        Type targetListType = new TypeToken<List<ProductDto>>() {
        }.getType();

        ProductCategoryType category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFindException("/api/products", "There is no category with this id: "));
        List<Product> products = repository.getProductsFilteredByCategory(category);

        return modelMapper.map(products, targetListType);
    }

    public ProductDto createProduct(CreateUpdateProductCommand command) {
        ProductCategoryType category = getProductCategoryType(command);
        Product product = new Product(
                command.getName(),
                command.getUnitPrice(),
                command.getPiece(),
                category,
                new ArrayList<>(),
                command.getDescription());

        repository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    private ProductCategoryType getProductCategoryType(CreateUpdateProductCommand command) {
        return categoryRepository
                .findById(command.getCategory())
                .orElseThrow(() -> new NotFindException("/api/products", "There is no category with this id: " + command.getCategory()));
    }

    @Transactional
    public ProductDto updateProduct(long id, CreateUpdateProductCommand command) {
        Product product = repository
                .findById(id)
                .orElseThrow(() -> new NotFindException("/api/products", "There is no product with this id: " + id));
        ProductCategoryType category = categoryRepository
                .findById(command.getCategory())
                .orElseThrow(() -> new NotFindException("/api/products", "There is no category with this id: " + command.getCategory()));

        product.setName(command.getName());
        product.setUnitPrice(command.getUnitPrice());
        product.setPiece(command.getPiece());
        product.setCategory(category);
        product.setDescription(command.getDescription());

        return modelMapper.map(product, ProductDto.class);
    }

    public ProductDto addRating(long id, AddRatingCommand command) {
        Product product = getProductWithRatings(id);

        product.addRating(command.getRating());
        repository.save(product);
        product.calculateRating();

        return modelMapper.map(product, ProductDto.class);
    }

    public void deleteProduct(long id) {
        repository.deleteById(id);
    }

    @Transactional(value = Transactional.TxType.MANDATORY)
    public Product getProductWithRatings(long id) {
        Product product = repository
                .findProductWithRatings(id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFindException("/api/products", "There is no product with this id: " + id));
        product.calculateRating();

        return product;
    }
}