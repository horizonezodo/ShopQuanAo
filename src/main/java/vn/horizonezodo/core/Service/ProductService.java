package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.Product;
import vn.horizonezodo.core.Entity.Variant;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.ProductInput;
import vn.horizonezodo.core.MongoRepo.ProductRepo;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Output.ProductOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepo repo;

    @Autowired
    private VariantService service;

    public Page<ProductOutput> getAllProduct(String cateId, int pageSize, int page){
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<Product> productPage = repo.findAllByCategoryId(cateId, pageable);
        List<ProductOutput> productOutputs = productPage.stream()
            .map(p -> {
            ProductOutput productOutput = new ProductOutput();
            productOutput.setId(p.getProductId());
            productOutput.setProductName(p.getProductName());
            productOutput.setCreateAt(p.getUpdateAt());
            productOutput.setListImage(p.getListImg());
            productOutput.setThumbImg(p.getThumbImg());
            List<Variant> variants = service.getListVariant(p.getProductId());
            productOutput.setListVariants(variants);
            productOutput.setTop(p.isTop());
            productOutput.setViewCount(p.getViewCount());
            return productOutput;
        }).collect(Collectors.toList());

        return new PageImpl<>(productOutputs, pageable, productPage.getTotalElements());
    }

    public ProductOutput getProductOutput(String id){
        Product product = repo.findById(id).orElseThrow(()-> new MessageException("Không tìm thấy product theo id: "+id));
        List<Variant> variants = service.getListVariant(id);
        ProductOutput productOutput = new ProductOutput();
        productOutput.setId(id);
        productOutput.setProductName(product.getProductName());
        productOutput.setCreateAt(product.getUpdateAt());
        productOutput.setThumbImg(product.getThumbImg());
        productOutput.setListImage(product.getListImg());
        productOutput.setListVariants(variants);
        productOutput.setViewCount(product.getViewCount());
        productOutput.setTop(product.isTop());
        return productOutput;
    }

    public Message addProduct(ProductInput input){
        Product product = new Product();
        product.setProductName(input.getProductName());
        product.setProductDescription(input.getProductDescription());
        product.setCategoryId(input.getCategoryId());
        product.setCreateAt(System.currentTimeMillis());
        product.setUpdateAt(System.currentTimeMillis());
        product.setThumbImg(input.getThumbImg());
        product.setListImg(input.getListImg());
        product.setActivate(input.isActivate());
        repo.save(product);
        return new Message("Thêm sản phẩm thành công");
    }

    public Message updateProduct(ProductInput input,String id){
        Product product = repo.findById(input.getProductId()).orElseThrow(() -> new MessageException("Không tìm thấy product theo id"));
        product.setProductName(input.getProductName());
        product.setProductDescription(input.getProductDescription());
        product.setCreateAt(System.currentTimeMillis());
        product.setUpdateAt(System.currentTimeMillis());
        product.setThumbImg(input.getThumbImg());
        product.setListImg(input.getListImg());
        product.setActivate(input.isActivate());
        repo.save(product);
        return new Message("Update product thành công");
    }

    public Message deActivateProduct(String id){
        Product product = repo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy product theo id"));
        product.setActivate(false);
        repo.save(product);
        return new Message("Deactivate product thành công");
    }

    public Message activateProduct(String id){
        Product product = repo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy product theo id"));
        product.setActivate(true);
        repo.save(product);
        return new Message("Activate product thành công");
    }

    public Message addTopProduct(String id){
        Product product = repo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy product theo id"));
        product.setTop(true);
        product.setUpdateAt(System.currentTimeMillis());
        repo.save(product);
        return new Message("Add product to top thành công");
    }

    public Page<ProductOutput> findByTop(boolean isTop, int pageSize, int page){
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<Product> productPage = repo.findAllByTop(isTop, pageable);
        List<ProductOutput> productOutputs = productPage.stream()
                .map(p -> {
                    ProductOutput productOutput = new ProductOutput();
                    productOutput.setId(p.getProductId());
                    productOutput.setProductName(p.getProductName());
                    productOutput.setCreateAt(p.getUpdateAt());
                    productOutput.setListImage(p.getListImg());
                    productOutput.setThumbImg(p.getThumbImg());
                    List<Variant> variants = service.getListVariant(p.getProductId());
                    productOutput.setListVariants(variants);
                    productOutput.setTop(p.isTop());
                    productOutput.setViewCount(p.getViewCount());
                    return productOutput;
                }).collect(Collectors.toList());

        return new PageImpl<>(productOutputs, pageable, productPage.getTotalElements());
    }


}
