package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.Size;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.SizeInput;
import vn.horizonezodo.core.MongoRepo.SizeRepo;
import vn.horizonezodo.core.Output.Message;

@Service
public class SizeService {
    @Autowired
    private SizeRepo repo;

    public Size addSize(SizeInput input){
        Size size = new Size();
        size.setName(input.getName());
        size.setDescription(input.getDescription());
        size = repo.save(size);
        return size;
    }

    public Size getSizeById(String id){
        Size size = repo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy size theo id"));
        return size;
    }

    public Message updateSize(SizeInput input, String id){
        Size size = getSizeById(id);
        size.setName(input.getName());
        size.setDescription(input.getDescription());
        repo.save(size);
        return new Message("Update size thành công");
    }

    public void deleteSize(String id){
        Size size = getSizeById(id);
        repo.delete(size);
    }

}
