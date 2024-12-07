package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.Category;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.CategoryInput;
import vn.horizonezodo.core.MongoRepo.CategoryRepo;
import vn.horizonezodo.core.Output.CateOutput;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    //level 1 là cha
    //level 2 là con

    @Autowired
    private CategoryRepo categoryRepo;

    public void addChildCategory(CategoryInput input){
        Category parentCate = categoryRepo.findById(input.getParentId()).orElseThrow(() -> new MessageException("Không tìm thấy danh mục cha"));
        parentCate.setChildIds(input.getListChildId());
        parentCate.setUpdateAt(System.currentTimeMillis());
        categoryRepo.save(parentCate);
    }

    public void createCate(CategoryInput input){
        Category cate = new Category();
        cate.setCateName(input.getName());
        cate.setCreateAt(System.currentTimeMillis());
        cate.setActivate(input.isActivate());
        cate.setLevel(input.getLevel());
        categoryRepo.save(cate);
    }

    public void updateCate(CategoryInput input,String id){
        Category category = categoryRepo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy danh mục"));
        category.setCateName(input.getName());
        category.setUpdateAt(System.currentTimeMillis());
        categoryRepo.save(category);
    }

    public void deleteParentCate(String id){
        Category category = categoryRepo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy danh mục"));
        category.setActivate(false);
        List<Category> childCate = getListChildCate(id);
        for(Category cate:childCate){
            cate.setActivate(false);
            categoryRepo.save(cate);
        }
        categoryRepo.save(category);
    }

    public List<Category> getListChildCate(String id){
        return categoryRepo.findAllById(id);
    }

    public void deleteChildCate(String id){
        Category category = categoryRepo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy danh mục"));
        category.setActivate(false);
        categoryRepo.save(category);
    }

    public List<CateOutput> getAllCate(){
        List<Category> parentList = categoryRepo.findAllByLevel(1);
        List<CateOutput> cateOutputs = new ArrayList<>();
        for (Category cate: parentList){
            CateOutput cateOutput = new CateOutput();
            cateOutput.setId(cate.getId());
            cateOutput.setCateName(cate.getCateName());
            cateOutput.setActivate(cate.isActivate());
            cateOutput.setCreateAt(cate.getCreateAt());
            cateOutput.setUpdateAt(cate.getUpdateAt());
            cateOutput.setChildCates(categoryRepo.findAllById(cate.getId()));
            cateOutputs.add(cateOutput);
        }
        return cateOutputs;
    }

    public CateOutput getCate(String id){
        Category cate = categoryRepo.findById(id).orElseThrow(()-> new MessageException("Không tìm thấy cate theo id: " + id));
        CateOutput cateOutput = new CateOutput();
        cateOutput.setId(cate.getId());
        cateOutput.setCateName(cate.getCateName());
        cateOutput.setActivate(cate.isActivate());
        cateOutput.setCreateAt(cate.getCreateAt());
        cateOutput.setUpdateAt(cate.getUpdateAt());
        cateOutput.setChildCates(categoryRepo.findAllById(id));
        return cateOutput;
    }

}
