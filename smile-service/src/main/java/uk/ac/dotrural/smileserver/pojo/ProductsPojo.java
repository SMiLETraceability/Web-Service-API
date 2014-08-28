/**
 * 
 */
package uk.ac.dotrural.smileserver.pojo;

import java.util.Collection;

/**
 * @author Charles Ofoegbu
 *
 */
public class ProductsPojo {
    public ProductsPojo(){
	
    }
    
    public ProductsPojo(Collection<ProductPojo> products){
	this.products = products;
    }
    
    public Collection<ProductPojo> getProducts() { 
	return products; 
    }

    public void setProducts(Collection<ProductPojo> products) {
	this.products = products;
    }

    private Collection<ProductPojo> products;
}
