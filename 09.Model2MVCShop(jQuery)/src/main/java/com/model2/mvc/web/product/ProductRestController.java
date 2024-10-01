package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


//==> 상품등록관리  RestController
@RestController
@RequestMapping("/product/*")
public class ProductRestController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductRestController(){
		System.out.println(this.getClass());
	}
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	//현:getProduct
	@RequestMapping( value="json/getProduct/{prodNo}", method=RequestMethod.GET )
	public Product getProduct( @PathVariable int prodNo ) throws Exception{
		
		System.out.println("/user/json/getProduct : GET");
		
		//Business Logic
		return productService.getProduct(prodNo);
	}

	@RequestMapping( value="json/updateProduct", method=RequestMethod.POST )
	public Product updateProduct(	@RequestBody Product product,
									HttpSession session ) throws Exception{
	
		System.out.println("/product/json/updateProduct : POST");
		//Business Logic
		System.out.println("::"+product);
		Product dbProduct=productService.getProduct(product.getProdNo());
		
		if( product.getProdName().equals(dbProduct.getProdName())){
			session.setAttribute("product", dbProduct);
		}
		
		return dbProduct;
	}

	@RequestMapping( value="json/updateProductView", method=RequestMethod.GET )
	public Product updateProductView(@RequestParam("prodNo") int prodNo ,
									HttpSession session )throws Exception{
		System.out.println("/product/updateProductView: GET");
		return  productService.getProduct(prodNo);
	}
	
	@RequestMapping( value="json/listProduct")
	public  Map<String, Object> listProduct( @PathVariable Search search ) throws Exception{
System.out.println("/product/listProduct : GET / POST");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		return productService.getProductList(search);
	}
	
}