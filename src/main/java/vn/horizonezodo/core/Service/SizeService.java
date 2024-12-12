package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.Color;
import vn.horizonezodo.core.Entity.Size;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.SizeInput;
import vn.horizonezodo.core.MongoRepo.SizeRepo;
import vn.horizonezodo.core.Output.Message;

import java.util.List;

@Service
public class SizeService {
    @Autowired
    private SizeRepo repo;

    public Size addSize(SizeInput input){
        Size size = new Size();
        size.setName(input.getName());
        size.setDescription(input.getDescription());
        size.setProductId(input.getProductId());
        size = repo.save(size);
        return size;
    }

    public Size getSizeById(String id){
        Size size = repo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy size theo id"));
        return size;
    }

    public Size updateSize(SizeInput input, String id){
        Size size = getSizeById(id);
        size.setName(input.getName());
        size.setDescription(input.getDescription());
        size = repo.save(size);
        return size;
    }

    public void deleteSize(Size size){
        repo.delete(size);
    }

    public List<Size> getAllByProductId(String productId){
        return repo.findAllByProductId(productId);
    }

}
