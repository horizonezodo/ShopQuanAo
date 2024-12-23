package vn.horizonezodo.core.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.horizonezodo.core.Entity.Variant;
import vn.horizonezodo.core.Input.ProductInput;
import vn.horizonezodo.core.Input.SearchProduct;
import vn.horizonezodo.core.Input.VariantInput;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Output.ProductOutput;
import vn.horizonezodo.core.Service.ProductService;
import vn.horizonezodo.core.Service.VariantService;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/service")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private VariantService variantService;

    @GetMapping("/product/get-all/{cateId}")
    public ResponseEntity<?> getAllProduct(@PathVariable String cateId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size){
        Page<ProductOutput> productOutputList = productService.getAllProduct(cateId, size, page);
        return new ResponseEntity<>(productOutputList, HttpStatus.OK);
    }

    @GetMapping("/product/get/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id")String id){
        ProductOutput productOutput = productService.getProductOutput(id);
        return new ResponseEntity<>(productOutput, HttpStatus.OK);
    }

    @PostMapping("/product/add-product")
    public ResponseEntity<?> addProduct(@RequestBody ProductInput input){
        productService.addProduct(input);
        return new ResponseEntity<>(new Message("Thêm sản phầm thành công"), HttpStatus.CREATED);
    }

    @PostMapping("/product/update-product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id")String id, ProductInput input){
        productService.updateProduct(input,id);
        return new ResponseEntity<>(new Message("Update product thành công"), HttpStatus.OK);
    }

    @PostMapping("/product/deactivate/{id}")
    public ResponseEntity<?> deActivateProduct(@PathVariable("id")String id){
        productService.deActivateProduct(id);
        return new ResponseEntity<>(new Message("Chuyển trạng thái hết hàng thành công"), HttpStatus.OK);
    }

    @PostMapping("/product/activate/{id}")
    public ResponseEntity<?> ActivateProduct(@PathVariable("id")String id){
        productService.activateProduct(id);
        return new ResponseEntity<>(new Message("Active product thành công"), HttpStatus.OK);
    }

    @PostMapping("/product/add-top/{id}")
    public ResponseEntity<?> AddTopProduct(@PathVariable("id")String id){
        productService.addTopProduct(id);
        return new ResponseEntity<>(new Message("Add top product thành công"), HttpStatus.OK);
    }

    @PostMapping("/product/update-variant/{id}")
    public ResponseEntity<?> updateVariant(@PathVariable("id")String id, VariantInput input){
        variantService.updateVariant(input, id);
        productService.updateProductPrice(input.getProductId());
        return new ResponseEntity<>(new Message("Update variant thành công"), HttpStatus.OK);
    }

    @PostMapping("/product/delete-variant/{id}")
    public ResponseEntity<?> deleteVariant(@PathVariable("id")String id){
        Variant variant = variantService.getById(id);
        variantService.deleteVariant(id);
        productService.updateProductPrice(variant.getProductId());
        return new ResponseEntity<>(new Message("Delete variant thành công"), HttpStatus.OK);
    }

    @PostMapping("/product/add-list-variant/{id}")
    public ResponseEntity<?> addListVariant(@PathVariable("id")String id,@RequestBody List<VariantInput> inputs){
        productService.addListVariant(inputs, id);
        return new ResponseEntity<>(new Message("Thêm thuộc tính sản phẩm thành công"), HttpStatus.OK);
    }

    @PostMapping("/product/search-product-for-user")
    public ResponseEntity<?> searchProductForUser(@RequestBody SearchProduct searchProduct){
        return new ResponseEntity<>(productService.searchProductForUser(searchProduct), HttpStatus.OK);
    }

    @PostMapping("/product/search-product-for-admin")
    public ResponseEntity<?> searchProductForAdmin(@RequestBody SearchProduct searchProduct){
        return new ResponseEntity<>(productService.searchProductForAdmin(searchProduct), HttpStatus.OK);
    }

 }
