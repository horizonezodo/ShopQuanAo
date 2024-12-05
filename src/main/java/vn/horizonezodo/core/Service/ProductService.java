package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.Product;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.ProductInput;
import vn.horizonezodo.core.MongoRepo.ProductRepo;
import vn.horizonezodo.core.Output.Message;

@Service
public class ProductService {
    @Autowired
    private ProductRepo repo;

    public Message addProduct(ProductInput input){
        Product product = new Product();
        product.setProductName(input.getProductName());
        product.setProductDescription(input.getProductDescription());
        product.setCategoryId(input.getCategoryId());
        product.setCreateAt(System.currentTimeMillis());
        product.setThumbImg(input.getThumbImg());
        product.setListImg(input.getListImg());
        product.setActivate(input.isActivate());
        repo.save(product);
        return new Message("Thêm sản phẩm thành công");
    }

    public Message updateProduct(ProductInput input){
        Product product = repo.findById(input.getProductId()).orElseThrow(() -> new MessageException("Không tìm thấy product theo id"));
        product.setProductName(input.getProductName());
        product.setProductDescription(input.getProductDescription());
        product.setCreateAt(System.currentTimeMillis());
        product.setThumbImg(input.getThumbImg());
        product.setListImg(input.getListImg());
        product.setActivate(input.isActivate());
        repo.save(product);
        return new Message("Update product thành công");
    }

    public Message deActivateProduct(ProductInput input){
        Product product = repo.findById(input.getProductId()).orElseThrow(() -> new MessageException("Không tìm thấy product theo id"));
        product.setActivate(input.isActivate());
        repo.save(product);
        return new Message("Deactivate product thành công");
    }

    public Message addTopProduct(ProductInput input){
        Product product = repo.findById(input.getProductId()).orElseThrow(() -> new MessageException("Không tìm thấy product theo id"));
        product.setTop(input.isTop());
        repo.save(product);
        return new Message("Add product to top thành công");
    }

}
