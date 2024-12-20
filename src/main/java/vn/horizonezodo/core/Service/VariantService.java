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

    public void addAllVariant(List<VariantInput> variantInputs,String productId){
        for (VariantInput input: variantInputs){
            addVariant(input, productId);
        }
    }

    public void addVariant(VariantInput input,String productId){
        Variant variant = new Variant();
        variant.setProductId(productId);
        variant.setPrice(input.getPrice());
        variant.setStock(input.isStock());
        variant.setSaleQuantity(input.getSaleQuantity());
        ColorInput colorInput = new ColorInput(input.getColorName(), input.getColorImg(),productId);
        Color color = colorService.addColor(colorInput);
        SizeInput sizeInput = new SizeInput(input.getSizeName(), input.getSizeDes(),productId);
        Size size = sizeService.addSize(sizeInput);
        variant.setSizeId(size.getId());
        variant.setColorId(color.getId());
        repo.save(variant);
    }

    public Message updateVariant(VariantInput input,String id){
        ColorInput colorInput = new ColorInput(input.getColorName(), input.getColorImg(),input.getProductId());
        Color color = colorService.updateColor(colorInput,input.getColorId());
        SizeInput sizeInput = new SizeInput(input.getSizeName(), input.getSizeDes(), input.getProductId());
        Size size = sizeService.updateSize(sizeInput, input.getSizeId());
        Variant variant = getById(id);
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
