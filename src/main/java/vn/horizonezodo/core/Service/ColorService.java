package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.Color;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.ColorInput;
import vn.horizonezodo.core.MongoRepo.ColorRepo;
import vn.horizonezodo.core.Output.Message;

@Service
public class ColorService {

    @Autowired
    private ColorRepo repo;

    public Color addColor(ColorInput input){
        Color color = new Color();
        color.setName(input.getName());
        color.setImg(input.getImg());
        color = repo.save(color);
        return color;
    }

    public Color findById(String id){
        Color color = repo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy color theo id"));
        return color;
    }

    public Color updateColor(ColorInput input,String id){
        Color color = findById(id);
        color.setName(input.getName());
        color.setImg(input.getImg());
        color = repo.save(color);
        return color;
    }

    public void deleteColor(Color color){
        repo.delete(color);
    }


}
