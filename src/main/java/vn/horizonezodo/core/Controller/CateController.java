package vn.horizonezodo.core.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.horizonezodo.core.Input.CategoryInput;
import vn.horizonezodo.core.Output.CateOutput;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/service/category")
public class CateController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create-parent-cate")
    public ResponseEntity<?> addParentCate(@RequestBody CategoryInput input){
        categoryService.createCate(input);
        return new ResponseEntity<>(new Message("Add cate thành công"), HttpStatus.OK);
    }

    @PostMapping("/add-child-cate")
    public ResponseEntity<?> addChildCate(@RequestBody CategoryInput input){
        categoryService.addChildCategory(input);
        return new ResponseEntity<>(new Message("Add child cate thành công"), HttpStatus.OK);
    }

    @PostMapping("/update-cate/{id}")
    public ResponseEntity<?> updateCate(@PathVariable("id")String id, CategoryInput input){
        categoryService.updateCate(input,id);
        return new ResponseEntity<>(new Message("Update cate thành công"), HttpStatus.OK);
    }

    @PostMapping("/delete-parent/{id}")
    public ResponseEntity<?> deleteParantCate(@PathVariable("id")String id){
        categoryService.deleteParentCate(id);
        return new ResponseEntity<>(new Message("Delete parent categorythành công"),HttpStatus.OK);
    }

    @PostMapping("/delete-child/{id}")
    public ResponseEntity<?> deleteChildCate(@PathVariable("id")String id){
        categoryService.deleteChildCate(id);
        return new ResponseEntity<>(new Message("Delete parent categorythành công"),HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllCate(){
        List<CateOutput> cateOutputList = categoryService.getAllCate();
        return new ResponseEntity<>(cateOutputList, HttpStatus.OK);
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> findAll(){
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/get-cate/{id}")
    public ResponseEntity<?> getCate(@PathVariable("id")String id){
        CateOutput cateOutput = categoryService.getCate(id);
        return new ResponseEntity<>(cateOutput, HttpStatus.OK);
    }

    @PostMapping("/activate-cate/{id}")
    public ResponseEntity<?> activateCate(@PathVariable("id")String id){
        categoryService.ActivateCate(id);
        return new ResponseEntity<>(new Message("Activate cate thành công"), HttpStatus.OK);
    }

}
