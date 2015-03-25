package edu.ap.jaxrs;
import javax.json.JsonArray;

import java.io.*;
import java.util.*;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;
import javax.ws.rs.*;
import javax.xml.bind.*;

@RequestScoped
@Path("/products")
public class ProductResource {
	
	@GET
	@Produces({"text/html"})
	public String getProductsHTML() {
		String htmlString = "<html><body>";
		try {
			/*
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductsXML.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File JSONFile = new File("/Users/Robin/Desktop/Product.json");
			ProductsXML productsXML = (ProductsXML)jaxbUnmarshaller.unmarshal(JSONFile);*/
			InputStream fis = new FileInputStream("Product.json");
			JsonReader jsonReader = Json.createReader(fis);
			JsonObject jsonObject = jsonReader.readObject();
			
			jsonReader.close();
	        fis.close();
	        
	        JsonArray data = jsonObject.getJsonArray("data");
	        if(data != null) {
	            String[] ids = new String[((CharSequence) data).length()];
	            String[] prices = new String[((CharSequence) data).length()];
	            String[] names = new String[((CharSequence) data).length()];
	            String[] descriptions = new String[((CharSequence) data).length()];
	            
	            for(int i = 0 ; i < ((CharSequence) data).length() ; i++) {
	                ids[i] = data.getString(i, "birthday");
	                prices[i] = data.getString(i, "price");
	                names[i] = data.getString(i, "birthday");
	                descriptions[i] = data.getString(i, "name");
	            }
	        }
			
			ArrayList<Product> listOfProducts = productsXML.getProducts();
			while(jsonObject.)
				htmlString += "<b>Name : " + product.getName() + "</b><br>";
				htmlString += "Id : " + product.getId() + "<br>";
				htmlString += "Brand : " + product.getBrand() + "<br>";
				htmlString += "Description : " + product.getDescription() + "<br>";
				htmlString += "Price : " + product.getPrice() + "<br>";
				htmlString += "<br><br>";
			}
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return htmlString;
	}
	
	@GET
	@Produces({"application/json"})
	public String getProductsJSON() {
		String jsonString = "{\"products\" : [";
		try {
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductsXML.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File JSONFile = new File("/Users/Robin/Desktop/Product.json");
			ProductsXML productsXML = (ProductsXML)jaxbUnmarshaller.unmarshal(JSONFile);
			ArrayList<Product> listOfProducts = productsXML.getProducts();
			
			for(Product product : listOfProducts) {
				jsonString += "{\"name\" : \"" + product.getName() + "\",";
				jsonString += "\"id\" : " + product.getId() + ",";
				jsonString += "\"brand\" : \"" + product.getBrand() + "\",";
				jsonString += "\"description\" : \"" + product.getDescription() + "\",";
				jsonString += "\"price\" : " + product.getPrice() + "},";
			}
			jsonString = jsonString.substring(0, jsonString.length()-1);
			jsonString += "]}";
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return jsonString;
	}
	
	@GET
	@Produces({"text/xml"})
	public String getProductsXML() {
		String content = "";
		File XMLfile = new File("/Users/Robin/Desktop/Product.json");
		try {
			content = new Scanner(XMLfile).useDelimiter("\\Z").next();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return content;
	}

	@GET
	@Path("/{shortname}")
	@Produces({"application/json"})
	public String getProductJSON(@PathParam("name") String shortname) {
		String jsonString = "";
		try {
			// get all products
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductsXML.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File JSONFile = new File("/Users/Robin/Desktop/Product.json");
			ProductsXML productsXML = (ProductsXML)jaxbUnmarshaller.unmarshal(JSONFile);
			ArrayList<Product> listOfProducts = productsXML.getProducts();
			
			// look for the product, using the shortname
			for(Product product : listOfProducts) {
				if(shortname.equalsIgnoreCase(product.getName())) {
					jsonString += "{\"name\" : \"" + product.getName() + "\",";
					jsonString += "\"id\" : " + product.getId() + ",";
					jsonString += "\"brand\" : \"" + product.getBrand() + "\",";
					jsonString += "\"description\" : \"" + product.getDescription() + "\",";
					jsonString += "\"price\" : " + product.getPrice() + "}";
				}
			}
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return jsonString;
	}
	
	@GET
	@Path("/{name}")
	@Produces({"text/xml"})
	public String getProductXML(@PathParam("name") String shortname) {
		String xmlString = "";
		try {
			// get all products
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductsXML.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File JSONFile = new File("/Users/Robin/Desktop/Product.json");
			ProductsXML productsXML = (ProductsXML)jaxbUnmarshaller.unmarshal(JSONFile);
			ArrayList<Product> listOfProducts = productsXML.getProducts();
			
			// look for the product, using the shortname
			for(Product product : listOfProducts) {
				if(shortname.equalsIgnoreCase(product.getName())) {
					JAXBContext jaxbContext2 = JAXBContext.newInstance(Product.class);
					Marshaller jaxbMarshaller = jaxbContext2.createMarshaller();
					StringWriter sw = new StringWriter();
					jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
					jaxbMarshaller.marshal(product, sw);
					xmlString = sw.toString();
				}
			}
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return xmlString;
	}
	
	@POST
	@Consumes({"text/xml"})
	public void processFromXML(String productXML) {
		
		/* newProductXML should look like this :
		 *  
		 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 <product>
        	<brand>BRAND</brand>
        	<description>DESCRIPTION</description>
        	<id>123456</id>
        	<price>20.0</price>
        	<shortname>SHORTNAME</shortname>
        	<sku>SKU</sku>
		 </product>
		 */
		
		try {
			// get all products
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductsXML.class);
			Unmarshaller jaxbUnmarshaller1 = jaxbContext1.createUnmarshaller();
			File JSONFile = new File("/Users/Robin/Desktop/Product.json");
			ProductsXML productsXML = (ProductsXML)jaxbUnmarshaller1.unmarshal(JSONFile);
			ArrayList<Product> listOfProducts = productsXML.getProducts();
			
			// unmarshal new product
			JAXBContext jaxbContext2 = JAXBContext.newInstance(Product.class);
			Unmarshaller jaxbUnmarshaller2 = jaxbContext2.createUnmarshaller();
			StringReader reader = new StringReader(productXML);
			Product newProduct = (Product)jaxbUnmarshaller2.unmarshal(reader);
			
			// add product to existing product list 
			// and update list of products in  productsXML
			listOfProducts.add(newProduct);
			productsXML.setProducts(listOfProducts);
			
			// marshal the updated productsXML object
			Marshaller jaxbMarshaller = jaxbContext1.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.marshal(productsXML, JSONFile);
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
	}
}