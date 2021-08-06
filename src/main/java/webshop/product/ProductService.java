package webshop.product;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import webshop.category.ProductCategoryType;
import webshop.category.ProductCategoryTypeRepository;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

    public ProductDto createProduct(CreateUpdateProductCommand command) {
        ProductCategoryType category = categoryRepository.getById(command.getCategory());
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

    @Transactional
    public ProductDto updateProduct(long id, CreateUpdateProductCommand command) {
        Product product = repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no product with this id: " + id));
        ProductCategoryType category = categoryRepository.getById(command.getCategory());

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
                .orElseThrow(() -> new IllegalArgumentException("There is no product with this id: " + id));
        product.calculateRating();

        return product;
    }
}