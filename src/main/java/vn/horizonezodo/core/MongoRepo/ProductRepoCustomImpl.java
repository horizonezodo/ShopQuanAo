package vn.horizonezodo.core.MongoRepo;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.horizonezodo.core.Entity.Product;
import vn.horizonezodo.core.Output.ProductOutput;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepoCustomImpl implements ProductRepoCustom{

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public Page<ProductOutput> searchProductsForUser(String keyword,String cateId, double minPrice, double maxPrice, long startDate, long endDate, int viewCount, String sortBy, boolean activate, Pageable pageable) {
        Query query = new Query();

        if(!Strings.isNullOrEmpty(keyword)){
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("productName").regex(keyword, "i"),
                    Criteria.where("productDescription").regex(keyword, "i")
            ));
        }

        if(!Strings.isNullOrEmpty(cateId)){
            query.addCriteria(Criteria.where("categoryId").is(cateId));
        }

        if(minPrice > 0){
            query.addCriteria(Criteria.where("minPrice").gte(minPrice));
        }

        if(maxPrice > 0){
            query.addCriteria(Criteria.where("maxPrice").lte(maxPrice));
        }

        if(startDate > 0){
            query.addCriteria(Criteria.where("createAt").gte(startDate));
        }

        if(endDate > 0){
            query.addCriteria(Criteria.where("updateAt").lte(endDate));
        }

        if(viewCount > 0){
            query.addCriteria(Criteria.where("viewCount").gte(viewCount));
        }

        if (activate) {
            query.addCriteria(Criteria.where("isActivate").is(activate));
        }

        if ("price_asc".equals(sortBy)) {
            query.with(pageable.getSort().and(Sort.by(Sort.Direction.ASC, "minPrice")));
        } else if ("price_dsc".equals(sortBy)) {
            query.with(pageable.getSort().and(Sort.by(Sort.Direction.DESC, "minPrice")));
        } else if ("popularity".equals(sortBy)) {
            query.with(pageable.getSort().and(Sort.by(Sort.Direction.DESC, "viewCount")));
        } else if ("oldness".equals(sortBy)){
            query.with(pageable.getSort().and(Sort.by(Sort.Direction.ASC, "createAt")));
        } else if ("newness".equals(sortBy)){
            query.with(pageable.getSort().and(Sort.by(Sort.Direction.DESC, "createAt")));
        }else{
            query.with(pageable.getSort());
        }
        long total = mongoTemplate.count(query, Product.class);
        query.with(pageable);

        List<Product> products = mongoTemplate.find(query, Product.class);
        List<ProductOutput> productOutputs = buildData(products);
        return new PageImpl<>(productOutputs, pageable, total);
    }

    @Override
    public Page<ProductOutput> searchProductsForAdmin(String keyword,String cateId, double minPrice, double maxPrice, long startDate, long endDate, int viewCount, String sortBy, Pageable pageable) {

        Query query = new Query();

        if(!Strings.isNullOrEmpty(keyword)){
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("productName").regex(keyword, "i"),
                    Criteria.where("productDescription").regex(keyword, "i")
            ));
        }

        if(!Strings.isNullOrEmpty(cateId)){
            query.addCriteria(Criteria.where("categoryId").is(cateId));
        }

        if(minPrice > 0){
            query.addCriteria(Criteria.where("minPrice").gte(minPrice));
        }

        if(maxPrice > 0){
            query.addCriteria(Criteria.where("maxPrice").lte(maxPrice));
        }

        if(startDate > 0){
            query.addCriteria(Criteria.where("createAt").gte(startDate));
        }

        if(endDate > 0){
            query.addCriteria(Criteria.where("updateAt").lte(endDate));
        }

        if(viewCount > 0){
            query.addCriteria(Criteria.where("viewCount").gte(viewCount));
        }

        if ("price_asc".equals(sortBy)) {
            query.with(pageable.getSort().and(Sort.by(Sort.Direction.ASC, "minPrice")));
        } else if ("price_dsc".equals(sortBy)) {
            query.with(pageable.getSort().and(Sort.by(Sort.Direction.DESC, "minPrice")));
        } else if ("popularity".equals(sortBy)) {
            query.with(pageable.getSort().and(Sort.by(Sort.Direction.DESC, "viewCount")));
        } else if ("oldness".equals(sortBy)){
            query.with(pageable.getSort().and(Sort.by(Sort.Direction.ASC, "createAt")));
        } else if ("newness".equals(sortBy)){
            query.with(pageable.getSort().and(Sort.by(Sort.Direction.DESC, "createAt")));
        }else{
            query.with(pageable.getSort());
        }

        long total = mongoTemplate.count(query, Product.class);
        query.with(pageable);

        List<Product> products = mongoTemplate.find(query, Product.class);
        List<ProductOutput> productOutputs = buildData(products);
        return new PageImpl<>(productOutputs, pageable, total);
    }

    @Override
    public List<Product> searchPublicProduct(String keyword, String cateId, double minPrice, double maxPrice, long startDate, long endDate, int viewCount, String sortBy, boolean activate, boolean top, int limit) {
        Query query = new Query();

        if(!Strings.isNullOrEmpty(keyword)){
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("productName").regex(keyword, "i"),
                    Criteria.where("productDescription").regex(keyword, "i")
            ));
        }

        if(!Strings.isNullOrEmpty(cateId)){
            query.addCriteria(Criteria.where("categoryId").is(cateId));
        }

        if(minPrice > 0){
            query.addCriteria(Criteria.where("minPrice").gte(minPrice));
        }

        if(maxPrice > 0){
            query.addCriteria(Criteria.where("maxPrice").lte(maxPrice));
        }

        if(startDate > 0){
            query.addCriteria(Criteria.where("createAt").gte(startDate));
        }

        if(endDate > 0){
            query.addCriteria(Criteria.where("updateAt").lte(endDate));
        }

        if(viewCount > 0){
            query.addCriteria(Criteria.where("viewCount").gte(viewCount));
        }

        if (top) {
            query.addCriteria(Criteria.where("isTop").is(top));
        }
        if (activate) {
            query.addCriteria(Criteria.where("isActivate").is(activate));
        }


        if("price_asc".equalsIgnoreCase(sortBy)){
            query.with(Sort.by(Sort.Direction.ASC, "minPrice"));
        }else if ("price_dsc".equals(sortBy)) {
            query.with(Sort.by(Sort.Direction.DESC, "minPrice"));
        } else if ("popularity".equals(sortBy)) {
            query.with(Sort.by(Sort.Direction.DESC, "viewCount"));
        }else if ("oldness".equals(sortBy)) {
            query.with(Sort.by(Sort.Direction.ASC, "createAt"));
        }else if ("newness".equals(sortBy)) {
            query.with(Sort.by(Sort.Direction.DESC, "createAt"));
        }
        query.limit(limit);
        List<Product> products = mongoTemplate.find(query, Product.class);
        return products;

    }

    private List<ProductOutput> buildData(List<Product> products){
       return products.stream()
                .map(p -> {
                    ProductOutput productOutput = new ProductOutput();
                    productOutput.setId(p.getId());
                    productOutput.setProductName(p.getProductName());
                    productOutput.setCreateAt(p.getUpdateAt());
                    productOutput.setListImage(p.getListImg());
                    productOutput.setThumbImg(p.getThumbImg());
                    productOutput.setTop(p.isTop());
                    productOutput.setActivate(p.isActivate());
                    productOutput.setMinPrice(p.getMinPrice());
                    productOutput.setMaxPrice(p.getMaxPrice());
                    productOutput.setViewCount(p.getViewCount());
                    return productOutput;
                }).collect(Collectors.toList());
    }

}
