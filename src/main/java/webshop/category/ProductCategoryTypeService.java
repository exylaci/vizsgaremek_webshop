package webshop.category;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductCategoryTypeService {
    private ProductCategoryTypeRepository repository;
    private ModelMapper modelMapper;

    public List<ProductCategoryTypeDto> getProductCategoryTypes() {
        Type targetListType = new TypeToken<List<ProductCategoryTypeDto>>() {
        }.getType();
        List<ProductCategoryType> productCategoryTypes = repository.findAll();
        return modelMapper.map(productCategoryTypes, targetListType);
    }

    public ProductCategoryTypeDto findProductCategoryType(long id) {
        ProductCategoryType productCategoryType = repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no product category type with this id: " + id));
        return modelMapper.map(productCategoryType, ProductCategoryTypeDto.class);
    }

    public ProductCategoryTypeDto createProductCategoryType(CreateUpdateProductCategoryTypeCommand command) {
        ProductCategoryType productCategoryType = new ProductCategoryType(
                command.getProductCategoryType(),
                command.getDescription());
        repository.save(productCategoryType);
        return modelMapper.map(productCategoryType, ProductCategoryTypeDto.class);
    }

    @Transactional
    public ProductCategoryTypeDto updateProductCategoryType(long id, CreateUpdateProductCategoryTypeCommand command) {
        ProductCategoryType productCategoryType = repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no product category type with this id: " + id));
        productCategoryType.setProductCategoryType(command.getProductCategoryType());
        productCategoryType.setDescription(command.getDescription());
        return modelMapper.map(productCategoryType, ProductCategoryTypeDto.class);
    }

    public void deleteProductCategoryType(long id) {
        repository.deleteById(id);
    }
}