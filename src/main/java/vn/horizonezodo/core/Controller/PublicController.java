package vn.horizonezodo.core.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.horizonezodo.core.Input.SearchProduct;
import vn.horizonezodo.core.Service.CategoryService;
import vn.horizonezodo.core.Service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/get-top-product")
    public ResponseEntity<?> getTopProduct(){
        return new ResponseEntity<>(productService.get20TopProduct(), HttpStatus.OK);
    }

    @GetMapping("/get-all-cate")
    public ResponseEntity<?> getAllCate(){
        return new ResponseEntity<>(categoryService.getAllCate(), HttpStatus.OK);
    }

    @GetMapping("/get-top-product-by-cate/{id}")
    public ResponseEntity<?> getTopProductByCate(@PathVariable("id")String id){
        return new ResponseEntity<>(productService.get20TopProductByCate(id), HttpStatus.OK);
    }

    @PostMapping("/search-product")
    public ResponseEntity<?> searchPublicProduct(@RequestBody SearchProduct searchProduct){
        return new ResponseEntity<>(productService.searchPublicProduct(searchProduct), HttpStatus.OK);
    }
}
