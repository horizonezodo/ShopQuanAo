package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.Color;
import vn.horizonezodo.core.Entity.Size;
import vn.horizonezodo.core.Entity.Variant;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.ColorInput;
import vn.horizonezodo.core.Input.SizeInput;
import vn.horizonezodo.core.Input.VariantInput;
import vn.horizonezodo.core.MongoRepo.VariantRepo;
import vn.horizonezodo.core.Output.Message;

import java.util.ArrayList;
import java.util.List;

@Service
public class VariantService {

    @Autowired
    private VariantRepo repo;

    @Autowired
    private ColorService colorService;

    @Autowired
    private SizeService sizeService;

    public Variant getById(String id){
        Variant variant = repo.findById(id).orElseThrow(() -> new MessageException("Không thể tìm tháy variant theo id: "+ id));
        return variant;
    }

    public List<Variant> getListVariant(String id){
        return repo.findAllByProductId(id);
    }

    public void addAllVariant(List<VariantInput> variantInputs){
        for (VariantInput input: variantInputs){
            addVariant(input);
        }
    }

    public void addVariant(VariantInput input){
        ColorInput colorInput = new ColorInput(input.getColorName(), input.getColorImg());
        Color color = colorService.addColor(colorInput);
        SizeInput sizeInput = new SizeInput(input.getSizeName(), input.getSizeDes());
        Size size = sizeService.addSize(sizeInput);
        Variant variant = new Variant();
        variant.setColorId(color.getId());
        variant.setProductId(input.getProductId());
        variant.setSizeId(size.getId());
        variant.setPrice(input.getPrice());
        variant.setStock(input.isStock());
        variant.setSaleQuantity(input.getSaleQuantity());
        repo.save(variant);
    }

    public Message updateVariant(VariantInput input){
        ColorInput colorInput = new ColorInput(input.getColorName(), input.getColorImg());
        Color color = colorService.updateColor(colorInput,input.getColorId());
        SizeInput sizeInput = new SizeInput(input.getSizeName(), input.getSizeDes());
        Size size = sizeService.updateSize(sizeInput, input.getSizeId());
        Variant variant = getById(input.getId());
        variant.setSaleQuantity(input.getSaleQuantity());
        variant.setStock(input.isStock());
        variant.setPrice(input.getPrice());
        repo.save(variant);
        return new Message("Update variant thành công");
    }

    public Message deleteVariant(String id){
        Variant variant = getById(id);
        Color color = colorService.findById(variant.getColorId());
        colorService.deleteColor(color);
        Size size = sizeService.getSizeById(variant.getSizeId());
        sizeService.deleteSize(size);
        repo.delete(variant);
        return new Message("Delete variant thành công");
    }

    public void activateVariant(String id){
        Variant variant = getById(id);
        variant.setStock(true);
        repo.save(variant);
    }
}
