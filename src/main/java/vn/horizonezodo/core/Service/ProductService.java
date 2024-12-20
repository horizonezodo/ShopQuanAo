package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.Color;
import vn.horizonezodo.core.Entity.Product;
import vn.horizonezodo.core.Entity.Size;
import vn.horizonezodo.core.Entity.Variant;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.ProductInput;
import vn.horizonezodo.core.Input.SearchProduct;
import vn.horizonezodo.core.Input.VariantInput;
import vn.horizonezodo.core.MongoRepo.ProductRepo;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Output.ProductOutput;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepo repo;

    @Autowired
    private VariantService service;

    @Autowired
    private ColorService colorService;

    @Autowired
    private SizeService sizeService;

    public Page<ProductOutput> getAllProduct(String cateId, int pageSize, int page){
        List<Product> pro = repo.findAllByCategoryId(cateId);
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<Product> productPage = repo.findAllByCategoryId(cateId, pageable);
        List<ProductOutput> productOutputs = productPage.stream()
            .map(p -> {
            ProductOutput productOutput = new ProductOutput();
            productOutput.setId(p.getId());
            productOutput.setProductName(p.getProductName());
            productOutput.setCreateAt(p.getUpdateAt());
            productOutput.setListImage(p.getListImg());
            productOutput.setThumbImg(p.getThumbImg());
            List<Variant> variants = service.getListVariant(p.getId());
            productOutput.setListVariants(variants);
            productOutput.setTop(p.isTop());
            productOutput.setActivate(p.isActivate());
            productOutput.setMinPrice(p.getMinPrice());
            productOutput.setMaxPrice(p.getMaxPrice());
            productOutput.setViewCount(p.getViewCount());
            return productOutput;
        }).collect(Collectors.toList());

        return new PageImpl<>(productOutputs, pageable, productPage.getTotalElements());
    }

    public ProductOutput getProductOutput(String id){
        Product product = repo.findById(id).orElseThrow(()-> new MessageException("Không tìm thấy product theo id: "+id));
        List<Variant> variants = service.getListVariant(id);
        List<Color> colors = colorService.getAllByProductId(id);
        List<Size> sizes = sizeService.getAllByProductId(id);
        ProductOutput productOutput = new ProductOutput();
        productOutput.setId(id);
        productOutput.setProductName(product.getProductName());
        productOutput.setCreateAt(product.getUpdateAt());
        productOutput.setThumbImg(product.getThumbImg());
        productOutput.setListImage(product.getListImg());
        productOutput.setListVariants(variants);
        productOutput.setColors(colors);
        productOutput.setSizes(sizes);
        productOutput.setViewCount(product.getViewCount());
        productOutput.setTop(product.isTop());
        productOutput.setMinPrice(product.getMinPrice());
        productOutput.setMaxPrice(product.getMaxPrice());
        productOutput.setActivate(product.isActivate());
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
        service.addAllVariant(input.getVariantInputs(), product.getId());
        double max = input.getVariantInputs().stream()
                .map(VariantInput::getPrice)
                .max(Double::compare)
                .orElse(Double.MIN_VALUE);

        double min = input.getVariantInputs().stream()
                .map(VariantInput::getPrice)
                .min(Double::compare)
                .orElse(Double.MAX_VALUE);
        product.setMinPrice(min);
        product.setMaxPrice(max);
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
                    productOutput.setId(p.getId());
                    productOutput.setProductName(p.getProductName());
                    productOutput.setCreateAt(p.getUpdateAt());
                    productOutput.setListImage(p.getListImg());
                    productOutput.setThumbImg(p.getThumbImg());
                    List<Variant> variants = service.getListVariant(p.getId());
                    productOutput.setListVariants(variants);
                    productOutput.setTop(p.isTop());
                    productOutput.setMinPrice(p.getMinPrice());
                    productOutput.setMaxPrice(p.getMaxPrice());
                    productOutput.setViewCount(p.getViewCount());
                    return productOutput;
                }).collect(Collectors.toList());

        return new PageImpl<>(productOutputs, pageable, productPage.getTotalElements());
    }

    public void addListVariant(List<VariantInput> variantInputs, String id) {
        Product product = repo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy product theo id: " + id));
        double max = variantInputs.stream()
                .map(VariantInput::getPrice)
                .max(Double::compare)
                .orElse(Double.MIN_VALUE);

        double min = variantInputs.stream()
                .map(VariantInput::getPrice)
                .min(Double::compare)
                .orElse(Double.MAX_VALUE);
        product.setMinPrice(min);
        product.setMaxPrice(max);
        repo.save(product);
        service.addAllVariant(variantInputs, id);
    }

    public void updateProductPrice(String id){
        Product product = repo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy product theo id: " + id));
        List<Variant> variants = service.getListVariant(product.getId());
        double max = variants.stream()
                .map(Variant::getPrice)
                .max(Double::compare)
                .orElse(Double.MIN_VALUE);

        double min = variants.stream()
                .map(Variant::getPrice)
                .min(Double::compare)
                .orElse(Double.MAX_VALUE);
        product.setMinPrice(min);
        product.setMaxPrice(max);
        repo.save(product);
    }

    public List<ProductOutput> get20TopProduct(){
        List<Product> products = repo.findTop20ByTopAndActivateOrderByUpdateAtAsc(true, true);
        List<ProductOutput> productOutputs = products.stream()
                .map(p -> {
                    ProductOutput productOutput = new ProductOutput();
                    productOutput.setId(p.getId());
                    productOutput.setProductName(p.getProductName());
                    productOutput.setCreateAt(p.getUpdateAt());
                    productOutput.setListImage(p.getListImg());
                    productOutput.setThumbImg(p.getThumbImg());
                    List<Variant> variants = service.getListVariant(p.getId());
                    productOutput.setListVariants(variants);
                    productOutput.setTop(p.isTop());
                    productOutput.setActivate(p.isActivate());
                    productOutput.setMinPrice(p.getMinPrice());
                    productOutput.setMaxPrice(p.getMaxPrice());
                    productOutput.setViewCount(p.getViewCount());
                    return productOutput;
                }).collect(Collectors.toList());
        return productOutputs;
    }

    public List<ProductOutput> get20TopProductByCate(String cateId){
        List<Product> products = repo.findTop20ByCategoryIdAndTopAndActivateOrderByIdAsc(cateId,true, true);
        List<ProductOutput> productOutputs = products.stream()
                .map(p -> {
                    ProductOutput productOutput = new ProductOutput();
                    productOutput.setId(p.getId());
                    productOutput.setProductName(p.getProductName());
                    productOutput.setCreateAt(p.getUpdateAt());
                    productOutput.setListImage(p.getListImg());
                    productOutput.setThumbImg(p.getThumbImg());
                    List<Variant> variants = service.getListVariant(p.getId());
                    productOutput.setListVariants(variants);
                    productOutput.setTop(p.isTop());
                    productOutput.setActivate(p.isActivate());
                    productOutput.setMinPrice(p.getMinPrice());
                    productOutput.setMaxPrice(p.getMaxPrice());
                    productOutput.setViewCount(p.getViewCount());
                    return productOutput;
                }).collect(Collectors.toList());
        return productOutputs;
    }

    public List<ProductOutput> searchPublicProduct(SearchProduct searchProduct){
        List<Product> products = repo.searchPublicProduct(searchProduct.getKeyword(), searchProduct.getCateId(), searchProduct.getMinPrice(), searchProduct.getMaxPrice(), searchProduct.getStartDate(), searchProduct.getEndDate(), searchProduct.getViewCount(), searchProduct.getSortBy(), searchProduct.isActivate(), searchProduct.isTop(), searchProduct.getLimit());
        return products.stream()
                .map(p -> {
                    ProductOutput productOutput = new ProductOutput();
                    productOutput.setId(p.getId());
                    productOutput.setProductName(p.getProductName());
                    productOutput.setCreateAt(p.getUpdateAt());
                    productOutput.setListImage(p.getListImg());
                    productOutput.setThumbImg(p.getThumbImg());
                    List<Variant> variants = service.getListVariant(p.getId());
                    productOutput.setListVariants(variants);
                    productOutput.setTop(p.isTop());
                    productOutput.setActivate(p.isActivate());
                    productOutput.setMinPrice(p.getMinPrice());
                    productOutput.setMaxPrice(p.getMaxPrice());
                    productOutput.setViewCount(p.getViewCount());
                    return productOutput;
                }).collect(Collectors.toList());
    }

    public Page<ProductOutput> searchProductForUser(SearchProduct searchProduct){
        Pageable pageable = PageRequest.of(searchProduct.getPage(), searchProduct.getLimit());
        return repo.searchProductsForUser(searchProduct.getKeyword(), searchProduct.getCateId(), searchProduct.getMinPrice(), searchProduct.getMaxPrice(), searchProduct.getStartDate(), searchProduct.getEndDate(), searchProduct.getViewCount(), searchProduct.getSortBy(), searchProduct.isActivate(), pageable);
    }

    public Page<ProductOutput> searchProductForAdmin(SearchProduct searchProduct){
        Pageable pageable = PageRequest.of(searchProduct.getPage(), searchProduct.getLimit());
        return repo.searchProductsForAdmin(searchProduct.getKeyword(), searchProduct.getCateId(), searchProduct.getMinPrice(), searchProduct.getMaxPrice(), searchProduct.getStartDate(), searchProduct.getEndDate(), searchProduct.getViewCount(), searchProduct.getSortBy(), pageable);
    }
}
