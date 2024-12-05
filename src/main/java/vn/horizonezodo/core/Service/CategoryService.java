package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.Category;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.CategoryInput;
import vn.horizonezodo.core.MongoRepo.CategoryRepo;
import vn.horizonezodo.core.Output.Message;

import javax.transaction.Transactional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Transactional
    public void addChildCategory(CategoryInput input){
        Category parentCate = categoryRepo.findById(input.getParentId()).orElseThrow(() -> new MessageException("Không tìm thấy danh mục cha"));
        Category childCate= new Category();
        childCate.setId(input.getChildId());
        childCate.setCateName(input.getName());
        childCate.setCreateAt(System.currentTimeMillis());
        categoryRepo.save(childCate);

        parentCate.getChildIds().add(input.getChildId());
        parentCate.setUpdateAt(System.currentTimeMillis());
        categoryRepo.save(parentCate);
    }

    @Transactional
    public void addParentCate(CategoryInput input){
        Category childCate = categoryRepo.findById(input.getParentId()).orElseThrow(() -> new MessageException("Không tìm thấy danh mục Con"));
        Category parentCategory = new Category();
        parentCategory.setId(input.getParentId());
        parentCategory.setCateName(input.getName());
        parentCategory.setCreateAt(System.currentTimeMillis());
        parentCategory.getChildIds().add(input.getChildId());
        categoryRepo.save(parentCategory);
    }

    public Message updateCate(CategoryInput input){
        Category category = categoryRepo.findById(input.getParentId()).orElseThrow(() -> new MessageException("Không tìm thấy danh mục"));
        category.setCateName(input.getName());
        category.setUpdateAt(System.currentTimeMillis());
        categoryRepo.save(category);
        return new Message("Cập nhật cate thành công");
    }

}
