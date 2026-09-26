package sg.edu.nus.restful.session1.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.restful.session1.dto.ProductPatchRequest;
import sg.edu.nus.restful.session1.dto.ProductRequest;
import sg.edu.nus.restful.session1.dto.ProductResponse;
import sg.edu.nus.restful.session1.exception.ProductNotFoundException;
import sg.edu.nus.restful.session1.model.Product;
import sg.edu.nus.restful.session1.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllActive(String brand, Long categoryId) {
        List<Product> products;

        if (brand != null && !brand.isBlank() && categoryId != null) {
            products = productRepository
                    .findByBrandIgnoreCaseAndCategoryIdAndActiveTrueOrderByIdAsc(brand, categoryId);
        } else if (brand != null && !brand.isBlank()) {
            products = productRepository.findByBrandIgnoreCaseAndActiveTrueOrderByIdAsc(brand);
        } else if (categoryId != null) {
            products = productRepository.findByCategoryIdAndActiveTrueOrderByIdAsc(categoryId);
        } else {
            products = productRepository.findByActiveTrueOrderByIdAsc();
        }

        return products.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return toResponse(findActiveEntity(id));
    }

    public ProductResponse create(ProductRequest request) {
        Product product = new Product(
                request.name().trim(),
                request.price(),
                request.stockQuantity(),
                request.categoryId(),
                normalizeBrand(request.brand()),
                true
        );
        return toResponse(productRepository.save(product));
    }

    public ProductResponse replace(Long id, ProductRequest request) {
        Product product = findActiveEntity(id);
        product.setName(request.name().trim());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setCategoryId(request.categoryId());
        product.setBrand(normalizeBrand(request.brand()));
        return toResponse(productRepository.save(product));
    }

    public ProductResponse patch(Long id, ProductPatchRequest request) {
        Product product = findActiveEntity(id);

        if (request.name() != null) {
            if (request.name().isBlank()) {
                throw new IllegalArgumentException("Name cannot be blank");
            }
            product.setName(request.name().trim());
        }
        if (request.price() != null) {
            product.setPrice(request.price());
        }
        if (request.stockQuantity() != null) {
            product.setStockQuantity(request.stockQuantity());
        }
        if (request.categoryId() != null) {
            product.setCategoryId(request.categoryId());
        }
        if (request.brand() != null) {
            product.setBrand(normalizeBrand(request.brand()));
        }

        return toResponse(productRepository.save(product));
    }

    public void softDelete(Long id) {
        Product product = findActiveEntity(id);
        product.setActive(false);
        productRepository.save(product);
    }

    private Product findActiveEntity(Long id) {
        return productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategoryId(),
                product.getBrand(),
                product.isActive()
        );
    }

    private String normalizeBrand(String brand) {
        return brand == null || brand.isBlank() ? null : brand.trim();
    }
}
