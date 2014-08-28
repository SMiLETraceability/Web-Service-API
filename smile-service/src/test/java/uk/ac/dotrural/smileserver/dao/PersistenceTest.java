/**
 * 
 */
package uk.ac.dotrural.smileserver.dao;

import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.*;

import uk.ac.dotrural.smile.entity.activity.Activity;
import uk.ac.dotrural.smile.entity.activitytype.ActivityType;
import uk.ac.dotrural.smile.entity.address.Address;
import uk.ac.dotrural.smile.entity.application.Application;
import uk.ac.dotrural.smile.entity.business.Business;
import uk.ac.dotrural.smile.entity.customfield.CustomField;
import uk.ac.dotrural.smile.entity.identifier.Identifier;
import uk.ac.dotrural.smile.entity.item.Item;
import uk.ac.dotrural.smile.entity.itemcollection.ItemCollection;
import uk.ac.dotrural.smile.entity.location.Location;
import uk.ac.dotrural.smile.entity.product.Product;
import uk.ac.dotrural.smile.entity.property.Property;

/**
 * @author Charles Ofoegbu
 *
 */
public class PersistenceTest {    
    private Product product;
    
    @Test
    public void activityTypeAndActivityPersistenceTest(){
	ActivityType aType = new ActivityType();
	aType.setName("Test Activity Type");
	aType.setCode("TestActivityCode");
	aType = (ActivityType)DataAccessObject.createNewRecord(aType);
	assertNotNull(aType.getId());	
	
	Activity activity = new Activity();
	activity.setActivityType(aType);
	activity.setTimeStamp(new Timestamp(new Date().getTime()));
	activity.setInitiator("Test Initiator");
	activity.setRecordId("Test Record Id");
	activity.setTargetEntity("Test Target Entity");
	activity = (Activity)DataAccessObject.createNewRecord(activity);
	assertNotNull(activity.getId());
	DataAccessObject.deleteRecord(activity);
	DataAccessObject.deleteRecord(aType);
	
    }
    
    @Test
    public void addressPersistenceTest(){
	Address address = new Address();
	address.setStreet("Test Street");
	address.setHouseNumber("Test house number");
	address = (Address)DataAccessObject.createNewRecord(address);
	assertNotNull(address.getId());
	DataAccessObject.deleteRecord(address);
    }
    
    @Test
    public void applicationPersistenceTest() {
	Application application = new Application();
	application.setDescription("Test App");
	application.setName("Test App");	
	application = (Application) DataAccessObject.createNewRecord(application);	
	assertNotNull(application.getId());
	DataAccessObject.deleteRecord(application);
    }

    @Test
    public void businessPersistenceTest(){
	Business business = new Business();
	business.setName("Test Business");
	business = (Business)DataAccessObject.createNewRecord(business);
	assertNotNull(business.getId());
	DataAccessObject.deleteRecord(business);
    }
    
    @Test
    public void customFieldPersistenceTest(){
	CustomField cField = new CustomField();
	cField.setKey("Test Key");
	cField.setValue("Test Value");
	cField = (CustomField)DataAccessObject.createNewRecord(cField);
	assertNotNull(cField.getId());
	DataAccessObject.deleteRecord(cField);
    }
    
    @Before
    public void createProductBeforeTest(){
	setProduct();
    }
    
    @Test
    public void identifierPersistenceTest(){	
	Identifier id = new Identifier();
	id.setKey("Test Key");
	id.setValue("Test Value");
	id.setProduct(getProduct());	
	id = (Identifier)DataAccessObject.createNewRecord(id);	
	assertNotNull(id.getId());
	DataAccessObject.deleteRecord(id);
    }

    @Test
    public void itemPersistenceTest(){
	Item item = new Item();
	item.setName("Test Item");
	item.setCreatedAt(new Timestamp(new Date().getTime()));
	item.setProduct(product);
	item = (Item)DataAccessObject.createNewRecord(item);	
	assertNotNull(item.getId());
	DataAccessObject.deleteRecord(item);
    }
    
    @After
    public void deleteProductAfterTest(){
	DataAccessObject.deleteRecord(getProduct());
    }
    
    @Test
    public void itemCollectionPersistenceTest(){
	ItemCollection itCol = new ItemCollection();
	itCol.setName("Test Collection");
	itCol.setCreatedAt(new Timestamp(new Date().getTime()));
	itCol = (ItemCollection)DataAccessObject.createNewRecord(itCol);	
	assertNotNull(itCol.getId());
	DataAccessObject.deleteRecord(itCol);
    }

    @Test
    public void locationPersistenceTest(){
	Location location = new Location();
	location.setLatitude(1.0);
	location.setLongitude(2.0);
	location.setTimeStamp(new Timestamp(new Date().getTime()));
    }
    
    @Test
    public void testSomething(){
	
    }
    
    @Test
    public void productPersistenceTest() {
	Product product = new Product();
	product.setCreatedAt(new Timestamp(new Date().getTime()));
	product.setDescription("Test Description");
	product.setFunctionalName("Test Functional Name");
	product.setBrand("Test Brand");
	product.setUrl("Test Url");
	product.setPrice(10.0);
	product = (Product)DataAccessObject.createNewRecord(product);	
	assertNotNull(product.getId());
	DataAccessObject.deleteRecord(product);
    }
    
    @Test
    public void propertyPersistenceTest(){
	Property property = new Property();
	property.setKey("Test Key");
	property.setValue("Test Value");	
	property = (Property)DataAccessObject.createNewRecord(property);
	assertNotNull(property);
	DataAccessObject.deleteRecord(property);
	
    }

    Product getProduct() {
	return product;
    }

    void setProduct() {	
	Product product = new Product();
	product.setCreatedAt(new Timestamp(new Date().getTime()));
	product.setDescription("Test Description");
	product.setFunctionalName("Test Functional Name");
	product.setBrand("Test Brand");
	product.setUrl("Test Url");
	product.setPrice(10.0);
	product = (Product)DataAccessObject.createNewRecord(product);		
	this.product = product;
    }
}
