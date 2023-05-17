package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.*;
import com.example.platforma_ticketing_be.repository.ProductRepository;
import com.example.platforma_ticketing_be.repository.ProductSpecificationImpl;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ProductSpecificationImpl productSpecification;
    @PersistenceContext
    private EntityManager entityManager;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper, ProductSpecificationImpl productSpecification) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.productSpecification = productSpecification;
    }

    public List<Product> filterProducts(Set<Product> products, Specification<Product> specification) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        Predicate predicate = specification.toPredicate(root, query, builder);
        query.where(predicate);

        List<Product> filteredProducts = new ArrayList<>();
        for (Product product: products) {
            Predicate productPredicate = builder.and(predicate, builder.equal(root, product));
            query.where(productPredicate);

            List<Product> result = entityManager.createQuery(query).getResultList();
            if (!result.isEmpty()) {
                filteredProducts.add(product);
            }
        }

        return filteredProducts;
    }

    public List<ProductDto> getAllProducts(ProductFilterDto productFilterDto){
        Set<Product> products = new HashSet<>(productRepository.findAll());
        Specification<Product> specification = this.productSpecification.getProducts(productFilterDto);
        List<Product> filteredProducts = filterProducts(products, specification);
        return filteredProducts.stream()
                .map(product -> this.modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    public List<ProductDto> getAllProductsByTheatreId(Long theatreId, ProductFilterDto productFilterDto){
        Set<Product> products = productRepository.getAllProductsByTheatreId(theatreId);
        Specification<Product> specification = this.productSpecification.getProducts(productFilterDto);
        List<Product> filteredProducts = filterProducts(products, specification);
        return filteredProducts.stream()
                .map(product -> this.modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    public List<ProductDto> getAllProductsByCategoryAndTheatreId(String category, Long theatreId, ProductFilterDto productFilterDto){
        Set<Product> products = productRepository.getAllProductsByCategoryAndTheatreId(category, theatreId);
        Specification<Product> specification = this.productSpecification.getProducts(productFilterDto);
        List<Product> filteredProducts = filterProducts(products, specification);
        return filteredProducts.stream()
                .map(product -> this.modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    private boolean checkIfUploadedFileIsOfImageType(MultipartFile file) {
        return Objects.requireNonNull(file.getContentType()).contains("image");
    }

    public void create(MultipartFile file, ProductDto productDto) throws IOException {
        System.out.println(productDto.getNumber());
        Product product;
        if(productDto.getId() != null){
            if(this.productRepository.findById(productDto.getId()).isPresent()){
                product = this.productRepository.findById(productDto.getId()).get();
                if(this.checkIfUploadedFileIsOfImageType(file)){
                    productDto.setImage(file.getBytes());
                    productDto.setImageName(file.getOriginalFilename());
                } else if(file.isEmpty()){
                    productDto.setImage(product.getImage());
                    productDto.setImageName(product.getImageName());
                }
            }
        } else if(this.checkIfUploadedFileIsOfImageType(file)){
            productDto.setImage(file.getBytes());
            productDto.setImageName(file.getOriginalFilename());
        }
        product = this.modelMapper.map(productDto, Product.class);
        productRepository.save(product);
    }

    public void delete(Long id){
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new EntityNotFoundException(Product.class.getSimpleName() + " with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public ProductDto getProductById(Long id){
        if(this.productRepository.findById(id).isPresent()){
            return this.modelMapper.map(this.productRepository.findById(id).get(), ProductDto.class);
        }
        return null;
    }
}
