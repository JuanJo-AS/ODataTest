package myservice.mynamespace.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.ex.ODataException;

public class DemoEdmProvider extends CsdlAbstractEdmProvider {
	
	// Service Namespace
	public static final String NAMESPACE = "OData.Demo";
	
	// EDM Container
	public static final String CONTAINER_NAME = "Container";
	public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

	// Entity Types Names
	public static final String ET_PRODUCT_NAME = "Product";
	public static final FullQualifiedName ET_PRODUCT_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_NAME);
	
	// Entity Set Names
	public static final String ES_PRODUCTS_NAME = "Products";
	
	@Override
	/**
	 * Para poder ofrecer nuestros datos necesitamos un EntityContainer que se encargara de llevar los EntitySets
	 */
	public CsdlEntityContainer getEntityContainer() throws ODataException {
		
		// create EntitySets
		List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
		entitySets.add(getEntitySet(CONTAINER, ES_PRODUCTS_NAME));
		
		// create EntityContainer
		CsdlEntityContainer entityContainer = new CsdlEntityContainer();
		entityContainer.setName(CONTAINER_NAME);
		entityContainer.setEntitySets(entitySets);
		
		return entityContainer;
	}

	@Override
	public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) throws ODataException {
		
		// This method is invoked when displaying the Service Document at e.g. http://localhost:8080/DemoService/DemoService.svc
		if(entityContainerName == null || entityContainerName.equals(CONTAINER)) {
			CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
			entityContainerInfo.setContainerName(CONTAINER);
			return entityContainerInfo;
		}
		return null;
	}

	@Override
	/*Con este metodo declaramos un set de entidades las cuales son las que se van a devolver cuando el usuario en este caso consulte la url
	 * http://localhost:8080/DemoService/DemoServlet.svc/Products
	 */
	public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) throws ODataException {
		
		if(entityContainer.equals(CONTAINER)) {
			if(entitySetName.equals(ES_PRODUCTS_NAME)) {
				CsdlEntitySet entitySet = new CsdlEntitySet();
				entitySet.setName(ES_PRODUCTS_NAME);
				entitySet.setType(ET_PRODUCT_FQN);
				
				return entitySet;
			}
		}
		
		return null;
	}
	

	@Override
	/*Este metodo nos permite obtener las propiedades de una entidad tales como que tipos de datos tiene, 
	 * como acceder a esos datos, como se reklaciona con las demas entidades, etc... METADATOS(non-Javadoc)
	 * @see org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider#getEntityType(org.apache.olingo.commons.api.edm.FullQualifiedName)
	 */
	public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
		// this method is called for one of the EntityTypes that are configured in te schema
		if(entityTypeName.equals(ET_PRODUCT_FQN)) {
			
			//create EntityType properties
			CsdlProperty id = new CsdlProperty().setName("ID").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
			CsdlProperty name = new CsdlProperty().setName("Name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			CsdlProperty description = new CsdlProperty().setName("Description").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
			
			//create CsdlPropertyRef for Key element
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName("ID");
			
			// configure EntityType
			CsdlEntityType entityType = new CsdlEntityType();
			entityType.setName(ET_PRODUCT_NAME);
			entityType.setProperties(Arrays.asList(id, name, description));
			entityType.setKey(Collections.singletonList(propertyRef));
			
			return entityType;
			
		}
		return null;
	}

	@Override
	
	public List<CsdlSchema> getSchemas() throws ODataException {
		
		// create Schema
		CsdlSchema schema = new CsdlSchema();
		schema.setNamespace(NAMESPACE);
		
		// add EntityTypes
		List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
		entityTypes.add(getEntityType(ET_PRODUCT_FQN));
		schema.setEntityTypes(entityTypes);
		
		// add EntityContainer
		schema.setEntityContainer(getEntityContainer());
		
		// finally
		List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
		schemas.add(schema);
		
		return schemas;
	}

	
	
}
